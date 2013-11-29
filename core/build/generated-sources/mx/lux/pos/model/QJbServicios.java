package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QJbServicios is a Querydsl query type for JbServicios
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QJbServicios extends EntityPathBase<JbServicios> {

    private static final long serialVersionUID = -1604952988;

    public static final QJbServicios jbServicios = new QJbServicios("jbServicios");

    public final StringPath servicio = createString("servicio");

    public QJbServicios(String variable) {
        super(JbServicios.class, forVariable(variable));
    }

    public QJbServicios(Path<? extends JbServicios> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QJbServicios(PathMetadata<?> metadata) {
        super(JbServicios.class, metadata);
    }

}

