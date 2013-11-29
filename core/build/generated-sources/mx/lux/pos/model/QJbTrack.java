package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QJbTrack is a Querydsl query type for JbTrack
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QJbTrack extends EntityPathBase<JbTrack> {

    private static final long serialVersionUID = -812556590;

    public static final QJbTrack jbTrack = new QJbTrack("jbTrack");

    public final StringPath emp = createString("emp");

    public final StringPath estado = createString("estado");

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final NumberPath<Integer> id_jbtrack = createNumber("id_jbtrack", Integer.class);

    public final StringPath id_mod = createString("id_mod");

    public final StringPath id_viaje = createString("id_viaje");

    public final StringPath obs = createString("obs");

    public final StringPath rx = createString("rx");

    public QJbTrack(String variable) {
        super(JbTrack.class, forVariable(variable));
    }

    public QJbTrack(Path<? extends JbTrack> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QJbTrack(PathMetadata<?> metadata) {
        super(JbTrack.class, metadata);
    }

}

