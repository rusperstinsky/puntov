package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QJb is a Querydsl query type for Jb
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QJb extends EntityPathBase<Jb> {

    private static final long serialVersionUID = 1410037241;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QJb jb = new QJb("jb");

    public final StringPath caja = createString("caja");

    public final StringPath cliente = createString("cliente");

    public final StringPath emp_atendio = createString("emp_atendio");

    public final StringPath estado = createString("estado");

    public final StringPath externo = createString("externo");

    public final DateTimePath<java.util.Date> fecha_mod = createDateTime("fecha_mod", java.util.Date.class);

    public final DateTimePath<java.util.Date> fecha_promesa = createDateTime("fecha_promesa", java.util.Date.class);

    public final DateTimePath<java.util.Date> fecha_venta = createDateTime("fecha_venta", java.util.Date.class);

    public final StringPath id_cliente = createString("id_cliente");

    public final StringPath id_grupo = createString("id_grupo");

    public final StringPath id_mod = createString("id_mod");

    public final StringPath id_viaje = createString("id_viaje");

    public final StringPath jb_tipo = createString("jb_tipo");

    public final StringPath material = createString("material");

    public final BooleanPath no_enviar = createBoolean("no_enviar");

    public final BooleanPath no_llamar = createBoolean("no_llamar");

    public final QNotaVenta notaVenta;

    public final NumberPath<Integer> num_llamada = createNumber("num_llamada", Integer.class);

    public final StringPath obs_ext = createString("obs_ext");

    public final StringPath ret_auto = createString("ret_auto");

    public final NumberPath<Integer> roto = createNumber("roto", Integer.class);

    public final StringPath rx = createString("rx");

    public final NumberPath<java.math.BigDecimal> saldo = createNumber("saldo", java.math.BigDecimal.class);

    public final StringPath surte = createString("surte");

    public final StringPath tipo_venta = createString("tipo_venta");

    public final DateTimePath<java.util.Date> volver_llamar = createDateTime("volver_llamar", java.util.Date.class);

    public QJb(String variable) {
        this(Jb.class, forVariable(variable), INITS);
    }

    public QJb(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QJb(PathMetadata<?> metadata, PathInits inits) {
        this(Jb.class, metadata, inits);
    }

    public QJb(Class<? extends Jb> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notaVenta = inits.isInitialized("notaVenta") ? new QNotaVenta(forProperty("notaVenta"), inits.get("notaVenta")) : null;
    }

}

