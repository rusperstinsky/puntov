package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QFacturasImpuestos is a Querydsl query type for FacturasImpuestos
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QFacturasImpuestos extends EntityPathBase<FacturasImpuestos> {

    private static final long serialVersionUID = -987104059;

    public static final QFacturasImpuestos facturasImpuestos = new QFacturasImpuestos("facturasImpuestos");

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final StringPath id_mod = createString("id_mod");

    public final StringPath idFactura = createString("idFactura");

    public final StringPath idImpuesto = createString("idImpuesto");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath rfc = createString("rfc");

    public QFacturasImpuestos(String variable) {
        super(FacturasImpuestos.class, forVariable(variable));
    }

    public QFacturasImpuestos(Path<? extends FacturasImpuestos> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QFacturasImpuestos(PathMetadata<?> metadata) {
        super(FacturasImpuestos.class, metadata);
    }

}

