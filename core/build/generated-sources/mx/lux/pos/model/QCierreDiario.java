package mx.lux.pos.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QCierreDiario is a Querydsl query type for CierreDiario
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCierreDiario extends EntityPathBase<CierreDiario> {

    private static final long serialVersionUID = 962714435;

    public static final QCierreDiario cierreDiario = new QCierreDiario("cierreDiario");

    public final NumberPath<java.math.BigDecimal> cancelaciones = createNumber("cancelaciones", java.math.BigDecimal.class);

    public final NumberPath<Integer> cantidadCancelaciones = createNumber("cantidadCancelaciones", Integer.class);

    public final NumberPath<Integer> cantidadModificaciones = createNumber("cantidadModificaciones", Integer.class);

    public final NumberPath<Integer> cantidadVentas = createNumber("cantidadVentas", Integer.class);

    public final NumberPath<java.math.BigDecimal> devoluciones = createNumber("devoluciones", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> dolaresDevoluciones = createNumber("dolaresDevoluciones", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> dolaresRecibidos = createNumber("dolaresRecibidos", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> efectivoDevoluciones = createNumber("efectivoDevoluciones", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> efectivoExternos = createNumber("efectivoExternos", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> efectivoNeto = createNumber("efectivoNeto", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> efectivoRecibido = createNumber("efectivoRecibido", java.math.BigDecimal.class);

    public final StringPath estado = createString("estado");

    public final StringPath facturaFinal = createString("facturaFinal");

    public final StringPath facturaInicial = createString("facturaInicial");

    public final DateTimePath<java.util.Date> fecha = createDateTime("fecha", java.util.Date.class);

    public final DateTimePath<java.util.Date> fechaCierre = createDateTime("fechaCierre", java.util.Date.class);

    public final DateTimePath<java.util.Date> horaCierre = createDateTime("horaCierre", java.util.Date.class);

    public final NumberPath<java.math.BigDecimal> ingresoBruto = createNumber("ingresoBruto", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> ingresoNeto = createNumber("ingresoNeto", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> modificaciones = createNumber("modificaciones", java.math.BigDecimal.class);

    public final StringPath observaciones = createString("observaciones");

    public final NumberPath<java.math.BigDecimal> ventaBruta = createNumber("ventaBruta", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> ventaNeta = createNumber("ventaNeta", java.math.BigDecimal.class);

    public QCierreDiario(String variable) {
        super(CierreDiario.class, forVariable(variable));
    }

    public QCierreDiario(Path<? extends CierreDiario> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QCierreDiario(PathMetadata<?> metadata) {
        super(CierreDiario.class, metadata);
    }

}

