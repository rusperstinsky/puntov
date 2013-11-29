package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QCotizacion is a Querydsl query type for Cotizacion
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCotizacion extends EntityPathBase<Cotizacion> {

    private static final long serialVersionUID = 167335758;

    public static final QCotizacion cotizacion = new QCotizacion("cotizacion");

    public final ListPath<CotizaDet, QCotizaDet> cotizaDet = this.<CotizaDet, QCotizaDet>createList("cotizaDet", CotizaDet.class, QCotizaDet.class);

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaVenta = createDateTime("fechaVenta", java.util.Date.class);

    public final NumberPath<Integer> idCliente = createNumber("idCliente", Integer.class);

    public final NumberPath<Integer> idCotiza = createNumber("idCotiza", Integer.class);

    public final StringPath idEmpleado = createString("idEmpleado");

    public final StringPath idFactura = createString("idFactura");

    public final NumberPath<Integer> idReceta = createNumber("idReceta", Integer.class);

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath nombre = createString("nombre");

    public final StringPath observaciones = createString("observaciones");

    public final StringPath tel = createString("tel");

    public final StringPath titulo = createString("titulo");

    public final StringPath udf1 = createString("udf1");

    public QCotizacion(String variable) {
        super(Cotizacion.class, forVariable(variable));
    }

    public QCotizacion(Path<? extends Cotizacion> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QCotizacion(PathMetadata<?> metadata) {
        super(Cotizacion.class, metadata);
    }

}

