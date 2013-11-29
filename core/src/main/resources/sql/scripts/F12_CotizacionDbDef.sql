-- Update Cotizacion.sql

BEGIN;
ALTER TABLE cotizacion RENAME TO zz_cotizacion;
ALTER TABLE cotiza_det RENAME TO zz_cotiza_det;

CREATE TABLE cotiza(
       id_cotiza SERIAL,
       id_sucursal INTEGER NOT NULL,
       id_cliente INTEGER,
       id_empleado TEXT,
       id_receta INTEGER,
       fecha_mod TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
       id_factura TEXT,
       fecha_venta TIMESTAMP WITHOUT TIME ZONE,
       nombre TEXT,
       telefono TEXT,
       observaciones TEXT,
       udf1 TEXT,
       titulo TEXT,
       CONSTRAINT cotiza_pk PRIMARY KEY (id_cotiza)
);

CREATE TABLE cotiza_det (
       id_cotiza_det SERIAL,
       id_sucursal INTEGER,
       id_cotiza INTEGER NOT NULL,
       id_articulo INTEGER NOT NULL,
       articulo TEXT,
       color TEXT,
       cantidad INTEGER,
       udf1 TEXT,
       fecha_mod TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
       CONSTRAINT cotiza_det_pk PRIMARY KEY (id_cotiza, id_articulo)
);

COMMIT;
