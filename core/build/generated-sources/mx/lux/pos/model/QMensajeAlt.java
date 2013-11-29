package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMensajeAlt is a Querydsl query type for MensajeAlt
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMensajeAlt extends EntityPathBase<MensajeAlt> {

    private static final long serialVersionUID = 1102622219;

    public static final QMensajeAlt mensajeAlt = new QMensajeAlt("mensajeAlt");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idioma = createString("idioma");

    public final NumberPath<Integer> idMensaje = createNumber("idMensaje", Integer.class);

    public final StringPath texto = createString("texto");

    public QMensajeAlt(String variable) {
        super(MensajeAlt.class, forVariable(variable));
    }

    public QMensajeAlt(Path<? extends MensajeAlt> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QMensajeAlt(PathMetadata<?> metadata) {
        super(MensajeAlt.class, metadata);
    }

}

