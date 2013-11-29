package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QFormaPago is a Querydsl query type for FormaPago
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QFormaPago extends EntityPathBase<FormaPago> {

    private static final long serialVersionUID = 1350334581;

    public static final QFormaPago formaPago = new QFormaPago("formaPago");

    public final BooleanPath aceptaDevoluciones = createBoolean("aceptaDevoluciones");

    public final BooleanPath aceptaEnPagos = createBoolean("aceptaEnPagos");

    public final BooleanPath aplicaNv = createBoolean("aplicaNv");

    public final NumberPath<Integer> columnaReporteCi = createNumber("columnaReporteCi", Integer.class);

    public final StringPath defaultFormaDevolucion = createString("defaultFormaDevolucion");

    public final StringPath descripcion = createString("descripcion");

    public final DateTimePath<java.util.Date> fechaModificacion = createDateTime("fechaModificacion", java.util.Date.class);

    public final StringPath id = createString("id");

    public final StringPath idMod = createString("idMod");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final ComparablePath<Character> idSync = createComparable("idSync", Character.class);

    public final BooleanPath modificable = createBoolean("modificable");

    public final BooleanPath pedirBanco = createBoolean("pedirBanco");

    public final BooleanPath pedirReferencia = createBoolean("pedirReferencia");

    public QFormaPago(String variable) {
        super(FormaPago.class, forVariable(variable));
    }

    public QFormaPago(Path<? extends FormaPago> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QFormaPago(PathMetadata<?> metadata) {
        super(FormaPago.class, metadata);
    }

}

