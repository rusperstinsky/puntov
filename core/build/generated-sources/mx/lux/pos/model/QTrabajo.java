package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QTrabajo is a Querydsl query type for Trabajo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTrabajo extends EntityPathBase<Trabajo> {

    private static final long serialVersionUID = -57858618;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QTrabajo trabajo = new QTrabajo("trabajo");

    public final StringPath caja = createString("caja");

    public final StringPath cliente = createString("cliente");

    public final StringPath empAtendio = createString("empAtendio");

    public final QEmpleado empleado;

    public final StringPath estado = createString("estado");

    public final StringPath externo = createString("externo");

    public final DateTimePath<java.util.Date> fechaPromesa = createDateTime("fechaPromesa", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaVenta = createDateTime("fechaVenta", java.util.Date.class);

    public final StringPath id = createString("id");

    public final StringPath idCliente = createString("idCliente");

    public final StringPath idGrupo = createString("idGrupo");

    public final StringPath idMod = createString("idMod");

    public final StringPath idViaje = createString("idViaje");

    public final StringPath jbTipo = createString("jbTipo");

    public final StringPath material = createString("material");

    public final BooleanPath noEnviar = createBoolean("noEnviar");

    public final BooleanPath noLlamar = createBoolean("noLlamar");

    public final NumberPath<Integer> numLlamada = createNumber("numLlamada", Integer.class);

    public final StringPath obsExt = createString("obsExt");

    public final StringPath retAuto = createString("retAuto");

    public final NumberPath<Integer> roto = createNumber("roto", Integer.class);

    public final NumberPath<java.math.BigDecimal> saldo = createNumber("saldo", java.math.BigDecimal.class);

    public final StringPath surte = createString("surte");

    public final StringPath tipoVenta = createString("tipoVenta");

    public final QTrabajoEstado trabajoEstado;

    public final ListPath<TrabajoTrack, QTrabajoTrack> trabajoTrack = this.<TrabajoTrack, QTrabajoTrack>createList("trabajoTrack", TrabajoTrack.class, QTrabajoTrack.class);

    public final DateTimePath<java.util.Date> volverLlamar = createDateTime("volverLlamar", java.util.Date.class);

    public QTrabajo(String variable) {
        this(Trabajo.class, forVariable(variable), INITS);
    }

    public QTrabajo(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTrabajo(PathMetadata<?> metadata, PathInits inits) {
        this(Trabajo.class, metadata, inits);
    }

    public QTrabajo(Class<? extends Trabajo> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.empleado = inits.isInitialized("empleado") ? new QEmpleado(forProperty("empleado"), inits.get("empleado")) : null;
        this.trabajoEstado = inits.isInitialized("trabajoEstado") ? new QTrabajoEstado(forProperty("trabajoEstado")) : null;
    }

}

