package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QDescuento is a Querydsl query type for Descuento
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDescuento extends EntityPathBase<Descuento> {

    private static final long serialVersionUID = 1042363143;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QDescuento descuento = new QDescuento("descuento");

    public final StringPath clave = createString("clave");

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idEmpleado = createString("idEmpleado");

    public final StringPath idFactura = createString("idFactura");

    public final StringPath idTipoD = createString("idTipoD");

    public final QNotaVenta notaVenta;

    public final StringPath porcentaje = createString("porcentaje");

    public final StringPath tipoClave = createString("tipoClave");

    public QDescuento(String variable) {
        this(Descuento.class, forVariable(variable), INITS);
    }

    public QDescuento(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDescuento(PathMetadata<?> metadata, PathInits inits) {
        this(Descuento.class, metadata, inits);
    }

    public QDescuento(Class<? extends Descuento> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notaVenta = inits.isInitialized("notaVenta") ? new QNotaVenta(forProperty("notaVenta"), inits.get("notaVenta")) : null;
    }

}

