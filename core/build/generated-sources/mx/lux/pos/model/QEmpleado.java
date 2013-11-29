package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QEmpleado is a Querydsl query type for Empleado
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEmpleado extends EntityPathBase<Empleado> {

    private static final long serialVersionUID = -1869736756;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QEmpleado empleado = new QEmpleado("empleado");

    public final StringPath apellidoMaterno = createString("apellidoMaterno");

    public final StringPath apellidoPaterno = createString("apellidoPaterno");

    public final DateTimePath<java.util.Date> fechaModificado = createDateTime("fechaModificado", java.util.Date.class);

    public final StringPath id = createString("id");

    public final NumberPath<Integer> idEmpresa = createNumber("idEmpresa", Integer.class);

    public final StringPath idModificado = createString("idModificado");

    public final NumberPath<Integer> idPuesto = createNumber("idPuesto", Integer.class);

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public final StringPath nombre = createString("nombre");

    public final StringPath passwd = createString("passwd");

    public final QSucursal sucursal;

    public QEmpleado(String variable) {
        this(Empleado.class, forVariable(variable), INITS);
    }

    public QEmpleado(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmpleado(PathMetadata<?> metadata, PathInits inits) {
        this(Empleado.class, metadata, inits);
    }

    public QEmpleado(Class<? extends Empleado> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sucursal = inits.isInitialized("sucursal") ? new QSucursal(forProperty("sucursal"), inits.get("sucursal")) : null;
    }

}

