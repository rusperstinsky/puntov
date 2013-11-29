package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QModificacionEmp is a Querydsl query type for ModificacionEmp
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QModificacionEmp extends EntityPathBase<ModificacionEmp> {

    private static final long serialVersionUID = 589378330;

    public static final QModificacionEmp modificacionEmp = new QModificacionEmp("modificacionEmp");

    public final StringPath empleadoActual = createString("empleadoActual");

    public final StringPath empleadoAnterior = createString("empleadoAnterior");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QModificacionEmp(String variable) {
        super(ModificacionEmp.class, forVariable(variable));
    }

    public QModificacionEmp(Path<? extends ModificacionEmp> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QModificacionEmp(PathMetadata<?> metadata) {
        super(ModificacionEmp.class, metadata);
    }

}

