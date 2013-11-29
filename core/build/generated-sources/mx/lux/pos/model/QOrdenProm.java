package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QOrdenProm is a Querydsl query type for OrdenProm
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QOrdenProm extends EntityPathBase<OrdenProm> {

    private static final long serialVersionUID = -855550487;

    public static final QOrdenProm ordenProm = new QOrdenProm("ordenProm");

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idFactura = createString("idFactura");

    public final NumberPath<Integer> idPromocion = createNumber("idPromocion", Integer.class);

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final NumberPath<java.math.BigDecimal> totalDescMonto = createNumber("totalDescMonto", java.math.BigDecimal.class);

    public QOrdenProm(String variable) {
        super(OrdenProm.class, forVariable(variable));
    }

    public QOrdenProm(Path<? extends OrdenProm> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QOrdenProm(PathMetadata<?> metadata) {
        super(OrdenProm.class, metadata);
    }

}

