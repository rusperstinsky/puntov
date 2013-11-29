package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QClienteProceso is a Querydsl query type for ClienteProceso
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QClienteProceso extends EntityPathBase<ClienteProceso> {

    private static final long serialVersionUID = -129519022;

    public static final QClienteProceso clienteProceso = new QClienteProceso("clienteProceso");

    public final StringPath etapa = createString("etapa");

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final NumberPath<Integer> idCliente = createNumber("idCliente", Integer.class);

    public final StringPath idMod = createString("idMod");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public QClienteProceso(String variable) {
        super(ClienteProceso.class, forVariable(variable));
    }

    public QClienteProceso(Path<? extends ClienteProceso> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QClienteProceso(PathMetadata<?> metadata) {
        super(ClienteProceso.class, metadata);
    }

}

