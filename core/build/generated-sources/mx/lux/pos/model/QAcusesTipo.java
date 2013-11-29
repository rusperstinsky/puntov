package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAcusesTipo is a Querydsl query type for AcusesTipo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAcusesTipo extends EntityPathBase<AcusesTipo> {

    private static final long serialVersionUID = -2077110525;

    public static final QAcusesTipo acusesTipo = new QAcusesTipo("acusesTipo");

    public final StringPath descr = createString("descr");

    public final StringPath id_tipo = createString("id_tipo");

    public final StringPath pagina = createString("pagina");

    public QAcusesTipo(String variable) {
        super(AcusesTipo.class, forVariable(variable));
    }

    public QAcusesTipo(Path<? extends AcusesTipo> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QAcusesTipo(PathMetadata<?> metadata) {
        super(AcusesTipo.class, metadata);
    }

}

