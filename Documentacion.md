# 🌲 DOCUMENTACIÓN COMPLETA - PROYECTOFINALBACK

## Sistema Bancario Integral con Detección de Fraude

> **Una arquitectura robusta y escalable para gestionar operaciones bancarias con seguridad de clase empresarial**

---

## 📋 Tabla de Contenidos

1. [Descripción General](#descripción-general)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Componentes Principales](#componentes-principales)
4. [Modelos de Datos](#modelos-de-datos)
5. [Enumeraciones](#enumeraciones)
6. [Servicios](#servicios)
7. [Controladores](#controladores)
8. [Configuración](#configuración)
9. [Utilidades](#utilidades)
10. [Excepciones](#excepciones)
11. [Repositorios](#repositorios)
12. [Pruebas](#pruebas)
13. [Flujos de Operación](#flujos-de-operación)
14. [Relaciones entre Componentes](#relaciones-entre-componentes)

---

## 🎯 Descripción General

**ProyectoFinalBack** es una API REST construida con **Spring Boot** que implementa un sistema bancario completo con las siguientes capacidades:

### Características Principales:

✅ **Gestión de Clientes** - Registro, actualización y control de estados de clientes  
✅ **Gestión de Empleados** - Administración del personal bancario  
✅ **Productos Financieros** - Créditos, Tarjetas (Débito/Crédito) y Cuentas Bancarias  
✅ **Transacciones** - Transferencias seguras entre cuentas con auditoría completa  
✅ **Detección de Fraude** - Sistema inteligente de reglas dinámicas  
✅ **Auditoría** - Registro completo de todas las operaciones  
✅ **Notificaciones** - Alertas por correo ante actividades sospechosas  
✅ **Documentación Interactiva** - Swagger/OpenAPI integrado  

### Stack Tecnológico:

- **Backend**: Spring Boot 3.x
- **Base de Datos**: PostgreSQL (JPA/Hibernate)
- **Mapeado de Objetos**: ModelMapper 3.2.5
- **Documentación API**: SpringDoc OpenAPI (Swagger 3.0)
- **Seguridad**: Encriptación AES para tarjetas
- **Transacciones**: Manejo transaccional con propagación REQUIRES_NEW
- **Lenguaje**: Java 21
- **Otros**: Lombok, Spring Boot Starter Mail

---

## 🏗️ Estructura del Proyecto

```
ProyectoFinalBack/
│
├── src/main/java/co/edu/unbosque/proyectofinalback/
│   │
│   ├── 📦 config/                           ← Configuraciones de la aplicación
│   │   ├── ModelMapperConfig.java           • Bean de ModelMapper para mapeo DTO-Entity
│   │   └── SwaggerConfig.java               • Configuración de OpenAPI/Swagger
│   │
│   ├── 🎮 controller/                       ← Endpoints REST
│   │   ├── ClienteController.java           • CRUD de clientes
│   │   ├── EmpleadoController.java          • CRUD de empleados
│   │   ├── CatalogoController.java          • Lectura de catálogos (read-only)
│   │   ├── ProductoFinancieroController.java • Gestión de productos
│   │   └── TransaccionController.java       • Procesamiento de transacciones
│   │
│   ├── 📊 model/                            ← Entidades y DTOs
│   │   │
│   │   ├── actores/
│   │   │   ├── Cliente.java                 • Entidad de clientes
│   │   │   ├── ClienteDTO.java              • DTO para transferencia de datos
│   │   │   ├── Empleado.java                • Entidad de empleados
│   │   │   └── EmpleadoDTO.java             • DTO de empleados
│   │   │
│   │   ├── auditoria/
│   │   │   ├── EventoAuditoria.java         • Registro de eventos
│   │   │   └── EventoAuditoriaDTO.java      • DTO de auditoría
│   │   │
│   │   ├── enums/
│   │   │   ├── EstadoActor.java             • {activo, inactivo, bloqueado, suspendido, investigacion}
│   │   │   ├── EstadoProductoEnum.java      • {activo, inactivo, bloqueado, suspendido}
│   │   │   ├── EstadoTransaccionEnum.java   • {pendiente, aplicada, rechazada, cancelada}
│   │   │   ├── Moneda.java                  • {COP, USD, EUR}
│   │   │   ├── Severidad.java               • {baja, media, critica}
│   │   │   ├── TipoCredito.java             • {personal, hipotecario, automotriz, educativo}
│   │   │   ├── TipoCuenta.java              • {ahorros, corriente}
│   │   │   ├── TipoOperacion.java           • {SUMA, RESTA}
│   │   │   ├── TipoResponsable.java         • {cliente, empleado, sistema}
│   │   │   ├── TipoTarjeta.java             • {debito, credito}
│   │   │   └── TipoTransaccion.java         • {transferencia, deposito, retiro}
│   │   │
│   │   ├── fraude/
│   │   │   ├── AlertaFraude.java            • Alerta de fraude detectado
│   │   │   ├── AlertaFraudeDTO.java
│   │   │   ├── AlertaTransaccion.java       • Alerta de transacción bloqueada
│   │   │   ├── AlertaTransaccionDTO.java
│   │   │   ├── EstadoAlerta.java            • {pendiente, revisada, validada, falso_positivo}
│   │   │   ├── EstadoAlertaDTO.java
│   │   │   ├── ReglaFraude.java             • Regla de detección configurable
│   │   │   └── ReglaFraudeDTO.java
│   │   │
│   │   ├── productos/
│   │   │   ├── ProductoFinanciero.java      • Clase padre abstracta
│   │   │   ├── ProductoFinancieroDTO.java
│   │   │   ├── Credito.java                 • Créditos con saldo pendiente
│   │   │   ├── CreditoDTO.java
│   │   │   ├── Tarjeta.java                 • Tarjetas (Débito/Crédito)
│   │   │   ├── TarjetaDTO.java
│   │   │   ├── Cuenta.java                  • Cuentas bancarias
│   │   │   ├── CuentaDTO.java
│   │   │   ├── EstadoProducto.java          • Estado del producto
│   │   │   └── EstadoProductoDTO.java
│   │   │
│   │   └── transaccion/
│   │       ├── Transaccion.java             • Registro de transacciones
│   │       ├── TransaccionDTO.java
│   │       ├── Canal.java                   • Canal de transacción
│   │       ├── CanalDTO.java
│   │       ├── EstadoTransaccion.java       • Estado de la transacción
│   │       └── EstadoTransaccionDTO.java
│   │
│   ├── 💼 service/                          ← Lógica de negocio
│   │   ├── AuditoriaService.java            • Persistencia de eventos (REQUIRES_NEW)
│   │   ├── CatalogoService.java             • Acceso a catálogos
│   │   ├── ClienteService.java              • ⭐ Gestión de clientes (CON AUDITORÍA)
│   │   ├── EmpleadoService.java             • Gestión de empleados
│   │   ├── FraudeService.java               • ⭐ Detección y bloqueo de fraude
│   │   ├── NotificacionService.java         • Envío de correos
│   │   ├── ProductoFinancieroService.java   • ⭐ Gestión de productos
│   │   ├── TransaccionService.java          • ⭐ Procesamiento de transacciones
│   │   └── TransaccionAuditoriaService.java • Auditoría de transacciones
│   │
│   ├── 🗄️ repository/                       ← Acceso a datos (Spring Data JPA)
│   │   ├── ClienteRepositorio.java
│   │   ├── EmpleadoRepositorio.java
│   │   ├── ProductoFinancieroRepositorio.java
│   │   ├── CreditoRepositorio.java
│   │   ├── TarjetaRepositorio.java
│   │   ├── CuentaRepositorio.java
│   │   ├── TransaccionRepositorio.java
│   │   ├── CanalRepositorio.java
│   │   ├── EstadoProductoRepositorio.java
│   │   ├── EstadoTransaccionRepositorio.java
│   │   ├── EventoAuditoriaRepositorio.java
│   │   ├── AlertaFraudeRepositorio.java
│   │   ├── AlertaTransaccionRepositorio.java
│   │   ├── ReglaFraudeRepositorio.java
│   │   └── EstadoAlertaRepositorio.java
│   │
│   ├── 🛠️ util/                             ← Utilidades y manejo de errores
│   │   ├── GlobalExceptionHandler.java      • Manejador centralizado de excepciones
│   │   ├── FuncionesTarjeta.java            • Encriptación y generación de tarjetas
│   │   └── exception/
│   │       ├── NoEncontradoException.java   • HTTP 404
│   │       ├── ReglaNegocioException.java   • HTTP 400
│   │       ├── FraudeDetectadoException.java• HTTP 403
│   │       └── RecursoDuplicadoException.java• HTTP 409
│   │
│   └── ProyectoFinalBackApplication.java     • Punto de entrada (Spring Boot)
│
└── resources/
    └── application.properties                • Configuración de BD y correos
```

---

## 🧩 Componentes Principales

### 1. **CONTROLADORES (Endpoints REST)**

Todos los controladores tienen CORS habilitado (`@CrossOrigin(origins = "*")`) y documentación Swagger.

#### **ClienteController** 🧑‍💼
```
Base: /clientes
├─ POST   /crear                    → Crear nuevo cliente
├─ POST   /eliminarPorId/{id}       → Desactivar cliente por ID
├─ POST   /eliminarPorDocumento/{doc} → Desactivar por documento
├─ PUT    /actualizarPorId          → Actualizar datos cliente
├─ POST   /actualizarPorDocumento   → Actualizar por documento
├─ POST   /actualizarEstado         → Cambiar estado (Activo/Inactivo)
├─ GET    /encontrarClienteId       → Buscar por ID
├─ GET    /econtrarClienteDocumento → Buscar por documento
└─ GET    /todosLosClientes         → Listar todos
```

#### **EmpleadoController** 👔
```
Base: /empleados
├─ POST   /crear                    → Crear empleado
├─ POST   /eliminar                 → Desactivar empleado
├─ POST   /actualizar               → Actualizar datos
├─ POST   /actualizarEstado         → Cambiar estado
├─ GET    /encontrarEmpleadoId      → Buscar por ID
└─ GET    /encontrarTodos           → Listar todos
```

#### **CatalogoController** 📚 (READ-ONLY)
```
Base: /catalogo
├─ GET    /canales                  → Todos los canales
├─ GET    /estadosTransaccion       → Estados de transacciones
├─ GET    /estadosProductos         → Estados de productos
├─ GET    /estadosAlerta            → Estados de alertas
└─ GET    /ReglasFraude             → Reglas de fraude activas
```

#### **ProductoFinancieroController** 💳

**Créditos:**
```
Base: /productos-financieros/credito
├─ POST   /crear                    → Crear crédito
├─ POST   /pagarCredito             → Realizar abono
├─ POST   /ajusteMontoCredito       → Modificar monto aprobado
├─ POST   /cambiarEstadoProducto    → Cambiar estado
├─ GET    /obtenerCredito           → Obtener por ID
├─ GET    /obtenerTodosLosCreditoPorCliente → Por cliente
└─ GET    /obtenerTodosLosCredito   → Listar todos
```

**Tarjetas:**
```
Base: /productos-financieros/tarjeta
├─ POST   /crearTarjeta             → Crear tarjeta (Débito/Crédito)
├─ POST   /desactivarId             → Desactivar por ID
├─ POST   /desactivarNumero         → Desactivar por número
├─ POST   /actualizacionEstado      → Cambiar estado
├─ POST   /actualizacionCupo        → Modificar límite de crédito
├─ POST   /renovarFechaVencimiento  → Extender validez
├─ GET    /encontrartarjetaPorId    → Obtener por ID
└─ GET    /listarTarjetas           → Listar todas
```

**Cuentas:**
```
Base: /productos-financieros/cuenta
├─ POST   /crearCuenta              → Crear cuenta (Ahorros/Corriente)
├─ POST   /desactivarCuenta         → Desactivar cuenta
├─ POST   /actualizarEstado         → Cambiar estado
├─ GET    /obtenerCuenta            → Obtener por ID
├─ GET    /obtenerTodasCuentasPorCliente → Por cliente
└─ GET    /todasLasCuentas          → Listar todas
```

#### **TransaccionController** 💸
```
Base: /transacciones
├─ POST   /procesarTransaccion      → Transferir entre cuentas ⚠️ CON VALIDACIÓN DE FRAUDE
├─ GET    /transaccionPorCliente    → Por cliente
└─ GET    /todasLasTransacciones    → Listar todas
```

---

## 📊 Modelos de Datos

### **Actores**

#### Cliente
```
Cliente
├── idCliente (PK)
├── tipoDocumento
├── numeroDocumento (UNIQUE)
├── nombre
├── apellido
├── direccionPrincipal
├── ciudad
├── telefono
├── email
├── estadoCliente (EstadoActor)
├── fechaAlta
└── [Relaciones]
    ├── 1:M → Productos Financieros
    ├── 1:M → Transacciones
    └── 1:M → Eventos de Auditoría
```

#### Empleado
```
Empleado
├── idEmpleado (PK)
├── nombre
├── apellido
├── cargo
├── estadoEmpleado (EstadoActor)
├── fechaAlta
└── [Relaciones]
    └── 1:M → Eventos de Auditoría
```

### **Productos Financieros**

#### ProductoFinanciero (Superclase)
```
ProductoFinanciero (Abstract)
├── idProducto (PK)
├── numeroProducto (UNIQUE)
├── cliente (FK)
├── fechaCreacion
└── estadoProducto (FK)
```

#### Credito
```
Credito extends ProductoFinanciero
├── tipoCredito (TipoCredito)
├── montoAprobado (BigDecimal)
├── saldoPendiente (BigDecimal)
├── tasaInteres (%)
└── fechaDesembolso
```

#### Tarjeta
```
Tarjeta extends ProductoFinanciero
├── tipoTarjeta (TipoTarjeta: débito/crédito)
├── numeroEnmascarado (Encriptado AES)
├── fechaVencimiento (MM/yy)
└── cupo (BigDecimal, null para débito)
```

#### Cuenta
```
Cuenta extends ProductoFinanciero
├── tipoCuenta (TipoCuenta: ahorros/corriente)
├── moneda (Moneda: COP/USD/EUR)
└── saldoCuenta (BigDecimal)
```

### **Auditoría**

#### EventoAuditoria
```
EventoAuditoria
├── idEvento (PK)
├── cliente (FK, obligatorio)
├── empleado (FK, nullable)
├── producto (FK, nullable)
├── transaccion (FK, nullable)
├── canal (FK)
├── tipoResponsable (TipoResponsable)
├── accion (String)
├── motivo (String)
├── fechaHora (LocalDateTime)
└── [Propósito]
    → Registro completo de quién, qué, cuándo y por qué
```

### **Transacciones**

#### Transaccion
```
Transaccion
├── idTransaccion (PK)
├── cliente (FK)
├── productoOrigen (FK → Cuenta)
├── productoDestino (FK → Cuenta)
├── canal (FK)
├── monto (BigDecimal)
├── fechaHora (LocalDateTime)
├── motivo (String)
├── estadoTransaccion (FK)
└── [Flujo]
    → INICIO (registrada con estado pendiente)
    → VALIDACIÓN DE FRAUDE
    → AJUSTE DE SALDOS
    → APLICADA o RECHAZADA
```

### **Fraude**

#### ReglaFraude
```
ReglaFraude
├── idRegla (PK)
├── nombre (String, UNIQUE)
├── tipoRegla (String)
│   ├── "monto_umbral"
│   ├── "frecuencia"
│   ├── "multiples_destinos"
│   ├── "intentos_fallidos"
│   └── "canal_inusual"
├── umbral (BigDecimal, nullable)
├── maxItentos (Integer)
├── ventanaMinutos (LocalTime)
├── activa (S/N)
└── descripcion
```

#### AlertaFraude
```
AlertaFraude
├── idAlerta (PK)
├── regla (FK)
├── tipoAlerta (String)
├── severidad (Severidad: baja/media/critica)
├── descripcion
├── fechaHora (LocalDateTime)
└── estadoAlerta (FK)
```

#### AlertaTransaccion
```
AlertaTransaccion
├── idAlerta (PK)
├── transaccion (FK)
└── fechaAsociacion (LocalDateTime)
```

---

## 🎲 Enumeraciones

| Enum | Valores | Uso |
|------|---------|-----|
| **EstadoActor** | activo, inactivo, bloqueado, suspendido, investigacion | Clientes y Empleados |
| **EstadoProductoEnum** | activo, inactivo, bloqueado, suspendido | Tarjetas, Créditos, Cuentas |
| **EstadoTransaccionEnum** | pendiente, aplicada, rechazada, cancelada | Transacciones |
| **TipoTarjeta** | debito, credito | Tarjetas |
| **TipoCuenta** | ahorros, corriente | Cuentas |
| **TipoCredito** | personal, hipotecario, automotriz, educativo | Créditos |
| **Moneda** | COP, USD, EUR | Cuentas |
| **TipoOperacion** | SUMA, RESTA | Ajuste de saldos |
| **TipoResponsable** | cliente, empleado, sistema | Auditoría |
| **Severidad** | baja, media, critica | Alertas de fraude |

---

## 💼 Servicios

Los servicios contienen toda la lógica de negocio y son donde ocurre la **inteligencia** del sistema.

### ⭐ **ClienteService** - Gestión de Clientes

**Responsabilidades:**
- Crear nuevos clientes
- Actualizar información de clientes
- Gestionar estados (Activo/Inactivo)
- Búsqueda por ID o Documento
- **Auditoría automática en cada operación**

**Métodos Principales:**

```java
// Crear cliente - AUDITORÍA INTEGRADA
public ClienteDTO crear(ClienteDTO clienteDTO)
├─ Valida que no exista cliente con mismo ID
├─ Establece estado ACTIVO y fecha de alta
├─ Guarda en base de datos
└─ ➜ Audita: "Se creo un cliente"

// Actualizar cliente - AUDITORÍA INTEGRADA
public ClienteDTO actualizarPorId(ClienteDTO clienteDTO)
├─ Verifica existencia del cliente
├─ Actualiza: tipoDocumento, nombre, apellido, dirección, etc.
└─ ➜ Audita: "Se actualizacion los datos de un cliente"

// Desactivar cliente - AUDITORÍA INTEGRADA
public boolean eliminarPorId(int id)
├─ Busca cliente por ID
├─ Cambia estado a INACTIVO (eliminación lógica)
└─ ➜ Audita: "Se desactivo un cliente"

// Actualizar estado
public ClienteDTO actualizarEstado(int id, String estado)

// Búsquedas
public ClienteDTO encontrarClienteId(int id)
public ClienteDTO encontrarClienteDocumento(String documento)
public List<ClienteDTO> todosLosClientes()
```

**Dependencias:**
- `ClienteRepositorio` - Acceso a datos
- `CatalogoService` - Obtener canal para auditoría
- `AuditoriaService` - Registrar eventos
- `ModelMapper` - Mapeo DTO ↔ Entity

**Relaciones:**
```
ClienteService
├─ Utilizado por: ClienteController
└─ Utiliza:
   ├─ ClienteRepositorio
   ├─ CatalogoService
   └─ AuditoriaService
```

---

### ⭐ **CatalogoService** - Datos Estáticos (Read-Only)

**Responsabilidades:**
- Acceso a catálogos (Canales, Estados, Reglas)
- Validación de existencia
- Búsqueda por ID o nombre

**Métodos Principales:**

```java
// CANALES
public CanalDTO encontrarCanalPorId(int idCanal)
public CanalDTO encontrarCanalPorNombre(String nombre)
public List<CanalDTO> encontrarTodosLosCanales()

// ESTADOS DE TRANSACCIÓN
public EstadoTransaccionDTO encontrarEstadoTransPorId(int id)
public EstadoTransaccionDTO encontrarEstadoTransPorNombre(String nombre)
public List<EstadoTransaccionDTO> encontrarTodosEstadosTrans()

// ESTADOS DE PRODUCTO
public EstadoProductoDTO encontrarEstadoProductoPorId(int id)
public EstadoProductoDTO encontrarEstadoProductoPorNombre(String nombre)
public List<EstadoProductoDTO> encontrarTodosEstadosProductos()

// ESTADOS DE ALERTA
public EstadoAlertaDTO encontrarAlertaPorId(int id)
public EstadoAlertaDTO encontrarAlertaPorNombre(String nombre)
public List<EstadoAlertaDTO> encontrarTodosEstadosAlerta()

// REGLAS DE FRAUDE
public ReglaFraudeDTO encontrarReglaFraudePorId(int id)
public ReglaFraudeDTO encontrarReglaFraudePorNombre(String nombre)
public List<ReglaFraudeDTO> encontrarTodasReglasFraude()
```

**Características:**
- ✅ Validación de existencia en BD
- ✅ Excepciones personalizadas (NoEncontradoException)
- ✅ Parsing automático de enums
- ✅ Caché implícita en repositorios

---

### ⭐ **EmpleadoService** - Gestión de Empleados

**Responsabilidades:**
- CRUD completo de empleados
- Gestión de estados
- Minimal (sin auditoría explícita como ClienteService)

**Métodos:**

```java
public EmpleadoDTO crear(EmpleadoDTO empleadoDTO)
├─ Crea empleado con estado ACTIVO
└─ Establece fecha de alta

public boolean eliminar(int idEmpleado)
├─ Cambia estado a INACTIVO

public EmpleadoDTO actualizar(EmpleadoDTO empleadoDTO)
├─ Actualiza nombre, apellido, cargo

public EmpleadoDTO actualizarEstado(int idEmpleado, String estado)

public EmpleadoDTO encontrarEmpleadoId(int idEmpleado)
public List<EmpleadoDTO> encontrarTodos()
```

---

### ⭐ **FraudeService** - Motor de Detección de Fraude

**🚨 COMPONENTE CRÍTICO DEL SISTEMA**

**Responsabilidades:**
- Análisis de transacciones contra reglas
- Detección de patrones sospechosos
- Bloqueo automático y notificación
- Auditoría de eventos de fraude

**Método Principal:**

```java
public void revisarTransaccion(
    Cliente cliente,
    Cuenta cuenta,
    Cuenta cuentaDestino,
    BigDecimal monto,
    int canalId,
    Transaccion transaccion)
```

**Flujo de Validación:**

1. **Validación de Estado del Beneficiario**
   ```
   Si (cuentaDestino.cliente está: bloqueado | investigacion | inactivo | suspendido)
   └─ ➜ BLOQUEAR + AUDITAR + NOTIFICAR
   ```

2. **Validación de Reglas Activas**
   ```
   for cada ReglaFraude activa:
   ```

   a) **monto_umbral**
   ```
   if (monto > regla.umbral)
      └─ AlertaFraude (severidad: BAJA)
      └─ Bloquer y notificar
   ```

   b) **frecuencia**
   ```
   transaccionesExitosas = contar en ventana de tiempo
   if (transaccionesExitosas >= regla.maxItentos)
      └─ AlertaFraude (severidad: MEDIA)
      └─ Bloquear y notificar
   ```

   c) **multiples_destinos**
   ```
   destinosDiferentes = contar cuentas destino en ventana
   if (destinosDiferentes >= regla.maxItentos)
      └─ AlertaFraude (severidad: MEDIA)
      └─ Bloquear y notificar
   ```

   d) **intentos_fallidos**
   ```
   intentosFallidos = contar transacciones RECHAZADAS en ventana
   if (intentosFallidos >= regla.maxItentos)
      └─ AlertaFraude (severidad: MEDIA)
      └─ Bloquear y notificar
   ```

   e) **canal_inusual**
   ```
   if (canalId no reconocido)
      └─ AlertaFraude (severidad: CRITICA)
      └─ Bloquear y notificar
   ```

**Resultado:**
```
✓ Si pasa todas validaciones → Transacción continúa
✗ Si falla cualquier validación → FraudeDetectadoException (HTTP 403)
```

**Dependencias:**
- `NotificacionService` - Enviar correos de bloqueo
- `ReglaFraudeRepositorio` - Obtener reglas activas
- `TransaccionService` - Contar transacciones para análisis
- `CatalogoService` - Validar canales
- `AuditoriaService` - Registrar evento de fraude

---

### ⭐ **NotificacionService** - Sistema de Alertas

**Responsabilidades:**
- Envío de correos de seguridad
- Notificación de transacciones bloqueadas

**Métodos:**

```java
public void enviarNotificacionBloqueo(
    String correoDestino,
    String razon,
    String monto,
    String destinatario,
    LocalDateTime fechaHora)
```

**Formato de Correo:**
```
Asunto: SEGURIDAD BOSQUEBANK 🌲

--------------------------------------------------------------------
TRANSACCION BLOQUEADA - Esta transacción se ha cancelado por: [RAZON]

Detalles de la operación:
------------------------------------------
Motivo: [RAZON]
Monto: [MONTO]
Enviado a: [DESTINARIO]
Hora exacta: [FECHA/HORA]
------------------------------------------

Si no reconoce esta actividad, por favor comuníquese de inmediato
con nuestras líneas de atención.
--------------------------------------------------------------------
```

**Configuración:**
- Remitente: `bosquebank@gmail.com`
- Manejo de excepciones: try-catch (log en fallos)

---

### ⭐ **ProductoFinancieroService** - Gestión de Productos

**🏦 SERVICIO COMPLEJO - 481 LÍNEAS**

**Responsabilidades:**
- Crear y gestionar Créditos, Tarjetas y Cuentas
- Encriptación de números de tarjeta
- Auditoría en cada operación
- Control de estados y límites

#### **Créditos:**

```java
// Crear crédito
public CreditoDTO crearCredito(
    CreditoDTO credito,
    int idCliente,
    String responsable)
├─ Genera número de producto único
├─ Establece estado ACTIVO
├─ Saldo pendiente = Monto aprobado
├─ Desembolso en 1 mes
└─ ➜ Audita operación

// Pagar crédito
public CreditoDTO pagarCredito(
    int idCredito,
    BigDecimal montoPago)
├─ Valida saldo pendiente > 0
├─ Valida monto > 0 y ≤ saldo pendiente
├─ Reduce saldo pendiente
└─ ➜ Audita operación

// Ajustar monto
public CreditoDTO ajusteMontoCredito(
    int idCredito,
    BigDecimal nuevoMonto,
    String responsable)
├─ Previene ajuste si está bloqueado
└─ ➜ Audita cambio

// Cambiar estado
public CreditoDTO cambiarEstadoProducto(
    int idCredito,
    int estadoProducto,
    String responsable)
```

#### **Tarjetas:**

```java
// Crear tarjeta
public TarjetaDTO creartarjeta(
    int idCliente,
    String tipoTarjeta,
    BigDecimal cupo)
├─ Genera número enmascarado (AES)
├─ Crea número según tipo:
│  └─ Débito: 2207XXXXXXXXXXXX
│  └─ Crédito: 2208XXXXXXXXXXXX
├─ Fecha vencimiento: +5 años (MM/yy)
└─ ➜ Audita operación

// Desactivar tarjeta
public boolean desactivarTarjetaID(
    int idTarjeta,
    String responsable)
├─ Cambia estado a INACTIVO
└─ ➜ Audita desactivación

// Actualizar cupo
public TarjetaDTO actualizacionCupo(
    int idTarjeta,
    BigDecimal cupo,
    int idEmpleado)
├─ Valida: cupo no sea negativo
├─ Valida: tarjeta no sea DÉBITO
└─ ➜ Audita cambio con empleado responsable

// Renovar vencimiento
public TarjetaDTO renovarFechaVencimiento(
    int idTarjeta,
    int idEmpleado)
├─ Genera fecha vencimiento: +5 años
└─ ➜ Audita renovación
```

#### **Cuentas:**

```java
// Crear cuenta
public CuentaDTO crearCuenta(
    int idCliente,
    String tipoCuenta,
    String moneda)
├─ Genera número de cuenta único
├─ Estado: ACTIVO
├─ Saldo inicial: $0
└─ ➜ Audita creación

// Desactivar cuenta
public boolean desactivarCuenta(int idProducto)
├─ Cambia a INACTIVO
└─ ➜ Audita desactivación

// Ajustar saldo (CRÍTICO)
public void ajustarSaldo(
    int idCuenta,
    BigDecimal num,
    TipoOperacion tipoOperacion,
    Transaccion transaccion)
├─ if RESTA:
│  ├─ Valida saldo suficiente
│  ├─ Resta del saldo
│  └─ ➜ Audita "Se resto..."
├─ if SUMA:
│  ├─ Suma al saldo
│  └─ ➜ Audita "Se sumo..."
└─ Throw ReglaNegocioException si insuficiente
```

**Utilidades Internas:**

```java
// Generar número único de producto
private String generarNumeroProducto()
├─ Formato: PF-XXXXXX (6 dígitos)
├─ Valida no exista en BD
└─ Retenta hasta generar único

// Auditoría integrada
private void auditar(Cliente cliente, ProductoFinanciero pf, String accion, String motivo, String responsable)
├─ Crea EventoAuditoria
├─ Asigna responsable (cliente/empleado/sistema)
└─ Delega a AuditoriaService
```

**Seguridad:**
- 🔒 Encriptación AES de números de tarjeta
- 🔒 Validaciones de monto y estados
- 🔒 Auditoría completa

---

### ⭐ **TransaccionService** - Orquestador de Transferencias

**🔄 FLUJO TRANSACCIONAL COMPLETO**

**Método Principal:**

```java
@Transactional
public TransaccionDTO procesarTransaccion(
    int idCuentaOrigen,
    int idCuentaDestino,
    BigDecimal monto,
    String motivo)
```

**Flujo Paso a Paso:**

```
1. CARGAR CUENTAS
   ├─ Obtener cuenta origen
   └─ Obtener cuenta destino

2. CREAR COMPROBANTE
   └─ Transaccion con estado PENDIENTE

3. REGISTRAR INICIO
   ├─ TransaccionAuditoriaService.registrarTransaccionInicio()
   └─ Guarda en BD para trazabilidad

4. VALIDACIONES BÁSICAS
   ├─ Monto > 0
   ├─ Origen ≠ Destino
   └─ Destino existe

5. REVISIÓN DE FRAUDE ⚠️
   └─ FraudeService.revisarTransaccion()
      ├─ Si detecta fraude → Excepción
      └─ Si pasa → Continuar

6. AJUSTAR SALDOS
   ├─ Restar de origen (RESTA)
   ├─ Sumar a destino (SUMA)
   └─ Cada operación genera auditoría

7. MARCAR APLICADA
   └─ Estado = "aplicada"

8. GUARDAR EN BD
   └─ Return TransaccionDTO
```

**Manejo de Excepciones:**

```java
try {
    // ... flujo normal ...
} catch (FraudeDetectadoException e) {
    ├─ TransaccionAuditoriaService.registrarRechazo()
    ├─ Estado = "Rechazada"
    ├─ Motivo = mensaje de fraude
    └─ Re-lanza excepción (HTTP 403)
}
```

**Métodos de Análisis:**

```java
// Para detección de fraude
public long frecuencia(
    Cuenta cuenta,
    LocalDateTime tiempoLimite,
    EstadoTransaccion estado)
└─ Contar transacciones en ventana de tiempo

public long multiplesDestinos(
    Cuenta cuenta,
    LocalDateTime tiempoLimite)
└─ Contar destinos únicos en ventana

public long intentosFallidos(
    Cuenta cuenta,
    LocalDateTime tiempoLimite,
    EstadoTransaccion estado)
└─ Contar transacciones rechazadas en ventana
```

---

### ⭐ **AuditoriaService** - Persistencia de Eventos

**Característica Especial:** `@Transactional(propagation = Propagation.REQUIRES_NEW)`

```java
public void guardarEvento(EventoAuditoria evento)
```

**Por qué REQUIRES_NEW?**
- Garantiza que auditoría se guarda INCLUSO si transacción principal falla
- Aislamiento completo de las transacciones
- Trazabilidad garantizada

---

### ⭐ **TransaccionAuditoriaService** - Auditoría de Transacciones

**Métodos:**

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void registrarTransaccionInicio(Transaccion transaccion)
└─ Guarda transacción con estado inicial

@Transactional(propagation = Propagation.REQUIRES_NEW)
public void registrarRechazo(int id, String motivo)
├─ Busca transacción por ID
├─ Cambia estado a "Rechazada"
├─ Asigna motivo (razón del rechazo)
└─ Guarda en BD
```

**Propósito:**
- Registrar transacciones fallidas por fraude
- Mantener auditoría incluso en escenarios de rollback

---

## 🎮 Controladores

### Mapeo de Endpoints

| Controlador | Ruta Base | Métodos | Descripción |
|------------|-----------|---------|-------------|
| **ClienteController** | `/clientes` | 8 | CRUD + auditoría |
| **EmpleadoController** | `/empleados` | 6 | CRUD básico |
| **CatalogoController** | `/catalogo` | 5 | Lectura de catálogos |
| **ProductoFinancieroController** | `/productos-financieros` | 24 | Créditos, Tarjetas, Cuentas |
| **TransaccionController** | `/transacciones` | 3 | Procesamiento y consultas |

### Decoradores Comunes

Todos los controladores usan:

```java
@RestController                     // Respuestas JSON automáticas
@RequestMapping("/ruta")            // Ruta base
@CrossOrigin(origins = "*")         // CORS habilitado
@Tag(name = "...", description)     // Swagger
public class XxxController { }
```

### Anotaciones en Métodos

```java
@Operation(summary = "...", description = "...")  // Swagger
@ApiResponse(responseCode = "200", description = "...")
@PostMapping("/ruta")               // HTTP POST
@GetMapping("/ruta")                // HTTP GET
@PutMapping("/ruta")                // HTTP PUT
@RequestParam Type param            // Query/Form params
@RequestBody Type body              // JSON body
@PathVariable Type id               // Path params
```

---

## ⚙️ Configuración

### **ModelMapperConfig**

```java
@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();          // Mapeo automático DTO ↔ Entity
    }
}
```

**Uso:**
```java
ClienteDTO dto = modelMapper.map(cliente, ClienteDTO.class);
Cliente entity = modelMapper.map(clienteDTO, Cliente.class);
```

### **SwaggerConfig**

```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Mi Sistema Bancario")
                .version("1.0")
                .description("Documentación de endpoints..."));
    }
}
```

**Acceso:** `http://localhost:8080/swagger-ui.html`

### **application.properties**

```properties
# Base de datos (ejemplo PostgreSQL)
spring.datasource.url=jdbc:postgresql://localhost:5432/BancoDB
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update

# Correo (para notificaciones)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=bosquebank@gmail.com
spring.mail.password=xxxx xxxx xxxx xxxx
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

---

## 🛠️ Utilidades

### **FuncionesTarjeta** - Utilitarios Criptográficos

```java
// Generar número de tarjeta válido
public static String crearNumero(String tipo)
├─ Débito: 2207 + 12 dígitos aleatorios
└─ Crédito: 2208 + 12 dígitos aleatorios

// Encriptar número (AES)
public static String enmascarar(String numeroTarjeta)
├─ Algoritmo: AES
├─ Clave: "1234567890123456"
└─ Salida: Base64 encriptado

// Desencriptar número
public static String desenmascarar(String numeroTarjeta)

// Generar fecha vencimiento
public static String generarVencimiento()
├─ Formato: MM/yy
└─ Validez: 5 años
```

---

## ⚠️ Excepciones

### **GlobalExceptionHandler**

Manejador centralizado de excepciones con responses estándar:

```json
{
    "fecha": "2026-05-05T14:30:45.123",
    "estado": 400,
    "error": "Bad Request",
    "mensaje": "ERROR: Ya existe un cliente con el id: 1"
}
```

### Excepciones Personalizadas

| Excepción | HTTP | Uso | Ejemplo |
|-----------|------|-----|---------|
| `NoEncontradoException` | 404 NOT FOUND | Recurso no existe | Cliente, producto no encontrado |
| `ReglaNegocioException` | 400 BAD REQUEST | Validación de negocio | Saldo insuficiente, monto negativo |
| `FraudeDetectadoException` | 403 FORBIDDEN | Fraude detectado | Transacción bloqueada |
| `RecursoDuplicadoException` | 409 CONFLICT | Duplicado | Cliente ID ya existe |
| `Exception` (genérica) | 500 INTERNAL ERROR | Errores no manejados | Bugs, errores de BD |

---

## 🗄️ Repositorios

Todos extienden `JpaRepository<Entity, ID>` y acceso a datos automático.

### **Principales:**

```java
// ClienteRepositorio
existsClienteByIdCliente(id) : boolean
findClienteByIdCliente(id) : Cliente
existsByNumeroDocumento(doc) : boolean
findByNumeroDocumento(doc) : Cliente

// CreditoRepositorio
existsCreditoByIdProducto(id) : boolean
findCreditosByIdProducto(id) : Credito
findAllByCliente(cliente) : List<Credito>

// TarjetaRepositorio
existsByIdProducto(id) : boolean
findTarjetaByIdProducto(id) : Tarjeta
existsByNumeroEnmascarado(num) : boolean
findByNumeroEnmascarado(num) : Tarjeta

// CuentaRepositorio
existsCuentaByIdProducto(id) : boolean
findCuentaByIdProducto(id) : Cuenta
findAllByCliente(cliente) : List<Cuenta>

// TransaccionRepositorio
findByCliente(cliente) : List<Transaccion>
countByProductoOrigenAndFechaHoraAfterAndEstadoTransaccion(...) : long
countByProductoOrigenAndFechaHoraAfter(...) : long

// CatalogoRepositorios
findByNombre, findById, existsBy*, etc.

// AlertaFraudeRepositorio
Gestión de alertas de fraude

// EventoAuditoriaRepositorio
Gestión de eventos de auditoría
```

---

## 🧪 Pruebas

El proyecto incluye pruebas básicas con Spring Boot Test.

### **ProyectoFinalBackApplicationTests**

- Prueba de carga del contexto de Spring Boot
- Verifica que la aplicación se inicie correctamente sin errores

**Ejecutar pruebas:**
```bash
mvn test
```

**Nota:** Las pruebas son mínimas; se recomienda expandir con pruebas unitarias e integrales para servicios críticos como FraudeService y TransaccionService.

---

## 🔄 Flujos de Operación

### **Flujo 1: Crear Cliente (CON AUDITORÍA)**

```
CLIENT                  CONTROLLER              SERVICE    REPOSITORY   AUDITORIA   BD
  │                          │                      │            │          │        │
  ├─ POST /clientes/crear    │                      │            │          │        │
  ├──────────────────────────>│                      │            │          │        │
  │      (ClienteDTO)         │                      │            │          │        │
  │                           │ crear(DTO)          │            │          │        │
  │                           ├─────────────────────>│            │          │        │
  │                           │                      │ Validar    │          │        │
  │                           │                      │ ID único   │          │        │
  │                           │                      ├───────────>│          │        │
  │                           │                      │            │ Existe?  │        │
  │                           │                      │            ├─────────>│        │
  │                           │                      │            │ (S/N)    │        │
  │                           │                      │            │<─────────┤        │
  │                           │                      │<───────────┤          │        │
  │                           │                      │ save()     │          │        │
  │                           │                      ├───────────────────────────────>│
  │                           │                      │            │          │   Insertar
  │                           │                      │            │          │        │
  │                           │                      │ auditar()  │          │        │
  │                           │                      ├────────────────────────>│       │
  │                           │                      │            │          │ Guardar evento
  │                           │                      │            │          ├──────>│
  │                           │                      │            │          │        │
  │                          DTO<───────────────────┤            │          │        │
  │                           │                      │            │          │        │
  │<─── 201 CREATED ──────────┤                      │            │          │        │
  │      (ClienteDTO)         │                      │            │          │        │
```

### **Flujo 2: Procesar Transacción (CON FRAUDE)**

```
CLIENT                  CONTROLLER              SERVICE            REPOSITORY        BD
  │                          │                      │                   │             │
  ├─ POST /transacciones     │                      │                   │             │
  ├──────────────────────────>│                      │                   │             │
  │ procesarTransaccion()    │                      │                   │             │
  │                           │ procesarTransaccion()│                   │             │
  │                           ├────────────────────>│                   │             │
  │                           │                      │ 1. Cargar cuentas │             │
  │                           │                      ├──────────────────>│             │
  │                           │                      │<──────────────────┤             │
  │                           │                      │                   │             │
  │                           │                      │ 2. Crear comprobante
  │                           │                      │ (Transaccion)      │             │
  │                           │                      │                   │             │
  │                           │                      │ 3. Registrar inicio
  │                           │                      │ TransaccionAuditoria
  │                           │                      ├──────────────────────────────> │
  │                           │                      │                   │      BEGIN │
  │                           │                      │                   │             │
  │                           │                      │ 4. Validaciones   │             │
  │                           │                      │    ✓ monto > 0    │             │
  │                           │                      │    ✓ origen ≠ dest│             │
  │                           │                      │    ✓ dest existe  │             │
  │                           │                      │                   │             │
  │                           │                      │ 5. REVISAR FRAUDE │             │
  │                           │                      │ FraudeService     │             │
  │                           │                      ├──────────────────>│             │
  │                           │                      │ a) Estado benefic. │             │
  │                           │                      │    OK              │             │
  │                           │                      │ b) Validar reglas  │             │
  │                           │                      │    ✓ Monto OK     │             │
  │                           │                      │    ✓ Frecuencia OK│             │
  │                           │                      │ → RESULTADO: OK    │             │
  │                           │                      │<──────────────────┤             │
  │                           │                      │                   │             │
  │                           │                      │ 6. Ajustar saldos │             │
  │                           │                      │ a) Restar origen  │             │
  │                           │                      ├──────────────────────────────> │
  │                           │                      │    UPDATE cuenta  │             │
  │                           │                      │                   │             │
  │                           │                      │ b) Sumar destino  │             │
  │                           │                      ├──────────────────────────────> │
  │                           │                      │    UPDATE cuenta  │             │
  │                           │                      │                   │             │
  │                           │                      │ 7. Estado = "aplicada"
  │                           │                      │                   │             │
  │                           │                      │ 8. SAVE           │             │
  │                           │                      ├──────────────────────────────> │
  │                           │                      │    INSERT transaction
  │                           │                      │                   │             │
  │                          DTO<───────────────────┤                   │             │
  │                           │                      │                   │             │
  │<─────── 200 OK ───────────┤                      │                   │             │
  │      (TransaccionDTO)     │                      │                   │             │
```

### **Flujo 3: Fraude Detectado**

```
   ┌─────────────────────────────────────────────┐
   │   FraudeService.revisarTransaccion()        │
   │                                             │
   │  Detecta: Monto > Umbral o Múltiples Desti│
   └────────────┬────────────────────────────────┘
                │
                │ FraudeDetectadoException
                │
         ┌──────▼──────┐
         │  BLOQUEAR   │
         └──────┬──────┘
                │
       ┌────────┴─────────┐
       │                  │
   ┌───▼────┐      ┌─────▼──────┐
   │Auditar │      │  Notificar │
   │evento  │      │  por correo│
   └───┬────┘      └─────┬──────┘
       │                 │
       │          Email: SEGURIDAD BOSQUEBANK 🌲
       │          Motivo: Monto excesivo
       │          Monto: $50,000
       │          Etc...
       │
   ┌───▼────────────────────┐
   │ Re-lanzar excepción    │
   │ HTTP 403 FORBIDDEN     │
   └────────────────────────┘
        │
        ├─ Cliente recibe error
        └─ Transacción RECHAZADA
           Estado = "Rechazada"
           Saldos SE REVIERTEN ✓
```

---

## 🌐 Relaciones entre Componentes

### **Jerarquía de Dependencias**

```
                    ┌─────────────────────────────┐
                    │   CONTROLADORES             │
                    │  (API REST)                 │
                    └──────┬──────────────────────┘
                           │
            ┌──────────────┼──────────────┐
            │              │              │
      ┌─────▼──┐    ┌─────▼──┐    ┌─────▼──┐
      │CLIENTE │    │EMPLEADO│    │CATALOGO│
      │SERVICE │    │SERVICE │    │SERVICE │
      └─────┬──┘    └─────┬──┘    └─────┬──┘
            │              │              │
            └──────────────┼──────────────┘
                           │
            ┌──────────────┼──────────────┐
            │              │              │
      ┌─────▼────┐   ┌─────▼──────┐   ┌─────▼─────┐
      │AUDITORIA │   │PRODUCTO    │   │FRAUDE     │
      │SERVICE   │   │FINANCIERO  │   │SERVICE    │
      └──────────┘   │SERVICE     │   └─────┬─────┘
                     └─────┬──────┘         │
                           │                │
            ┌──────────────┼────────────────┤
            │              │                │
      ┌─────▼──────┐  ┌────▼───────┐  ┌────▼──────┐
      │TRANSACCION │  │NOTIFICACION│  │TRANSACCION│
      │SERVICE     │  │SERVICE     │  │AUDITORIA  │
      └─────┬──────┘  └────────────┘  │SERVICE    │
            │                         └───────────┘
      ┌─────▼──────────────┐
      │  REPOSITORIOS      │
      │  (JPA / Hibernate) │
      └────────┬───────────┘
               │
        ┌──────▼──────┐
        │  BASE DATOS │
        │  (PostgreSQL) │
        └─────────────┘
```

### **Flujo de Datos: Crear Cliente**

```
ClienteController
    ├─ @PostMapping("/crear")
    ├─ Recibe: ClienteDTO
    │
    └─> ClienteService.crear(ClienteDTO)
         ├─ Validar ID único (ClienteRepositorio.exists)
         ├─ Mapear DTO a Entity (ModelMapper)
         ├─ Guardar (ClienteRepositorio.save)
         │
         └─> Auditar
              ├─ ClienteService.auditar()
              ├─ Crear EventoAuditoria
              ├─ CatalogoService.encontrarCanalPorId(1)
              │
              └─> AuditoriaService.guardarEvento(evento)
                   └─ EventoAuditoriaRepositorio.save
                       (Con REQUIRES_NEW transaction)
         
         └─> Retornar ClienteDTO mapeado
                (Entity mapeado a DTO)
    
    └─> ResponseEntity<ClienteDTO> 201 CREATED
```

### **Interacciones Principales:**

```
TransaccionController
    │
    └─> TransaccionService.procesarTransaccion()
         │
         ├─> ProductoFinancieroService.obtenerCuenta() [2x]
         │
         ├─> TransaccionAuditoriaService.registrarTransaccionInicio()
         │
         ├─> FraudeService.revisarTransaccion()
         │    │
         │    ├─> ReglaFraudeRepositorio (obtener reglas)
         │    │
         │    ├─> TransaccionService.frecuencia()
         │    │
         │    ├─> NotificacionService.enviarNotificacionBloqueo()
         │    │
         │    └─> AuditoriaService.guardarEvento()
         │
         ├─> ProductoFinancieroService.ajustarSaldo() [2x]
         │    │
         │    └─> AuditoriaService.guardarEvento()
         │
         └─> TransaccionRepositorio.save()
```

---

## 📈 Casos de Uso Principales

### **Caso 1: Crear Cliente**

```
1. Usuario crea cliente con: Documento, Nombre, Apellido, etc.
2. Sistema valida ID único
3. Sistema guarda cliente en BD
4. Sistema registra evento en auditoría
5. Sistema retorna cliente creado
```

**Responsabilidad:** ClienteService + AuditoriaService

---

### **Caso 2: Procesar Transacción (Exitosa)**

```
1. Usuario inicia transferencia: Cta Origen → Cta Destino, Monto, Motivo
2. Sistema carga cuentas y valida existencia
3. Sistema registra inicio de transacción
4. Sistema valida: Monto > 0, Origen ≠ Destino
5. FRAUDE CHECK:
   ├─ ¿Beneficiario está bloqueado? NO
   ├─ ¿Monto excede umbral? NO
   ├─ ¿Múltiples destinos? NO
   └─ → OK, CONTINUAR
6. Sistema resta monto de cuenta origen
7. Sistema suma monto a cuenta destino
8. Sistema marca transacción como "aplicada"
9. Sistema retorna comprobante
```

**Responsabilidades:** TransaccionService + FraudeService + ProductoFinancieroService

---

### **Caso 3: Procesar Transacción (Fraude Detectado)**

```
1. Usuario inicia transferencia
2. [Pasos 2-4 igual]
3. FRAUDE CHECK:
   ├─ ¿Monto es $500,000 (> umbral $100,000)? SÍ ❌
   └─ → FRAUDE DETECTADO
4. Sistema BLOQUEA transacción
5. Sistema registra AlertaFraude:
   ├─ Tipo: "monto_umbral"
   ├─ Severidad: BAJA
   └─ Descripción: "Monto $500,000 supera umbral..."
6. Sistema registra evento en auditoría
7. Sistema envía EMAIL al cliente:
   ├─ Asunto: SEGURIDAD BOSQUEBANK 🌲
   ├─ Motivo: Monto excesivo
   └─ Hora exacta: ...
8. Sistema lanza FraudeDetectadoException
9. Cliente recibe HTTP 403 FORBIDDEN
```

**Responsabilidades:** FraudeService + NotificacionService + AuditoriaService

---

### **Caso 4: Crear Tarjeta de Crédito**

```
1. Usuario crea tarjeta de crédito para cliente, tipo "credito", cupo $50,000
2. Sistema genera número de tarjeta:
   ├─ Formato: 2208XXXXXXXXXXXX (crédito = 2208)
   └─ Valida único en BD
3. Sistema encripta número (AES)
4. Sistema guarda como: [número encriptado base64]
5. Sistema genera fecha vencimiento: +5 años
6. Sistema crear tarjeta en BD
7. Sistema registra evento en auditoría
8. Sistema retorna TarjetaDTO
```

**Responsabilidades:** ProductoFinancieroService + FuncionesTarjeta + AuditoriaService

---

## 🔐 Seguridad

### **Encriptación de Tarjetas**

```
Algoritmo: AES (Advanced Encryption Standard)
Tamaño de clave: 128 bits (16 caracteres)
Clave: "1234567890123456"
Codificación: Base64

Ejemplo:
Número original:  2208123456789012
Encriptado:       4Q2r8mX9kL+pOqW7nR5sT2uV3xYzAb6cDeFg8hIjK9l=
```

### **Validaciones**

```
✓ Saldo suficiente en operaciones de resta
✓ Montos no negativos
✓ Clientes/Cuentas existen antes de usar
✓ Estados válidos en transiciones
✓ Números de producto únicos
✓ IDs únicos de cliente
```

### **Auditoría Completa**

```
Cada operación registra:
├─ ¿Quién? (Cliente, Empleado, Sistema)
├─ ¿Qué? (Acción: crear, actualizar, eliminar)
├─ ¿Cuándo? (Timestamp)
├─ ¿Por qué? (Motivo)
└─ ¿En qué? (Producto, Transacción)
```

---

## 📝 Diagrama E-R Simplificado

```
┌─────────────┐                    ┌──────────────┐
│  CLIENTE    │                    │  EMPLEADO    │
├─────────────┤                    ├──────────────┤
│ id          │◄───────────┐       │ id           │
│ documento   │            └───────────┼─ 1:M       │
│ nombre      │                    │ nombre       │
│ email       │                    │ cargo        │
│ estado      │                    │ estado       │
└─────────────┘                    └──────────────┘
       │                                  │
       ├─ 1:M ◄──────────────────────────┼─────────────┐
       │                                  │              │
       ▼                                  ▼              │
┌──────────────────────┐        ┌────────────────────┐  │
│ EVENTO_AUDITORIA     │        │ PRODUCTO_FINANCIERO│  │
├──────────────────────┤        ├────────────────────┤  │
│ id                   │        │ id                 │  │
│ cliente_id(FK)       │        │ cliente_id(FK)◄────┼──┘
│ empleado_id(FK)      │        │ numero_producto    │
│ producto_id(FK)      │        │ tipo (Polimórfico) │
│ transaccion_id(FK)   │        │ estado_id(FK)      │
│ accion               │        │ fecha_creacion     │
│ motivo               │        └────────────────────┘
│ fecha_hora           │                 │
└──────────────────────┘        ┌────────┴──────┬───────────┐
                                │               │           │
                                ▼               ▼           ▼
                        ┌──────────────┐ ┌──────────┐ ┌─────────┐
                        │   CREDITO    │ │ TARJETA  │ │ CUENTA  │
                        ├──────────────┤ ├──────────┤ ├─────────┤
                        │ monto_aprob  │ │ numero   │ │ tipo    │
                        │ saldo_pend   │ │ vencim   │ │ moneda  │
                        │ tasa_interes │ │ cupo     │ │ saldo   │
                        └──────────────┘ └──────────┘ └─────────┘


┌─────────────────────┐         ┌──────────────────┐
│  TRANSACCION        │         │  CANAL           │
├─────────────────────┤         ├──────────────────┤
│ id                  │         │ id               │
│ cliente_id(FK)      │         │ nombre           │
│ producto_origen(FK) │         │ descripcion      │
│ producto_destino(FK)│         └──────────────────┘
│ canal_id(FK)        │
│ monto               │         ┌──────────────────┐
│ fecha_hora          │         │ ESTADO_TRANSACCION
│ estado_id(FK)       │         ├──────────────────┤
│ motivo              │         │ id               │
└─────────────────────┘         │ nombre           │
                                └──────────────────┘

┌───────────────────┐
│  REGLA_FRAUDE     │
├───────────────────┤
│ id(PK)            │
│ nombre            │
│ tipo_regla        │
│ umbral            │
│ max_intentos      │
│ ventana_minutos   │
│ activa            │
└───────────────────┘


┌─────────────────┐        ┌────────────────┐
│ ALERTA_FRAUDE   │        │ ALERTA_TRANSAC │
├─────────────────┤        ├────────────────┤
│ id              │        │ id             │
│ regla_id(FK)    │        │ transaccion_id │
│ tipo_alerta     │        │ fecha_asoc     │
│ severidad       │        └────────────────┘
│ descripcion     │
│ fecha_hora      │
│ estado_alerta   │
└─────────────────┘
```

---

## 🚀 Cómo Usar Este Proyecto

### **1. Configuración Inicial**

```bash
# Clonar proyecto
git clone <repo>

# Instalar dependencias Maven
mvn clean install

# Configurar application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/BancoDB
spring.datasource.username=postgres
spring.datasource.password=password
spring.mail.password=xxxx xxxx xxxx xxxx

# Iniciar aplicación
mvn spring-boot:run
```

### **2. Acceder a Swagger**

```
http://localhost:8080/swagger-ui.html
```

### **3. Ejemplos de Uso**

**Crear Cliente:**
```json
POST /clientes/crear
{
    "tipoDocumento": "CC",
    "numeroDocumento": "1234567890",
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan@example.com",
    "telefono": "3105678901",
    "ciudad": "Bogotá",
    "direccionPrincipal": "Calle 10 #20-30"
}
```

**Crear Cuenta:**
```json
POST /productos-financieros/cuenta/crearCuenta?idCliente=1&tipoCuenta=ahorros&moneda=COP
```

**Procesar Transacción:**
```json
POST /transacciones/procesarTransaccion?idCuentaOrigen=1&idCuentaDestino=2&monto=50000&motivo=Pago
```

---

## 📚 Conclusión

**ProyectoFinalBack** es una aplicación bancaria **robusta, escalable y segura** que demuestra:

✅ Arquitectura en capas limpia (Controller → Service → Repository)  
✅ Separación de responsabilidades  
✅ Manejo transaccional avanzado  
✅ Detección de fraude inteligente  
✅ Auditoría completa de operaciones  
✅ Tratamiento de errores centralizado  
✅ Documentación API automática (Swagger)  
✅ Seguridad con encriptación  
✅ Code reusabilidad y mantenibilidad  

---

## 📞 Soporte

Para preguntas o mejoras, contacte al equipo de desarrollo.

**Documento generado:** 2026-05-14  
**Versión:** 1.0  
**Estado:** ✅ Producción

---

**🌲 BOSQUEBANK - Sistema Bancario Integral 🌲**
