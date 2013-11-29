package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QDetalleNotaVenta is a Querydsl query type for DetalleNotaVenta
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDetalleNotaVenta extends EntityPathBase<DetalleNotaVenta> {

    private static final long serialVersionUID = 1505258056;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QDetalleNotaVenta detalleNotaVenta = new QDetalleNotaVenta("detalleNotaVenta");

    public final QArticulo articulo;

    public final NumberPath<Double> cantidadFac = createNumber("cantidadFac", Double.class);

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> idArticulo = createNumber("idArticulo", Integer.class);

    public final StringPath idFactura = createString("idFactura");

    public final StringPath idMod = createString("idMod");

    public final StringPath idRepVenta = createString("idRepVenta");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public final StringPath idTipoDetalle = createString("idTipoDetalle");

    public final QNotaVenta notaVenta;

    public final NumberPath<java.math.BigDecimal> precioCalcLista = createNumber("precioCalcLista", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> precioCalcOferta = createNumber("precioCalcOferta", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> precioConv = createNumber("precioConv", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> precioFactura = createNumber("precioFactura", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> precioUnitFinal = createNumber("precioUnitFinal", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> precioUnitLista = createNumber("precioUnitLista", java.math.BigDecimal.class);

    public final QSucursal sucursal;

    public final StringPath surte = createString("surte");

    public final QTipoDetalle tipoDetalle;

    public QDetalleNotaVenta(String variable) {
        this(DetalleNotaVenta.class, forVariable(variable), INITS);
    }

    public QDetalleNotaVenta(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDetalleNotaVenta(PathMetadata<?> metadata, PathInits inits) {
        this(DetalleNotaVenta.class, metadata, inits);
    }

    public QDetalleNotaVenta(Class<? extends DetalleNotaVenta> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.articulo = inits.isInitialized("articulo") ? new QArticulo(forProperty("articulo"), inits.get("articulo")) : null;
        this.notaVenta = inits.isInitialized("notaVenta") ? new QNotaVenta(forProperty("notaVenta"), inits.get("notaVenta")) : null;
        this.sucursal = inits.isInitialized("sucursal") ? new QSucursal(forProperty("sucursal"), inits.get("sucursal")) : null;
        this.tipoDetalle = inits.isInitialized("tipoDetalle") ? new QTipoDetalle(forProperty("tipoDetalle")) : null;
    }

}

