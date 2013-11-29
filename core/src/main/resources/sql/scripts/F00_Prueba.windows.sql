CREATE TEMP TABLE t_parametro (
  id text,
  valor text
);

COPY t_parametro (id, valor) FROM STDIN;
intentos_acuses	3
emp_electronico	99
acuse_seg_ciclo	3600
url_acuse_ajuste_venta	http://localhost:8048/cgi-bin/WService=Wmaestro/e-can_venta_n.p
url_acuse_venta_dia	http://localhost:8048/cgi-bin/WService=Wmaestro/e-lee_venta_n.p
imprime_duplicado	no
demo	true
\.

UPDATE gparametro 
   SET valor = t_parametro.valor
  FROM t_parametro
 WHERE id_parametro = t_parametro.id;

DROP TABLE t_parametro;
