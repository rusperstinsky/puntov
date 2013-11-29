CREATE TEMP TABLE t_parametro (
  id text,
  valor text
);

COPY t_parametro (id, valor) FROM STDIN;
intentos_acuses	3
emp_electronico	99
acuse_seg_ciclo	3600
ruta_catalogos	/home/paso/catalogos
ruta_cierre	/home/paso/cierre
ruta_comprobantes	/home/paso/facturas
ruta_inv_tr	/home/paso/inv
ruta_lista_precios	/home/paso/lp
ruta_por_enviar	/home/paso/por_enviar
ruta_por_recibir	/home/paso/por_recibir
ruta_recibidos	/home/paso/recibidos
ruta_remision	/home/paso/remesa
comando_zip	7z a -tzip 
url_acuse_ajuste_venta	http://localhost:8048/cgi-bin/WService=Wmaestro/e-can_venta_n.p
url_acuse_venta_dia	http://localhost:8048/cgi-bin/WService=Wmaestro/e-lee_venta_n.p
impresora_ticket	lpr -P lp3 
imprime_duplicado	no
demo	true
\.

UPDATE gparametro 
   SET valor = t_parametro.valor
  FROM t_parametro
 WHERE id_parametro = t_parametro.id;

DROP TABLE t_parametro;
