package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSucursal is a Querydsl query type for Sucursal
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSucursal extends EntityPathBase<Sucursal> {

    private static final long serialVersionUID = -798596479;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QSucursal sucursal = new QSucursal("sucursal");

    public final StringPath centroCostos = createString("centroCostos");

    public final StringPath ciudad = createString("ciudad");

    public final StringPath colonia = createString("colonia");

    public final StringPath cp = createString("cp");

    public final StringPath direccion = createString("direccion");

    public final BooleanPath domingo = createBoolean("domingo");

    public final QEstado estado;

    public final DateTimePath<java.util.Date> fechaModificado = createDateTime("fechaModificado", java.util.Date.class);

    public final QEmpleado gerente;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idEstado = createString("idEstado");

    public final StringPath idGerente = createString("idGerente");

    public final StringPath idLocalidad = createString("idLocalidad");

    public final StringPath idModificado = createString("idModificado");

    public final StringPath idSync = createString("idSync");

    public final BooleanPath impresionFact = createBoolean("impresionFact");

    public final NumberPath<Integer> letraAscii = createNumber("letraAscii", Integer.class);

    public final StringPath marca = createString("marca");

    public final QMunicipio municipio;

    public final StringPath nombre = createString("nombre");

    public final NumberPath<Integer> numFactura = createNumber("numFactura", Integer.class);

    public final NumberPath<Integer> numRepVentas = createNumber("numRepVentas", Integer.class);

    public final NumberPath<Integer> por100Anticipo = createNumber("por100Anticipo", Integer.class);

    public final BooleanPath sears = createBoolean("sears");

    public final BooleanPath serieNumOrden = createBoolean("serieNumOrden");

    public final NumberPath<Integer> serieRepVentas = createNumber("serieRepVentas", Integer.class);

    public final StringPath telefonos = createString("telefonos");

    public QSucursal(String variable) {
        this(Sucursal.class, forVariable(variable), INITS);
    }

    public QSucursal(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSucursal(PathMetadata<?> metadata, PathInits inits) {
        this(Sucursal.class, metadata, inits);
    }

    public QSucursal(Class<? extends Sucursal> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.estado = inits.isInitialized("estado") ? new QEstado(forProperty("estado")) : null;
        this.gerente = inits.isInitialized("gerente") ? new QEmpleado(forProperty("gerente"), inits.get("gerente")) : null;
        this.municipio = inits.isInitialized("municipio") ? new QMunicipio(forProperty("municipio"), inits.get("municipio")) : null;
    }

}

