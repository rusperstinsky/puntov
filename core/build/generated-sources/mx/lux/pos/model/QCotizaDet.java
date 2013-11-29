package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QCotizaDet is a Querydsl query type for CotizaDet
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCotizaDet extends EntityPathBase<CotizaDet> {

    private static final long serialVersionUID = -133179318;

    public static final QCotizaDet cotizaDet = new QCotizaDet("cotizaDet");

    public final StringPath articulo = createString("articulo");

    public final NumberPath<Integer> cantidad = createNumber("cantidad", Integer.class);

    public final StringPath color = createString("color");

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final NumberPath<Integer> idCotiza = createNumber("idCotiza", Integer.class);

    public final NumberPath<Integer> idCotizaDet = createNumber("idCotizaDet", Integer.class);

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final NumberPath<Integer> sku = createNumber("sku", Integer.class);

    public final StringPath udf1 = createString("udf1");

    public QCotizaDet(String variable) {
        super(CotizaDet.class, forVariable(variable));
    }

    public QCotizaDet(Path<? extends CotizaDet> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QCotizaDet(PathMetadata<?> metadata) {
        super(CotizaDet.class, metadata);
    }

}

