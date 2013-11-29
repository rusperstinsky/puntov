package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QListaPrecios is a Querydsl query type for ListaPrecios
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QListaPrecios extends EntityPathBase<ListaPrecios> {

    private static final long serialVersionUID = 345913451;

    public static final QListaPrecios listaPrecios = new QListaPrecios("listaPrecios");

    public final DateTimePath<java.util.Date> fechaAct = createDateTime("fechaAct", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaActAuto = createDateTime("fechaActAuto", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaCarga = createDateTime("fechaCarga", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final StringPath filename = createString("filename");

    public final StringPath id = createString("id");

    public final StringPath tipoCarga = createString("tipoCarga");

    public QListaPrecios(String variable) {
        super(ListaPrecios.class, forVariable(variable));
    }

    public QListaPrecios(Path<? extends ListaPrecios> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QListaPrecios(PathMetadata<?> metadata) {
        super(ListaPrecios.class, metadata);
    }

}

