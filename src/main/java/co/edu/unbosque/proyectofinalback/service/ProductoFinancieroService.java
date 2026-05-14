package co.edu.unbosque.proyectofinalback.service;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.actores.Empleado;
import co.edu.unbosque.proyectofinalback.model.auditoria.EventoAuditoria;
import co.edu.unbosque.proyectofinalback.model.enums.*;
import co.edu.unbosque.proyectofinalback.model.productos.*;
import co.edu.unbosque.proyectofinalback.model.transaccion.Canal;
import co.edu.unbosque.proyectofinalback.model.transaccion.Transaccion;
import co.edu.unbosque.proyectofinalback.repository.*;
import co.edu.unbosque.proyectofinalback.util.FuncionesTarjeta;
import co.edu.unbosque.proyectofinalback.util.event.CreditoEvent;
import co.edu.unbosque.proyectofinalback.util.event.CuentaEvent;
import co.edu.unbosque.proyectofinalback.util.event.TarjetaEvent;
import co.edu.unbosque.proyectofinalback.util.exception.NoEncontradoException;
import co.edu.unbosque.proyectofinalback.util.exception.ReglaNegocioException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class ProductoFinancieroService {


    private final TarjetaRepositorio tarjetaRepositorio;
    private final CreditoRepositorio creditoRepositorio;
    private final CuentaRepositorio cuentaRepositorio;
    private final ProductoFinancieroRepositorio productoRepositorio;
    private final ApplicationEventPublisher eventPublisher;
    private final EmpleadoService empleadoService;

    private final CatalogoService catalogoService;
    private final ClienteService clienteServicio;
    private final AuditoriaService auditoriaService;

    private final ModelMapper modelMapper;

    public ProductoFinancieroService(TarjetaRepositorio tarjetaRepositorio, CreditoRepositorio creditoRepositorio,
                                     CuentaRepositorio cuentaRepositorio, CatalogoService catalogoService,
                                     ClienteService clienteServicio, ProductoFinancieroRepositorio productoRepositorio,
                                     AuditoriaService auditoriaService, ModelMapper modelMapper,
                                     ApplicationEventPublisher eventPublisher, EmpleadoService empleadoService) {
        this.tarjetaRepositorio = tarjetaRepositorio;
        this.creditoRepositorio = creditoRepositorio;
        this.cuentaRepositorio = cuentaRepositorio;
        this.catalogoService = catalogoService;
        this.clienteServicio = clienteServicio;
        this.productoRepositorio = productoRepositorio;
        this.auditoriaService = auditoriaService;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
        this.empleadoService = empleadoService;
    }

    /*
    Credito
     */
    @Transactional
    public CreditoDTO crearCredito(CreditoDTO credito,int idCliente) {

        if (credito.getMontoAprobado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El cupo no puede ser negativo");
        }

        Credito nuevoCredito = Credito.builder()
                .cliente(modelMapper.map(clienteServicio.encontrarClienteId(idCliente), Cliente.class))
                .numeroProducto(generarNumeroProducto())
                .fechaCreacion(LocalDateTime.now())
                .estadoProducto(modelMapper.map(catalogoService.encontrarEstadoProductoPorId(1), EstadoProducto.class))
                .tipoCredito(credito.getTipoCredito())
                .montoAprobado(credito.getMontoAprobado())
                .saldoPendiente(credito.getMontoAprobado())
                .tasaInteres(credito.getTasaInteres())
                .fechaDesembolso(LocalDateTime.now().plusMonths(1))
                .build();

        Credito creditoGuardado = creditoRepositorio.save(nuevoCredito);

        eventPublisher.publishEvent(new CreditoEvent(creditoGuardado));

        return modelMapper.map(creditoGuardado, CreditoDTO.class);
    }

    @Transactional
    public CreditoDTO pagarCredito(int idCredito, BigDecimal montoPago) {


        Credito credito = creditoRepositorio.findById(idCredito).orElseThrow(() -> new NoEncontradoException("No se encontro un credito con el id: " + idCredito));


        BigDecimal saldoActual = credito.getSaldoPendiente();

        if (saldoActual.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El crédito ya no tiene saldo pendiente por pagar.");
        }

        if (montoPago.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El monto a pagar debe ser mayor a cero.");
        }

        if (montoPago.compareTo(saldoActual) > 0) {
            throw new ReglaNegocioException("El pago es mayor a lo que debe. Saldo pendiente: " + saldoActual);
        }

        BigDecimal nuevoSaldo = saldoActual.subtract(montoPago);

        credito.setSaldoPendiente(nuevoSaldo);

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) == 0) {
            credito.setEstadoProducto(modelMapper.map(catalogoService.encontrarEstadoProductoPorId(2), EstadoProducto.class));
        }

        auditar(credito.getCliente(), credito, "Se hizo un pago al credito", "Se creo un credito", "cliente");

        return modelMapper.map(creditoRepositorio.save(credito), CreditoDTO.class);
    }


    @Transactional
    public CreditoDTO ajusteMontoCredito(int idCredito, BigDecimal nuevoMonto, String responsable) {

        if (!creditoRepositorio.existsCreditoByIdProducto(idCredito)) {
            throw new NoEncontradoException("No se encontro un credito con el id: " + idCredito);
        }

        Credito credito = creditoRepositorio.findCreditosByIdProducto(idCredito);

        if (credito.getEstadoProducto().getNombreEstadoProducto().equals(EstadoProductoEnum.bloqueado) && nuevoMonto.compareTo(credito.getMontoAprobado()) > 0) {
            throw new ReglaNegocioException("No se puede ajustar el monto del credito porque esta bloqueado");
        }

        credito.setMontoAprobado(nuevoMonto);
        creditoRepositorio.save(credito);

        auditar(credito.getCliente(), credito, "Se ajusto el monto de un credito", "Se ajusto el monto de un credito: " + nuevoMonto, responsable);

        return modelMapper.map(credito, CreditoDTO.class);

    }

    @Transactional
    public CreditoDTO cambiarEstadoProducto(int idCredito, int estadoProducto, String responsable) {

        Credito credito = creditoRepositorio.findById(idCredito).orElseThrow(() -> new NoEncontradoException("No se encontró el credito con el id: " + idCredito));

        credito.setEstadoProducto(modelMapper.map(catalogoService.encontrarEstadoProductoPorId(estadoProducto), EstadoProducto.class));

        auditar(credito.getCliente(), credito, "Se cambio el estado de un credito", "Se cambio el estado del credito a: " + estadoProducto, responsable);

        return modelMapper.map(credito, CreditoDTO.class);
    }

    public CreditoDTO obtenerCredito(int idCredito) {

        if (!creditoRepositorio.existsCreditoByIdProducto(idCredito)) {
            throw new NoEncontradoException("No se encontro ningun credito con el id: " + idCredito);
        }

        Credito credito = creditoRepositorio.findCreditosByIdProducto(idCredito);

        return modelMapper.map(credito, CreditoDTO.class);

    }


    public List<CreditoDTO> obtenerTodosCreditosPorCliente(int idCliente) {

        Cliente cliente = modelMapper.map(clienteServicio.encontrarClienteId(idCliente), Cliente.class);
        List<Credito> creditosCliente = creditoRepositorio.findAllByCliente(cliente);

        if (creditosCliente == null || creditosCliente.isEmpty()) {
            throw new NoEncontradoException("No se encontraron creditos para el cliente con el id: " + idCliente);
        }

        return creditosCliente.stream().map(x -> modelMapper.map(x, CreditoDTO.class)).toList();
    }

    public List<CreditoDTO> obtenerTodosLosCreditos() {

        return creditoRepositorio.findAll().stream().map(x -> modelMapper.map(x, CreditoDTO.class)).toList();

    }

    /*
    Tarjeta
     */
    @Transactional
    public TarjetaDTO creartarjeta(int idCliente, String tipoTarjeta, BigDecimal cupo) throws Exception {

        if (cupo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El cupo no puede ser negativo");
        }

        Tarjeta nuevaTarjeta = Tarjeta.builder()
                .cliente(modelMapper.map(clienteServicio.encontrarClienteId(idCliente), Cliente.class))
                .numeroProducto(generarNumeroProducto())
                .fechaCreacion(LocalDateTime.now())
                .estadoProducto(modelMapper.map(catalogoService.encontrarEstadoProductoPorId(1), EstadoProducto.class))
                .tipoTarjeta(TipoTarjeta.parseTipotarjeta(tipoTarjeta))
                .numeroEnmascarado(FuncionesTarjeta.enmascarar(FuncionesTarjeta.crearNumero(tipoTarjeta)))
                .fechaVencimiento(FuncionesTarjeta.generarVencimiento())
                .cupo(cupo)
                .build();

        if(nuevaTarjeta.getTipoTarjeta().equals(TipoTarjeta.debito) && cupo.compareTo(BigDecimal.ZERO) > 0){
            nuevaTarjeta.setCupo(BigDecimal.ZERO);
        }

        Tarjeta tarjetaGuarda = tarjetaRepositorio.save(nuevaTarjeta);

        eventPublisher.publishEvent(new TarjetaEvent(tarjetaGuarda));

        TarjetaDTO tarjetaDTO = modelMapper.map(tarjetaGuarda, TarjetaDTO.class);
        tarjetaDTO.setNumeroEnmascarado(FuncionesTarjeta.desenmascarar(tarjetaDTO.getNumeroEnmascarado()));

        return tarjetaDTO;
    }

    @Transactional
    public boolean desactivarTarjetaID(int idTarjeta, String responsable) {

        Tarjeta tarjeta = tarjetaRepositorio.findById(idTarjeta).orElseThrow(() -> new NoEncontradoException("No se encontro la tarjeta con el id: " + idTarjeta));
        tarjeta.setEstadoProducto(modelMapper.map(catalogoService.encontrarEstadoProductoPorId(2), EstadoProducto.class));

        auditar(tarjeta.getCliente(),tarjeta, "Se desactivo una tarjeta", "Se desactivo una tarjeta", responsable);

        return true;

    }

    @Transactional
    public boolean desactivarTarjetaNumero(String numeroTarjeta, String responsable) throws Exception {

        String numEncriptadoDB = FuncionesTarjeta.enmascarar(numeroTarjeta);

        if (tarjetaRepositorio.existsByNumeroEnmascarado(numEncriptadoDB)) {
            Tarjeta tarjeta = tarjetaRepositorio.findByNumeroEnmascarado(numEncriptadoDB);
            tarjeta.setEstadoProducto(modelMapper.map(catalogoService.encontrarEstadoProductoPorId(2), EstadoProducto.class));

            auditar(tarjeta.getCliente(), tarjeta,"Se desactivo la tarjeta", "Se desactivo una tarjeta", responsable);

            return true;
        } else {
            throw new NoEncontradoException("No existe ese numero de tarjeta: " + numeroTarjeta);
        }

    }

    @Transactional
    public TarjetaDTO actualizacionEstado(int idEstado, int idTarjeta, String responsable) {

        if (tarjetaRepositorio.existsByIdProducto(idTarjeta)) {

            Tarjeta tarjeta = tarjetaRepositorio.findTarjetaByIdProducto(idTarjeta);
            tarjeta.setEstadoProducto(modelMapper.map(catalogoService.encontrarEstadoProductoPorId(idEstado), EstadoProducto.class));

            auditar(tarjeta.getCliente(), tarjeta,"Se actualizo el estado de una tarjeta a: " + catalogoService.encontrarEstadoProductoPorId(idEstado).getNombreEstadoProducto(),
                    "Se actualizo el estado de una tarjeta", responsable);

            return modelMapper.map(tarjeta, TarjetaDTO.class);
        } else {
            throw new NoEncontradoException("No existe la tarjeta con el id: " + idTarjeta);
        }

    }

    @Transactional
    public TarjetaDTO actualizacionCupo(int idTarjeta, BigDecimal cupo, int idEmpleado) {

        if (cupo.compareTo(BigDecimal.ZERO) < 0)
            throw new ReglaNegocioException("El cupo no puede ser negativo");

        if (!tarjetaRepositorio.existsByIdProducto(idTarjeta))
            throw new NoEncontradoException("No existe la tarjeta con el id: " + idTarjeta);

        Tarjeta tarjeta = tarjetaRepositorio.findTarjetaByIdProducto(idTarjeta);

        if (tarjeta.getTipoTarjeta().equals(TipoTarjeta.debito))
            throw new ReglaNegocioException("tarjeta DEBITO no puede tener un cupo");

        tarjeta.setCupo(cupo);

        EventoAuditoria auditoria = EventoAuditoria.builder().
                cliente(tarjeta.getCliente()).
                empleado(modelMapper.map(empleadoService.encontrarEmpleadoId(idEmpleado), Empleado.class)).
                canal(modelMapper.map(catalogoService.encontrarCanalPorId(1), Canal.class)).
                producto(tarjeta).
                tipoResponsable(TipoResponsable.empleado).
                accion("Acutaliazacion de cupo a: " + cupo).
                fechaHora(LocalDateTime.now()).
                motivo("Actualizacion de cupo").build();

        auditoriaService.guardarEvento(auditoria);

        return modelMapper.map(tarjetaRepositorio.save(tarjeta), TarjetaDTO.class);

    }

    @Transactional
    public TarjetaDTO renovarFechaVencimiento(int idTarjeta, int idEmpleado) {

        if (!tarjetaRepositorio.existsByIdProducto(idTarjeta))
            throw new NoEncontradoException("No se encontro una tarjeta con el id: " + idTarjeta);

        Tarjeta tarjeta = tarjetaRepositorio.findTarjetaByIdProducto(idTarjeta);
        tarjeta.setFechaVencimiento(FuncionesTarjeta.generarVencimiento());

        EventoAuditoria auditoria = EventoAuditoria.builder().
                cliente(tarjeta.getCliente()).
                empleado(modelMapper.map(empleadoService.encontrarEmpleadoId(idEmpleado), Empleado.class)).
                canal(modelMapper.map(catalogoService.encontrarCanalPorId(1), Canal.class)).
                producto(tarjeta).
                tipoResponsable(TipoResponsable.empleado).
                accion("Renovacion de fecha de vencimiento").
                fechaHora(LocalDateTime.now()).
                motivo("Renovacion de fecha de vencimiento").build();

        auditoriaService.guardarEvento(auditoria);

        return modelMapper.map(tarjetaRepositorio.save(tarjeta), TarjetaDTO.class);

    }

    @Transactional
    public TarjetaDTO encontrarTarjetaPorId(int idTarjeta) throws Exception {

        TarjetaDTO tarjeta = modelMapper.map(tarjetaRepositorio.findById(idTarjeta), TarjetaDTO.class);
        tarjeta.setNumeroEnmascarado(FuncionesTarjeta.desenmascarar(tarjeta.getNumeroEnmascarado()));

        return tarjeta;
    }

    @Transactional
    public List<TarjetaDTO> listarTarjetas() throws Exception {

        List<TarjetaDTO> tarjetas = tarjetaRepositorio.findAll()
                .stream()
                .map(x -> modelMapper.map(x, TarjetaDTO.class))
                .toList();

        for(TarjetaDTO tarjeta : tarjetas){
            tarjeta.setNumeroEnmascarado(FuncionesTarjeta.desenmascarar(tarjeta.getNumeroEnmascarado()));
        }
        return tarjetas;
    }

    /*
    Cuenta
     */

    @Transactional
    public CuentaDTO crearCuenta(int idCliente, String tipoCuenta, String moneda) {

        Cliente cliente = modelMapper.map(clienteServicio.encontrarClienteId(idCliente), Cliente.class);

        Cuenta nuevaCuenta = Cuenta.builder()
                .numeroProducto(generarNumeroProducto())
                .cliente(cliente)
                .fechaCreacion(LocalDateTime.now())
                .estadoProducto(modelMapper.map(catalogoService.encontrarEstadoProductoPorId(1), EstadoProducto.class))
                .tipoCuenta(TipoCuenta.parseTipoCuenta(tipoCuenta))
                .moneda(Moneda.parseMoneda(moneda))
                .saldoCuenta(BigDecimal.ZERO)
                .build();

        Cuenta cuentaGuardada = cuentaRepositorio.save(nuevaCuenta);

        eventPublisher.publishEvent(new CuentaEvent(cuentaGuardada));

        return modelMapper.map(cuentaGuardada, CuentaDTO.class);
    }

    @Transactional
    public boolean desactivarCuenta(int idProducto) {


        if (cuentaRepositorio.existsById(idProducto)) {
            Cuenta cuenta = cuentaRepositorio.findById(idProducto).orElseThrow(() -> new NoEncontradoException("No existe la cuenta con el id: " + idProducto));
            cuenta.setEstadoProducto(modelMapper.map(catalogoService.encontrarEstadoProductoPorId(2), EstadoProducto.class));

            auditar(cuenta.getCliente(), cuenta, "Se desactivo una cuenta", "Se desactivo una cuenta", "cliente");

            return true;
        } else {
            throw new NoEncontradoException("No existe la tarjeta con el id: " + idProducto);
        }

    }

    @Transactional
    public CuentaDTO actualizarEstado(int idProducto, int idEstado, String responsable) {

        Cuenta cuenta = cuentaRepositorio.findById(idProducto).orElseThrow(() -> new NoEncontradoException("No existe la cuanta con el id: " + idProducto));
        cuenta.setEstadoProducto(modelMapper.map(catalogoService.encontrarEstadoProductoPorId(idEstado), EstadoProducto.class));

        auditar(cuenta.getCliente(), cuenta, "Se actualizo el estado de una cuenta", "Se actualizo el estado de una cuenta", responsable);

        return modelMapper.map(cuenta, CuentaDTO.class);

    }

    @Transactional
    public void ajustarSaldo(int idCuenta, BigDecimal num, TipoOperacion tipoOperacion, Transaccion transaccion) {

        if (!cuentaRepositorio.existsCuentaByIdProducto(idCuenta))
            throw new NoEncontradoException("No se encontro la cuenta con el id: " + idCuenta);

        Cuenta cuenta = cuentaRepositorio.findCuentaByIdProducto(idCuenta);
        EventoAuditoria auditoria = EventoAuditoria.builder().
                cliente(cuenta.getCliente()).
                transaccion(transaccion).
                canal(modelMapper.map(catalogoService.encontrarCanalPorId(1), Canal.class)).
                producto(cuenta).
                tipoResponsable(TipoResponsable.empleado).
                fechaHora(LocalDateTime.now()).
                motivo("Se realizo una transaccion").build();

        if (TipoOperacion.RESTA == tipoOperacion) {

            if (cuenta.getSaldoCuenta().compareTo(num) < 0) {
                throw new ReglaNegocioException("No tiene saldo suficiente en la cuenta, su saldo es de: " + cuenta.getSaldoCuenta());
            }

            auditoria.setAccion("Se resto el saldo de la cuenta: " + num);
            auditoriaService.guardarEvento(auditoria);

            cuenta.setSaldoCuenta(cuenta.getSaldoCuenta().subtract(num));
        } else if (TipoOperacion.SUMA == tipoOperacion) {
            cuenta.setSaldoCuenta(cuenta.getSaldoCuenta().add(num));

            auditoria.setAccion("Se sumo el saldo de la cuenta: " + num);
            auditoriaService.guardarEvento(auditoria);

        } else {
            throw new ReglaNegocioException("Tipo de operacion no valida");
        }

    }

    @Transactional
    public CuentaDTO abono(int idCuenta, BigDecimal monto){

        if (!cuentaRepositorio.existsCuentaByIdProducto(idCuenta))
            throw new NoEncontradoException("No se encontro la cuenta con el id: " + idCuenta);

        Cuenta cuenta = cuentaRepositorio.findCuentaByIdProducto(idCuenta);
        cuenta.setSaldoCuenta(cuenta.getSaldoCuenta().add(monto));
        cuentaRepositorio.save(cuenta);

        return modelMapper.map(cuenta, CuentaDTO.class);

    }

    public CuentaDTO obtenerCuenta(int idCuenta) {

        if (!cuentaRepositorio.existsCuentaByIdProducto(idCuenta))
            throw new NoEncontradoException("No se encontro la cuenta con el id: " + idCuenta);

        return modelMapper.map(cuentaRepositorio.findById(idCuenta), CuentaDTO.class);
    }

    public List<CuentaDTO> obtenerTodasCuentasPorCliente(int idCliente) {
        Cliente cliente = modelMapper.map(clienteServicio.encontrarClienteId(idCliente), Cliente.class);
        return cuentaRepositorio.findAllByCliente(cliente).stream().map(cuenta -> modelMapper.map(cuenta, CuentaDTO.class)).toList();
    }

    public List<CuentaDTO> todasLasCuentas(){
        return cuentaRepositorio.findAll().stream().map(cuenta -> modelMapper.map(cuenta, CuentaDTO.class)).toList();
    }

    public List<ProductoFinanciero> todosLosProductos(){
        return productoRepositorio.findAll().stream().map(producto -> modelMapper.map(producto, ProductoFinanciero.class)).toList();
    }

    private String generarNumeroProducto() {
        Random random = new Random();
        String numeroGenerado;
        boolean existe;

        do {
            long numero = 100000L + (long) (random.nextDouble() * 900000L);
            numeroGenerado = String.valueOf(numero);

            existe = productoRepositorio.existsByNumeroProducto(numeroGenerado);

        } while (existe);

        return "PF-" + numeroGenerado;
    }

    private void auditar(Cliente cliente, ProductoFinanciero productoFinanciero, String accion, String motivo, String responsable){

        EventoAuditoria eventoAuditoria = EventoAuditoria.builder()
                .cliente(cliente)
                .canal(modelMapper.map(catalogoService.encontrarCanalPorId(1), Canal.class))
                .producto(productoFinanciero)
                .tipoResponsable(TipoResponsable.tipoResponsable(responsable))
                .accion(accion)
                .fechaHora(LocalDateTime.now())
                .motivo(motivo)
                .build();

        auditoriaService.guardarEvento(eventoAuditoria);

    }


}
