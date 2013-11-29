package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QParametro is a Querydsl query type for Parametro
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QParametro extends EntityPathBase<Parametro> {

    private static final long serialVersionUID = 1494294872;

    public static final QParametro parametro = new QParametro("parametro");

    public final StringPath descripcion = createString("descripcion");

    public final StringPath id = createString("id");

    public final StringPath valor = createString("valor");

    public QParametro(String variable) {
        super(Parametro.class, forVariable(variable));
    }

    public QParametro(Path<? extends Parametro> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QParametro(PathMetadata<?> metadata) {
        super(Parametro.class, metadata);
    }

}

