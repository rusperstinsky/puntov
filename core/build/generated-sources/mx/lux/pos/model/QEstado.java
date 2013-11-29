package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QEstado is a Querydsl query type for Estado
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEstado extends EntityPathBase<Estado> {

    private static final long serialVersionUID = -845456985;

    public static final QEstado estado = new QEstado("estado");

    public final StringPath edo1 = createString("edo1");

    public final StringPath id = createString("id");

    public final StringPath nombre = createString("nombre");

    public final StringPath rango1 = createString("rango1");

    public final StringPath rango2 = createString("rango2");

    public QEstado(String variable) {
        super(Estado.class, forVariable(variable));
    }

    public QEstado(Path<? extends Estado> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QEstado(PathMetadata<?> metadata) {
        super(Estado.class, metadata);
    }

}

