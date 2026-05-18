# BosqueBank - Scripts de Base de Datos

## 1. Descripción general

Este repositorio contiene los scripts SQL necesarios para la creación, carga, validación y mantenimiento de la base de datos relacional del proyecto **BosqueBank**.

BosqueBank es un proyecto académico orientado al diseño e implementación de una base de datos para una entidad financiera simulada, enfocada en:

- Control de productos financieros.
- Registro de transacciones.
- Detección básica de fraude.
- Gestión de alertas.
- Trazabilidad mínima mediante eventos de auditoría.

La base de datos fue diseñada para PostgreSQL y está compuesta por tablas como `cliente`, `producto_financiero`, `cuenta`, `tarjeta`, `credito`, `transaccion`, `alerta_fraude`, `regla_fraude`, `empleado` y `evento_auditoria`.

---

## 2. Motor de base de datos

| Elemento | Tecnología |
|---|---|
| RDBMS | PostgreSQL |
| Sistema operativo recomendado | Debian Server |
| Entorno de virtualización | VMware |
| Backend previsto | Spring Framework |
| Lenguaje | SQL |

---

## 3. Estructura recomendada del repositorio

```text
bosquebank-database/
│
├── README.md
│
├── 01_creacion_objetos/
│   └── Creacion de la DB.sql
│
├── 02_insercion_registros/
│   └── Insert de datos.sql
│
└── 03_scripts_varios/
    ├── 03_consultas_validacion.sql
    ├── 04_consultas_negocio.sql
    ├── 05_backup_restore.sql
    └── 06_drop_clean.sql
```

---

## 4. Scripts de creación de objetos

### Archivo sugerido

```text
01_creacion_objetos/ Creacion de la DB.sql
```

### Descripción

Este script contiene la definición estructural de la base de datos. Incluye la creación de tablas, claves primarias, claves foráneas, restricciones, índices y comentarios básicos.

El script actual contempla las siguientes tablas:

- `cliente`
- `estado_producto`
- `estado_transaccion`
- `canal`
- `regla_fraude`
- `estado_alerta`
- `empleado`
- `producto_financiero`
- `cuenta`
- `tarjeta`
- `credito`
- `transaccion`
- `alerta_fraude`
- `alerta_transaccion`
- `evento_auditoria`

En el script actualizado, varias claves se manejan con `BIGSERIAL`, por ejemplo `id_cliente`, `id_regla`, `id_empleado`, `id_producto`, `id_transaccion`, `id_alerta` e `id_evento`. Además, los campos `activa` en `regla_fraude` y `es_fallida` en `transaccion` se manejan como valores booleanos.

### Orden lógico de creación

El orden de creación de objetos debe respetar las dependencias entre tablas:

1. `cliente`
2. `estado_producto`
3. `estado_transaccion`
4. `canal`
5. `regla_fraude`
6. `estado_alerta`
7. `empleado`
8. `producto_financiero`
9. `cuenta`
10. `tarjeta`
11. `credito`
12. `transaccion`
13. `alerta_fraude`
14. `alerta_transaccion`
15. `evento_auditoria`

### Ejecución del script

Desde terminal:

```bash
psql -U postgres -d bosquebank -f 01_creacion_objetos/01_create_schema_bosquebank.sql
```

Si se usa un usuario de aplicación:

```bash
psql -U app_bosquebank -d bosquebank -f 01_creacion_objetos/01_create_schema_bosquebank.sql
```

---

## 5. Scripts de inserción de registros

### Archivo sugerido

```text
02_insercion_registros/02_seed_bosquebank.sql
```

### Descripción

Este script contiene los registros iniciales y datos de prueba necesarios para validar el funcionamiento de la base de datos.

Debe incluir:

- Datos iniciales de catálogos.
- Clientes de prueba.
- Empleados.
- Productos financieros.
- Cuentas.
- Tarjetas.
- Créditos.
- Transacciones.
- Reglas de fraude.
- Alertas de fraude.
- Relaciones entre alertas y transacciones.
- Eventos de auditoría.

### Catálogos mínimos

#### Estados de producto

```sql
INSERT INTO estado_producto (id_estado_producto, nombre_estado) VALUES
(1, 'activo'),
(2, 'bloqueado'),
(3, 'cancelado');
```

#### Estados de transacción

```sql
INSERT INTO estado_transaccion (id_estado_transaccion, nombre_estado) VALUES
(1, 'pendiente'),
(2, 'aplicada'),
(3, 'rechazada'),
(4, 'reversada');
```

#### Estados de alerta

```sql
INSERT INTO estado_alerta (id_estado_alerta, nombre_estado) VALUES
(1, 'abierta'),
(2, 'en_revision'),
(3, 'cerrada');
```

#### Canales

```sql
INSERT INTO canal (id_canal, nombre, descripcion) VALUES
(1, 'web', 'Canal web'),
(2, 'app', 'Aplicacion movil'),
(3, 'cajero', 'Cajero automatico'),
(4, 'oficina', 'Oficina bancaria');
```

### Consideraciones para los datos de prueba

El script de inserción debe simular diferentes escenarios:

- Clientes activos.
- Clientes bloqueados.
- Clientes en investigación.
- Productos financieros activos.
- Productos financieros bloqueados.
- Productos financieros cancelados.
- Transacciones aplicadas.
- Transacciones rechazadas.
- Transacciones reversadas.
- Transacciones fallidas.
- Alertas de fraude abiertas.
- Alertas en revisión.
- Alertas cerradas.
- Eventos de auditoría realizados por cliente, empleado o sistema.

### Nota importante sobre booleanos

En el script actualizado:

- `transaccion.es_fallida` es de tipo `BOOLEAN`.
- `regla_fraude.activa` es de tipo `BOOLEAN`.

Por lo tanto, los valores deben insertarse como:

```sql
TRUE
FALSE
```

Ejemplo:

```sql
INSERT INTO regla_fraude (nombre, tipo_regla, umbral, ventana_minutos, max_intentos, activa)
VALUES ('Monto alto', 'monto', 2000000, NULL, NULL, TRUE);
```

```sql
INSERT INTO transaccion (
    id_cliente,
    id_producto_origen,
    id_producto_destino,
    id_estado_transaccion,
    id_canal,
    tipo_transaccion,
    monto,
    es_fallida
)
VALUES (
    1,
    1,
    2,
    2,
    2,
    'transferencia',
    250000,
    FALSE
);
```

### Ejecución del script

```bash
psql -U postgres -d bosquebank -f 02_insercion_registros/02_seed_bosquebank.sql
```

---

## 6. Scripts varios

### Carpeta sugerida

```text
03_scripts_varios/
```

Esta carpeta contiene scripts complementarios para validación, pruebas, consultas de negocio, respaldo, restauración y limpieza.

---

### 6.1 Script de consultas de validación

#### Archivo sugerido

```text
03_scripts_varios/03_consultas_validacion.sql
```

#### Descripción

Contiene consultas básicas para verificar que las tablas fueron creadas correctamente y que los datos iniciales fueron cargados.

#### Consultas sugeridas

```sql
SELECT COUNT(*) FROM cliente;
SELECT COUNT(*) FROM producto_financiero;
SELECT COUNT(*) FROM cuenta;
SELECT COUNT(*) FROM tarjeta;
SELECT COUNT(*) FROM credito;
SELECT COUNT(*) FROM transaccion;
SELECT COUNT(*) FROM alerta_fraude;
SELECT COUNT(*) FROM evento_auditoria;
```

Validación de catálogos:

```sql
SELECT * FROM estado_producto;
SELECT * FROM estado_transaccion;
SELECT * FROM estado_alerta;
SELECT * FROM canal;
```

---

### 6.2 Script de consultas de negocio

#### Archivo sugerido

```text
03_scripts_varios/04_consultas_negocio.sql
```

#### Descripción

Contiene consultas SQL para validar casos de uso del sistema.

#### Productos financieros con estado

```sql
SELECT 
    pf.id_producto,
    pf.numero_producto,
    ep.nombre_estado AS estado_producto
FROM producto_financiero pf
INNER JOIN estado_producto ep
    ON pf.id_estado_producto = ep.id_estado_producto;
```

#### Transacciones con canal y estado

```sql
SELECT 
    t.id_transaccion,
    t.tipo_transaccion,
    t.monto,
    t.fecha_hora,
    c.nombre AS canal,
    et.nombre_estado AS estado_transaccion
FROM transaccion t
INNER JOIN canal c
    ON t.id_canal = c.id_canal
INNER JOIN estado_transaccion et
    ON t.id_estado_transaccion = et.id_estado_transaccion;
```

#### Transacciones fallidas

```sql
SELECT *
FROM transaccion
WHERE es_fallida = TRUE;
```

#### Alertas abiertas

```sql
SELECT 
    af.id_alerta,
    af.tipo_alerta,
    af.severidad,
    af.fecha_hora,
    ea.nombre_estado AS estado_alerta
FROM alerta_fraude af
INNER JOIN estado_alerta ea
    ON af.id_estado_alerta = ea.id_estado_alerta
WHERE ea.nombre_estado = 'abierta';
```

#### Alertas con regla de fraude

```sql
SELECT 
    af.id_alerta,
    af.tipo_alerta,
    af.severidad,
    rf.nombre AS regla,
    rf.tipo_regla,
    rf.umbral,
    rf.activa
FROM alerta_fraude af
INNER JOIN regla_fraude rf
    ON af.id_regla = rf.id_regla;
```

#### Transacciones asociadas a alertas

```sql
SELECT 
    af.id_alerta,
    af.tipo_alerta,
    af.severidad,
    t.id_transaccion,
    t.tipo_transaccion,
    t.monto,
    t.es_fallida
FROM alerta_fraude af
INNER JOIN alerta_transaccion atx
    ON af.id_alerta = atx.id_alerta
INNER JOIN transaccion t
    ON atx.id_transaccion = t.id_transaccion;
```

#### Eventos de auditoría

```sql
SELECT *
FROM evento_auditoria;
```

#### Eventos de auditoría ejecutados por empleados

```sql
SELECT 
    ea.id_evento,
    ea.accion,
    ea.fecha_hora,
    ea.motivo,
    e.nombres,
    e.apellidos,
    e.cargo
FROM evento_auditoria ea
INNER JOIN empleado e
    ON ea.id_empleado = e.id_empleado
WHERE ea.tipo_responsable = 'empleado';
```

---

### 6.3 Script de backup y restauración

#### Archivo sugerido

```text
03_scripts_varios/05_backup_restore.sql
```

#### Backup desde terminal

```bash
pg_dump -U postgres -d bosquebank -F c -f bosquebank_backup.dump
```

#### Restauración desde terminal

```bash
pg_restore -U postgres -d bosquebank bosquebank_backup.dump
```

#### Backup en formato SQL plano

```bash
pg_dump -U postgres -d bosquebank -f bosquebank_backup.sql
```

#### Restauración de SQL plano

```bash
psql -U postgres -d bosquebank -f bosquebank_backup.sql
```

---

### 6.4 Script de limpieza

#### Archivo sugerido

```text
03_scripts_varios/06_drop_clean.sql
```

#### Descripción

Permite limpiar la base de datos eliminando tablas en orden seguro.

```sql
DROP TABLE IF EXISTS evento_auditoria CASCADE;
DROP TABLE IF EXISTS alerta_transaccion CASCADE;
DROP TABLE IF EXISTS alerta_fraude CASCADE;
DROP TABLE IF EXISTS regla_fraude CASCADE;
DROP TABLE IF EXISTS estado_alerta CASCADE;
DROP TABLE IF EXISTS transaccion CASCADE;
DROP TABLE IF EXISTS estado_transaccion CASCADE;
DROP TABLE IF EXISTS canal CASCADE;
DROP TABLE IF EXISTS credito CASCADE;
DROP TABLE IF EXISTS tarjeta CASCADE;
DROP TABLE IF EXISTS cuenta CASCADE;
DROP TABLE IF EXISTS producto_financiero CASCADE;
DROP TABLE IF EXISTS estado_producto CASCADE;
DROP TABLE IF EXISTS empleado CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
```

---

## 7. Creación de base de datos

En PostgreSQL, la creación de la base puede hacerse antes de ejecutar los scripts del esquema.

### En terminal o psql

```sql
DROP DATABASE IF EXISTS bosquebank;
CREATE DATABASE bosquebank WITH OWNER = postgres ENCODING = 'UTF8';
```

Luego conectarse a la base:

```bash
psql -U postgres -d bosquebank
```

---

## 8. Flujo recomendado de ejecución

Ejecutar los scripts en este orden:

```text
1. Crear base de datos bosquebank.
2. Ejecutar script de creación de objetos.
3. Ejecutar script de inserción de registros.
4. Ejecutar script de consultas de validación.
5. Ejecutar scripts varios según necesidad.
```

Ejemplo:

```bash
psql -U postgres -d bosquebank -f 01_creacion_objetos/01_create_schema_bosquebank.sql
psql -U postgres -d bosquebank -f 02_insercion_registros/02_seed_bosquebank.sql
psql -U postgres -d bosquebank -f 03_scripts_varios/03_consultas_validacion.sql
```

---

## 9. Observaciones sobre el script actual

En el script actual existen columnas que representan relaciones conceptuales importantes, pero no todas están declaradas como claves foráneas.

Por ejemplo:

- `transaccion.id_producto_origen`
- `transaccion.id_producto_destino`
- `evento_auditoria.id_producto`
- `evento_auditoria.id_transaccion`
- `evento_auditoria.id_alerta`

Estas columnas son útiles para el modelo y para las consultas, pero si se desea reforzar la integridad referencial, se recomienda declararlas como claves foráneas hacia sus tablas correspondientes.

Script sugerido:

```sql
ALTER TABLE transaccion
ADD CONSTRAINT fk_transaccion_producto_origen
FOREIGN KEY (id_producto_origen)
REFERENCES producto_financiero(id_producto);

ALTER TABLE transaccion
ADD CONSTRAINT fk_transaccion_producto_destino
FOREIGN KEY (id_producto_destino)
REFERENCES producto_financiero(id_producto);

ALTER TABLE evento_auditoria
ADD CONSTRAINT fk_ev_producto
FOREIGN KEY (id_producto)
REFERENCES producto_financiero(id_producto);

ALTER TABLE evento_auditoria
ADD CONSTRAINT fk_ev_transaccion
FOREIGN KEY (id_transaccion)
REFERENCES transaccion(id_transaccion);

ALTER TABLE evento_auditoria
ADD CONSTRAINT fk_ev_alerta
FOREIGN KEY (id_alerta)
REFERENCES alerta_fraude(id_alerta);
```

---

## 10. Integración con Spring Framework

Ejemplo de configuración en `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bosquebank
spring.datasource.username=app_bosquebank
spring.datasource.password=Cambiar_Clave_Segura_123
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Si PostgreSQL está en una máquina virtual:

```properties
spring.datasource.url=jdbc:postgresql://IP_DE_LA_VM:5432/bosquebank
```

---

## 11. Estado del proyecto

Estado actual:

```text
Base de datos diseñada, normalizada e implementable en PostgreSQL.
```

Incluye:

- Modelo Entidad-Relación Extendido.
- Reducción a tablas.
- Normalización.
- Script de creación de objetos.
- Script de inserción de registros.
- Scripts varios de validación y mantenimiento.
- Preparación para integración con Spring Framework.

---

## 12. Equipo de trabajo

Este proyecto fue desarrollado por:

| Integrante | Rol sugerido |
|---|---|
| Santiago Monrroy | Modelado y documentación |
| Michael Zabala | Implementación y pruebas |
| Brayan Herrera | Diseño de base de datos y consultas |

Universidad El Bosque  
Proyecto de Bases de Datos  
2026
