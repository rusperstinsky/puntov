package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QGrupoArticulo is a Querydsl query type for GrupoArticulo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGrupoArticulo extends EntityPathBase<GrupoArticulo> {

    private static final long serialVersionUID = -1812778749;

    public static final QGrupoArticulo grupoArticulo = new QGrupoArticulo("grupoArticulo");

    public final StringPath descripcion = createString("descripcion");

    public final NumberPath<Integer> idGrupo = createNumber("idGrupo", Integer.class);

    public QGrupoArticulo(String variable) {
        super(GrupoArticulo.class, forVariable(variable));
    }

    public QGrupoArticulo(Path<? extends GrupoArticulo> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QGrupoArticulo(PathMetadata<?> metadata) {
        super(GrupoArticulo.class, metadata);
    }

}

