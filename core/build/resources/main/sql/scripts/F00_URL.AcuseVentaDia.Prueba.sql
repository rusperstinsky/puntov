--- F14_URL_AcuseVentaDiaPrueba.sql
--- Cambia el valor de los parametros para notificaciones de venta en tiempo real en maqiunas de prueba.

INSERT INTO gparametro (id_parametro) VALUES ('url_acuse_venta_dia');
UPDATE gparametro 
   SET valor = 'http://sgi.lux.mx.prueba:8048/cgi-bin/wspd_cgi.sh/WService=Wmaestro/e-lee_venta_n.p' 
 WHERE id_parametro = 'url_acuse_venta_dia';

INSERT INTO gparametro (id_parametro) VALUES ('url_acuse_ajuste_venta');
UPDATE gparametro 
   SET valor = 'http://sgi.lux.mx.prueba:8048/cgi-bin/wspd_cgi.sh/WService=Wmaestro/e-can_venta_n.p' 
 WHERE id_parametro = 'url_acuse_ajuste_venta';

INSERT INTO gparametro (id_parametro) VALUES ('acuse_seg_ciclo');
UPDATE gparametro
   SET valor = '600'
 WHERE id_parametro = 'acuse_seg_ciclo';
