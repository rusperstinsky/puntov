package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QCliente is a Querydsl query type for Cliente
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCliente extends EntityPathBase<Cliente> {

    private static final long serialVersionUID = 1870163417;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QCliente cliente = new QCliente("cliente");

    public final StringPath apellidoMaterno = createString("apellidoMaterno");

    public final StringPath apellidoPaterno = createString("apellidoPaterno");

    public final BooleanPath avisar = createBoolean("avisar");

    public final NumberPath<Integer> calif = createNumber("calif", Integer.class);

    public final QClientePais clientePais;

    public final StringPath codigo = createString("codigo");

    public final StringPath colonia = createString("colonia");

    public final StringPath cuc = createString("cuc");

    public final StringPath direccion = createString("direccion");

    public final StringPath email = createString("email");

    public final StringPath extAdicional = createString("extAdicional");

    public final StringPath extTrabajo = createString("extTrabajo");

    public final BooleanPath fCasada = createBoolean("fCasada");

    public final DateTimePath<java.util.Date> fechaAlta = createDateTime("fechaAlta", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaImp = createDateTime("fechaImp", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaImpUpdate = createDateTime("fechaImpUpdate", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaModificado = createDateTime("fechaModificado", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaNacimiento = createDateTime("fechaNacimiento", java.util.Date.class);

    public final BooleanPath finado = createBoolean("finado");

    public final StringPath histCli = createString("histCli");

    public final StringPath histCuc = createString("histCuc");

    public final DateTimePath<java.util.Date> horaAlta = createDateTime("horaAlta", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idAtendio = createString("idAtendio");

    public final StringPath idConvenio = createString("idConvenio");

    public final StringPath idEstado = createString("idEstado");

    public final StringPath idLocalidad = createString("idLocalidad");

    public final StringPath idModificado = createString("idModificado");

    public final NumberPath<Integer> idOftalmologo = createNumber("idOftalmologo", Integer.class);

    public final NumberPath<Integer> idSucursal = createNumber("idSucursal", Integer.class);

    public final StringPath idSync = createString("idSync");

    public final BooleanPath modImp = createBoolean("modImp");

    public final QMunicipio municipio;

    public final StringPath nombre = createString("nombre");

    public final StringPath obs = createString("obs");

    public final StringPath ori = createString("ori");

    public final NumberPath<Integer> receta = createNumber("receta", Integer.class);

    public final StringPath rfc = createString("rfc");

    public final BooleanPath sexo = createBoolean("sexo");

    public final StringPath sUsaAnteojos = createString("sUsaAnteojos");

    public final StringPath telefonoAdicional = createString("telefonoAdicional");

    public final StringPath telefonoCasa = createString("telefonoCasa");

    public final StringPath telefonoTrabajo = createString("telefonoTrabajo");

    public final StringPath tipo = createString("tipo");

    public final StringPath tipoImp = createString("tipoImp");

    public final StringPath tipoOftalmologo = createString("tipoOftalmologo");

    public final StringPath titulo = createString("titulo");

    public final StringPath udf1 = createString("udf1");

    public final StringPath udf2 = createString("udf2");

    public final StringPath udf4 = createString("udf4");

    public final StringPath udf5 = createString("udf5");

    public final StringPath udf6 = createString("udf6");

    public QCliente(String variable) {
        this(Cliente.class, forVariable(variable), INITS);
    }

    public QCliente(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCliente(PathMetadata<?> metadata, PathInits inits) {
        this(Cliente.class, metadata, inits);
    }

    public QCliente(Class<? extends Cliente> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clientePais = inits.isInitialized("clientePais") ? new QClientePais(forProperty("clientePais")) : null;
        this.municipio = inits.isInitialized("municipio") ? new QMunicipio(forProperty("municipio"), inits.get("municipio")) : null;
    }

}

