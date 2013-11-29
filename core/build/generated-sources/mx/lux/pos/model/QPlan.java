package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPlan is a Querydsl query type for Plan
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPlan extends EntityPathBase<Plan> {

    private static final long serialVersionUID = 2131281834;

    public static final QPlan plan = new QPlan("plan");

    public final StringPath descripcion = createString("descripcion");

    public final StringPath id = createString("id");

    public QPlan(String variable) {
        super(Plan.class, forVariable(variable));
    }

    public QPlan(Path<? extends Plan> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPlan(PathMetadata<?> metadata) {
        super(Plan.class, metadata);
    }

}

