package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMoneda is a Querydsl query type for Moneda
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMoneda extends EntityPathBase<Moneda> {

    private static final long serialVersionUID = -620292777;

    public static final QMoneda moneda = new QMoneda("moneda");

    public final StringPath descripcion = createString("descripcion");

    public final StringPath idMoneda = createString("idMoneda");

    public QMoneda(String variable) {
        super(Moneda.class, forVariable(variable));
    }

    public QMoneda(Path<? extends Moneda> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QMoneda(PathMetadata<?> metadata) {
        super(Moneda.class, metadata);
    }

}

