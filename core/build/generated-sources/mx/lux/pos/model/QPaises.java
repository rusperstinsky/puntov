package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPaises is a Querydsl query type for Paises
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPaises extends EntityPathBase<Paises> {

    private static final long serialVersionUID = -547470070;

    public static final QPaises paises = new QPaises("paises");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> orden = createNumber("orden", Integer.class);

    public final StringPath pais = createString("pais");

    public QPaises(String variable) {
        super(Paises.class, forVariable(variable));
    }

    public QPaises(Path<? extends Paises> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPaises(PathMetadata<?> metadata) {
        super(Paises.class, metadata);
    }

}

