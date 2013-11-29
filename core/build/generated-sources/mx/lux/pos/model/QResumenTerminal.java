package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QResumenTerminal is a Querydsl query type for ResumenTerminal
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QResumenTerminal extends EntityPathBase<ResumenTerminal> {

    private static final long serialVersionUID = 979917916;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QResumenTerminal resumenTerminal = new QResumenTerminal("resumenTerminal");

    public final DateTimePath<java.util.Date> fechaCierre = createDateTime("fechaCierre", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idTerminal = createString("idTerminal");

    public final QSucursal sucursal;

    public final QTerminal terminal;

    public final NumberPath<java.math.BigDecimal> total = createNumber("total", java.math.BigDecimal.class);

    public QResumenTerminal(String variable) {
        this(ResumenTerminal.class, forVariable(variable), INITS);
    }

    public QResumenTerminal(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QResumenTerminal(PathMetadata<?> metadata, PathInits inits) {
        this(ResumenTerminal.class, metadata, inits);
    }

    public QResumenTerminal(Class<? extends ResumenTerminal> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sucursal = inits.isInitialized("sucursal") ? new QSucursal(forProperty("sucursal"), inits.get("sucursal")) : null;
        this.terminal = inits.isInitialized("terminal") ? new QTerminal(forProperty("terminal"), inits.get("terminal")) : null;
    }

}

