--- F08_URL_AcuseVentaDia.sql
--- Cambia el valor de los parametros para notificaciones de venta en tiempo real.

INSERT INTO gparametro (id_parametro) VALUES ('url_acuse_venta_dia');
UPDATE gparametro 
   SET valor = 'http://sgi.lux.mx:8048/cgi-bin/wspd_cgi.sh/WService=Wmaestro/e-lee_venta_n.p' 
 WHERE id_parametro = 'url_acuse_venta_dia';

INSERT INTO gparametro (id_parametro) VALUES ('url_acuse_ajuste_venta');
UPDATE gparametro 
   SET valor = 'http://sgi.lux.mx:8048/cgi-bin/wspd_cgi.sh/WService=Wmaestro/e-can_venta_n.p' 
 WHERE id_parametro = 'url_acuse_ajuste_venta';
