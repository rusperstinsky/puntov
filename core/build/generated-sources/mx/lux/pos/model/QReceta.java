package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QReceta is a Querydsl query type for Receta
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QReceta extends EntityPathBase<Receta> {

    private static final long serialVersionUID = -486709437;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QReceta receta = new QReceta("receta");

    public final StringPath altOblR = createString("altOblR");

    public final QCliente cliente;

    public final StringPath diCercaR = createString("diCercaR");

    public final StringPath diLejosR = createString("diLejosR");

    public final StringPath diOd = createString("diOd");

    public final StringPath diOi = createString("diOi");

    public final QEmpleado empleado;

    public final NumberPath<Integer> examen = createNumber("examen", Integer.class);

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaReceta = createDateTime("fechaReceta", java.util.Date.class);

    public final BooleanPath fImpresa = createBoolean("fImpresa");

    public final StringPath folio = createString("folio");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> idCliente = createNumber("idCliente", Integer.class);

    public final StringPath idMod = createString("idMod");

    public final StringPath idOptometrista = createString("idOptometrista");

    public final StringPath idRxOri = createString("idRxOri");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public final StringPath material_arm = createString("material_arm");

    public final QNotaVenta notaVenta;

    public final StringPath observacionesR = createString("observacionesR");

    public final StringPath odAdcR = createString("odAdcR");

    public final StringPath odAdiR = createString("odAdiR");

    public final StringPath odAvR = createString("odAvR");

    public final StringPath odCilR = createString("odCilR");

    public final StringPath odEjeR = createString("odEjeR");

    public final StringPath odEsfR = createString("odEsfR");

    public final StringPath odPrismaH = createString("odPrismaH");

    public final StringPath odPrismaV = createString("odPrismaV");

    public final StringPath oiAdcR = createString("oiAdcR");

    public final StringPath oiAdiR = createString("oiAdiR");

    public final StringPath oiAvR = createString("oiAvR");

    public final StringPath oiCilR = createString("oiCilR");

    public final StringPath oiEjeR = createString("oiEjeR");

    public final StringPath oiEsfR = createString("oiEsfR");

    public final StringPath oiPrismaH = createString("oiPrismaH");

    public final StringPath oiPrismaV = createString("oiPrismaV");

    public final StringPath sUsoAnteojos = createString("sUsoAnteojos");

    public final StringPath tipoOpt = createString("tipoOpt");

    public final StringPath tratamientos = createString("tratamientos");

    public final StringPath udf5 = createString("udf5");

    public final StringPath udf6 = createString("udf6");

    public QReceta(String variable) {
        this(Receta.class, forVariable(variable), INITS);
    }

    public QReceta(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QReceta(PathMetadata<?> metadata, PathInits inits) {
        this(Receta.class, metadata, inits);
    }

    public QReceta(Class<? extends Receta> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cliente = inits.isInitialized("cliente") ? new QCliente(forProperty("cliente"), inits.get("cliente")) : null;
        this.empleado = inits.isInitialized("empleado") ? new QEmpleado(forProperty("empleado"), inits.get("empleado")) : null;
        this.notaVenta = inits.isInitialized("notaVenta") ? new QNotaVenta(forProperty("notaVenta"), inits.get("notaVenta")) : null;
    }

}

