-- =========================================================
-- 03_consultas_validacion.sql
-- Proyecto BosqueBank
-- Propósito: validar creación de objetos, catálogos, relaciones y datos cargados.
-- RDBMS: PostgreSQL
-- =========================================================

-- =========================================================
-- 1. Validación de existencia y conteo de registros por tabla
-- =========================================================

SELECT 'cliente' AS tabla, COUNT(*) AS total_registros FROM cliente
UNION ALL
SELECT 'estado_producto', COUNT(*) FROM estado_producto
UNION ALL
SELECT 'estado_transaccion', COUNT(*) FROM estado_transaccion
UNION ALL
SELECT 'canal', COUNT(*) FROM canal
UNION ALL
SELECT 'regla_fraude', COUNT(*) FROM regla_fraude
UNION ALL
SELECT 'estado_alerta', COUNT(*) FROM estado_alerta
UNION ALL
SELECT 'empleado', COUNT(*) FROM empleado
UNION ALL
SELECT 'producto_financiero', COUNT(*) FROM producto_financiero
UNION ALL
SELECT 'cuenta', COUNT(*) FROM cuenta
UNION ALL
SELECT 'tarjeta', COUNT(*) FROM tarjeta
UNION ALL
SELECT 'credito', COUNT(*) FROM credito
UNION ALL
SELECT 'transaccion', COUNT(*) FROM transaccion
UNION ALL
SELECT 'alerta_fraude', COUNT(*) FROM alerta_fraude
UNION ALL
SELECT 'alerta_transaccion', COUNT(*) FROM alerta_transaccion
UNION ALL
SELECT 'evento_auditoria', COUNT(*) FROM evento_auditoria
ORDER BY tabla;

-- =========================================================
-- 2. Validación de catálogos mínimos
-- =========================================================

SELECT * FROM estado_producto ORDER BY id_estado_producto;
SELECT * FROM estado_transaccion ORDER BY id_estado_transaccion;
SELECT * FROM estado_alerta ORDER BY id_estado_alerta;
SELECT * FROM canal ORDER BY id_canal;

-- =========================================================
-- 3. Validación de productos financieros y subtipos
-- =========================================================

-- Productos con su cliente y estado.
SELECT
    pf.id_producto,
    pf.numero_producto,
    c.id_cliente,
    c.nombres,
    c.apellidos,
    ep.nombre_estado AS estado_producto,
    pf.fecha_creacion
FROM producto_financiero pf
INNER JOIN cliente c
    ON pf.id_cliente = c.id_cliente
INNER JOIN estado_producto ep
    ON pf.id_estado_producto = ep.id_estado_producto
ORDER BY pf.id_producto;

-- Conteo por tipo lógico de producto.
SELECT 'cuenta' AS tipo_producto, COUNT(*) AS total FROM cuenta
UNION ALL
SELECT 'tarjeta', COUNT(*) FROM tarjeta
UNION ALL
SELECT 'credito', COUNT(*) FROM credito;

-- Productos sin subtipo asignado: debería devolver 0 filas si todos los productos están clasificados.
SELECT pf.*
FROM producto_financiero pf
LEFT JOIN cuenta cta ON pf.id_producto = cta.id_producto
LEFT JOIN tarjeta tar ON pf.id_producto = tar.id_producto
LEFT JOIN credito cre ON pf.id_producto = cre.id_producto
WHERE cta.id_producto IS NULL
  AND tar.id_producto IS NULL
  AND cre.id_producto IS NULL;

-- Productos asignados a más de un subtipo: debería devolver 0 filas por especialización disyunta.
SELECT
    pf.id_producto,
    pf.numero_producto,
    (CASE WHEN cta.id_producto IS NOT NULL THEN 1 ELSE 0 END +
     CASE WHEN tar.id_producto IS NOT NULL THEN 1 ELSE 0 END +
     CASE WHEN cre.id_producto IS NOT NULL THEN 1 ELSE 0 END) AS cantidad_subtipos
FROM producto_financiero pf
LEFT JOIN cuenta cta ON pf.id_producto = cta.id_producto
LEFT JOIN tarjeta tar ON pf.id_producto = tar.id_producto
LEFT JOIN credito cre ON pf.id_producto = cre.id_producto
WHERE (CASE WHEN cta.id_producto IS NOT NULL THEN 1 ELSE 0 END +
       CASE WHEN tar.id_producto IS NOT NULL THEN 1 ELSE 0 END +
       CASE WHEN cre.id_producto IS NOT NULL THEN 1 ELSE 0 END) > 1;

-- =========================================================
-- 4. Validación de transacciones
-- =========================================================

-- Transacciones con cliente, canal y estado.
SELECT
    t.id_transaccion,
    t.tipo_transaccion,
    t.monto,
    t.es_fallida,
    t.fecha_hora,
    c.nombres,
    c.apellidos,
    ca.nombre AS canal,
    et.nombre_estado AS estado_transaccion
FROM transaccion t
INNER JOIN cliente c
    ON t.id_cliente = c.id_cliente
INNER JOIN canal ca
    ON t.id_canal = ca.id_canal
INNER JOIN estado_transaccion et
    ON t.id_estado_transaccion = et.id_estado_transaccion
ORDER BY t.id_transaccion;

-- Transacciones con monto inválido: debería devolver 0 filas por CHECK.
SELECT *
FROM transaccion
WHERE monto <= 0;

-- Transacciones sin producto origen ni destino: debería devolver 0 filas por CHECK.
SELECT *
FROM transaccion
WHERE id_producto_origen IS NULL
  AND id_producto_destino IS NULL;

-- Transacciones reversadas con su transacción original.
SELECT
    tr.id_transaccion AS id_reverso,
    tr.tipo_transaccion AS tipo_reverso,
    tr.monto AS monto_reverso,
    tr.fecha_hora AS fecha_reverso,
    tor.id_transaccion AS id_original,
    tor.tipo_transaccion AS tipo_original,
    tor.monto AS monto_original,
    tor.fecha_hora AS fecha_original
FROM transaccion tr
INNER JOIN transaccion tor
    ON tr.id_transaccion_original = tor.id_transaccion
WHERE tr.tipo_transaccion = 'reverso'
ORDER BY tr.id_transaccion;

-- =========================================================
-- 5. Validación de alertas de fraude
-- =========================================================

-- Alertas con estado y regla que las generó.
SELECT
    af.id_alerta,
    af.tipo_alerta,
    af.severidad,
    af.fecha_hora,
    ea.nombre_estado AS estado_alerta,
    rf.nombre AS regla,
    rf.tipo_regla,
    rf.umbral,
    rf.ventana_minutos,
    rf.max_intentos,
    rf.activa
FROM alerta_fraude af
INNER JOIN estado_alerta ea
    ON af.id_estado_alerta = ea.id_estado_alerta
INNER JOIN regla_fraude rf
    ON af.id_regla = rf.id_regla
ORDER BY af.id_alerta;

-- Transacciones asociadas a alertas.
SELECT
    af.id_alerta,
    af.tipo_alerta,
    af.severidad,
    t.id_transaccion,
    t.tipo_transaccion,
    t.monto,
    t.es_fallida,
    atx.fecha_asociacion
FROM alerta_fraude af
INNER JOIN alerta_transaccion atx
    ON af.id_alerta = atx.id_alerta
INNER JOIN transaccion t
    ON atx.id_transaccion = t.id_transaccion
ORDER BY af.id_alerta, t.id_transaccion;

-- =========================================================
-- 6. Validación de auditoría
-- =========================================================

-- Eventos de auditoría con canal y responsable cuando aplique.
SELECT
    ea.id_evento,
    ea.tipo_responsable,
    ea.accion,
    ea.fecha_hora,
    ea.motivo,
    ca.nombre AS canal,
    c.nombres AS cliente_nombres,
    c.apellidos AS cliente_apellidos,
    e.nombres AS empleado_nombres,
    e.apellidos AS empleado_apellidos
FROM evento_auditoria ea
INNER JOIN canal ca
    ON ea.id_canal = ca.id_canal
LEFT JOIN cliente c
    ON ea.id_cliente = c.id_cliente
LEFT JOIN empleado e
    ON ea.id_empleado = e.id_empleado
ORDER BY ea.id_evento;

-- Eventos con tipo_responsable empleado sin id_empleado: revisión de calidad de datos.
SELECT *
FROM evento_auditoria
WHERE tipo_responsable = 'empleado'
  AND id_empleado IS NULL;

-- Eventos con tipo_responsable cliente sin id_cliente: revisión de calidad de datos.
SELECT *
FROM evento_auditoria
WHERE tipo_responsable = 'cliente'
  AND id_cliente IS NULL;
