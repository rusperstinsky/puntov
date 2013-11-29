package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMonedaDetalle is a Querydsl query type for MonedaDetalle
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMonedaDetalle extends EntityPathBase<MonedaDetalle> {

    private static final long serialVersionUID = -2057363424;

    public static final QMonedaDetalle monedaDetalle = new QMonedaDetalle("monedaDetalle");

    public final DateTimePath<java.util.Date> fechaActiva = createDateTime("fechaActiva", java.util.Date.class);

    public final StringPath idMoneda = createString("idMoneda");

    public final NumberPath<Integer> numSerial = createNumber("numSerial", Integer.class);

    public final NumberPath<java.math.BigDecimal> tipoCambio = createNumber("tipoCambio", java.math.BigDecimal.class);

    public QMonedaDetalle(String variable) {
        super(MonedaDetalle.class, forVariable(variable));
    }

    public QMonedaDetalle(Path<? extends MonedaDetalle> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QMonedaDetalle(PathMetadata<?> metadata) {
        super(MonedaDetalle.class, metadata);
    }

}

