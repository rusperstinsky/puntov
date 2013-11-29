package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPagoExterno is a Querydsl query type for PagoExterno
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPagoExterno extends EntityPathBase<PagoExterno> {

    private static final long serialVersionUID = -2061837227;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QPagoExterno pagoExterno = new QPagoExterno("pagoExterno");

    public final StringPath claveP = createString("claveP");

    public final BooleanPath confirm = createBoolean("confirm");

    public final QEmpleado empleado;

    public final QExterno externo;

    public final StringPath factura = createString("factura");

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final QFormaPago formaPago;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idBanco = createString("idBanco");

    public final StringPath idBancoEmi = createString("idBancoEmi");

    public final StringPath idEmpleado = createString("idEmpleado");

    public final StringPath idFormaPago = createString("idFormaPago");

    public final StringPath idFPago = createString("idFPago");

    public final StringPath idMod = createString("idMod");

    public final StringPath idPlan = createString("idPlan");

    public final StringPath idTerminal = createString("idTerminal");

    public final NumberPath<java.math.BigDecimal> monto = createNumber("monto", java.math.BigDecimal.class);

    public final StringPath refClave = createString("refClave");

    public final StringPath referencia = createString("referencia");

    public final QTerminal terminal;

    public QPagoExterno(String variable) {
        this(PagoExterno.class, forVariable(variable), INITS);
    }

    public QPagoExterno(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPagoExterno(PathMetadata<?> metadata, PathInits inits) {
        this(PagoExterno.class, metadata, inits);
    }

    public QPagoExterno(Class<? extends PagoExterno> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.empleado = inits.isInitialized("empleado") ? new QEmpleado(forProperty("empleado"), inits.get("empleado")) : null;
        this.externo = inits.isInitialized("externo") ? new QExterno(forProperty("externo"), inits.get("externo")) : null;
        this.formaPago = inits.isInitialized("formaPago") ? new QFormaPago(forProperty("formaPago")) : null;
        this.terminal = inits.isInitialized("terminal") ? new QTerminal(forProperty("terminal"), inits.get("terminal")) : null;
    }

}

