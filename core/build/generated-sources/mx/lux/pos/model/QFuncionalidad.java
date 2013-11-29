package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QFuncionalidad is a Querydsl query type for Funcionalidad
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QFuncionalidad extends EntityPathBase<Funcionalidad> {

    private static final long serialVersionUID = 2139925900;

    public static final QFuncionalidad funcionalidad = new QFuncionalidad("funcionalidad");

    public final BooleanPath activo = createBoolean("activo");

    public final StringPath id = createString("id");

    public QFuncionalidad(String variable) {
        super(Funcionalidad.class, forVariable(variable));
    }

    public QFuncionalidad(Path<? extends Funcionalidad> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QFuncionalidad(PathMetadata<?> metadata) {
        super(Funcionalidad.class, metadata);
    }

}

