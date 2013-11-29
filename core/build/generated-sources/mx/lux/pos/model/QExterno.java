package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QExterno is a Querydsl query type for Externo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QExterno extends EntityPathBase<Externo> {

    private static final long serialVersionUID = -296084306;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QExterno externo = new QExterno("externo");

    public final StringPath armazon = createString("armazon");

    public final QCliente cliente;

    public final StringPath factura = createString("factura");

    public final DateTimePath<java.util.Date> fechaEntrega = createDateTime("fechaEntrega", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaFactura = createDateTime("fechaFactura", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaPromesa = createDateTime("fechaPromesa", java.util.Date.class);

    public final StringPath forma = createString("forma");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> idCliente = createNumber("idCliente", Integer.class);

    public final StringPath lente = createString("lente");

    public final StringPath material = createString("material");

    public final StringPath origen = createString("origen");

    public final QTrabajo trabajo;

    public QExterno(String variable) {
        this(Externo.class, forVariable(variable), INITS);
    }

    public QExterno(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QExterno(PathMetadata<?> metadata, PathInits inits) {
        this(Externo.class, metadata, inits);
    }

    public QExterno(Class<? extends Externo> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cliente = inits.isInitialized("cliente") ? new QCliente(forProperty("cliente"), inits.get("cliente")) : null;
        this.trabajo = inits.isInitialized("trabajo") ? new QTrabajo(forProperty("trabajo"), inits.get("trabajo")) : null;
    }

}

