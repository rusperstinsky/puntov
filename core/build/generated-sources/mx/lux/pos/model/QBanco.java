package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QBanco is a Querydsl query type for Banco
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBanco extends EntityPathBase<Banco> {

    private static final long serialVersionUID = 1631982682;

    public static final QBanco banco = new QBanco("banco");

    public final BooleanPath aceptaDevolucion = createBoolean("aceptaDevolucion");

    public final DateTimePath<java.util.Date> fechaModificacion = createDateTime("fechaModificacion", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idMod = createString("idMod");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public final BooleanPath ivaComision = createBoolean("ivaComision");

    public final StringPath nombre = createString("nombre");

    public final NumberPath<Integer> porComisionTc = createNumber("porComisionTc", Integer.class);

    public QBanco(String variable) {
        super(Banco.class, forVariable(variable));
    }

    public QBanco(Path<? extends Banco> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QBanco(PathMetadata<?> metadata) {
        super(Banco.class, metadata);
    }

}

