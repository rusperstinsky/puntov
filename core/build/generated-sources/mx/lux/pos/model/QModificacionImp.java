package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QModificacionImp is a Querydsl query type for ModificacionImp
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QModificacionImp extends EntityPathBase<ModificacionImp> {

    private static final long serialVersionUID = 589382174;

    public static final QModificacionImp modificacionImp = new QModificacionImp("modificacionImp");

    public final StringPath convenioAnterior = createString("convenioAnterior");

    public final StringPath convenioNuevo = createString("convenioNuevo");

    public final StringPath estadoAnterior = createString("estadoAnterior");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idEmpleadoAnterior = createString("idEmpleadoAnterior");

    public final StringPath idEmpleadoNuevo = createString("idEmpleadoNuevo");

    public final NumberPath<java.math.BigDecimal> montoAnterior = createNumber("montoAnterior", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> montoNuevo = createNumber("montoNuevo", java.math.BigDecimal.class);

    public final NumberPath<Integer> pcAnterior = createNumber("pcAnterior", Integer.class);

    public final NumberPath<Integer> pcNuevo = createNumber("pcNuevo", Integer.class);

    public final StringPath tipoAnterior = createString("tipoAnterior");

    public final StringPath tipoNuevo = createString("tipoNuevo");

    public final NumberPath<java.math.BigDecimal> ventaAnterior = createNumber("ventaAnterior", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> ventaNueva = createNumber("ventaNueva", java.math.BigDecimal.class);

    public QModificacionImp(String variable) {
        super(ModificacionImp.class, forVariable(variable));
    }

    public QModificacionImp(Path<? extends ModificacionImp> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QModificacionImp(PathMetadata<?> metadata) {
        super(ModificacionImp.class, metadata);
    }

}

