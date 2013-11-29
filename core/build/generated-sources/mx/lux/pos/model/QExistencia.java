package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QExistencia is a Querydsl query type for Existencia
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QExistencia extends EntityPathBase<Existencia> {

    private static final long serialVersionUID = -1186613604;

    public static final QExistencia existencia = new QExistencia("existencia");

    public final NumberPath<Integer> cantidad = createNumber("cantidad", Integer.class);

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final NumberPath<Integer> idArticulo = createNumber("idArticulo", Integer.class);

    public final StringPath idMod = createString("idMod");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public QExistencia(String variable) {
        super(Existencia.class, forVariable(variable));
    }

    public QExistencia(Path<? extends Existencia> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QExistencia(PathMetadata<?> metadata) {
        super(Existencia.class, metadata);
    }

}

