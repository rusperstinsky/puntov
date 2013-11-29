package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMensaje is a Querydsl query type for Mensaje
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMensaje extends EntityPathBase<Mensaje> {

    private static final long serialVersionUID = 1959883454;

    public static final QMensaje mensaje = new QMensaje("mensaje");

    public final StringPath clave = createString("clave");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath texto = createString("texto");

    public QMensaje(String variable) {
        super(Mensaje.class, forVariable(variable));
    }

    public QMensaje(Path<? extends Mensaje> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QMensaje(PathMetadata<?> metadata) {
        super(Mensaje.class, metadata);
    }

}

