package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVoucherTmp is a Querydsl query type for VoucherTmp
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QVoucherTmp extends EntityPathBase<VoucherTmp> {

    private static final long serialVersionUID = 930097994;

    public static final QVoucherTmp voucherTmp = new QVoucherTmp("voucherTmp");

    public final StringPath autorizacion = createString("autorizacion");

    public final NumberPath<Integer> cantidad = createNumber("cantidad", Integer.class);

    public final DateTimePath<java.util.Date> fechaCierre = createDateTime("fechaCierre", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaModificacion = createDateTime("fechaModificacion", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idFPago = createString("idFPago");

    public final StringPath idTerminal = createString("idTerminal");

    public final StringPath numeroTarjeta = createString("numeroTarjeta");

    public final StringPath plan = createString("plan");

    public QVoucherTmp(String variable) {
        super(VoucherTmp.class, forVariable(variable));
    }

    public QVoucherTmp(Path<? extends VoucherTmp> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QVoucherTmp(PathMetadata<?> metadata) {
        super(VoucherTmp.class, metadata);
    }

}

