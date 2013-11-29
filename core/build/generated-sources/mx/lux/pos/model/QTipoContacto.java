package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QTipoContacto is a Querydsl query type for TipoContacto
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTipoContacto extends EntityPathBase<TipoContacto> {

    private static final long serialVersionUID = -982135004;

    public static final QTipoContacto tipoContacto = new QTipoContacto("tipoContacto");

    public final StringPath descripcion = createString("descripcion");

    public final NumberPath<Integer> id_tipo_contacto = createNumber("id_tipo_contacto", Integer.class);

    public QTipoContacto(String variable) {
        super(TipoContacto.class, forVariable(variable));
    }

    public QTipoContacto(Path<? extends TipoContacto> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QTipoContacto(PathMetadata<?> metadata) {
        super(TipoContacto.class, metadata);
    }

}

