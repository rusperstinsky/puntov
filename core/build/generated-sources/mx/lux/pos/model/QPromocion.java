package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPromocion is a Querydsl query type for Promocion
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPromocion extends EntityPathBase<Promocion> {

    private static final long serialVersionUID = -1259884941;

    public static final QPromocion promocion = new QPromocion("promocion");

    public final BooleanPath aplicaAuto = createBoolean("aplicaAuto");

    public final BooleanPath aplicaConvenio = createBoolean("aplicaConvenio");

    public final StringPath articulo = createString("articulo");

    public final StringPath articuloC = createString("articuloC");

    public final StringPath articuloProm = createString("articuloProm");

    public final StringPath descripcion = createString("descripcion");

    public final NumberPath<java.math.BigDecimal> descuento = createNumber("descuento", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> descuentoC = createNumber("descuentoC", java.math.BigDecimal.class);

    public final StringPath idGenerico = createString("idGenerico");

    public final StringPath idGenericoC = createString("idGenericoC");

    public final StringPath idGrupoTienda = createString("idGrupoTienda");

    public final NumberPath<Integer> idPromocion = createNumber("idPromocion", Integer.class);

    public final StringPath marca = createString("marca");

    public final StringPath marcaC = createString("marcaC");

    public final BooleanPath obligatoria = createBoolean("obligatoria");

    public final NumberPath<java.math.BigDecimal> precioDescontado = createNumber("precioDescontado", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> precioDescontadoC = createNumber("precioDescontadoC", java.math.BigDecimal.class);

    public final BooleanPath precioOferta = createBoolean("precioOferta");

    public final NumberPath<Integer> prioridad = createNumber("prioridad", Integer.class);

    public final StringPath subtipo = createString("subtipo");

    public final StringPath subtipoC = createString("subtipoC");

    public final StringPath tipo = createString("tipo");

    public final StringPath tipoC = createString("tipoC");

    public final StringPath tipoPrecio = createString("tipoPrecio");

    public final StringPath tipoPrecioC = createString("tipoPrecioC");

    public final StringPath tipoPromocion = createString("tipoPromocion");

    public final DateTimePath<java.util.Date> vigenciaFin = createDateTime("vigenciaFin", java.util.Date.class);

    public final DateTimePath<java.util.Date> vigenciaIni = createDateTime("vigenciaIni", java.util.Date.class);

    public QPromocion(String variable) {
        super(Promocion.class, forVariable(variable));
    }

    public QPromocion(Path<? extends Promocion> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPromocion(PathMetadata<?> metadata) {
        super(Promocion.class, metadata);
    }

}

