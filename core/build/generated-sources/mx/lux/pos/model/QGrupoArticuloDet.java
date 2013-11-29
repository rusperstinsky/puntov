package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QGrupoArticuloDet is a Querydsl query type for GrupoArticuloDet
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGrupoArticuloDet extends EntityPathBase<GrupoArticuloDet> {

    private static final long serialVersionUID = 427137040;

    public static final QGrupoArticuloDet grupoArticuloDet = new QGrupoArticuloDet("grupoArticuloDet");

    public final StringPath articulo = createString("articulo");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> idGrupo = createNumber("idGrupo", Integer.class);

    public QGrupoArticuloDet(String variable) {
        super(GrupoArticuloDet.class, forVariable(variable));
    }

    public QGrupoArticuloDet(Path<? extends GrupoArticuloDet> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QGrupoArticuloDet(PathMetadata<?> metadata) {
        super(GrupoArticuloDet.class, metadata);
    }

}

