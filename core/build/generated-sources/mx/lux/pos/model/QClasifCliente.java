package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QClasifCliente is a Querydsl query type for ClasifCliente
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QClasifCliente extends EntityPathBase<ClasifCliente> {

    private static final long serialVersionUID = -1396597055;

    public static final QClasifCliente clasifCliente = new QClasifCliente("clasifCliente");

    public final DateTimePath<java.util.Date> fechaModificacion = createDateTime("fechaModificacion", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> idClasifCliente = createNumber("idClasifCliente", Integer.class);

    public final NumberPath<Integer> idCliente = createNumber("idCliente", Integer.class);

    public final StringPath idFactura = createString("idFactura");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public QClasifCliente(String variable) {
        super(ClasifCliente.class, forVariable(variable));
    }

    public QClasifCliente(Path<? extends ClasifCliente> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QClasifCliente(PathMetadata<?> metadata) {
        super(ClasifCliente.class, metadata);
    }

}

