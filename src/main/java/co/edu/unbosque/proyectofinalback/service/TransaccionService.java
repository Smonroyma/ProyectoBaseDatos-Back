package co.edu.unbosque.proyectofinalback.service;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.enums.TipoOperacion;
import co.edu.unbosque.proyectofinalback.model.enums.TipoTransaccion;
import co.edu.unbosque.proyectofinalback.model.productos.Cuenta;
import co.edu.unbosque.proyectofinalback.model.transaccion.*;
import co.edu.unbosque.proyectofinalback.repository.TransaccionRepositorio;
import co.edu.unbosque.proyectofinalback.util.exception.FraudeDetectadoException;
import co.edu.unbosque.proyectofinalback.util.exception.NoEncontradoException;
import co.edu.unbosque.proyectofinalback.util.exception.ReglaNegocioException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransaccionService {

    private final TransaccionRepositorio transaccionRepository;
    private final ProductoFinancieroService productoService;
    private final FraudeService fraudeService;
    private final ClienteService clienteService;
    private final ModelMapper modelMapper;
    private final CatalogoService catalogoService;
    private final TransaccionAuditoriaService transaccionAuditoriaService;



    public TransaccionService(TransaccionRepositorio transaccionRepository, ProductoFinancieroService productoService,
                              FraudeService fraudeService,
                              ClienteService clienteService, CatalogoService catalogoService,
                              TransaccionAuditoriaService transaccionAuditoriaService) {
        this.transaccionRepository = transaccionRepository;
        this.productoService = productoService;
        this.fraudeService = fraudeService;
        this.clienteService = clienteService;
        this.catalogoService = catalogoService;
        this.transaccionAuditoriaService = transaccionAuditoriaService;
        this.modelMapper = new ModelMapper();
    }

    @Transactional
    public TransaccionDTO procesarTransaccion(int idCuentaOrigen, int idCuentaDestino, BigDecimal monto, String motivo, String tipoTransaccion, int idCanal) {

        Cuenta cuentaOrigen = modelMapper.map(productoService.obtenerCuenta(idCuentaOrigen), Cuenta.class);
        Cuenta cuentaDestino = modelMapper.map(productoService.obtenerCuenta(idCuentaDestino), Cuenta.class);
        Cliente cliente = cuentaOrigen.getCliente();
        CanalDTO canal = catalogoService.encontrarCanalPorId(idCanal);

        Transaccion comprobante = Transaccion.builder()
                .cliente(cliente)
                .tipoTransaccion(TipoTransaccion.tipoTransaccion(tipoTransaccion))
                .productoOrigen(cuentaOrigen)
                .productoDestino(cuentaDestino)
                .canal(modelMapper.map(canal, Canal.class))
                .monto(monto)
                .fechaHora(LocalDateTime.now())
                .motivo(motivo)
                .fallida(false)
                .build();

        comprobante.setEstadoTransaccion(modelMapper.map(catalogoService.encontrarEstadoTransPorId(2), EstadoTransaccion.class));

        transaccionAuditoriaService.registrarTransaccionInicio(comprobante);

        try {

            //Validaciones basicas
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ReglaNegocioException("El monto a transferir debe ser mayor a cero.");
            }
            if (idCuentaOrigen == idCuentaDestino) {
                throw new ReglaNegocioException("No puedes transferir dinero a la misma cuenta.");
            }

            if (productoService.obtenerCuenta(idCuentaDestino) == null) {
                throw new NoEncontradoException("No se encontro la cuenta destino");
            }

            fraudeService.revisarTransaccion(cliente, cuentaOrigen, cuentaDestino, monto, 1, comprobante);

            productoService.ajustarSaldo(cuentaOrigen.getIdProducto(), monto, TipoOperacion.RESTA, comprobante);
            productoService.ajustarSaldo(cuentaDestino.getIdProducto(), monto, TipoOperacion.SUMA, comprobante);
            comprobante.setEstadoTransaccion(modelMapper.map(catalogoService.encontrarEstadoTransPorNombre("aplicada"), EstadoTransaccion.class));
            return modelMapper.map(transaccionRepository.save(comprobante), TransaccionDTO.class);

        } catch (FraudeDetectadoException e) {
            transaccionAuditoriaService.registrarRechazo(comprobante.getIdTransaccion(), e.getMessage());
            throw e;
        }
    }

    public List<TransaccionDTO> transaccionPorCliente(int idCliente) {

        return transaccionRepository.findByCliente(modelMapper.map(clienteService.encontrarClienteId(idCliente), Cliente.class))
                .stream().map(x -> modelMapper.map(x, TransaccionDTO.class)).toList();

    }

    public List<TransaccionDTO> todasLasTransacciones() {
        return transaccionRepository.findAll().stream().map(x -> modelMapper.map(x, TransaccionDTO.class)).collect(Collectors.toList());
    }

    /*
    Metodos para deteccion de fraude
     */

    public long frecuencia(Cuenta cuenta, LocalDateTime tiempoLimite, EstadoTransaccion estadoTransaccion) {
        return transaccionRepository.countByProductoOrigenAndFechaHoraAfterAndEstadoTransaccion(cuenta, tiempoLimite, estadoTransaccion);
    }

    public long multiplesDestinos(Cuenta cuenta, LocalDateTime tiempoLimite) {
        return transaccionRepository.countByProductoOrigenAndFechaHoraAfter(cuenta, tiempoLimite);
    }

    public long intentosFallidos(Cuenta cuenta, LocalDateTime tiempoLimite, EstadoTransaccion estadoTransaccion) {
        return transaccionRepository.countByProductoOrigenAndFechaHoraAfterAndEstadoTransaccion(cuenta, tiempoLimite, estadoTransaccion);
    }


}
