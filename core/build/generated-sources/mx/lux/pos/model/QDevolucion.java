package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QDevolucion is a Querydsl query type for Devolucion
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDevolucion extends EntityPathBase<Devolucion> {

    private static final long serialVersionUID = 703868169;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QDevolucion devolucion = new QDevolucion("devolucion");

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final QFormaPago formaPago;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> idBanco = createNumber("idBanco", Integer.class);

    public final StringPath idFormaPago = createString("idFormaPago");

    public final NumberPath<Integer> idMod = createNumber("idMod", Integer.class);

    public final NumberPath<Integer> idPago = createNumber("idPago", Integer.class);

    public final QModificacion modificacion;

    public final NumberPath<java.math.BigDecimal> monto = createNumber("monto", java.math.BigDecimal.class);

    public final QPago pago;

    public final StringPath referencia = createString("referencia");

    public final StringPath tipo = createString("tipo");

    public final StringPath transf = createString("transf");

    public QDevolucion(String variable) {
        this(Devolucion.class, forVariable(variable), INITS);
    }

    public QDevolucion(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDevolucion(PathMetadata<?> metadata, PathInits inits) {
        this(Devolucion.class, metadata, inits);
    }

    public QDevolucion(Class<? extends Devolucion> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.formaPago = inits.isInitialized("formaPago") ? new QFormaPago(forProperty("formaPago")) : null;
        this.modificacion = inits.isInitialized("modificacion") ? new QModificacion(forProperty("modificacion"), inits.get("modificacion")) : null;
        this.pago = inits.isInitialized("pago") ? new QPago(forProperty("pago"), inits.get("pago")) : null;
    }

}

