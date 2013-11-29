package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QOrdenPromDet is a Querydsl query type for OrdenPromDet
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QOrdenPromDet extends EntityPathBase<OrdenPromDet> {

    private static final long serialVersionUID = -1368555158;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QOrdenPromDet ordenPromDet = new QOrdenPromDet("ordenPromDet");

    public final NumberPath<java.math.BigDecimal> descuentoMonto = createNumber("descuentoMonto", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> descuentoPorcentaje = createNumber("descuentoPorcentaje", java.math.BigDecimal.class);

    public final QDetalleNotaVenta detalleNotaVenta;

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final NumberPath<Integer> idArticulo = createNumber("idArticulo", Integer.class);

    public final StringPath idFactura = createString("idFactura");

    public final NumberPath<Integer> idOrdenProm = createNumber("idOrdenProm", Integer.class);

    public final NumberPath<Integer> idOrdenPromDet = createNumber("idOrdenPromDet", Integer.class);

    public final NumberPath<Integer> idPromocion = createNumber("idPromocion", Integer.class);

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public QOrdenPromDet(String variable) {
        this(OrdenPromDet.class, forVariable(variable), INITS);
    }

    public QOrdenPromDet(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QOrdenPromDet(PathMetadata<?> metadata, PathInits inits) {
        this(OrdenPromDet.class, metadata, inits);
    }

    public QOrdenPromDet(Class<? extends OrdenPromDet> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.detalleNotaVenta = inits.isInitialized("detalleNotaVenta") ? new QDetalleNotaVenta(forProperty("detalleNotaVenta"), inits.get("detalleNotaVenta")) : null;
    }

}

