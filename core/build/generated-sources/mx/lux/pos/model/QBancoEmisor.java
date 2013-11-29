package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QBancoEmisor is a Querydsl query type for BancoEmisor
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBancoEmisor extends EntityPathBase<BancoEmisor> {

    private static final long serialVersionUID = -431503825;

    public static final QBancoEmisor bancoEmisor = new QBancoEmisor("bancoEmisor");

    public final StringPath descripcion = createString("descripcion");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath tipo = createString("tipo");

    public QBancoEmisor(String variable) {
        super(BancoEmisor.class, forVariable(variable));
    }

    public QBancoEmisor(Path<? extends BancoEmisor> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QBancoEmisor(PathMetadata<?> metadata) {
        super(BancoEmisor.class, metadata);
    }

}

