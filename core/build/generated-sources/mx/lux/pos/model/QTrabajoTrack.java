package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QTrabajoTrack is a Querydsl query type for TrabajoTrack
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTrabajoTrack extends EntityPathBase<TrabajoTrack> {

    private static final long serialVersionUID = 1301710629;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QTrabajoTrack trabajoTrack = new QTrabajoTrack("trabajoTrack");

    public final StringPath emp = createString("emp");

    public final StringPath estado = createString("estado");

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final StringPath id = createString("id");

    public final StringPath id_mod = createString("id_mod");

    public final StringPath id_viaje = createString("id_viaje");

    public final StringPath obs = createString("obs");

    public final QTrabajo trabajo;

    public QTrabajoTrack(String variable) {
        this(TrabajoTrack.class, forVariable(variable), INITS);
    }

    public QTrabajoTrack(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTrabajoTrack(PathMetadata<?> metadata, PathInits inits) {
        this(TrabajoTrack.class, metadata, inits);
    }

    public QTrabajoTrack(Class<? extends TrabajoTrack> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.trabajo = inits.isInitialized("trabajo") ? new QTrabajo(forProperty("trabajo"), inits.get("trabajo")) : null;
    }

}

