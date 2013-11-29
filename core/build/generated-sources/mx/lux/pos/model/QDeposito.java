package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QDeposito is a Querydsl query type for Deposito
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDeposito extends EntityPathBase<Deposito> {

    private static final long serialVersionUID = -2119446126;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QDeposito deposito = new QDeposito("deposito");

    public final QEmpleado empleado;

    public final DateTimePath<java.util.Date> fechaCierre = createDateTime("fechaCierre", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaDeposito = createDateTime("fechaDeposito", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaIngreso = createDateTime("fechaIngreso", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaModificacion = createDateTime("fechaModificacion", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idBanco = createString("idBanco");

    public final StringPath idEmpleado = createString("idEmpleado");

    public final NumberPath<java.math.BigDecimal> monto = createNumber("monto", java.math.BigDecimal.class);

    public final NumberPath<Integer> numeroDeposito = createNumber("numeroDeposito", Integer.class);

    public final StringPath referencia = createString("referencia");

    public final StringPath tipoDeposito = createString("tipoDeposito");

    public QDeposito(String variable) {
        this(Deposito.class, forVariable(variable), INITS);
    }

    public QDeposito(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDeposito(PathMetadata<?> metadata, PathInits inits) {
        this(Deposito.class, metadata, inits);
    }

    public QDeposito(Class<? extends Deposito> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.empleado = inits.isInitialized("empleado") ? new QEmpleado(forProperty("empleado"), inits.get("empleado")) : null;
    }

}

