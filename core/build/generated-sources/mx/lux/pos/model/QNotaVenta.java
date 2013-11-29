package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QNotaVenta is a Querydsl query type for NotaVenta
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QNotaVenta extends EntityPathBase<NotaVenta> {

    private static final long serialVersionUID = 1197538077;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QNotaVenta notaVenta = new QNotaVenta("notaVenta");

    public final StringPath cantLente = createString("cantLente");

    public final QCliente cliente;

    public final BooleanPath descuento = createBoolean("descuento");

    public final SetPath<DetalleNotaVenta, QDetalleNotaVenta> detalles = this.<DetalleNotaVenta, QDetalleNotaVenta>createSet("detalles", DetalleNotaVenta.class, QDetalleNotaVenta.class);

    public final StringPath empEntrego = createString("empEntrego");

    public final QEmpleado empleado;

    public final QExamen examen;

    public final StringPath factura = createString("factura");

    public final BooleanPath fArmazonCli = createBoolean("fArmazonCli");

    public final DateTimePath<java.util.Date> fechaEntrega = createDateTime("fechaEntrega", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaHoraFactura = createDateTime("fechaHoraFactura", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaPrometida = createDateTime("fechaPrometida", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaRecOrd = createDateTime("fechaRecOrd", java.util.Date.class);

    public final BooleanPath fExpideFactura = createBoolean("fExpideFactura");

    public final BooleanPath fResumenNotasMo = createBoolean("fResumenNotasMo");

    public final DateTimePath<java.util.Date> horaEntrega = createDateTime("horaEntrega", java.util.Date.class);

    public final StringPath id = createString("id");

    public final NumberPath<Integer> idCliente = createNumber("idCliente", Integer.class);

    public final StringPath idConvenio = createString("idConvenio");

    public final StringPath idEmpleado = createString("idEmpleado");

    public final StringPath idEmpleadoDescto = createString("idEmpleadoDescto");

    public final StringPath idMod = createString("idMod");

    public final NumberPath<Integer> idRepVenta = createNumber("idRepVenta", Integer.class);

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public final StringPath lc = createString("lc");

    public final NumberPath<java.math.BigDecimal> montoDescuento = createNumber("montoDescuento", java.math.BigDecimal.class);

    public final NumberPath<Integer> numeroOrden = createNumber("numeroOrden", Integer.class);

    public final StringPath observacionesNv = createString("observacionesNv");

    public final ListPath<OrdenPromDet, QOrdenPromDet> OrdenPromDet = this.<OrdenPromDet, QOrdenPromDet>createList("OrdenPromDet", OrdenPromDet.class, QOrdenPromDet.class);

    public final SetPath<Pago, QPago> pagos = this.<Pago, QPago>createSet("pagos", Pago.class, QPago.class);

    public final BooleanPath polEnt = createBoolean("polEnt");

    public final NumberPath<java.math.BigDecimal> poliza = createNumber("poliza", java.math.BigDecimal.class);

    public final NumberPath<Integer> por100Descuento = createNumber("por100Descuento", Integer.class);

    public final NumberPath<Integer> receta = createNumber("receta", Integer.class);

    public final StringPath sFactura = createString("sFactura");

    public final StringPath sucDest = createString("sucDest");

    public final QSucursal sucursal;

    public final NumberPath<java.math.BigDecimal> sumaPagos = createNumber("sumaPagos", java.math.BigDecimal.class);

    public final StringPath tDeduc = createString("tDeduc");

    public final StringPath tipoCli = createString("tipoCli");

    public final StringPath tipoDescuento = createString("tipoDescuento");

    public final StringPath tipoEntrega = createString("tipoEntrega");

    public final StringPath tipoNotaVenta = createString("tipoNotaVenta");

    public final StringPath tipoVenta = createString("tipoVenta");

    public final StringPath udf2 = createString("udf2");

    public final StringPath udf3 = createString("udf3");

    public final StringPath udf4 = createString("udf4");

    public final StringPath udf5 = createString("udf5");

    public final NumberPath<java.math.BigDecimal> ventaNeta = createNumber("ventaNeta", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> ventaTotal = createNumber("ventaTotal", java.math.BigDecimal.class);

    public QNotaVenta(String variable) {
        this(NotaVenta.class, forVariable(variable), INITS);
    }

    public QNotaVenta(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QNotaVenta(PathMetadata<?> metadata, PathInits inits) {
        this(NotaVenta.class, metadata, inits);
    }

    public QNotaVenta(Class<? extends NotaVenta> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cliente = inits.isInitialized("cliente") ? new QCliente(forProperty("cliente"), inits.get("cliente")) : null;
        this.empleado = inits.isInitialized("empleado") ? new QEmpleado(forProperty("empleado"), inits.get("empleado")) : null;
        this.examen = inits.isInitialized("examen") ? new QExamen(forProperty("examen"), inits.get("examen")) : null;
        this.sucursal = inits.isInitialized("sucursal") ? new QSucursal(forProperty("sucursal"), inits.get("sucursal")) : null;
    }

}

