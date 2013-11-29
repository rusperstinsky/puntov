-- F13 Insert Empleado Prueba 2
-- 

INSERT INTO empleado (id_empleado) VALUES ('9998');
UPDATE empleado
       SET nombre_empleado='Calidad de Software', 
           ap_pat_empleado='', 
           ap_mat_empleado='', 
           id_puesto=1, 
           passwd='0000', 
           id_sync='1', 
           fecha_mod=now(), 
           id_mod='', 
           id_sucursal=esta_sucursal() 
       WHERE id_empleado = '9998'; 
