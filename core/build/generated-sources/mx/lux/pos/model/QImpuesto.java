package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QImpuesto is a Querydsl query type for Impuesto
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QImpuesto extends EntityPathBase<Impuesto> {

    private static final long serialVersionUID = 814866771;

    public static final QImpuesto impuesto = new QImpuesto("impuesto");

    public final NumberPath<java.math.BigDecimal> cantidad = createNumber("cantidad", java.math.BigDecimal.class);

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final StringPath id = createString("id");

    public final StringPath nombre = createString("nombre");

    public final NumberPath<Double> tasa = createNumber("tasa", Double.class);

    public final BooleanPath vigente = createBoolean("vigente");

    public QImpuesto(String variable) {
        super(Impuesto.class, forVariable(variable));
    }

    public QImpuesto(Path<? extends Impuesto> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QImpuesto(PathMetadata<?> metadata) {
        super(Impuesto.class, metadata);
    }

}

