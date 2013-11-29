package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QRetornoDet is a Querydsl query type for RetornoDet
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRetornoDet extends EntityPathBase<RetornoDet> {

    private static final long serialVersionUID = -1072342385;

    public static final QRetornoDet retornoDet = new QRetornoDet("retornoDet");

    public final NumberPath<Integer> cantidad = createNumber("cantidad", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> idTransaccion = createNumber("idTransaccion", Integer.class);

    public final NumberPath<java.math.BigDecimal> importe = createNumber("importe", java.math.BigDecimal.class);

    public final NumberPath<Integer> sku = createNumber("sku", Integer.class);

    public QRetornoDet(String variable) {
        super(RetornoDet.class, forVariable(variable));
    }

    public QRetornoDet(Path<? extends RetornoDet> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QRetornoDet(PathMetadata<?> metadata) {
        super(RetornoDet.class, metadata);
    }

}

