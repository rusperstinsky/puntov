package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QJbNotas is a Querydsl query type for JbNotas
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QJbNotas extends EntityPathBase<JbNotas> {

    private static final long serialVersionUID = -818168884;

    public static final QJbNotas jbNotas = new QJbNotas("jbNotas");

    public final StringPath cliente = createString("cliente");

    public final StringPath condicion = createString("condicion");

    public final StringPath dejo = createString("dejo");

    public final StringPath emp = createString("emp");

    public final DateTimePath<java.util.Date> fecha_mod = createDateTime("fecha_mod", java.util.Date.class);

    public final DateTimePath<java.util.Date> fecha_orden = createDateTime("fecha_orden", java.util.Date.class);

    public final DateTimePath<java.util.Date> fecha_prom = createDateTime("fecha_prom", java.util.Date.class);

    public final StringPath id_cliente = createString("id_cliente");

    public final StringPath id_mod = createString("id_mod");

    public final NumberPath<Integer> id_nota = createNumber("id_nota", Integer.class);

    public final StringPath instruccion = createString("instruccion");

    public final StringPath servicio = createString("servicio");

    public final StringPath tipo_serv = createString("tipo_serv");

    public QJbNotas(String variable) {
        super(JbNotas.class, forVariable(variable));
    }

    public QJbNotas(Path<? extends JbNotas> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QJbNotas(PathMetadata<?> metadata) {
        super(JbNotas.class, metadata);
    }

}

