package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QFormaContacto is a Querydsl query type for FormaContacto
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QFormaContacto extends EntityPathBase<FormaContacto> {

    private static final long serialVersionUID = -25221077;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QFormaContacto formaContacto = new QFormaContacto("formaContacto");

    public final StringPath contacto = createString("contacto");

    public final DateTimePath<java.util.Date> fecha_mod = createDateTime("fecha_mod", java.util.Date.class);

    public final NumberPath<Integer> id_cliente = createNumber("id_cliente", Integer.class);

    public final NumberPath<Integer> id_sucursal = createNumber("id_sucursal", Integer.class);

    public final NumberPath<Integer> id_tipo_contacto = createNumber("id_tipo_contacto", Integer.class);

    public final StringPath observaciones = createString("observaciones");

    public final StringPath rx = createString("rx");

    public final QTipoContacto tipoContacto;

    public QFormaContacto(String variable) {
        this(FormaContacto.class, forVariable(variable), INITS);
    }

    public QFormaContacto(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QFormaContacto(PathMetadata<?> metadata, PathInits inits) {
        this(FormaContacto.class, metadata, inits);
    }

    public QFormaContacto(Class<? extends FormaContacto> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tipoContacto = inits.isInitialized("tipoContacto") ? new QTipoContacto(forProperty("tipoContacto")) : null;
    }

}

