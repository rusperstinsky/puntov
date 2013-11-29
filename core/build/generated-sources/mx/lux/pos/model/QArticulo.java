package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QArticulo is a Querydsl query type for Articulo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QArticulo extends EntityPathBase<Articulo> {

    private static final long serialVersionUID = 3213532;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QArticulo articulo1 = new QArticulo("articulo1");

    public final StringPath articulo = createString("articulo");

    public final NumberPath<Integer> cantExistencia = createNumber("cantExistencia", Integer.class);

    public final StringPath codigoColor = createString("codigoColor");

    public final StringPath descripcion = createString("descripcion");

    public final StringPath descripcionColor = createString("descripcionColor");

    public final QExistencia existencia;

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final QGenerico generico;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idCb = createString("idCb");

    public final StringPath idDisenoLente = createString("idDisenoLente");

    public final StringPath idGenerico = createString("idGenerico");

    public final StringPath idGenSubtipo = createString("idGenSubtipo");

    public final StringPath idGenTipo = createString("idGenTipo");

    public final StringPath idMod = createString("idMod");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public final StringPath marca = createString("marca");

    public final NumberPath<java.math.BigDecimal> precio = createNumber("precio", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> precioO = createNumber("precioO", java.math.BigDecimal.class);

    public final QPrecio precios;

    public final StringPath proveedor = createString("proveedor");

    public final StringPath sArticulo = createString("sArticulo");

    public final StringPath subtipo = createString("subtipo");

    public final StringPath tipo = createString("tipo");

    public QArticulo(String variable) {
        this(Articulo.class, forVariable(variable), INITS);
    }

    public QArticulo(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QArticulo(PathMetadata<?> metadata, PathInits inits) {
        this(Articulo.class, metadata, inits);
    }

    public QArticulo(Class<? extends Articulo> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.existencia = inits.isInitialized("existencia") ? new QExistencia(forProperty("existencia")) : null;
        this.generico = inits.isInitialized("generico") ? new QGenerico(forProperty("generico")) : null;
        this.precios = inits.isInitialized("precios") ? new QPrecio(forProperty("precios")) : null;
    }

}

