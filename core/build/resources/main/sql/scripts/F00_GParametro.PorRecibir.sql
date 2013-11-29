-- GParametro.PorRecibir.sql
--

COPY (SELECT valor FROM gparametro WHERE id_parametro = 'ruta_por_recibir') TO STDOUT;
