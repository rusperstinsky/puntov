package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QRelacionPlan is a Querydsl query type for RelacionPlan
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRelacionPlan extends EntityPathBase<RelacionPlan> {

    private static final long serialVersionUID = 901041143;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QRelacionPlan relacionPlan = new QRelacionPlan("relacionPlan");

    public final StringPath activo = createString("activo");

    public final QBancoDeposito bancoDeposito;

    public final StringPath idBanco = createString("idBanco");

    public final StringPath idPlan = createString("idPlan");

    public final QPlan plan;

    public QRelacionPlan(String variable) {
        this(RelacionPlan.class, forVariable(variable), INITS);
    }

    public QRelacionPlan(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QRelacionPlan(PathMetadata<?> metadata, PathInits inits) {
        this(RelacionPlan.class, metadata, inits);
    }

    public QRelacionPlan(Class<? extends RelacionPlan> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bancoDeposito = inits.isInitialized("bancoDeposito") ? new QBancoDeposito(forProperty("bancoDeposito")) : null;
        this.plan = inits.isInitialized("plan") ? new QPlan(forProperty("plan")) : null;
    }

}

