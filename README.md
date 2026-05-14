# ProyectoFinalBack 🌲

## Sistema Bancario Integral con Detección de Fraude

Una API REST robusta y escalable construida con Spring Boot para gestionar operaciones bancarias con seguridad de clase empresarial.

### 🚀 Características Principales

- **Gestión Completa**: Clientes, empleados, productos financieros (créditos, tarjetas, cuentas)
- **Transacciones Seguras**: Transferencias con validación automática de fraude
- **Auditoría Total**: Registro de todas las operaciones para trazabilidad
- **Notificaciones**: Alertas por correo ante actividades sospechosas
- **Documentación Interactiva**: Swagger/OpenAPI integrado
- **Seguridad Avanzada**: Encriptación AES para datos sensibles

### 🛠️ Tecnologías

- **Backend**: Spring Boot 3.x
- **Base de Datos**: PostgreSQL
- **Mapeo**: ModelMapper, JPA/Hibernate
- **Documentación**: SpringDoc OpenAPI
- **Lenguaje**: Java 21
- **Otros**: Lombok, Spring Mail

### 📦 Instalación y Uso

1. **Clonar el repositorio**:
   ```bash
   git clone <url-del-repo>
   cd ProyectoFinalBack
   ```

2. **Configurar la base de datos**:
   - Editar `src/main/resources/application.properties`
   - Configurar conexión PostgreSQL

3. **Instalar dependencias y ejecutar**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Acceder a la API**:
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Base: `http://localhost:8080`

### 📋 Ejemplos de Uso

#### Crear Cliente
```bash
POST /clientes/crear
Content-Type: application/json

{
  "tipoDocumento": "CC",
  "numeroDocumento": "123456789",
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@email.com",
  "telefono": "3001234567",
  "ciudad": "Bogotá",
  "direccionPrincipal": "Calle 123 #45-67"
}
```

#### Procesar Transacción
```bash
POST /transacciones/procesarTransaccion?idCuentaOrigen=1&idCuentaDestino=2&monto=100000&motivo=Transferencia
```

### 🏗️ Arquitectura

- **Controladores**: Endpoints REST con CORS habilitado
- **Servicios**: Lógica de negocio con auditoría integrada
- **Repositorios**: Acceso a datos con Spring Data JPA
- **Utilidades**: Encriptación, manejo de excepciones
- **Configuración**: ModelMapper, Swagger

### 🔐 Seguridad

- Detección de fraude en tiempo real
- Encriptación de números de tarjeta
- Validaciones estrictas de negocio
- Auditoría completa de operaciones

### 📚 Documentación

Para documentación completa, ver `Documentacion.md`.

### 👥 Contribuidores

- Brayan David Herrera Acero - Desarrollador
- Michael Sebastián Zabala Ávila - Desarrollador
- Santiago Monroy Manrique - Desarrollador

---

**🌲 BOSQUEBANK🌲**
