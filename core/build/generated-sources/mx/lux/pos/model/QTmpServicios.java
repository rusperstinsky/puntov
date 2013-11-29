package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QTmpServicios is a Querydsl query type for TmpServicios
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTmpServicios extends EntityPathBase<TmpServicios> {

    private static final long serialVersionUID = 1496571527;

    public static final QTmpServicios tmpServicios = new QTmpServicios("tmpServicios");

    public final StringPath cliente = createString("cliente");

    public final StringPath condicion = createString("condicion");

    public final StringPath dejo = createString("dejo");

    public final StringPath emp = createString("emp");

    public final DateTimePath<java.util.Date> fecha_prom = createDateTime("fecha_prom", java.util.Date.class);

    public final StringPath id_cliente = createString("id_cliente");

    public final StringPath id_factura = createString("id_factura");

    public final NumberPath<Integer> id_serv = createNumber("id_serv", Integer.class);

    public final StringPath instruccion = createString("instruccion");

    public final StringPath servicio = createString("servicio");

    public QTmpServicios(String variable) {
        super(TmpServicios.class, forVariable(variable));
    }

    public QTmpServicios(Path<? extends TmpServicios> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QTmpServicios(PathMetadata<?> metadata) {
        super(TmpServicios.class, metadata);
    }

}

