package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAcuse is a Querydsl query type for Acuse
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAcuse extends EntityPathBase<Acuse> {

    private static final long serialVersionUID = 1631125956;

    public static final QAcuse acuse = new QAcuse("acuse");

    public final StringPath contenido = createString("contenido");

    public final DateTimePath<java.util.Date> fechaAcuso = createDateTime("fechaAcuso", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaCarga = createDateTime("fechaCarga", java.util.Date.class);

    public final StringPath folio = createString("folio");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idTipo = createString("idTipo");

    public final NumberPath<Integer> intentos = createNumber("intentos", Integer.class);

    public QAcuse(String variable) {
        super(Acuse.class, forVariable(variable));
    }

    public QAcuse(Path<? extends Acuse> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QAcuse(PathMetadata<?> metadata) {
        super(Acuse.class, metadata);
    }

}

