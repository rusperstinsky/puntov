package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QEntregadoExterno is a Querydsl query type for EntregadoExterno
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEntregadoExterno extends EntityPathBase<EntregadoExterno> {

    private static final long serialVersionUID = 2076088845;

    public static final QEntregadoExterno entregadoExterno = new QEntregadoExterno("entregadoExterno");

    public final StringPath facturaTxt = createString("facturaTxt");

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaPago = createDateTime("fechaPago", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idFactura = createString("idFactura");

    public final StringPath idSucursal = createString("idSucursal");

    public final NumberPath<java.math.BigDecimal> pago = createNumber("pago", java.math.BigDecimal.class);

    public QEntregadoExterno(String variable) {
        super(EntregadoExterno.class, forVariable(variable));
    }

    public QEntregadoExterno(Path<? extends EntregadoExterno> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QEntregadoExterno(PathMetadata<?> metadata) {
        super(EntregadoExterno.class, metadata);
    }

}

