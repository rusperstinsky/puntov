package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QTerminal is a Querydsl query type for Terminal
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTerminal extends EntityPathBase<Terminal> {

    private static final long serialVersionUID = 50833021;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QTerminal terminal = new QTerminal("terminal");

    public final StringPath afiliacion = createString("afiliacion");

    public final QBancoDeposito bancoDeposito;

    public final StringPath descripcion = createString("descripcion");

    public final StringPath id = createString("id");

    public final NumberPath<Integer> idBancoDep = createNumber("idBancoDep", Integer.class);

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath numero = createString("numero");

    public final BooleanPath promocion = createBoolean("promocion");

    public final QSucursal sucursal;

    public QTerminal(String variable) {
        this(Terminal.class, forVariable(variable), INITS);
    }

    public QTerminal(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTerminal(PathMetadata<?> metadata, PathInits inits) {
        this(Terminal.class, metadata, inits);
    }

    public QTerminal(Class<? extends Terminal> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bancoDeposito = inits.isInitialized("bancoDeposito") ? new QBancoDeposito(forProperty("bancoDeposito")) : null;
        this.sucursal = inits.isInitialized("sucursal") ? new QSucursal(forProperty("sucursal"), inits.get("sucursal")) : null;
    }

}

