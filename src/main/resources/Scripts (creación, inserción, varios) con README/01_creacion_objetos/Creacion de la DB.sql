-- =========================================
-- CREACIÓN DE BASE DE DATOS
-- =========================================
-- Nota: Ejecutar estas 2 líneas por separado si usas un GUI como pgAdmin
DROP DATABASE IF EXISTS bosquebank;
CREATE DATABASE bosquebank WITH OWNER = postgres ENCODING = 'UTF8';

-- \c bosquebank

-- =========================================
-- LIMPIEZA PREVIA
-- =========================================
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

-- =========================================
-- TABLAS MAESTRAS
-- =========================================

CREATE TABLE cliente (
    id_cliente              BIGSERIAL PRIMARY KEY, -- Uso de SERIAL para autoincremento
    tipo_documento          VARCHAR(20) NOT NULL,
    numero_documento        VARCHAR(30) NOT NULL,
    nombres                 VARCHAR(80) NOT NULL,
    apellidos               VARCHAR(80) NOT NULL,
    direccion_principal     VARCHAR(150),
    ciudad                  VARCHAR(80),
    telefono                VARCHAR(20),
    email                   VARCHAR(120),
    estado_cliente          VARCHAR(20) NOT NULL,
    fecha_alta              DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT uq_cliente_documento UNIQUE (tipo_documento, numero_documento),
    CONSTRAINT ck_cliente_estado
        CHECK (estado_cliente IN ('activo', 'bloqueado', 'investigacion'))
);

CREATE TABLE estado_producto (
    id_estado_producto      SMALLINT PRIMARY KEY,
    nombre_estado           VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE estado_transaccion (
    id_estado_transaccion   SMALLINT PRIMARY KEY,
    nombre_estado           VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE canal (
    id_canal                SMALLINT PRIMARY KEY,
    nombre                  VARCHAR(40) NOT NULL UNIQUE,
    descripcion             VARCHAR(150)
);

CREATE TABLE regla_fraude (
    id_regla                BIGSERIAL PRIMARY KEY,
    nombre                  VARCHAR(80) NOT NULL,
    tipo_regla              VARCHAR(40) NOT NULL,
    umbral                  NUMERIC(15,2),
    ventana_minutos         SMALLINT,
    max_intentos            SMALLINT,
    activa                  BOOLEAN NOT NULL DEFAULT TRUE -- Tipo nativo boolean
);

CREATE TABLE estado_alerta (
    id_estado_alerta        SMALLINT PRIMARY KEY,
    nombre_estado           VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE empleado (
    id_empleado             BIGSERIAL PRIMARY KEY,
    nombres                 VARCHAR(80) NOT NULL,
    apellidos               VARCHAR(80) NOT NULL,
    cargo                   VARCHAR(60) NOT NULL,
    estado_empleado         VARCHAR(20) NOT NULL,
    fecha_alta              DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT ck_empleado_estado
        CHECK (estado_empleado IN ('activo', 'inactivo', 'suspendido'))
);

-- =========================================
-- PRODUCTOS FINANCIEROS (Herencia Lógica)
-- =========================================

CREATE TABLE producto_financiero (
    id_producto             BIGSERIAL PRIMARY KEY,
    id_cliente              BIGINT NOT NULL,
    id_estado_producto      SMALLINT NOT NULL,
    numero_producto         VARCHAR(30) NOT NULL UNIQUE,
    fecha_creacion          DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT fk_producto_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente),
    CONSTRAINT fk_producto_estado FOREIGN KEY (id_estado_producto) REFERENCES estado_producto (id_estado_producto)
);

CREATE TABLE cuenta (
    id_producto             BIGINT PRIMARY KEY,
    tipo_cuenta             VARCHAR(20) NOT NULL,
    saldo_actual            NUMERIC(15,2) NOT NULL DEFAULT 0,
    moneda                  VARCHAR(10) NOT NULL,
    CONSTRAINT fk_cuenta_producto FOREIGN KEY (id_producto) REFERENCES producto_financiero (id_producto) ON DELETE CASCADE,
    CONSTRAINT ck_cuenta_tipo CHECK (tipo_cuenta IN ('ahorro', 'corriente')),
    CONSTRAINT ck_cuenta_saldo CHECK (saldo_actual >= 0)
);

CREATE TABLE tarjeta (
    id_producto             BIGINT PRIMARY KEY,
    tipo_tarjeta            VARCHAR(20) NOT NULL,
    numero_enmascarado      VARCHAR(100) NOT NULL,
    fecha_vencimiento       DATE NOT NULL,
    cupo                    NUMERIC(15,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_tarjeta_producto FOREIGN KEY (id_producto) REFERENCES producto_financiero (id_producto) ON DELETE CASCADE,
    CONSTRAINT ck_tarjeta_tipo CHECK (tipo_tarjeta IN ('debito', 'credito')),
    CONSTRAINT ck_tarjeta_cupo CHECK (cupo >= 0)
);

CREATE TABLE credito (
    id_producto             BIGINT PRIMARY KEY,
    tipo_credito            VARCHAR(30) NOT NULL,
    monto_aprobado          NUMERIC(15,2) NOT NULL,
    saldo_pendiente         NUMERIC(15,2) NOT NULL,
    tasa_interes            NUMERIC(5,2) NOT NULL,
    fecha_desembolso        DATE NOT NULL,
    CONSTRAINT fk_credito_producto FOREIGN KEY (id_producto) REFERENCES producto_financiero (id_producto) ON DELETE CASCADE,
    CONSTRAINT ck_credito_monto CHECK (monto_aprobado >= 0),
    CONSTRAINT ck_credito_saldo CHECK (saldo_pendiente >= 0)
);

-- =========================================
-- TRANSACCIONES Y FRAUDE
-- =========================================

CREATE TABLE transaccion (
    id_transaccion              BIGSERIAL PRIMARY KEY,
    id_cliente                  BIGINT NOT NULL,
    id_producto_origen          BIGINT,
    id_producto_destino         BIGINT,
    id_estado_transaccion       SMALLINT NOT NULL,
    id_canal                    SMALLINT NOT NULL,
    id_transaccion_original     BIGINT,
    tipo_transaccion            VARCHAR(30) NOT NULL,
    monto                       NUMERIC(15,2) NOT NULL,
    fecha_hora                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo                      VARCHAR(200),
    es_fallida                  BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_transaccion_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente),
    CONSTRAINT fk_transaccion_estado FOREIGN KEY (id_estado_transaccion) REFERENCES estado_transaccion (id_estado_transaccion),
    CONSTRAINT fk_transaccion_canal FOREIGN KEY (id_canal) REFERENCES canal (id_canal),
    CONSTRAINT fk_transaccion_original FOREIGN KEY (id_transaccion_original) REFERENCES transaccion (id_transaccion),
    CONSTRAINT ck_transaccion_monto CHECK (monto > 0),
    CONSTRAINT ck_transaccion_origen_destino CHECK (id_producto_origen IS NOT NULL OR id_producto_destino IS NOT NULL)
);

CREATE TABLE alerta_fraude (
    id_alerta               BIGSERIAL PRIMARY KEY,
    id_regla                BIGINT NOT NULL,
    id_estado_alerta        SMALLINT NOT NULL,
    fecha_hora              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_alerta             VARCHAR(40) NOT NULL,
    severidad               VARCHAR(20) NOT NULL,
    descripcion             VARCHAR(200),

    CONSTRAINT fk_alerta_regla FOREIGN KEY (id_regla) REFERENCES regla_fraude (id_regla),
    CONSTRAINT fk_alerta_estado FOREIGN KEY (id_estado_alerta) REFERENCES estado_alerta (id_estado_alerta),
    CONSTRAINT ck_alerta_severidad CHECK (severidad IN ('baja', 'media', 'alta', 'critica'))
);

CREATE TABLE alerta_transaccion (
    id_alerta               BIGINT NOT NULL,
    id_transaccion          BIGINT NOT NULL,
    fecha_asociacion        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_alerta, id_transaccion),
    CONSTRAINT fk_at_alerta FOREIGN KEY (id_alerta) REFERENCES alerta_fraude (id_alerta) ON DELETE CASCADE,
    CONSTRAINT fk_at_transaccion FOREIGN KEY (id_transaccion) REFERENCES transaccion (id_transaccion) ON DELETE CASCADE
);

-- =========================================
-- AUDITORÍA
-- =========================================

CREATE TABLE evento_auditoria (
    id_evento               BIGSERIAL PRIMARY KEY,
    id_cliente              BIGINT,
    id_empleado             BIGINT,
    id_canal                SMALLINT NOT NULL,
    id_producto             BIGINT,
    id_transaccion          BIGINT,
    id_alerta               BIGINT,
    tipo_responsable        VARCHAR(20) NOT NULL,
    accion                  VARCHAR(60) NOT NULL,
    fecha_hora              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo                  VARCHAR(200),

    CONSTRAINT fk_ev_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente),
    CONSTRAINT fk_ev_empleado FOREIGN KEY (id_empleado) REFERENCES empleado (id_empleado),
    CONSTRAINT fk_ev_canal FOREIGN KEY (id_canal) REFERENCES canal (id_canal),
    CONSTRAINT ck_evento_tipo_responsable CHECK (tipo_responsable IN ('cliente', 'empleado', 'sistema'))
);

-- =========================================
-- ÍNDICES 
-- =========================================
CREATE INDEX idx_producto_cliente ON producto_financiero(id_cliente);
CREATE INDEX idx_transaccion_fecha_hora ON transaccion(fecha_hora);
CREATE INDEX idx_alerta_fecha_hora ON alerta_fraude(fecha_hora);

