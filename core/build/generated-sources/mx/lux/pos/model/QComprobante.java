package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QComprobante is a Querydsl query type for Comprobante
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QComprobante extends EntityPathBase<Comprobante> {

    private static final long serialVersionUID = 1864041747;

    public static final QComprobante comprobante = new QComprobante("comprobante");

    public final StringPath cadenaOriginal = createString("cadenaOriginal");

    public final StringPath calle = createString("calle");

    public final StringPath codigoPostal = createString("codigoPostal");

    public final StringPath colonia = createString("colonia");

    public final SetPath<DetalleComprobante, QDetalleComprobante> detalles = this.<DetalleComprobante, QDetalleComprobante>createSet("detalles", DetalleComprobante.class, QDetalleComprobante.class);

    public final StringPath email = createString("email");

    public final StringPath estado = createString("estado");

    public final StringPath estatus = createString("estatus");

    public final StringPath exterior = createString("exterior");

    public final StringPath factura = createString("factura");

    public final DateTimePath<java.util.Date> fechaImpresion = createDateTime("fechaImpresion", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaModificacion = createDateTime("fechaModificacion", java.util.Date.class);

    public final StringPath formaPago = createString("formaPago");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idCliente = createString("idCliente");

    public final StringPath idEmpleado = createString("idEmpleado");

    public final StringPath idFactura = createString("idFactura");

    public final StringPath idFiscal = createString("idFiscal");

    public final StringPath idOrigen = createString("idOrigen");

    public final StringPath importe = createString("importe");

    public final NumberPath<java.math.BigDecimal> impuestos = createNumber("impuestos", java.math.BigDecimal.class);

    public final StringPath interior = createString("interior");

    public final StringPath localidad = createString("localidad");

    public final StringPath metodoPago = createString("metodoPago");

    public final StringPath municipio = createString("municipio");

    public final StringPath nombre = createString("nombre");

    public final StringPath observaciones = createString("observaciones");

    public final StringPath paciente = createString("paciente");

    public final StringPath pais = createString("pais");

    public final StringPath razon = createString("razon");

    public final StringPath referencia = createString("referencia");

    public final StringPath rfc = createString("rfc");

    public final StringPath rx = createString("rx");

    public final StringPath sello = createString("sello");

    public final NumberPath<java.math.BigDecimal> subtotal = createNumber("subtotal", java.math.BigDecimal.class);

    public final StringPath ticket = createString("ticket");

    public final StringPath tipo = createString("tipo");

    public final StringPath tipoFactura = createString("tipoFactura");

    public final StringPath url = createString("url");

    public final StringPath xml = createString("xml");

    public QComprobante(String variable) {
        super(Comprobante.class, forVariable(variable));
    }

    public QComprobante(Path<? extends Comprobante> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QComprobante(PathMetadata<?> metadata) {
        super(Comprobante.class, metadata);
    }

}

