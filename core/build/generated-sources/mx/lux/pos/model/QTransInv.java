package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QTransInv is a Querydsl query type for TransInv
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTransInv extends EntityPathBase<TransInv> {

    private static final long serialVersionUID = -1782030294;

    public static final QTransInv transInv = new QTransInv("transInv");

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final NumberPath<Integer> folio = createNumber("folio", Integer.class);

    public final StringPath idEmpleado = createString("idEmpleado");

    public final StringPath idTipoTrans = createString("idTipoTrans");

    public final NumberPath<Integer> numReg = createNumber("numReg", Integer.class);

    public final StringPath observaciones = createString("observaciones");

    public final StringPath referencia = createString("referencia");

    public final NumberPath<Integer> sucursal = createNumber("sucursal", Integer.class);

    public final NumberPath<Integer> sucursalDestino = createNumber("sucursalDestino", Integer.class);

    public QTransInv(String variable) {
        super(TransInv.class, forVariable(variable));
    }

    public QTransInv(Path<? extends TransInv> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QTransInv(PathMetadata<?> metadata) {
        super(TransInv.class, metadata);
    }

}

