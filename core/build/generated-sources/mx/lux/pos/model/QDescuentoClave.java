package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QDescuentoClave is a Querydsl query type for DescuentoClave
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDescuentoClave extends EntityPathBase<DescuentoClave> {

    private static final long serialVersionUID = -2055515392;

    public static final QDescuentoClave descuentoClave = new QDescuentoClave("descuentoClave");

    public final StringPath clave_descuento = createString("clave_descuento");

    public final StringPath descripcion_descuento = createString("descripcion_descuento");

    public final NumberPath<Double> porcenaje_descuento = createNumber("porcenaje_descuento", Double.class);

    public final StringPath tipo = createString("tipo");

    public final BooleanPath vigente = createBoolean("vigente");

    public QDescuentoClave(String variable) {
        super(DescuentoClave.class, forVariable(variable));
    }

    public QDescuentoClave(Path<? extends DescuentoClave> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QDescuentoClave(PathMetadata<?> metadata) {
        super(DescuentoClave.class, metadata);
    }

}

