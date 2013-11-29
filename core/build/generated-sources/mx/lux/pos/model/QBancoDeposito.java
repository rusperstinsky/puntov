package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QBancoDeposito is a Querydsl query type for BancoDeposito
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBancoDeposito extends EntityPathBase<BancoDeposito> {

    private static final long serialVersionUID = 1880570891;

    public static final QBancoDeposito bancoDeposito = new QBancoDeposito("bancoDeposito");

    public final StringPath cuenta = createString("cuenta");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath nombre = createString("nombre");

    public final StringPath tipo = createString("tipo");

    public QBancoDeposito(String variable) {
        super(BancoDeposito.class, forVariable(variable));
    }

    public QBancoDeposito(Path<? extends BancoDeposito> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QBancoDeposito(PathMetadata<?> metadata) {
        super(BancoDeposito.class, metadata);
    }

}

