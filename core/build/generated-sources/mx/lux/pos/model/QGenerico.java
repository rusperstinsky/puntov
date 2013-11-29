package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QGenerico is a Querydsl query type for Generico
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGenerico extends EntityPathBase<Generico> {

    private static final long serialVersionUID = -1252506247;

    public static final QGenerico generico = new QGenerico("generico");

    public final StringPath descripcion = createString("descripcion");

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final StringPath id = createString("id");

    public final StringPath idMod = createString("idMod");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public final BooleanPath inventariable = createBoolean("inventariable");

    public final StringPath surte = createString("surte");

    public QGenerico(String variable) {
        super(Generico.class, forVariable(variable));
    }

    public QGenerico(Path<? extends Generico> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QGenerico(PathMetadata<?> metadata) {
        super(Generico.class, metadata);
    }

}

