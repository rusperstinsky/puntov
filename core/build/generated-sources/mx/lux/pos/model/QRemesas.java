package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QRemesas is a Querydsl query type for Remesas
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRemesas extends EntityPathBase<Remesas> {

    private static final long serialVersionUID = 2101111001;

    public static final QRemesas remesas = new QRemesas("remesas");

    public final StringPath archivo = createString("archivo");

    public final NumberPath<Integer> articulos = createNumber("articulos", Integer.class);

    public final StringPath clave = createString("clave");

    public final StringPath docto = createString("docto");

    public final StringPath estado = createString("estado");

    public final DateTimePath<java.util.Date> fecha_carga = createDateTime("fecha_carga", java.util.Date.class);

    public final DateTimePath<java.util.Date> fecha_mod = createDateTime("fecha_mod", java.util.Date.class);

    public final DateTimePath<java.util.Date> fecha_recibido = createDateTime("fecha_recibido", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idDocto = createString("idDocto");

    public final StringPath idTipoDocto = createString("idTipoDocto");

    public final StringPath letra = createString("letra");

    public final StringPath sistema = createString("sistema");

    public QRemesas(String variable) {
        super(Remesas.class, forVariable(variable));
    }

    public QRemesas(Path<? extends Remesas> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QRemesas(PathMetadata<?> metadata) {
        super(Remesas.class, metadata);
    }

}

