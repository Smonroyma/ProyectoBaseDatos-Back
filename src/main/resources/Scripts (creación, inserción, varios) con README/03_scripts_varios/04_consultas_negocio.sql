-- =========================================================
-- 04_consultas_negocio.sql
-- Proyecto BosqueBank
-- Propósito: consultas de negocio para demo, validación funcional y presentación.
-- RDBMS: PostgreSQL
-- =========================================================

-- =========================================================
-- 1. Clientes y productos financieros
-- =========================================================

-- Clientes activos.
SELECT *
FROM cliente
WHERE estado_cliente = 'activo'
ORDER BY id_cliente;

-- Clientes bloqueados junto con sus productos financieros.
SELECT
    c.id_cliente,
    c.nombres,
    c.apellidos,
    c.estado_cliente,
    pf.id_producto,
    pf.numero_producto,
    ep.nombre_estado AS estado_producto
FROM cliente c
INNER JOIN producto_financiero pf
    ON c.id_cliente = pf.id_cliente
INNER JOIN estado_producto ep
    ON pf.id_estado_producto = ep.id_estado_producto
WHERE c.estado_cliente = 'bloqueado'
ORDER BY c.id_cliente, pf.id_producto;

-- Productos financieros junto con su estado actual.
SELECT
    pf.id_producto,
    pf.numero_producto,
    pf.fecha_creacion,
    ep.nombre_estado AS estado_producto
FROM producto_financiero pf
INNER JOIN estado_producto ep
    ON pf.id_estado_producto = ep.id_estado_producto
ORDER BY pf.id_producto;

-- Número de producto y estado de todos los productos financieros.
SELECT
    pf.numero_producto,
    ep.nombre_estado AS estado_producto
FROM producto_financiero pf
INNER JOIN estado_producto ep
    ON pf.id_estado_producto = ep.id_estado_producto
ORDER BY pf.numero_producto;

-- Productos financieros pertenecientes a un cliente específico.
-- Cambiar el valor 1 por el id_cliente requerido.
SELECT
    pf.*
FROM producto_financiero pf
WHERE pf.id_cliente = 1
ORDER BY pf.id_producto;

-- Clientes que tienen al menos un producto bloqueado.
SELECT DISTINCT
    c.id_cliente,
    c.nombres,
    c.apellidos,
    c.estado_cliente
FROM cliente c
INNER JOIN producto_financiero pf
    ON c.id_cliente = pf.id_cliente
INNER JOIN estado_producto ep
    ON pf.id_estado_producto = ep.id_estado_producto
WHERE ep.nombre_estado = 'bloqueado'
ORDER BY c.id_cliente;

-- =========================================================
-- 2. Cuentas, tarjetas y créditos
-- =========================================================

-- Cuentas con información general del producto y cliente.
SELECT
    cta.id_producto,
    pf.numero_producto,
    cli.id_cliente,
    cli.nombres,
    cli.apellidos,
    cta.tipo_cuenta,
    cta.saldo_actual,
    cta.moneda
FROM cuenta cta
INNER JOIN producto_financiero pf
    ON cta.id_producto = pf.id_producto
INNER JOIN cliente cli
    ON pf.id_cliente = cli.id_cliente
ORDER BY cta.id_producto;

-- Cuentas con saldo mayor a 2.000.000.
SELECT *
FROM cuenta
WHERE saldo_actual > 2000000
ORDER BY saldo_actual DESC;

-- Tarjetas de crédito con cupo mayor a 2.000.000.
SELECT
    t.id_producto,
    pf.numero_producto,
    t.tipo_tarjeta,
    t.numero_enmascarado,
    t.fecha_vencimiento,
    t.cupo
FROM tarjeta t
INNER JOIN producto_financiero pf
    ON t.id_producto = pf.id_producto
WHERE t.tipo_tarjeta = 'credito'
  AND t.cupo > 2000000
ORDER BY t.cupo DESC;

-- Créditos con saldo pendiente mayor a cero.
SELECT
    cr.id_producto,
    pf.numero_producto,
    cr.tipo_credito,
    cr.monto_aprobado,
    cr.saldo_pendiente,
    cr.tasa_interes,
    cr.fecha_desembolso
FROM credito cr
INNER JOIN producto_financiero pf
    ON cr.id_producto = pf.id_producto
WHERE cr.saldo_pendiente > 0
ORDER BY cr.saldo_pendiente DESC;

-- =========================================================
-- 3. Transacciones
-- =========================================================

-- Todas las transacciones con cliente, canal y estado.
SELECT
    t.id_transaccion,
    cli.id_cliente,
    cli.nombres,
    cli.apellidos,
    t.tipo_transaccion,
    t.monto,
    t.fecha_hora,
    t.es_fallida,
    ca.nombre AS canal,
    et.nombre_estado AS estado_transaccion,
    t.id_producto_origen,
    t.id_producto_destino
FROM transaccion t
INNER JOIN cliente cli
    ON t.id_cliente = cli.id_cliente
INNER JOIN canal ca
    ON t.id_canal = ca.id_canal
INNER JOIN estado_transaccion et
    ON t.id_estado_transaccion = et.id_estado_transaccion
ORDER BY t.fecha_hora DESC;

-- Transacciones realizadas por un cliente específico.
-- Cambiar el valor 1 por el id_cliente requerido.
SELECT *
FROM transaccion
WHERE id_cliente = 1
ORDER BY fecha_hora DESC;

-- Transacciones realizadas por canal app.
SELECT
    t.*,
    ca.nombre AS canal
FROM transaccion t
INNER JOIN canal ca
    ON t.id_canal = ca.id_canal
WHERE ca.nombre = 'app'
ORDER BY t.fecha_hora DESC;

-- Transacciones cuyo estado sea aplicada.
SELECT
    t.*,
    et.nombre_estado AS estado_transaccion
FROM transaccion t
INNER JOIN estado_transaccion et
    ON t.id_estado_transaccion = et.id_estado_transaccion
WHERE et.nombre_estado = 'aplicada'
ORDER BY t.fecha_hora DESC;

-- Transacciones cuyo estado sea rechazada.
SELECT
    t.*,
    et.nombre_estado AS estado_transaccion
FROM transaccion t
INNER JOIN estado_transaccion et
    ON t.id_estado_transaccion = et.id_estado_transaccion
WHERE et.nombre_estado = 'rechazada'
ORDER BY t.fecha_hora DESC;

-- Transacciones fallidas.
SELECT *
FROM transaccion
WHERE es_fallida = TRUE
ORDER BY fecha_hora DESC;

-- Transacciones con monto mayor a un umbral determinado.
-- Cambiar 2000000 por el valor requerido.
SELECT *
FROM transaccion
WHERE monto > 2000000
ORDER BY monto DESC;

-- Transferencias realizadas entre productos financieros.
SELECT *
FROM transaccion
WHERE tipo_transaccion = 'transferencia'
  AND id_producto_origen IS NOT NULL
  AND id_producto_destino IS NOT NULL
ORDER BY fecha_hora DESC;

-- Transacciones con producto origen, pero sin producto destino.
SELECT *
FROM transaccion
WHERE id_producto_origen IS NOT NULL
  AND id_producto_destino IS NULL
ORDER BY fecha_hora DESC;

-- Transacciones con producto destino, pero sin producto origen.
SELECT *
FROM transaccion
WHERE id_producto_origen IS NULL
  AND id_producto_destino IS NOT NULL
ORDER BY fecha_hora DESC;

-- Reversos o anulaciones.
SELECT *
FROM transaccion
WHERE tipo_transaccion IN ('reverso', 'anulacion')
ORDER BY fecha_hora DESC;

-- Transacciones reversadas junto con su transacción original.
SELECT
    tr.id_transaccion AS id_transaccion_reverso,
    tr.tipo_transaccion AS tipo_reverso,
    tr.monto AS monto_reverso,
    tr.fecha_hora AS fecha_reverso,
    tor.id_transaccion AS id_transaccion_original,
    tor.tipo_transaccion AS tipo_original,
    tor.monto AS monto_original,
    tor.fecha_hora AS fecha_original
FROM transaccion tr
INNER JOIN transaccion tor
    ON tr.id_transaccion_original = tor.id_transaccion
WHERE tr.tipo_transaccion = 'reverso'
ORDER BY tr.fecha_hora DESC;

-- =========================================================
-- 4. Fraude básico y alertas
-- =========================================================

-- Reglas de fraude activas.
SELECT *
FROM regla_fraude
WHERE activa = TRUE
ORDER BY id_regla;

-- Todas las alertas de fraude.
SELECT *
FROM alerta_fraude
ORDER BY fecha_hora DESC;

-- Alertas abiertas.
SELECT
    af.*,
    ea.nombre_estado AS estado_alerta
FROM alerta_fraude af
INNER JOIN estado_alerta ea
    ON af.id_estado_alerta = ea.id_estado_alerta
WHERE ea.nombre_estado = 'abierta'
ORDER BY af.fecha_hora DESC;

-- Alertas de severidad alta o crítica.
SELECT *
FROM alerta_fraude
WHERE severidad IN ('alta', 'critica')
ORDER BY fecha_hora DESC;

-- Transacciones asociadas a una alerta específica.
-- Cambiar el valor 1 por el id_alerta requerido.
SELECT
    af.id_alerta,
    af.tipo_alerta,
    af.severidad,
    t.id_transaccion,
    t.tipo_transaccion,
    t.monto,
    t.fecha_hora,
    t.es_fallida
FROM alerta_fraude af
INNER JOIN alerta_transaccion atx
    ON af.id_alerta = atx.id_alerta
INNER JOIN transaccion t
    ON atx.id_transaccion = t.id_transaccion
WHERE af.id_alerta = 1
ORDER BY t.fecha_hora DESC;

-- Alertas junto con la regla que las generó.
SELECT
    af.id_alerta,
    af.tipo_alerta,
    af.severidad,
    af.fecha_hora,
    rf.nombre AS nombre_regla,
    rf.tipo_regla,
    rf.umbral,
    rf.ventana_minutos,
    rf.max_intentos,
    rf.activa
FROM alerta_fraude af
INNER JOIN regla_fraude rf
    ON af.id_regla = rf.id_regla
ORDER BY af.fecha_hora DESC;

-- =========================================================
-- 5. Auditoría
-- =========================================================

-- Eventos de auditoría con cliente, empleado y canal.
SELECT
    ea.id_evento,
    ea.tipo_responsable,
    ea.accion,
    ea.fecha_hora,
    ea.motivo,
    ca.nombre AS canal,
    cli.nombres AS cliente_nombres,
    cli.apellidos AS cliente_apellidos,
    emp.nombres AS empleado_nombres,
    emp.apellidos AS empleado_apellidos,
    emp.cargo
FROM evento_auditoria ea
INNER JOIN canal ca
    ON ea.id_canal = ca.id_canal
LEFT JOIN cliente cli
    ON ea.id_cliente = cli.id_cliente
LEFT JOIN empleado emp
    ON ea.id_empleado = emp.id_empleado
ORDER BY ea.fecha_hora DESC;

-- Eventos ejecutados por empleados.
SELECT
    ea.id_evento,
    ea.accion,
    ea.fecha_hora,
    ea.motivo,
    emp.nombres,
    emp.apellidos,
    emp.cargo
FROM evento_auditoria ea
INNER JOIN empleado emp
    ON ea.id_empleado = emp.id_empleado
WHERE ea.tipo_responsable = 'empleado'
ORDER BY ea.fecha_hora DESC;

-- Eventos generados por el sistema.
SELECT *
FROM evento_auditoria
WHERE tipo_responsable = 'sistema'
ORDER BY fecha_hora DESC;

-- Eventos relacionados con transacciones.
SELECT *
FROM evento_auditoria
WHERE id_transaccion IS NOT NULL
ORDER BY fecha_hora DESC;

-- Eventos relacionados con productos.
SELECT *
FROM evento_auditoria
WHERE id_producto IS NOT NULL
ORDER BY fecha_hora DESC;

-- Eventos relacionados con alertas.
SELECT *
FROM evento_auditoria
WHERE id_alerta IS NOT NULL
ORDER BY fecha_hora DESC;
