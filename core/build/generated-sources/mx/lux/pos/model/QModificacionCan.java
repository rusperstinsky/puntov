package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QModificacionCan is a Querydsl query type for ModificacionCan
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QModificacionCan extends EntityPathBase<ModificacionCan> {

    private static final long serialVersionUID = 589376034;

    public static final QModificacionCan modificacionCan = new QModificacionCan("modificacionCan");

    public final StringPath estadoAnterior = createString("estadoAnterior");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QModificacionCan(String variable) {
        super(ModificacionCan.class, forVariable(variable));
    }

    public QModificacionCan(Path<? extends ModificacionCan> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QModificacionCan(PathMetadata<?> metadata) {
        super(ModificacionCan.class, metadata);
    }

}

