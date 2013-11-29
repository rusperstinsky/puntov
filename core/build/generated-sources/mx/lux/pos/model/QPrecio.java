package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPrecio is a Querydsl query type for Precio
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPrecio extends EntityPathBase<Precio> {

    private static final long serialVersionUID = -531904633;

    public static final QPrecio precio1 = new QPrecio("precio1");

    public final StringPath articulo = createString("articulo");

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath lista = createString("lista");

    public final NumberPath<java.math.BigDecimal> precio = createNumber("precio", java.math.BigDecimal.class);

    public final StringPath surte = createString("surte");

    public QPrecio(String variable) {
        super(Precio.class, forVariable(variable));
    }

    public QPrecio(Path<? extends Precio> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPrecio(PathMetadata<?> metadata) {
        super(Precio.class, metadata);
    }

}

