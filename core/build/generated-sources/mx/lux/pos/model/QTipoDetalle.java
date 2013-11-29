package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QTipoDetalle is a Querydsl query type for TipoDetalle
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTipoDetalle extends EntityPathBase<TipoDetalle> {

    private static final long serialVersionUID = 1544347650;

    public static final QTipoDetalle tipoDetalle = new QTipoDetalle("tipoDetalle");

    public final BooleanPath acc = createBoolean("acc");

    public final BooleanPath derecho = createBoolean("derecho");

    public final StringPath descripcion = createString("descripcion");

    public final DateTimePath<java.util.Date> fechaModificacion = createDateTime("fechaModificacion", java.util.Date.class);

    public final BooleanPath fte = createBoolean("fte");

    public final StringPath id = createString("id");

    public final StringPath idMod = createString("idMod");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final ComparablePath<Character> idSync = createComparable("idSync", Character.class);

    public final BooleanPath izquierdo = createBoolean("izquierdo");

    public final NumberPath<Integer> partes = createNumber("partes", Integer.class);

    public final NumberPath<Integer> porcentaje = createNumber("porcentaje", Integer.class);

    public QTipoDetalle(String variable) {
        super(TipoDetalle.class, forVariable(variable));
    }

    public QTipoDetalle(Path<? extends TipoDetalle> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QTipoDetalle(PathMetadata<?> metadata) {
        super(TipoDetalle.class, metadata);
    }

}

