-- =========================================================
-- 06_drop_clean.sql
-- Proyecto BosqueBank
-- Propósito: limpiar objetos de la base de datos de forma segura.
-- RDBMS: PostgreSQL
-- =========================================================

-- =========================================================
-- ADVERTENCIA
-- Este script elimina las tablas del proyecto BosqueBank.
-- Debe ejecutarse únicamente en ambientes de desarrollo o pruebas.
-- =========================================================

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

-- =========================================================
-- Validación posterior: no deberían quedar tablas de usuario.
-- =========================================================
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public'
ORDER BY table_name;

-- =========================================================
-- Opcional: reiniciar esquema público por completo.
-- Usar solo si se desea limpiar todo el esquema public.
-- =========================================================
-- DROP SCHEMA public CASCADE;
-- CREATE SCHEMA public;
-- GRANT ALL ON SCHEMA public TO postgres;
-- GRANT ALL ON SCHEMA public TO public;
