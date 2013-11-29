package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QJbLlamada is a Querydsl query type for JbLlamada
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QJbLlamada extends EntityPathBase<JbLlamada> {

    private static final long serialVersionUID = 2135586617;

    public static final QJbLlamada jbLlamada = new QJbLlamada("jbLlamada");

    public final StringPath contesto = createString("contesto");

    public final StringPath emp_atendio = createString("emp_atendio");

    public final StringPath emp_llamo = createString("emp_llamo");

    public final StringPath estado = createString("estado");

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final BooleanPath grupo = createBoolean("grupo");

    public final StringPath id_mod = createString("id_mod");

    public final NumberPath<Integer> num_llamada = createNumber("num_llamada", Integer.class);

    public final StringPath obs = createString("obs");

    public final StringPath rx = createString("rx");

    public final StringPath tipo = createString("tipo");

    public QJbLlamada(String variable) {
        super(JbLlamada.class, forVariable(variable));
    }

    public QJbLlamada(Path<? extends JbLlamada> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QJbLlamada(PathMetadata<?> metadata) {
        super(JbLlamada.class, metadata);
    }

}

