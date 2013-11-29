package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QTipoTransInv is a Querydsl query type for TipoTransInv
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTipoTransInv extends EntityPathBase<TipoTransInv> {

    private static final long serialVersionUID = 866171646;

    public static final QTipoTransInv tipoTransInv = new QTipoTransInv("tipoTransInv");

    public final StringPath descripcion = createString("descripcion");

    public final StringPath idTipoTrans = createString("idTipoTrans");

    public final StringPath tipoMov = createString("tipoMov");

    public final NumberPath<Integer> ultimoFolio = createNumber("ultimoFolio", Integer.class);

    public QTipoTransInv(String variable) {
        super(TipoTransInv.class, forVariable(variable));
    }

    public QTipoTransInv(Path<? extends TipoTransInv> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QTipoTransInv(PathMetadata<?> metadata) {
        super(TipoTransInv.class, metadata);
    }

}

