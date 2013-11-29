package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInstitucionIc is a Querydsl query type for InstitucionIc
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInstitucionIc extends EntityPathBase<InstitucionIc> {

    private static final long serialVersionUID = -1283063038;

    public static final QInstitucionIc institucionIc = new QInstitucionIc("institucionIc");

    public final BooleanPath aceptarVales = createBoolean("aceptarVales");

    public final StringPath copiaP = createString("copiaP");

    public final StringPath copiaT = createString("copiaT");

    public final StringPath estatusConvenio = createString("estatusConvenio");

    public final BooleanPath f_acumular_tope = createBoolean("f_acumular_tope");

    public final DateTimePath<java.util.Date> fechaMod = createDateTime("fechaMod", java.util.Date.class);

    public final BooleanPath fRestricLente = createBoolean("fRestricLente");

    public final StringPath id = createString("id");

    public final StringPath id_mod = createString("id_mod");

    public final StringPath idConvenioPrinci = createString("idConvenioPrinci");

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public final StringPath inicialesIc = createString("inicialesIc");

    public final BooleanPath mejorPrecio = createBoolean("mejorPrecio");

    public final StringPath memoCondiciones = createString("memoCondiciones");

    public final NumberPath<java.math.BigDecimal> porcentaje_al_tope = createNumber("porcentaje_al_tope", java.math.BigDecimal.class);

    public final StringPath razonSocial = createString("razonSocial");

    public final StringPath tipoConvenio = createString("tipoConvenio");

    public final NumberPath<java.math.BigDecimal> topeAnt = createNumber("topeAnt", java.math.BigDecimal.class);

    public QInstitucionIc(String variable) {
        super(InstitucionIc.class, forVariable(variable));
    }

    public QInstitucionIc(Path<? extends InstitucionIc> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QInstitucionIc(PathMetadata<?> metadata) {
        super(InstitucionIc.class, metadata);
    }

}

