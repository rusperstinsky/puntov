package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QTitulo is a Querydsl query type for Titulo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTitulo extends EntityPathBase<Titulo> {

    private static final long serialVersionUID = -425235462;

    public static final QTitulo titulo1 = new QTitulo("titulo1");

    public final DateTimePath<java.util.Date> fechaModificado = createDateTime("fechaModificado", java.util.Date.class);

    public final StringPath idModificado = createString("idModificado");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public final NumberPath<Integer> orden = createNumber("orden", Integer.class);

    public final ComparablePath<Character> sexoTitulo = createComparable("sexoTitulo", Character.class);

    public final StringPath titulo = createString("titulo");

    public QTitulo(String variable) {
        super(Titulo.class, forVariable(variable));
    }

    public QTitulo(Path<? extends Titulo> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QTitulo(PathMetadata<?> metadata) {
        super(Titulo.class, metadata);
    }

}

