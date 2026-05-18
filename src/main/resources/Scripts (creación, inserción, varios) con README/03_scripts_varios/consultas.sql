----Ver alertas con su regla de fraude
/u bosquebank
-- =========================================================
-- CONSULTAS SQL POSTGRESQL - BOSQUEBANK
-- =========================================================

-- 1. Obtener todos los clientes registrados
SELECT *
FROM cliente;


-- 2. Obtener los clientes activos
SELECT *
FROM cliente
WHERE estado_cliente = 'activo';


-- 3. Obtener los clientes bloqueados
SELECT *
FROM cliente
WHERE estado_cliente = 'bloqueado';


-- 4. Obtener los clientes en investigación
SELECT *
FROM cliente
WHERE estado_cliente = 'investigacion';


-- 5. Obtener nombre, apellido y documento de todos los clientes
SELECT 
    nombres,
    apellidos,
    tipo_documento,
    numero_documento
FROM cliente;


-- 6. Obtener clientes de una ciudad específica
SELECT *
FROM cliente
WHERE ciudad = 'Bogota';


-- 7. Obtener clientes con correo registrado
SELECT *
FROM cliente
WHERE email IS NOT NULL
  AND email <> '';


-- 8. Obtener clientes sin teléfono registrado
SELECT *
FROM cliente
WHERE telefono IS NULL
   OR telefono = '';


-- 9. Obtener clientes activos de una ciudad específica
SELECT *
FROM cliente
WHERE estado_cliente = 'activo'
  AND ciudad = 'Bogota';


-- 10. Obtener clientes bloqueados junto con sus productos financieros
SELECT 
    c.id_cliente,
    c.nombres,
    c.apellidos,
    c.estado_cliente,
    pf.id_producto,
    pf.numero_producto,
    pf.fecha_creacion
FROM cliente c
INNER JOIN producto_financiero pf
    ON c.id_cliente = pf.id_cliente
WHERE c.estado_cliente = 'bloqueado';


-- =========================================================
-- PRODUCTOS FINANCIEROS
-- =========================================================

-- 11. Obtener todos los productos financieros
SELECT *
FROM producto_financiero;


-- 12. Obtener productos financieros activos
SELECT 
    pf.*,
    ep.nombre_estado
FROM producto_financiero pf
INNER JOIN estado_producto ep
    ON pf.id_estado_producto = ep.id_estado_producto
WHERE ep.nombre_estado = 'activo';


-- 13. Obtener productos financieros bloqueados
SELECT 
    pf.*,
    ep.nombre_estado
FROM producto_financiero pf
INNER JOIN estado_producto ep
    ON pf.id_estado_producto = ep.id_estado_producto
WHERE ep.nombre_estado = 'bloqueado';


-- 14. Obtener productos financieros cancelados
SELECT 
    pf.*,
    ep.nombre_estado
FROM producto_financiero pf
INNER JOIN estado_producto ep
    ON pf.id_estado_producto = ep.id_estado_producto
WHERE ep.nombre_estado = 'cancelado';


-- 15. Obtener productos financieros de un cliente específico
SELECT *
FROM producto_financiero
WHERE id_cliente = 1;


-- 16. Obtener clientes junto con sus productos financieros
SELECT 
    c.id_cliente,
    c.nombres,
    c.apellidos,
    pf.id_producto,
    pf.numero_producto,
    pf.fecha_creacion
FROM cliente c
INNER JOIN producto_financiero pf
    ON c.id_cliente = pf.id_cliente;


-- 17. Obtener productos financieros junto con su estado actual
SELECT 
    pf.id_producto,
    pf.numero_producto,
    pf.fecha_creacion,
    ep.nombre_estado AS estado_producto
FROM producto_financiero pf
INNER JOIN estado_producto ep
    ON pf.id_estado_producto = ep.id_estado_producto;


-- 18. Obtener productos financieros creados después de una fecha determinada
SELECT *
FROM producto_financiero
WHERE fecha_creacion > DATE '2025-03-01';


-- 19. Obtener número de producto y estado de todos los productos financieros
SELECT 
    pf.numero_producto,
    ep.nombre_estado AS estado_producto
FROM producto_financiero pf
INNER JOIN estado_producto ep
    ON pf.id_estado_producto = ep.id_estado_producto;


-- 20. Obtener clientes que tengan al menos un producto financiero bloqueado
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
WHERE ep.nombre_estado = 'bloqueado';


-- =========================================================
-- CUENTAS, TARJETAS Y CRÉDITOS
-- =========================================================

-- 21. Obtener todas las cuentas registradas
SELECT *
FROM cuenta;


-- 22. Obtener cuentas de ahorro
SELECT *
FROM cuenta
WHERE tipo_cuenta = 'ahorro';


-- 23. Obtener cuentas corrientes
SELECT *
FROM cuenta
WHERE tipo_cuenta = 'corriente';


-- 24. Obtener cuentas con saldo mayor a un valor determinado
SELECT *
FROM cuenta
WHERE saldo_actual > 2000000;


-- 25. Obtener todas las tarjetas registradas
SELECT *
FROM tarjeta;


-- 26. Obtener tarjetas de crédito
SELECT *
FROM tarjeta
WHERE tipo_tarjeta = 'credito';


-- 27. Obtener tarjetas débito
SELECT *
FROM tarjeta
WHERE tipo_tarjeta = 'debito';


-- 28. Obtener tarjetas con cupo mayor a un valor determinado
SELECT *
FROM tarjeta
WHERE cupo > 2000000;


-- 29. Obtener todos los créditos registrados
SELECT *
FROM credito;


-- 30. Obtener créditos cuyo saldo pendiente sea mayor a cero
SELECT *
FROM credito
WHERE saldo_pendiente > 0;


-- =========================================================
-- TRANSACCIONES
-- =========================================================

-- 31. Obtener todas las transacciones registradas
SELECT *
FROM transaccion;


-- 32. Obtener transacciones realizadas por un cliente específico
SELECT *
FROM transaccion
WHERE id_cliente = 1;


-- 33. Obtener transacciones realizadas por canal app
SELECT 
    t.*,
    c.nombre AS canal
FROM transaccion t
INNER JOIN canal c
    ON t.id_canal = c.id_canal
WHERE c.nombre = 'app';


-- 34. Obtener transacciones realizadas por canal web
SELECT 
    t.*,
    c.nombre AS canal
FROM transaccion t
INNER JOIN canal c
    ON t.id_canal = c.id_canal
WHERE c.nombre = 'web';


-- 35. Obtener transacciones realizadas por canal cajero
SELECT 
    t.*,
    c.nombre AS canal
FROM transaccion t
INNER JOIN canal c
    ON t.id_canal = c.id_canal
WHERE c.nombre = 'cajero';


-- 36. Obtener transacciones cuyo estado sea aplicada
SELECT 
    t.*,
    et.nombre_estado AS estado_transaccion
FROM transaccion t
INNER JOIN estado_transaccion et
    ON t.id_estado_transaccion = et.id_estado_transaccion
WHERE et.nombre_estado = 'aplicada';


-- 37. Obtener transacciones cuyo estado sea rechazada
SELECT 
    t.*,
    et.nombre_estado AS estado_transaccion
FROM transaccion t
INNER JOIN estado_transaccion et
    ON t.id_estado_transaccion = et.id_estado_transaccion
WHERE et.nombre_estado = 'rechazada';


-- 38. Obtener transacciones cuyo estado sea reversada
SELECT 
    t.*,
    et.nombre_estado AS estado_transaccion
FROM transaccion t
INNER JOIN estado_transaccion et
    ON t.id_estado_transaccion = et.id_estado_transaccion
WHERE et.nombre_estado = 'reversada';


-- 39. Obtener transacciones fallidas
SELECT *
FROM transaccion
WHERE es_fallida = 'S';


-- 40. Obtener transacciones cuyo monto sea mayor a un umbral determinado
SELECT *
FROM transaccion
WHERE monto > 2000000;


-- 41. Obtener transferencias realizadas entre productos financieros
SELECT *
FROM transaccion
WHERE tipo_transaccion = 'transferencia'
  AND id_producto_origen IS NOT NULL
  AND id_producto_destino IS NOT NULL;


-- 42. Obtener transacciones que tengan producto origen pero no producto destino
SELECT *
FROM transaccion
WHERE id_producto_origen IS NOT NULL
  AND id_producto_destino IS NULL;


-- 43. Obtener transacciones que tengan producto destino pero no producto origen
SELECT *
FROM transaccion
WHERE id_producto_origen IS NULL
  AND id_producto_destino IS NOT NULL;


-- 44. Obtener transacciones que correspondan a reversos o anulaciones
SELECT *
FROM transaccion
WHERE tipo_transaccion IN ('reverso', 'anulacion');


-- 45. Obtener transacciones reversadas junto con su transacción original
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
WHERE tr.tipo_transaccion = 'reverso';


-- =========================================================
-- FRAUDE Y ALERTAS
-- =========================================================

-- 46. Obtener todas las alertas de fraude registradas
SELECT *
FROM alerta_fraude;

select *
from regla_fraude rf;


-- 47. Obtener alertas de fraude en estado abierta
SELECT 
    af.*,
    ea.nombre_estado AS estado_alerta
FROM alerta_fraude af
INNER JOIN estado_alerta ea
    ON af.id_estado_alerta = ea.id_estado_alerta
WHERE ea.nombre_estado = 'abierta';


-- 48. Obtener alertas con severidad alta o crítica
SELECT *
FROM alerta_fraude
WHERE severidad IN ('alta', 'critica');


-- 49. Obtener transacciones asociadas a una alerta de fraude específica
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
WHERE af.id_alerta = 1;


-- 50. Obtener alertas de fraude junto con la regla que las generó
SELECT 
    af.id_alerta,
    af.tipo_alerta,
    af.severidad,
    af.fecha_hora,
    rf.id_regla,
    rf.nombre AS nombre_regla,
    rf.tipo_regla,
    rf.umbral,
    rf.ventana_minutos,
    rf.max_intentos,
    rf.activa
FROM alerta_fraude af
INNER JOIN regla_fraude rf
    ON af.id_regla = rf.id_regla;