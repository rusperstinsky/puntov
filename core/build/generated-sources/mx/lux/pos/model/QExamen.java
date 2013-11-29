package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QExamen is a Querydsl query type for Examen
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QExamen extends EntityPathBase<Examen> {

    private static final long serialVersionUID = -841393847;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QExamen examen = new QExamen("examen");

    public final StringPath avSaOdLejosEx = createString("avSaOdLejosEx");

    public final StringPath avSaOiLejosEx = createString("avSaOiLejosEx");

    public final QCliente cliente;

    public final StringPath di_od = createString("di_od");

    public final StringPath diOi = createString("diOi");

    public final StringPath factura = createString("factura");

    public final DateTimePath<java.util.Date> fechaAlta = createDateTime("fechaAlta", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final DateTimePath<java.util.Date> horaAlta = createDateTime("horaAlta", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath id_mod = createString("id_mod");

    public final StringPath idAtendio = createString("idAtendio");

    public final NumberPath<Integer> idCliente = createNumber("idCliente", Integer.class);

    public final StringPath idExOri = createString("idExOri");

    public final NumberPath<Integer> idOftalmologo = createNumber("idOftalmologo", Integer.class);

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public final StringPath objDiEx = createString("objDiEx");

    public final StringPath objOdCilEx = createString("objOdCilEx");

    public final StringPath objOdEjeEx = createString("objOdEjeEx");

    public final StringPath objOdEsfEx = createString("objOdEsfEx");

    public final StringPath objOiCilEx = createString("objOiCilEx");

    public final StringPath objOiEjeEx = createString("objOiEjeEx");

    public final StringPath objOiEsfEx = createString("objOiEsfEx");

    public final StringPath observacionesEx = createString("observacionesEx");

    public final StringPath subOdAdcEx = createString("subOdAdcEx");

    public final StringPath subOdAdiEx = createString("subOdAdiEx");

    public final StringPath subOdAvEx = createString("subOdAvEx");

    public final StringPath subOdCilEx = createString("subOdCilEx");

    public final StringPath subOdEjeEx = createString("subOdEjeEx");

    public final StringPath subOdEsfEx = createString("subOdEsfEx");

    public final StringPath subOiAdcEx = createString("subOiAdcEx");

    public final StringPath subOiAdiEx = createString("subOiAdiEx");

    public final StringPath subOiAvEx = createString("subOiAvEx");

    public final StringPath subOiCilEx = createString("subOiCilEx");

    public final StringPath subOiEjeEx = createString("subOiEjeEx");

    public final StringPath subOiEsfEx = createString("subOiEsfEx");

    public final StringPath tipoCli = createString("tipoCli");

    public final StringPath tipoOft = createString("tipoOft");

    public final StringPath udf1 = createString("udf1");

    public final StringPath udf2 = createString("udf2");

    public final StringPath udf3 = createString("udf3");

    public QExamen(String variable) {
        this(Examen.class, forVariable(variable), INITS);
    }

    public QExamen(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QExamen(PathMetadata<?> metadata, PathInits inits) {
        this(Examen.class, metadata, inits);
    }

    public QExamen(Class<? extends Examen> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cliente = inits.isInitialized("cliente") ? new QCliente(forProperty("cliente"), inits.get("cliente")) : null;
    }

}

