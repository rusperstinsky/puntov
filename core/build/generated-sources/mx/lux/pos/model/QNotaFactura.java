package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QNotaFactura is a Querydsl query type for NotaFactura
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QNotaFactura extends EntityPathBase<NotaFactura> {

    private static final long serialVersionUID = -1656952823;

    public static final QNotaFactura notaFactura = new QNotaFactura("notaFactura");

    public final StringPath calle = createString("calle");

    public final StringPath codigoPostal = createString("codigoPostal");

    public final StringPath colonia = createString("colonia");

    public final StringPath email = createString("email");

    public final StringPath estado = createString("estado");

    public final StringPath estatus = createString("estatus");

    public final StringPath factura = createString("factura");

    public final StringPath facturaOri = createString("facturaOri");

    public final DateTimePath<java.util.Date> fechaImpresion = createDateTime("fechaImpresion", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final StringPath formaPago = createString("formaPago");

    public final StringPath id = createString("id");

    public final StringPath idCliente = createString("idCliente");

    public final StringPath idEmpleado = createString("idEmpleado");

    public final StringPath idFactura = createString("idFactura");

    public final StringPath importe = createString("importe");

    public final StringPath localidad = createString("localidad");

    public final StringPath metodoPago = createString("metodoPago");

    public final StringPath municipio = createString("municipio");

    public final StringPath noExterior = createString("noExterior");

    public final StringPath noInterior = createString("noInterior");

    public final StringPath nombre = createString("nombre");

    public final StringPath observaciones = createString("observaciones");

    public final StringPath paciente = createString("paciente");

    public final StringPath pais = createString("pais");

    public final StringPath razon = createString("razon");

    public final StringPath referencia = createString("referencia");

    public final StringPath rfc = createString("rfc");

    public final StringPath rx = createString("rx");

    public final StringPath ticket = createString("ticket");

    public final StringPath tipo = createString("tipo");

    public final StringPath tipoFac = createString("tipoFac");

    public final StringPath url = createString("url");

    public final StringPath xml = createString("xml");

    public QNotaFactura(String variable) {
        super(NotaFactura.class, forVariable(variable));
    }

    public QNotaFactura(Path<? extends NotaFactura> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QNotaFactura(PathMetadata<?> metadata) {
        super(NotaFactura.class, metadata);
    }

}

