package mx.lux.pos.service.impl;

import mx.lux.pos.model.*;
import mx.lux.pos.repository.*;
import mx.lux.pos.service.ReportService;
import mx.lux.pos.service.SucursalService;
import mx.lux.pos.service.business.Registry;
import mx.lux.pos.service.business.ReportBusiness;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service( "reportService" )
@Transactional( readOnly = true )
public class ReportServiceImpl implements ReportService {

    private static Logger log = LoggerFactory.getLogger( ReportServiceImpl.class );
    private static String CIERRE_DIARIO = "reports/Cierre_Diario.jrxml";
    private static String INGRESOS_POR_SUCURSAL = "reports/Ingresos_Sucursal.jrxml";
    private static String INGRESOS_POR_VENDEDOR_RESUMIDO = "reports/Ingresos_Vendedor_Resumido.jrxml";
    private static String INGRESOS_POR_VENDEDOR_COMPLETO = "reports/Ingresos_Vendedor_Completo.jrxml";
    private static String VENTAS = "reports/Ventas.jrxml";
    private static String VENTAS_COMPLETO = "reports/Ventas_Completo.jrxml";
    private static String VENTAS_POR_VENDEDOR_COMPLETO = "reports/Venta_Por_Vendedor_Completo.jrxml";
    private static String VENTAS_POR_VENDEDOR_RESUMIDO = "reports/Venta_Por_Vendedor_Resumido.jrxml";
    private static String TRABAJOS_SIN_ENTREGAR = "reports/Trabajos_Sin_Entregar.jrxml";
    private static String CANCELACIONES_RESUMIDO = "reports/Cancelaciones.jrxml";
    private static String CANCELACIONES_COMPLETO = "reports/Cancelaciones_Completo.jrxml";
    private static String VENTA_POR_LINEA_FACTURA = "reports/Venta_Por_Linea.jrxml";
    private static String VENTA_POR_LINEA_ARTICULO = "reports/Venta_Por_Linea_Articulo.jrxml";
    private static String VENTA_POR_MARCA = "reports/Ventas_Por_Marca.jrxml";
    private static String VENTA_POR_VENDEDOR_MARCA = "reports/Ventas_Por_Vendedor_Por_Marca.jrxml";
    private static String EXISTENCIAS_POR_MARCA = "reports/Existencias_Por_Marca.jrxml";
    private static String EXISTENCIAS_POR_MARCA_RESUMIDO = "reports/Existencias_Por_Marca_Resumido.jrxml";
    private static String EXISTENCIAS_POR_ARTICULO = "reports/Existencias_Por_Articulo.jrxml";
    private static String CONTROL_DE_TRABAJOS = "reports/Control_de_Trabajos.jrxml";
    private static String TRABAJOS_ENTREGADOS = "reports/Trabajos_Entregados.jrxml";
    private static String TRABAJOS_ENTREGADOS_POR_EMPLEADO = "reports/Trabajos_Entregados_Por_Empleado.jrxml";
    private static String FACTURAS_FISCALES = "reports/Facturas_Fiscales.jrxml";
    private static String DESCUENTOS = "reports/Descuentos.jrxml";
    private static String PROMOCIONES_APLICADAS = "reports/Promociones.jrxml";
    private static String PAGOS = "reports/Pagos.jrxml";
    private static String COTIZACIONES = "reports/Cotizaciones.jrxml";
    private static String EXAMENES_RESUMIDO = "reports/Examenes.jrxml";
    private static String EXAMENES_COMPLETO = "reports/Examenes_Completo.jrxml";
    private static String VENTAS_POR_OPTOMETRISTA_COMPLETO = "reports/Ventas_Por_Optometrista_Completo.jrxml";
    private static String VENTAS_POR_OPTOMETRISTA_RESUMIDO = "reports/Ventas_Por_Optometrista_Resumido.jrxml";
    private static String PROMOCIONES = "reports/Lista_de_Promociones.jrxml";
    private static String KARDEX = "reports/Kardex.jrxml";
    private static String VENTAS_DEL_DIA = "reports/Ventas_Del_Dia.jrxml";
    private static String INGRESOS_POR_PERIODO = "reports/Ingresos_Por_Periodo.jrxml";

    @Resource
    private NotaVentaRepository notaVentaRepository;

    @Resource
    private CotizacionRepository cotizacionRepository;

    @Resource
    private OrdenPromDetRepository ordenPromDetRepository;

    @Resource
    private NotaFacturaRepository notaFacturaRepository;

    @Resource
    private ModificacionRepository modificacionRepository;

    @Resource
    private TrabajoRepository trabajoRepository;

    @Resource
    private ExternoRepository externoRepository;

    @Resource
    private DevolucionRepository devolucionRepository;

    @Resource
    private PagoRepository pagoRepository;

    @Resource
    private SucursalService sucursalService;

    @Resource
    private ParametroRepository parametroRepository;

    @Resource
    private ImpuestoRepository impuestoRepository;

    @Resource
    private ReportBusiness reportBusiness;

    @Resource
    private EmpleadoRepository empleadoRepository;

    @Resource
    private ArticuloRepository articuloRepository;


    @Override
    public String obtenerReporteCierreDiario( Date fecha ) {
        log.info( "obtenerReporteCierreDiario" );

        if ( fecha != null ) {
            File report = new File( System.getProperty( "java.io.tmpdir" ), "Cierre-Diario.html" );
            org.springframework.core.io.Resource template = new ClassPathResource( CIERRE_DIARIO );
            log.info( "Ruta:{}", report.getAbsolutePath() );

            Date fechaInicio = DateUtils.truncate( fecha, Calendar.DAY_OF_MONTH );
            Date fechaFin = new Date( DateUtils.ceiling( fecha, Calendar.DAY_OF_MONTH ).getTime() - 1 );

            Sucursal sucursal = sucursalService.obtenSucursalActual();
            List<ResumenCierre> lstPagos = reportBusiness.obtenerVentasCierreDiario( fechaInicio, fechaFin );
            List<ResumenCierre> lstDevoluciones = reportBusiness.obtenerDevolucionesCierreDiario( fechaInicio, fechaFin );
            Parametro ivaVigenteParam = parametroRepository.findOne( TipoParametro.IVA_VIGENTE.getValue() );
            Impuesto iva = impuestoRepository.findOne( ivaVigenteParam.getValor() );


            Integer totalFacturas = 0;
            BigDecimal totalVenta = BigDecimal.ZERO;
            BigDecimal totalEfectivo = BigDecimal.ZERO;
            BigDecimal totalTarjeta = BigDecimal.ZERO;
            BigDecimal totalTarjetaUSD = BigDecimal.ZERO;
            BigDecimal totalUSDEfectivo = BigDecimal.ZERO;
            BigDecimal totalTransferencia = BigDecimal.ZERO;
            BigDecimal totalOtros = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;
            double totalVentaIVA = 0.0;
            double totalEfectivoIVA = 0.0;
            double totalTarjetaIVA = 0.0;
            double totalTarjetaUSDIVA = 0.0;
            double totalUSDEfectivoIVA = 0.0;
            double totalTransferenciaIVA = 0.0;
            double totalOtrosIVA = 0.0;
            double totalIVA = 0.0;

            for(ResumenCierre venta : lstPagos){
                for(DetalleIngresoPorDia ingreso : venta.getLstPagos()){
                    totalFacturas = totalFacturas+1;
                    totalVenta = totalVenta.add(ingreso.getMontoPago());
                    totalEfectivo = totalEfectivo.add(ingreso.getPagoEf());
                    totalTarjeta = totalTarjeta.add(ingreso.getPagoTN());
                    totalTarjetaUSD = totalTarjetaUSD.add(ingreso.getPagoTD());
                    totalUSDEfectivo = totalUSDEfectivo.add(ingreso.getPagoEfUs());
                    totalTransferencia = totalTransferencia.add(ingreso.getPagoTR());
                    totalOtros = totalOtros.add(ingreso.getPagoOtros());
                    total = total.add(ingreso.getMontoTotal());
                }
            }
            for(ResumenCierre devolucion : lstDevoluciones){
                for(DetalleIngresoPorDia montoDev : devolucion.getLstPagos()){
                    totalFacturas = totalFacturas-1;
                    totalVenta = totalVenta.subtract(montoDev.getMontoPago());
                    totalEfectivo = totalEfectivo.subtract(montoDev.getPagoEf());
                    totalTarjeta = totalTarjeta.subtract(montoDev.getPagoTN());
                    totalTarjetaUSD = totalTarjetaUSD.subtract(montoDev.getPagoTD());
                    totalUSDEfectivo = totalUSDEfectivo.subtract(montoDev.getPagoEfUs());
                    totalTransferencia = totalTransferencia.subtract(montoDev.getPagoTR());
                    totalOtros = totalOtros.subtract(montoDev.getPagoOtros());
                    total = total.subtract(montoDev.getMontoTotal());
                }

            }

            totalVentaIVA = totalVenta.doubleValue() / ( 1 + ( iva.getTasa().doubleValue() / 100 ) );
            totalEfectivoIVA = totalEfectivo.doubleValue() / ( 1 + ( iva.getTasa().doubleValue() / 100 ) );
            totalTarjetaIVA = totalTarjeta.doubleValue() / ( 1 + ( iva.getTasa().doubleValue() / 100 ) );
            totalTarjetaUSDIVA = totalTarjetaUSD.doubleValue() / ( 1 + ( iva.getTasa().doubleValue() / 100 ) );
            totalUSDEfectivoIVA = totalUSDEfectivo.doubleValue() / ( 1 + ( iva.getTasa().doubleValue() / 100 ) );
            totalTransferenciaIVA = totalTransferencia.doubleValue() / ( 1 + ( iva.getTasa().doubleValue() / 100 ) );
            totalOtrosIVA = totalOtros.doubleValue() / ( 1 + ( iva.getTasa().doubleValue() / 100 ) );
            totalIVA = total.doubleValue() / ( 1 + ( iva.getTasa().doubleValue() / 100 ) );

            Boolean isSunglass = Registry.isSunglass();
            //NumberFormat formatter = new DecimalFormat( "$#,##0.00" );

            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
            parametros.put( "fechaCierre", new SimpleDateFormat( "dd/MM/yyyy" ).format( fecha ) );
            parametros.put( "lstPagos", lstPagos );
            parametros.put( "lstDevoluciones", lstDevoluciones );

            parametros.put( "totalFacturas", totalFacturas );
            parametros.put( "totalVenta", totalVenta );
            parametros.put( "totalEfectivo", totalEfectivo );
            parametros.put( "totalTarjeta", totalTarjeta );
            parametros.put( "totalTarjetaUSD", totalTarjetaUSD );
            parametros.put( "totalUSDEfectivo", totalUSDEfectivo );
            parametros.put( "totalTransferencia", totalTransferencia );
            parametros.put( "totalOtros", totalOtros );
            parametros.put( "total", total );
            parametros.put( "totalVentaIVA", totalVentaIVA );
            parametros.put( "totalEfectivoIVA", totalEfectivoIVA );
            parametros.put( "totalTarjetaIVA", totalTarjetaIVA );
            parametros.put( "totalTarjetaUSDIVA", totalTarjetaUSDIVA );
            parametros.put( "totalUSDEfectivoIVA", totalUSDEfectivoIVA );
            parametros.put( "totalTransferenciaIVA", totalTransferenciaIVA );
            parametros.put( "totalOtrosIVA", totalOtrosIVA );
            parametros.put( "totalIVA", totalIVA );

            parametros.put( "isSunglass", isSunglass );
            parametros.put( "sucursal", sucursal.getNombre() );

            String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
            log.info( "reporte:{}", reporte );

        }
        return null;
    }


    @Override
    public String obtenerReporteIngresosXSucursal( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteIngresosXSucursal()" );

        if ( fechaInicio != null && fechaFin != null ) {

            File report = new File( System.getProperty( "java.io.tmpdir" ), "Ingresos-por-Sucursal.html" );
            org.springframework.core.io.Resource template = new ClassPathResource( INGRESOS_POR_SUCURSAL );
            log.info( "Ruta:{}", report.getAbsolutePath() );

            Parametro ivaVigenteParam = parametroRepository.findOne( TipoParametro.IVA_VIGENTE.getValue() );
            Impuesto iva = impuestoRepository.findOne( ivaVigenteParam.getValor() );
            Double ivaTasa = iva.getTasa();
            BigDecimal ivaMonto = new BigDecimal( ivaTasa ).divide( new BigDecimal( 100 ) );

            fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
            fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );
            List<IngresoPorDia> listaPagos = reportBusiness.obtenerIngresoporDia( fechaInicio, fechaFin );
            Sucursal sucursal = sucursalService.obtenSucursalActual();

            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
            parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
            parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
            parametros.put( "sucursal", sucursal.getNombre() );
            parametros.put( "lstpagos", listaPagos );
            if ( listaPagos.size() > 0 ) {
                parametros.put( "lstpagosPrim", listaPagos.get( 0 ).getMontoAcumulado() );
                parametros.put( "ivaMonto", ivaMonto );
            }

            String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
            log.info( "reporte:{}", reporte );

        }

        return null;
    }


    public String obtenerReporteIngresosXVendedorResumido( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteIngresosXVendedorResumido()" );

        if ( fechaInicio != null && fechaFin != null ) {
            fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
            fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

            File report = new File( System.getProperty( "java.io.tmpdir" ), "Ingresos-por-Vendedor-Resumido.html" );
            org.springframework.core.io.Resource template = new ClassPathResource( INGRESOS_POR_VENDEDOR_RESUMIDO );
            log.info( "Ruta:{}", report.getAbsolutePath() );

            List<IngresoPorVendedor> lstIngresos = reportBusiness.obtenerIngresoporVendedor( fechaInicio, fechaFin );

            Sucursal sucursal = sucursalService.obtenSucursalActual();

            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
            parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
            parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
            parametros.put( "sucursal", sucursal.getNombre() );
            parametros.put( "lstIngresos", lstIngresos );

            String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
            log.info( "reporte:{}", reporte );
            return reporte;
        }
        return null;

    }


    public String obtenerReporteIngresosXVendedorCompleto( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteIngresosXVendedorCompleto()" );

        if ( fechaInicio != null && fechaFin != null ) {
            fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
            fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

            File report = new File( System.getProperty( "java.io.tmpdir" ), "Ingresos-por-Vendedor.html" );
            org.springframework.core.io.Resource template = new ClassPathResource( INGRESOS_POR_VENDEDOR_COMPLETO );
            log.info( "Ruta:{}", report.getAbsolutePath() );

            List<IngresoPorVendedor> lstIngresos = reportBusiness.obtenerIngresoporVendedor( fechaInicio, fechaFin );
            Sucursal sucursal = sucursalService.obtenSucursalActual();

            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
            parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
            parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
            parametros.put( "sucursal", sucursal.getNombre() );
            parametros.put( "lstIngresos", lstIngresos );

            String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
            log.info( "reporte:{}", reporte );
        }
        return null;

    }


    public String obtenerReporteVentas( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteVentas()" );

        if ( fechaInicio != null && fechaFin != null ) {
            fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
            fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

            File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas.html" );
            org.springframework.core.io.Resource template = new ClassPathResource( VENTAS );
            log.info( "Ruta:{}", report.getAbsolutePath() );

            List<IngresoPorDia> lstIngresos = reportBusiness.obtenerVentasporDia( fechaInicio, fechaFin );
            Sucursal sucursal = sucursalService.obtenSucursalActual();

            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
            parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
            parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
            parametros.put( "sucursal", sucursal.getNombre() );
            parametros.put( "lstIngresos", lstIngresos );
            if ( lstIngresos.size() > 0 ) {
                parametros.put( "monto", lstIngresos.get( 0 ).getMontoAcumulado() );
            }

            String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
            log.info( "reporte:{}", reporte );

        }
        return null;

    }


    public String obtenerReporteVentasporVendedorResumido( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteVentasporVendedorResumido()" );

        if ( fechaInicio != null && fechaFin != null ) {
            fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
            fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

            File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-por-Vendedor.html" );
            org.springframework.core.io.Resource template = new ClassPathResource( VENTAS_POR_VENDEDOR_RESUMIDO );
            log.info( "Ruta:{}", report.getAbsolutePath() );

            List<IngresoPorVendedor> lstVentas = reportBusiness.obtenerVentasporVendedor( fechaInicio, fechaFin );
            BigDecimal montoTotal = BigDecimal.ZERO;
            Integer totalPiezas = 0;
            Integer totalFacturas = 0;
            for(IngresoPorVendedor ingreso : lstVentas){
                montoTotal = montoTotal.add(ingreso.getTotalPagosIva());
                totalPiezas = totalPiezas+ingreso.getPiezas();
                totalFacturas = totalFacturas+ingreso.getNoFacturas().intValue();
            }
            Sucursal sucursal = sucursalService.obtenSucursalActual();

            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
            parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
            parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
            parametros.put( "sucursal", sucursal.getNombre() );
            parametros.put( "lstVentas", lstVentas );
            parametros.put( "montoTotal", montoTotal );
            parametros.put( "totalPiezas", totalPiezas );
            parametros.put( "totalFacturas", totalFacturas );

            String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
            log.info( "reporte:{}", reporte );
        }
        return null;

    }


    public String obtenerReporteVentasporVendedorCompleto( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteVentasporVendedorCompleto()" );

        if ( fechaInicio != null && fechaFin != null ) {
            fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
            fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

            File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-por-Vendedor.html" );
            org.springframework.core.io.Resource template = new ClassPathResource( VENTAS_POR_VENDEDOR_COMPLETO );
            log.info( "Ruta:{}", report.getAbsolutePath() );

            List<IngresoPorVendedor> lstVentas = reportBusiness.obtenerVentasporVendedor( fechaInicio, fechaFin );
            Sucursal sucursal = sucursalService.obtenSucursalActual();

            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
            parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
            parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
            parametros.put( "sucursal", sucursal.getNombre() );
            parametros.put( "lstVentas", lstVentas );

            String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
            log.info( "reporte:{}", reporte );
        }
        return null;

    }


    public String obtenerReporteTrabajosSinEntregar() {
        log.info( "obtenerReporteTrabajosSinEntregar()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Trabajos-Sin-Entregar.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( TRABAJOS_SIN_ENTREGAR );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        QNotaVenta venta = QNotaVenta.notaVenta;
        List<NotaVenta> lstVentas = ( List<NotaVenta> ) notaVentaRepository.findAll( venta.fechaEntrega.isNull().
                and( venta.sFactura.notEqualsIgnoreCase( "T" ) ).and( venta.factura.isNotNull() ).and( venta.factura.isNotEmpty() ) );
        BigDecimal ventasVenta = BigDecimal.ZERO;
        BigDecimal ventasSaldo = BigDecimal.ZERO;
        for ( NotaVenta ventas : lstVentas ) {
            ventasVenta = ventasVenta.add( ventas.getVentaTotal() );
            ventasSaldo = ventasSaldo.add( ventas.getVentaNeta().subtract( ventas.getSumaPagos() ) );
        }

        QExterno externo = QExterno.externo;
        List<Externo> lstExternos = ( List<Externo> ) externoRepository.findAll( externo.fechaEntrega.isNull() );
        BigDecimal externoSaldo = BigDecimal.ZERO;
        for ( Externo externos : lstExternos ) {
            externoSaldo = externoSaldo.add( externos.getTrabajo().getSaldo() );
        }

        QTrabajo trabajo = QTrabajo.trabajo;
        List<Trabajo> lstTrabajos = ( List<Trabajo> ) trabajoRepository.findAll( trabajo.jbTipo.eq( "OS" ).
                or( trabajo.jbTipo.eq( "GAR" ) ).and( trabajo.estado.ne( "TE" ) ) );
        BigDecimal trabajoSaldo = BigDecimal.ZERO;
        for ( Trabajo trabajos : lstTrabajos ) {
            trabajoSaldo = trabajoSaldo.add( trabajos.getSaldo() );
        }

        Integer totalFacturas;
        BigDecimal totalVentas;
        BigDecimal totalSaldos;
        totalVentas = ventasVenta.add( trabajoSaldo );
        totalSaldos = ventasSaldo.add( externoSaldo.add( trabajoSaldo ) );
        totalFacturas = lstVentas.size() + lstExternos.size() + lstTrabajos.size();

        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstVentas", lstVentas );
        parametros.put( "lstTrabajos", lstTrabajos );
        parametros.put( "lstExternos", lstExternos );
        parametros.put( "totalVentas", totalVentas );
        parametros.put( "totalSaldos", totalSaldos );
        parametros.put( "totalFacturas", totalFacturas );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;

    }


    public String obtenerReporteCancelacionesResumido( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteCancelacionesResumido()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Cancelaciones.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( CANCELACIONES_RESUMIDO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        Sucursal sucursal = sucursalService.obtenSucursalActual();
        List<FacturasPorEmpleado> lstFacturas = reportBusiness.obtenerFacturasporVendedor( fechaInicio, fechaFin );
        log.info( "tamañoLista:{}", lstFacturas.size() );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstFacturas", lstFacturas );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;

    }


    public String obtenerReporteCancelacionesCompleto( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteCancelacionesCompleto()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Cancelaciones.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( CANCELACIONES_COMPLETO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );
        List<FacturasPorEmpleado> lstModificaciones = reportBusiness.obtenerFacturasporVendedor( fechaInicio, fechaFin );
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstModificaciones", lstModificaciones );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;

    }


    public String obtenerReporteVentasporLineaFactura( Date fechaInicio, Date fechaFin, String articulo, boolean gogle, boolean oftalmico, boolean todo ) {
        log.info( "obtenerReporteVentasporLineaFactura()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-Por-Linea-Factura.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( VENTA_POR_LINEA_FACTURA );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );
        List<NotaVenta> lstArticulos = reportBusiness.obtenerVentasLineaporFacturas( fechaInicio, fechaFin, articulo, gogle, oftalmico, todo );
        QModificacion modificacion = QModificacion.modificacion;
        List<Modificacion> lstCancelaciones = ( List<Modificacion> ) modificacionRepository.findAll( modificacion.fecha.between( fechaInicio, fechaFin ),
                modificacion.idFactura.asc() );
        Collections.sort( lstArticulos, new Comparator<NotaVenta>() {
            @Override
            public int compare( NotaVenta o1, NotaVenta o2 ) {
                return o1.getFactura().compareToIgnoreCase( o2.getFactura() );
            }
        } );
        Integer totalFact = 0;
        BigDecimal totalMonto = BigDecimal.ZERO;
        Integer totalFactCan = 0;
        BigDecimal totalMontoCan = BigDecimal.ZERO;
        for ( NotaVenta nota : lstArticulos ) {
            totalFact = totalFact + 1;
            for ( DetalleNotaVenta det : nota.getDetalles() ) {
                totalMonto = totalMonto.add( det.getPrecioUnitFinal() );
            }
        }
        for ( Modificacion mod : lstCancelaciones ) {
            totalFactCan = totalFactCan + 1;
            for ( DetalleNotaVenta det : mod.getNotaVenta().getDetalles() ) {
                totalMontoCan = totalMontoCan.add( det.getPrecioUnitFinal() );
            }
        }
        Integer facturas = totalFact - totalFactCan;
        BigDecimal monto = totalMonto.subtract( totalMontoCan );
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstArticulos", lstArticulos );
        parametros.put( "lstCancelaciones", lstCancelaciones );
        parametros.put( "facturas", facturas );
        parametros.put( "monto", monto );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteVentasporLineaArticulo( Date fechaInicio, Date fechaFin, String articulo, boolean gogle, boolean oftalmico, boolean todo ) {
        log.info( "obtenerReporteVentasporLineaArticulo()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-Por-Linea-Factura.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( VENTA_POR_LINEA_ARTICULO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );
        List<FacturasPorEmpleado> lstArticulos = reportBusiness.obtenerVentasLineaporArticulos( fechaInicio, fechaFin, articulo, gogle, oftalmico, todo );
        Collections.sort( lstArticulos, new Comparator<FacturasPorEmpleado>() {
            @Override
            public int compare( FacturasPorEmpleado o1, FacturasPorEmpleado o2 ) {
                return o1.getIdArticulo().compareTo( o2.getIdArticulo() );
            }
        } );
        Integer totalArt = 0;
        BigDecimal totalMonto = BigDecimal.ZERO;
        for ( FacturasPorEmpleado art : lstArticulos ) {
            totalArt = totalArt + art.getCantidad();
            totalMonto = totalMonto.add( art.getImporte() );
        }
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstArticulos", lstArticulos );
        parametros.put( "totalArt", totalArt );
        parametros.put( "totalMonto", totalMonto );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteVentasMarca( Date fechaInicio, Date fechaFin, String marca, boolean noMostrarArticulos,
                                             boolean ordenarMarca, boolean ordenarImporte, boolean artTodos, boolean artAccesorios, boolean artArmazones ) {
        log.info( "obtenerReporteVentasMarca()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-Por-Marca.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( VENTA_POR_MARCA );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        Sucursal sucursal = sucursalService.obtenSucursalActual();

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );
        List<FacturasPorEmpleado> lstVentas = reportBusiness.obtenerVentasMarca( fechaInicio, fechaFin, marca.toUpperCase().trim(),
                noMostrarArticulos, ordenarMarca, ordenarImporte, artTodos, artAccesorios, artArmazones );
        Integer totalArticulos = 0;
        BigDecimal totalMonto = BigDecimal.ZERO;
        for ( FacturasPorEmpleado factura : lstVentas ) {
            totalArticulos = totalArticulos + factura.getCantidad();
            totalMonto = totalMonto.add( factura.getImporte() );
        }

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstVentas", lstVentas );
        parametros.put( "totalArticulos", totalArticulos );
        parametros.put( "totalMonto", totalMonto );
        parametros.put( "noMostrarArticulos", noMostrarArticulos );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteVentasVendedorporMarca( Date fechaInicio, Date fechaFin, String marca, boolean mostrarArticulos, boolean gogle, boolean oftalmico, boolean todo ) {
        log.info( "obtenerReporteVentasVendedorporMarca()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-Por-Vendedor-Por-Marca.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( VENTA_POR_VENDEDOR_MARCA );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );
        List<IngresoPorVendedor> lstVentas = reportBusiness.obtenerVentasporVendedorporMarca( fechaInicio, fechaFin, marca, mostrarArticulos, gogle, oftalmico, todo );

        Integer totalArticulos = 0;
        BigDecimal montoTotal = BigDecimal.ZERO;
        BigDecimal montoTotalSinIva = BigDecimal.ZERO;
        for(IngresoPorVendedor ingreso : lstVentas){
            totalArticulos = totalArticulos+ingreso.getContador().intValue();
            montoTotal = montoTotal.add(ingreso.getTotalPagos());
            montoTotalSinIva = montoTotalSinIva.add(ingreso.getTotalPagosIva());
        }
        Collections.sort(lstVentas, new Comparator<IngresoPorVendedor>() {
            @Override
            public int compare(IngresoPorVendedor o1, IngresoPorVendedor o2) {
                return o1.getIdEmpleado().compareTo(o2.getIdEmpleado());
            }
        });
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstVentas", lstVentas );
        parametros.put( "totalArticulos", totalArticulos );
        parametros.put( "montoTotal", montoTotal );
        parametros.put( "montoTotalSinIva", montoTotalSinIva );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteExistenciasporMarca( String marca, boolean gogle, boolean oftalmico, boolean todo ) {
        log.info( "obtenerReporteExistenciasporMarca()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Existencias-Por-Marca.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( EXISTENCIAS_POR_MARCA );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        List<FacturasPorEmpleado> lstArticulos = reportBusiness.obtenerExistenciasporMarcaCompleto( marca, gogle, oftalmico, todo );
        Collections.sort( lstArticulos, new Comparator<FacturasPorEmpleado>() {
            @Override
            public int compare(FacturasPorEmpleado o1, FacturasPorEmpleado o2) {
                return o1.getMarca().compareTo(o2.getMarca());
            }
        });
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstArticulos", lstArticulos );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteExistenciasporMarcaResumido( String marca, boolean gogle, boolean oftalmico, boolean todo ) {
        log.info( "obtenerReporteExistenciasporMarcaResumido()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Existencias-Por-Marca.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( EXISTENCIAS_POR_MARCA_RESUMIDO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        List<FacturasPorEmpleado> lstArticulos = reportBusiness.obtenerExistenciasporMarcaResumido( marca, gogle, oftalmico, todo );
        Integer cantTotal = 0;
        for(FacturasPorEmpleado factura : lstArticulos){
            cantTotal = cantTotal+factura.getCantidad();
        }
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstArticulos", lstArticulos );
        parametros.put( "cantTotal", cantTotal );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteExistenciasporArticulo( String marca, String descripcion, String color ) {
        log.info( "obtenerReporteExistenciasporArticulo()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Existencias-Por-Articulo.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( EXISTENCIAS_POR_ARTICULO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        List<Articulo> lstArticulos = reportBusiness.obtenerExistenciasporArticulo( marca, descripcion, color );
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Integer totalArticulos = 0;
        BigDecimal totalMonto = BigDecimal.ZERO;
        for( Articulo articulo : lstArticulos ){
            totalArticulos = totalArticulos+articulo.getCantExistencia().intValue();
            totalMonto = totalMonto.add(articulo.getPrecio());
        }

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstArticulos", lstArticulos );
        parametros.put( "totalMonto", totalMonto );
        parametros.put( "totalArticulos", totalArticulos );


        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteControldeTrabajos( boolean retenidos, boolean porEnviar, boolean pino, boolean sucursal, boolean todos, boolean factura, boolean fechaPromesa ) {
        log.info( "obtenerReporteControldeTrabajos()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Control-de-Trabajos.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( CONTROL_DE_TRABAJOS );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        List<SaldoPorEstado> lstTrabajos = reportBusiness.obtenerTrabajos( retenidos, porEnviar, pino, sucursal, todos, factura, fechaPromesa );
        Sucursal sucursalNom = sucursalService.obtenSucursalActual();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "sucursal", sucursalNom.getNombre() );
        parametros.put( "lstTrabajos", lstTrabajos );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteTrabajosEntregados( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteTrabajosEntregados()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Trabajos-Entregados.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( TRABAJOS_ENTREGADOS );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );
        List<TrabajoTrack> lstTrabajos = reportBusiness.obtenerTrabajosporEntregar( fechaInicio, fechaFin );
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstTrabajos", lstTrabajos );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteTrabajosEntregadosporEmpleado( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteTrabajosEntregadosporEmpleado()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Trabajos-Entregados-Por-Empleado.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( TRABAJOS_ENTREGADOS_POR_EMPLEADO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );
        List<SaldoPorEstado> lstTrabajos = reportBusiness.obtenerTrabajosporEntregarporEmpleado( fechaInicio, fechaFin );
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstTrabajos", lstTrabajos );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteVentasCompleto( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteVentasCompleto()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-Completo.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( VENTAS_COMPLETO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Parametro ivaVigenteParam = parametroRepository.findOne( TipoParametro.IVA_VIGENTE.getValue() );
        Impuesto iva = impuestoRepository.findOne( ivaVigenteParam.getValor() );
        BigDecimal ivaTasa = new BigDecimal( iva.getTasa() ).divide( new BigDecimal( 100 ) );

        List<VentasPorDia> lstVentas = reportBusiness.obtenerVentasPorPeriodo( fechaInicio, fechaFin );
        List<VentasPorDia> lstVentasCanc = reportBusiness.obtenerVentasCanceladasPorPeriodo( fechaInicio, fechaFin );
        //List<VentasPorDia> lstNotasCredito = reportBusiness.obtenerNotasDeCreditoEnVentasPorPeriodo( fechaInicio, fechaFin );
        List<VentasPorDia> lstNotasCredito = new ArrayList<VentasPorDia>();

        Integer totalFacturas = 0;
        BigDecimal totalVentas = BigDecimal.ZERO;
        BigDecimal totalVentasSinIva = BigDecimal.ZERO;
        BigDecimal totalVentasCanc = BigDecimal.ZERO;
        BigDecimal totalVentasCancSinIva = BigDecimal.ZERO;
        BigDecimal totalNotasCredito = BigDecimal.ZERO;
        BigDecimal totalNotasCreditoSinIva = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalSinIva = BigDecimal.ZERO;
        for ( VentasPorDia venta : lstVentas ) {
            if(venta.getMontoTotal().compareTo(BigDecimal.ZERO) > 0){
                totalFacturas = totalFacturas + 1;
                totalVentas = totalVentas.add( venta.getMontoTotal() );
                totalVentasSinIva = totalVentasSinIva.add( new BigDecimal( venta.getMontoSinIva() ) );
            }
        }

        for( VentasPorDia cancelaciones : lstVentasCanc ){
            totalFacturas = totalFacturas-1;
            totalVentasCanc = totalVentasCanc.add( cancelaciones.getMontoTotal() );
            totalVentasCancSinIva = totalVentasCancSinIva.add( new BigDecimal( cancelaciones.getMontoSinIva() ) );
        }

        /*for( VentasPorDia notaCredito : lstNotasCredito ){
            totalNotasCredito = totalNotasCredito.add( notaCredito.getMontoTotal() );
            totalNotasCreditoSinIva = totalNotasCreditoSinIva.add( new BigDecimal( notaCredito.getMontoSinIva() ) );
            if( notaCredito.getEsNotaCredito() ){
                totalFacturas = totalFacturas-1;
            }
        }*/
        total = totalVentas.subtract(totalVentasCanc.abs().add(totalNotasCredito.abs()));
        totalSinIva = totalVentasSinIva.subtract(totalVentasCancSinIva.abs().add(totalNotasCreditoSinIva.abs()));
        BigDecimal promedioVentas = total.divide( new BigDecimal(totalFacturas), 10, BigDecimal.ROUND_CEILING );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstVentas", lstVentas );
        parametros.put( "lstVentasCanc", lstVentasCanc );
        parametros.put( "lstNotasCredito", lstNotasCredito );
        parametros.put( "ivaTasa", ivaTasa );
        parametros.put( "totalFacturas", totalFacturas );
        parametros.put( "totalVentas", total );
        parametros.put( "totalVentasSinIva", totalSinIva );
        parametros.put( "promedioVentas", promedioVentas );
        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteFacturasFiscales( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteFacturasFiscales()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Facturas-Fiscales.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( FACTURAS_FISCALES );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        Sucursal sucursal = sucursalService.obtenSucursalActual();
        QNotaFactura factura = QNotaFactura.notaFactura;
        List<NotaFactura> lstFactura = ( List<NotaFactura> ) notaFacturaRepository.findAll( factura.fechaImpresion.between( fechaInicio, fechaFin ) );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstFactura", lstFactura );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );


        return null;
    }


    public String obtenerReporteDescuentos( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteDescuentos()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Descuentos.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( DESCUENTOS );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        Sucursal sucursal = sucursalService.obtenSucursalActual();
        List<DescuentosPorTipo> lstDescuentos = reportBusiness.obtenerDescuentosporTipo( fechaInicio, fechaFin );
        Integer totalDesc = lstDescuentos.size();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstDescuentos", lstDescuentos );
        parametros.put( "totalDesc", totalDesc );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReportePromocionesAplicadas( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReportePromociones()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Promociones-Aplicadas.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( PROMOCIONES_APLICADAS );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        List<PromocionesAplicadas> lstPromociones = reportBusiness.obtenerPromocionesAplicadas( fechaInicio, fechaFin );
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstPromociones", lstPromociones );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReportePagos( Date fechaInicio, Date fechaFin, String formaPago, String factura ) {
        log.info( "obtenerReportePagos()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Pagos.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( PAGOS );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        Sucursal sucursal = sucursalService.obtenSucursalActual();
        List<DescuentosPorTipo> lstPagos = reportBusiness.obtenerPagosporTipo( fechaInicio, fechaFin, formaPago, factura );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstPagos", lstPagos );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteCotizaciones( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteCotizaciones()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Cotizaciones.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( COTIZACIONES );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        Sucursal sucursal = sucursalService.obtenSucursalActual();

        QCotizacion cotizacion = QCotizacion.cotizacion;
        List<Cotizacion> lstCotizaciones = ( List<Cotizacion> ) cotizacionRepository.findAll( cotizacion.fechaMod.between( fechaInicio, fechaFin ) );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstCotizaciones", lstCotizaciones );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteExamenesResumido( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteExamenesResumido()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Examenes-Resumido.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( EXAMENES_RESUMIDO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        Sucursal sucursal = sucursalService.obtenSucursalActual();
        List<DescuentosPorTipo> lstExamenes = reportBusiness.obtenerExamenesporEmpleado( fechaInicio, fechaFin );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstExamenes", lstExamenes );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteExamenesCompleto( Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteExamenesCompleto()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Examenes-Completo.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( EXAMENES_COMPLETO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        Sucursal sucursal = sucursalService.obtenSucursalActual();
        List<DescuentosPorTipo> lstExamenes = reportBusiness.obtenerExamenesporEmpleado( fechaInicio, fechaFin );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstExamenes", lstExamenes );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteVentasporOptometrista( Date fechaInicio, Date fechaFin, boolean todoTipo, boolean referido, boolean rx,
                                                       boolean lux, boolean todaVenta, boolean primera, boolean mayor, boolean resumen ) {
        log.info( "obtenerReporteVentasporOptometrista()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-Por-Optometrista-Completo.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( VENTAS_POR_OPTOMETRISTA_COMPLETO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        Sucursal sucursal = sucursalService.obtenSucursalActual();
        List<IngresoPorVendedor> lstVentas = reportBusiness.obtenerVentasporOptometristaCompleto( fechaInicio, fechaFin,
                todoTipo, referido, rx, lux, todaVenta, primera, mayor, resumen );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstVentas", lstVentas );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteVentasporOptometristaResumido( Date fechaInicio, Date fechaFin, boolean todoTipo, boolean referido, boolean rx,
                                                               boolean lux, boolean todaVenta, boolean primera, boolean mayor, boolean resumen ) {
        log.info( "obtenerReporteVentasporOptometrista()" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-Por-Optometrista-Resumido.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( VENTAS_POR_OPTOMETRISTA_RESUMIDO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        Sucursal sucursal = sucursalService.obtenSucursalActual();
        List<IngresoPorVendedor> lstVentas = reportBusiness.obtenerVentasporOptometristaCompleto( fechaInicio, fechaFin,
                todoTipo, referido, rx, lux, todaVenta, primera, mayor, resumen );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstVentas", lstVentas );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }

    public String obtenerReportePromociones( Date fechaImpresion ) {
        log.info( "obtenerReportePromociones()" );
        log.info( "fecha::", fechaImpresion );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Promociones.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( PROMOCIONES );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        Sucursal sucursal = sucursalService.obtenSucursalActual();
        List<Promocion> lstPromociones = reportBusiness.obtenerPromociones( fechaImpresion );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstPromociones", lstPromociones );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        log.info( "reporte:{}", reporte );

        return null;
    }


    public String obtenerReporteDeKardex( Integer sku, Date fechaInicio, Date fechaFin ) {
        log.info( "obtenerReporteDeKardex" );

        File report = new File( System.getProperty( "java.io.tmpdir" ), "Kardex-Por-SKU.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( KARDEX );
        log.info( "Ruta:{}", report.getAbsolutePath() );
        fechaInicio = DateUtils.truncate( fechaInicio, Calendar.DAY_OF_MONTH );
        fechaFin = new Date( DateUtils.ceiling( fechaFin, Calendar.DAY_OF_MONTH ).getTime() - 1 );

        Sucursal sucursal = sucursalService.obtenSucursalActual();
        List<KardexPorArticulo> lstKardexTmp = reportBusiness.obtenerKardex( sku, fechaInicio, fechaFin );
        Collections.reverse( lstKardexTmp );
        List<KardexPorArticulo> lstKardex = new ArrayList<KardexPorArticulo>();
        Articulo articulo = articuloRepository.findOne( sku );
        Integer exisInicial = 0;
        Integer exisActual = 0;
        for ( KardexPorArticulo kardex : lstKardexTmp ) {
            if ( ( kardex.getFecha().after( fechaInicio ) || ( new Date( kardex.getFecha().getTime() ).equals( fechaInicio ) ) ) && ( kardex.getFecha().before( fechaFin ) || kardex.getFecha().equals( fechaFin ) ) ) {
                lstKardex.add( kardex );
            }
        }

        if ( lstKardex.size() > 0 ) {
          exisInicial = lstKardex.get( 0 ).getSaldoInicio();
          exisActual = articulo.getCantExistencia();
        } else {
          exisInicial = articulo.getCantExistencia();
          exisActual = articulo.getCantExistencia();
        }

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "articuloSku", articulo.getId() );
        parametros.put( "articuloArticulo", articulo.getArticulo() );
        parametros.put( "articuloDescripcion", articulo.getDescripcion() );
        parametros.put( "articuloPrecio", articulo.getPrecio() );
        parametros.put( "lstKardex", lstKardex );
        parametros.put( "existenciaInicial", exisInicial );
        parametros.put( "existenciaActual", exisActual );

        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        return null;
    }


    public String obtenerReporteDeVentasDelDiaActual( Date fechaVentas, Boolean artPrecioMayorCero ) {
        log.debug( "obtenerReporteDeVentasDelDiaActual()" );
        File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-del-dia.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( VENTAS_DEL_DIA );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        Date fechaInicio = DateUtils.truncate( fechaVentas, Calendar.DAY_OF_MONTH );
        Date fechaFin = new Date( DateUtils.ceiling( fechaVentas, Calendar.DAY_OF_MONTH ).getTime() - 1 );
        Sucursal sucursal = sucursalService.obtenSucursalActual();
        List<VentasPorDia> lstVentas = reportBusiness.obtenerVentasDelDiaActual( fechaInicio, fechaFin, artPrecioMayorCero );
        List<VentasPorDia> lstCancelaciones = reportBusiness.obtenerCancelacionesDelDiaActual( fechaInicio, fechaFin, artPrecioMayorCero );
        List<VentasPorDia> lstVentasGen = reportBusiness.obtenerVentasDelDiaActualPorGenerico( fechaInicio, fechaFin, artPrecioMayorCero );
        //List<VentasPorDia> lstNotasCredito = reportBusiness.obtenerNotasDeCreditoEnVentasDelDiaActual( fechaInicio, fechaFin, artPrecioMayorCero );
        List<VentasPorDia> lstNotasCredito = new ArrayList<VentasPorDia>();
        Integer totalArticulos = 0;
        Integer totalFacturas = 0;
        BigDecimal totalMonto = BigDecimal.ZERO;
        BigDecimal totalDescuento = BigDecimal.ZERO;
        BigDecimal totalMontoConDescuento = BigDecimal.ZERO;
        Integer totalCanArticulos = 0;
        Integer totalCanFacturas = 0;
        BigDecimal totalCanMonto = BigDecimal.ZERO;
        BigDecimal totalCanDescuento = BigDecimal.ZERO;
        BigDecimal totalCanMontoConDescuento = BigDecimal.ZERO;
        BigDecimal totalNotaCreditoMonto = BigDecimal.ZERO;
        BigDecimal totalNotaCreditoMontoConDescuento = BigDecimal.ZERO;
        for ( VentasPorDia venta : lstVentas ) {
            if( venta.getMontoConDescuento().compareTo(BigDecimal.ZERO) > 0 ){
                totalFacturas = totalFacturas + 1;
                totalArticulos = totalArticulos + ( venta.getContadorArt() );
                totalMonto = totalMonto.add( venta.getMontoTotal() );
                totalDescuento = totalDescuento.add( venta.getMontoDescuento() );
                totalMontoConDescuento = totalMontoConDescuento.add( venta.getMontoConDescuento() );
            }
        }
        for ( VentasPorDia cancelaciones : lstCancelaciones ) {
            totalCanFacturas = totalCanFacturas + 1;
            totalCanArticulos = totalCanArticulos + ( cancelaciones.getContadorArtNeg() );
            totalCanMonto = totalCanMonto.add( cancelaciones.getMontoTotalCancelado() );
            totalCanMontoConDescuento = totalCanMontoConDescuento.add( cancelaciones.getMontoConDescCancelado() );
            totalCanDescuento = totalCanDescuento.add( cancelaciones.getMontoTotalDescuentoCan() );
        }
        for( VentasPorDia notasCredito : lstNotasCredito ){
            totalNotaCreditoMonto = totalNotaCreditoMonto.add( notasCredito.getMontoTotal() );
            totalNotaCreditoMontoConDescuento = totalNotaCreditoMontoConDescuento.add( notasCredito.getMontoConDescuento() );
            if( notasCredito.getEsNotaCredito() ){
                totalFacturas = totalFacturas-1;
            }
        }
        totalArticulos = totalArticulos - totalCanArticulos;
        totalMonto = totalMonto.subtract( totalCanMonto.add(totalNotaCreditoMonto) );
        totalMontoConDescuento = totalMontoConDescuento.subtract( totalCanMontoConDescuento.add(totalNotaCreditoMontoConDescuento) );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaVentas ) );
        parametros.put( "horaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstVentas", lstVentas );
        parametros.put( "totalArticulos", totalArticulos );
        parametros.put( "totalFacturas", totalFacturas );
        parametros.put( "totalMonto", totalMonto );
        parametros.put( "totalDescuento", totalDescuento );
        parametros.put( "totalMontoConDescuento", totalMontoConDescuento );
        parametros.put( "totalCanMontoConDescuento", totalCanMontoConDescuento );
        parametros.put( "totalNotaCreditoMonto", totalNotaCreditoMontoConDescuento );
        parametros.put( "lstCancelaciones", lstCancelaciones );
        parametros.put( "lstVentasGen", lstVentasGen );
        parametros.put( "lstNotasCredito", lstNotasCredito );


        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        return null;
    }


    public String obtenerReporteDeIngresosPorPeriodo( Date dateStart, Date dateEnd ) {
        log.debug( "obtenerReporteDeIngresosPorPeriodo()" );
        File report = new File( System.getProperty( "java.io.tmpdir" ), "Ventas-del-dia.html" );
        org.springframework.core.io.Resource template = new ClassPathResource( INGRESOS_POR_PERIODO );
        log.info( "Ruta:{}", report.getAbsolutePath() );

        Date fechaInicio = DateUtils.truncate( dateStart, Calendar.DAY_OF_MONTH );
        Date fechaFin = new Date( DateUtils.ceiling( dateEnd, Calendar.DAY_OF_MONTH ).getTime() - 1 );
        Sucursal sucursal = sucursalService.obtenSucursalActual();

        List<IngresoPorDia> lstIngresos = reportBusiness.obtenerPagosPorPeriodo( fechaInicio, fechaFin );

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put( "fechaActual", new SimpleDateFormat( "hh:mm" ).format( new Date() ) );
        parametros.put( "fechaInicio", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaInicio ) );
        parametros.put( "fechaFin", new SimpleDateFormat( "dd/MM/yyyy" ).format( fechaFin ) );
        parametros.put( "sucursal", sucursal.getNombre() );
        parametros.put( "lstIngresos", lstIngresos );


        String reporte = reportBusiness.CompilayGeneraReporte( template, parametros, report );
        return null;
    }

}
