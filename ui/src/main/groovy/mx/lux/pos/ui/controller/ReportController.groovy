package mx.lux.pos.ui.controller

import groovy.util.logging.Slf4j
import mx.lux.pos.service.ReportService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mx.lux.pos.ui.view.dialog.*
import org.apache.commons.lang.time.DateUtils
import java.text.SimpleDateFormat
import java.text.DateFormat
import org.apache.commons.lang3.time.DateUtils
import java.text.NumberFormat
import org.apache.commons.lang.StringUtils

@Slf4j
@Component
class ReportController {

  private static ReportService reportService
  private static DateSelectionDialog dateDialog
  private static TwoDatesSelectionDialog twoDateDialog
  private static TwoDatesSelectionFilterDialog twoDateFilterDialog
  private static TwoDatesSelectionFilterLineDialog twoDatesSelectionFilterLineDialog
  private static TwoDatesSelectionFilterBrandDialog twoDatesSelectionFilterBrandDialog
  private static TwoDatesSelectionFilterBrandArticleDialog twoDatesSelectionFilterBrandArticleDialog
  private static ArticleSelectionFilterDialog articleSelectionFilterDialog
  private static ArticleAndColorSelectionFilterDialog articleAndColorSelectionFilterDialog
  private static FilterDialog filterDialog
  private static TwoDatesSelectionFilterbyEmployeeDialog twoDatesSelectionFilterbyEmployeeDialog
  private static TwoDatesSelectionFilterPaymentsDialog twoDatesSelectionFilterPaymentsDialog
  private static TwoDatesSelectionRadioFilterDialog twoDatesSelectionRadioFilterDialog
  private static KardexReportDialog kardexReportDialog
  private static DateSelectionCheckBoxDialog salesForDayDialog

  static enum Report {
    DailyClose, IncomePerBranch, SellerRevenue, Sales,
    SalesbySeller, UndeliveredJobs, Cancellations,
    SalesbyLine, SalesbyBrand, SalesbySellerbyBrand,
    StockbyBrand, StockbyBrandColor, JobControl,
    WorkSubmitted, TaxBills, Discounts, PromotionsinSales,
    Payments, Quote, Exams, OptometristSales,
    Promotions, Kardex, SalesToday, PaymentsbyPeriod
  }

  @Autowired
  ReportController( ReportService reportService ) {
    this.reportService = reportService
  }

  // Internal Methods
  static void fireDailyCloseReport( ) {
    if ( dateDialog == null ) {
      dateDialog = new DateSelectionDialog()
    }
    dateDialog.setTitle( "Reporte de Cierre Diario" )
    dateDialog.activate()
    Date reportForDate = dateDialog.getSelectedDate()
    if ( reportForDate != null && dateDialog.button ) {
      log.debug( "Imprime el reporte de Cierre Diario" )
      reportService.obtenerReporteCierreDiario( reportForDate )
    } else {
      log.debug( "Cancelar y continuar" )
    }
  }

  static void fireIncomePerBranchReport( ) {
    if ( twoDateDialog == null ) {
      twoDateDialog = new TwoDatesSelectionDialog()
    }
    twoDateDialog.setTitle( "Reporte de Ingresos por Sucursal" )
    twoDateDialog.activate()
    Date reportForDateStart = twoDateDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDateDialog.getSelectedDateEnd()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDateDialog.button ) {
      log.debug( "Imprime el reporte de Ingresos por Sucursal" )
      reportService.obtenerReporteIngresosXSucursal( reportForDateStart, reportForDateEnd )
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireSellerRevenueReport( ) {
    if ( twoDateFilterDialog == null ) {
      twoDateFilterDialog = new TwoDatesSelectionFilterDialog()
    }
    twoDateFilterDialog.setTitle( "Reporte de Ingresos por Vendedor" )
    twoDateFilterDialog.activate()
    Date reportForDateStart = twoDateFilterDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDateFilterDialog.getSelectedDateEnd()
    boolean resumen = twoDateFilterDialog.getCbResume()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDateFilterDialog.button ) {
      if ( resumen == true ) {
        log.debug( "Imprime el reporte de Ingresos por Vendedor Resumido" )
        reportService.obtenerReporteIngresosXVendedorResumido( reportForDateStart, reportForDateEnd )
      }
      if ( resumen == false ) {
        reportService.obtenerReporteIngresosXVendedorCompleto( reportForDateStart, reportForDateEnd )
        log.debug( "Imprime el reporte de Ingresos por Vendedor Completo" )
      }
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireSalesReport( ) {
    if ( twoDateDialog == null ) {
      twoDateDialog = new TwoDatesSelectionDialog()
    }
    twoDateDialog.setTitle( "Reporte de Ventas" )
    twoDateDialog.activate()
    Date reportForDateStart = twoDateDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDateDialog.getSelectedDateEnd()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDateDialog.button ) {
      log.debug( "Imprime el reporte de Ventas Completo" )
      reportService.obtenerReporteVentasCompleto( reportForDateStart, reportForDateEnd )
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireSalesbySellerReport( ) {
    if ( twoDateFilterDialog == null ) {
      twoDateFilterDialog = new TwoDatesSelectionFilterDialog()
    }
    twoDateFilterDialog.setTitle( "Reporte de Ventas por Vendedor" )
    twoDateFilterDialog.activate()
    Date reportForDateStart = twoDateFilterDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDateFilterDialog.getSelectedDateEnd()
    boolean resumen = twoDateFilterDialog.getCbResume()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDateFilterDialog.button ) {
      if ( resumen == true ) {
        log.debug( "Imprime el reporte de Ventas por Vendedor Resumido" )
        reportService.obtenerReporteVentasporVendedorResumido( reportForDateStart, reportForDateEnd )
      }
      if ( resumen == false ) {
        reportService.obtenerReporteVentasporVendedorCompleto( reportForDateStart, reportForDateEnd )
        log.debug( "Imprime el reporte de Ventas por Vendedor Completo" )
      }
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireUndeliveredJobsReport( ) {
    log.debug( "Imprime el reporte de Trabajos sin Entregar" )
    reportService.obtenerReporteTrabajosSinEntregar()
  }

  static void fireCancellationsReport( ) {
    if ( twoDateFilterDialog == null ) {
      twoDateFilterDialog = new TwoDatesSelectionFilterDialog()
    }
    twoDateFilterDialog.setTitle( "Reporte de Cancelaciones" )
    twoDateFilterDialog.activate()

    Date reportForDateStart = twoDateFilterDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDateFilterDialog.getSelectedDateEnd()
    boolean resumen = twoDateFilterDialog.getCbResume()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDateFilterDialog.button ) {
      if ( resumen ) {
        log.debug( "Imprime el reporte de Cancelaciones Resumido" )
        reportService.obtenerReporteCancelacionesResumido( reportForDateStart, reportForDateEnd )
      } else {
        reportService.obtenerReporteCancelacionesCompleto( reportForDateStart, reportForDateEnd )
        log.debug( "Imprime el reporte de Cancelaciones Completo" )
      }
    } else {
      log.debug( "Cancelar_continuar" )
    }

  }

  static void fireSalesbyLineReport( ) {
    if ( twoDatesSelectionFilterLineDialog == null ) {
      twoDatesSelectionFilterLineDialog = new TwoDatesSelectionFilterLineDialog()
    }
    twoDatesSelectionFilterLineDialog.setTitle( "Reporte de Ventas por Linea" )
    twoDatesSelectionFilterLineDialog.activate()
    String articuloDesc = twoDatesSelectionFilterLineDialog.getselectedArticle()
    Date reportForDateStart = twoDatesSelectionFilterLineDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDatesSelectionFilterLineDialog.getSelectedDateEnd()
    boolean articulo = twoDatesSelectionFilterLineDialog.getCbArticulo()
    boolean Factura = twoDatesSelectionFilterLineDialog.getCbFactura()
    boolean gogle = twoDatesSelectionFilterLineDialog.getCbGogle()
    boolean oftalmico = twoDatesSelectionFilterLineDialog.getCbOftalmico()
    boolean todo = twoDatesSelectionFilterLineDialog.getCbTodo()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDatesSelectionFilterLineDialog.button ) {
      if ( articulo == true ) {
        log.debug( "Imprime el reporte de Ventas por Linea por Articulo" )
        reportService.obtenerReporteVentasporLineaArticulo( reportForDateStart, reportForDateEnd, articuloDesc, gogle, oftalmico, todo )
      } else {
        log.debug( "Imprime el reporte de Ventas por Linea por Factura" )
        reportService.obtenerReporteVentasporLineaFactura( reportForDateStart, reportForDateEnd, articuloDesc, gogle, oftalmico, todo )
      }

    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireSalesbyBrandReport( ) {
    if ( twoDatesSelectionFilterBrandArticleDialog == null ) {
      twoDatesSelectionFilterBrandArticleDialog = new TwoDatesSelectionFilterBrandArticleDialog( true )
    }
    twoDatesSelectionFilterBrandArticleDialog.setTitle( "Reporte de Ventas por Marca" )
    twoDatesSelectionFilterBrandArticleDialog.activate()
    String articuloDesc = twoDatesSelectionFilterBrandArticleDialog.getselectedArticle()
    Date reportForDateStart = twoDatesSelectionFilterBrandArticleDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDatesSelectionFilterBrandArticleDialog.getSelectedDateEnd()
    boolean articulos = twoDatesSelectionFilterBrandArticleDialog.getCbArticulos()
    boolean orderByBrand = twoDatesSelectionFilterBrandArticleDialog.marca
    boolean orderByAmount = twoDatesSelectionFilterBrandArticleDialog.importe

    boolean artTodos = twoDatesSelectionFilterBrandArticleDialog.todo
    boolean artAccesorios = twoDatesSelectionFilterBrandArticleDialog.accesorios
    boolean artArmazones = twoDatesSelectionFilterBrandArticleDialog.armazon

    if ( reportForDateStart != null && reportForDateEnd != null && twoDatesSelectionFilterBrandArticleDialog.button ) {
      log.debug( "Imprime el reporte de Ventas por Marca" )
      reportService.obtenerReporteVentasMarca( reportForDateStart, reportForDateEnd, articuloDesc, articulos,
              orderByBrand, orderByAmount, artTodos, artAccesorios, artArmazones )
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireSalesbySellerbyBrandReport( ) {
    if ( twoDatesSelectionFilterBrandDialog == null ) {
        twoDatesSelectionFilterBrandDialog = new TwoDatesSelectionFilterBrandDialog( )
    }
      twoDatesSelectionFilterBrandDialog.setTitle( "Reporte de Ventas por Vendedor por Marca" )
      twoDatesSelectionFilterBrandDialog.activate()
    String articuloDesc = twoDatesSelectionFilterBrandDialog.getselectedArticle()
    Date reportForDateStart = twoDatesSelectionFilterBrandDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDatesSelectionFilterBrandDialog.getSelectedDateEnd()
    boolean solar = false
    boolean oftalmico = false
    boolean todo = true
    if ( reportForDateStart != null && reportForDateEnd != null && twoDatesSelectionFilterBrandDialog.button ) {
      log.debug( "Imprime el reporte de Ventas por Vendedor por Marca" )
      reportService.obtenerReporteVentasVendedorporMarca( reportForDateStart, reportForDateEnd, articuloDesc, false, solar, oftalmico, todo )
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireStockbyBrandReport( ) {
    if ( articleSelectionFilterDialog == null ) {
      articleSelectionFilterDialog = new ArticleSelectionFilterDialog()
    }
    articleSelectionFilterDialog.setTitle( "Reporte de Existencias por Marca" )
    articleSelectionFilterDialog.activate()
    String articuloDesc = articleSelectionFilterDialog.getselectedArticle()
    boolean resumen = articleSelectionFilterDialog.getCbResume()
    boolean gogle = false//articleSelectionFilterDialog.getCbGogle()
    boolean oftalmico = false//articleSelectionFilterDialog.getCbOftalmico()
    boolean todo = true//articleSelectionFilterDialog.getCbTodo()
    if ( articleSelectionFilterDialog.button ) {
      if ( resumen == true ) {
        log.debug( "Imprime el reporte de Existencias por Marca Resumido" )
        reportService.obtenerReporteExistenciasporMarcaResumido( articuloDesc, gogle, oftalmico, todo )
      } else {
        log.debug( "Imprime el reporte de Existencias por Marca" )
        reportService.obtenerReporteExistenciasporMarca( articuloDesc, gogle, oftalmico, todo )
      }
    }
  }

  static void fireStockbyBrandColorReport( ) {
    if ( articleAndColorSelectionFilterDialog == null ) {
      articleAndColorSelectionFilterDialog = new ArticleAndColorSelectionFilterDialog()
    }
    articleAndColorSelectionFilterDialog.setTitle( "Reporte de Existencias por Articulo" )
    articleAndColorSelectionFilterDialog.activate()
    String articuloDesc = articleAndColorSelectionFilterDialog.getselectedArticle()
    String descripcion = articleAndColorSelectionFilterDialog.getselectedDescription()
    String colorDesc = articleAndColorSelectionFilterDialog.getselectedColor()
    if ( articleAndColorSelectionFilterDialog.button ) {
      log.debug( "Imprime el reporte de Existencias por Articulo" )
      reportService.obtenerReporteExistenciasporArticulo( articuloDesc, descripcion, colorDesc )
    }
  }

  static void fireJobControlReport( ) {
    if ( filterDialog == null ) {
      filterDialog = new FilterDialog()
    }
    filterDialog.setTitle( "Reporte de Control de Trabajos" )
    filterDialog.activate()
    boolean retenido = filterDialog.getRetenido()
    boolean porEnviar = filterDialog.getPorEnviar()
    boolean pino = filterDialog.getPino()
    boolean sucursal = filterDialog.getSucursal()
    boolean todos = filterDialog.getTodo()
    boolean factura = filterDialog.getFactura()
    boolean fecha = filterDialog.getFecha()
    if ( filterDialog.button ) {
      log.debug( "Imprime el reporte de Control de Trabajos" )
      reportService.obtenerReporteControldeTrabajos( retenido, porEnviar, pino, sucursal, todos, factura, fecha );
    }
  }

  static void fireWorkSubmittenReport( ) {
    if ( twoDatesSelectionFilterbyEmployeeDialog == null ) {
      twoDatesSelectionFilterbyEmployeeDialog = new TwoDatesSelectionFilterbyEmployeeDialog()
    }
    twoDatesSelectionFilterbyEmployeeDialog.setTitle( "Reporte de Trabajos Entregados" )
    twoDatesSelectionFilterbyEmployeeDialog.activate()
    Date reportForDateStart = twoDatesSelectionFilterbyEmployeeDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDatesSelectionFilterbyEmployeeDialog.getSelectedDateEnd()
    boolean resumen = twoDatesSelectionFilterbyEmployeeDialog.getCbResume()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDatesSelectionFilterbyEmployeeDialog.button ) {
      if ( resumen == true ) {
        log.debug( "Imprime el reporte de Trabajos Entregados por Empleado" )
        reportService.obtenerReporteTrabajosEntregadosporEmpleado( reportForDateStart, reportForDateEnd )
      }
      if ( resumen == false ) {
        log.debug( "Imprime el reporte de Trabajos Entregados sin Corte por Empleado" )
        reportService.obtenerReporteTrabajosEntregados( reportForDateStart, reportForDateEnd )
      }
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireTaxBillsReport( ) {
    if ( twoDateDialog == null ) {
      twoDateDialog = new TwoDatesSelectionDialog()
    }
    twoDateDialog.setTitle( "Facturas Fiscales" )
    twoDateDialog.activate()
    Date reportForDateStart = twoDateDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDateDialog.getSelectedDateEnd()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDateDialog.button ) {
      log.debug( "Imprime el reporte de Facturas Fiscales" )
      reportService.obtenerReporteFacturasFiscales( reportForDateStart, reportForDateEnd )
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireDiscountsReport( ) {
    if ( twoDateDialog == null ) {
      twoDateDialog = new TwoDatesSelectionDialog()
    }
    twoDateDialog.setTitle( "Descuentos" )
    twoDateDialog.activate()
    Date reportForDateStart = twoDateDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDateDialog.getSelectedDateEnd()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDateDialog.button ) {
      log.debug( "Imprime el reporte de Descuentos" )
      reportService.obtenerReporteDescuentos( reportForDateStart, reportForDateEnd )
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void firePromotionsReport( ) {
    if ( twoDateDialog == null ) {
      twoDateDialog = new TwoDatesSelectionDialog()
    }
    twoDateDialog.setTitle( "Promociones en Ventas" )
    twoDateDialog.activate()
    Date reportForDateStart = twoDateDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDateDialog.getSelectedDateEnd()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDateDialog.button ) {
      log.debug( "Imprime el reporte de Promociones" )
      reportService.obtenerReportePromocionesAplicadas( reportForDateStart, reportForDateEnd )
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void firePaymentsReport( ) {
    if ( twoDatesSelectionFilterPaymentsDialog == null ) {
      twoDatesSelectionFilterPaymentsDialog = new TwoDatesSelectionFilterPaymentsDialog()
    }
    twoDatesSelectionFilterPaymentsDialog.setTitle( "Pagos" )
    twoDatesSelectionFilterPaymentsDialog.activate()
    Date reportForDateStart = twoDatesSelectionFilterPaymentsDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDatesSelectionFilterPaymentsDialog.getSelectedDateEnd()
    String payment = twoDatesSelectionFilterPaymentsDialog.getSelectedPayment()
    String bill = twoDatesSelectionFilterPaymentsDialog.getSelectedBill()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDatesSelectionFilterPaymentsDialog.button ) {
      log.debug( "Imprime el reporte de Pagos" )
      reportService.obtenerReportePagos( reportForDateStart, reportForDateEnd, payment, bill )
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireQuoteReport( ) {
    if ( twoDateDialog == null ) {
      twoDateDialog = new TwoDatesSelectionDialog()
    }
    twoDateDialog.setTitle( "Cotizaciones" )
    twoDateDialog.activate()
    Date reportForDateStart = twoDateDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDateDialog.getSelectedDateEnd()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDateDialog.button ) {
      log.debug( "Imprime el reporte de Cotizaciones" )
      reportService.obtenerReporteCotizaciones( reportForDateStart, reportForDateEnd )
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireExamsReport( ) {
    if ( twoDateFilterDialog == null ) {
      twoDateFilterDialog = new TwoDatesSelectionFilterDialog()
    }
    twoDateFilterDialog.setTitle( "Reporte de Examenes" )
    twoDateFilterDialog.activate()
    Date reportForDateStart = twoDateFilterDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDateFilterDialog.getSelectedDateEnd()
    boolean resumen = twoDateFilterDialog.getCbResume()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDateFilterDialog.button ) {
      if ( resumen == true ) {
        log.debug( "Imprime el reporte de Examenes Resumido" )
        reportService.obtenerReporteExamenesResumido( reportForDateStart, reportForDateEnd )
      }
      if ( resumen == false ) {
        reportService.obtenerReporteExamenesCompleto( reportForDateStart, reportForDateEnd )
        log.debug( "Imprime el reporte de Examenes Completo" )
      }
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void fireOptometristSalesReport( ) {
    if ( twoDatesSelectionRadioFilterDialog == null ) {
      twoDatesSelectionRadioFilterDialog = new TwoDatesSelectionRadioFilterDialog()
    }
    twoDatesSelectionRadioFilterDialog.setTitle( "Reporte de Ventas por Optometrista" )
    twoDatesSelectionRadioFilterDialog.activate()
    Date reportForDateStart = twoDatesSelectionRadioFilterDialog.getSelectedDateStart()
    Date reportForDateEnd = twoDatesSelectionRadioFilterDialog.getSelectedDateEnd()
    boolean todoTipo = twoDatesSelectionRadioFilterDialog.getCbTodoTipo()
    boolean referidos = twoDatesSelectionRadioFilterDialog.getCbReferidos()
    boolean rx = twoDatesSelectionRadioFilterDialog.getCbRx()
    boolean lux = twoDatesSelectionRadioFilterDialog.getCbLux()
    boolean totalventas = twoDatesSelectionRadioFilterDialog.getCbTodoVentas()
    boolean primera = twoDatesSelectionRadioFilterDialog.getCbPrimeras()
    boolean mayor = twoDatesSelectionRadioFilterDialog.getCbMayor()
    boolean resumen = twoDatesSelectionRadioFilterDialog.getCbResumen()
    if ( reportForDateStart != null && reportForDateEnd != null && twoDatesSelectionRadioFilterDialog.button ) {
      if ( resumen ) {
        log.debug( "Imprime el reporte de Ventas por Optometrista Resumido" )
        reportService.obtenerReporteVentasporOptometristaResumido( reportForDateStart, reportForDateEnd, todoTipo, referidos, rx, lux, totalventas, primera, mayor, resumen )
      } else {
        log.debug( "Imprime el reporte de Ventas por Optometrista" )
        reportService.obtenerReporteVentasporOptometrista( reportForDateStart, reportForDateEnd, todoTipo, referidos, rx, lux, totalventas, primera, mayor, resumen )
      }
    } else {
      log.debug( "Cancelar_continuar" )
    }
  }

  static void firePromotionsListReport(){
    log.debug( "Imprime el reporte de promociones que se pueden aplicar" )

    Date fechaImpresion = DateUtils.truncate( new Date(), Calendar.DAY_OF_MONTH )
    reportService.obtenerReportePromociones( fechaImpresion )

  }

  static void kardexByDateAndSkuReport() {
    log.debug( 'Imprime el reporte de kardex por sku y fecha' )
    if ( kardexReportDialog == null ) {
      kardexReportDialog = new KardexReportDialog()
    }
    kardexReportDialog.setTitle( "Kardex Por Articulo" )
    kardexReportDialog.activate()
    String strSku =kardexReportDialog.getSku()
    Date reportForDateStart = kardexReportDialog.getSelectedDateStart()
    Date reportForDateEnd = kardexReportDialog.getSelectedDateEnd()
    if( StringUtils.trimToEmpty(strSku) != '' && reportForDateStart != null && reportForDateEnd != null ){
      Integer sku = NumberFormat.getInstance().parse( strSku )
      reportService.obtenerReporteDeKardex( sku, reportForDateStart, reportForDateEnd )
    }
  }

  static void todaySales(){
    log.debug( 'Imprime el reporte de ventas del dia' )
    if( salesForDayDialog == null ){
      salesForDayDialog = new DateSelectionCheckBoxDialog()
    }
    salesForDayDialog.setTitle( "Ventas Por Dia" )
    salesForDayDialog.activate()
    Date salesDate = salesForDayDialog.getSelectedDate()
    Boolean artPrecioMayorCero = salesForDayDialog.getCbArtCero()
    if( salesDate != null ){
      reportService.obtenerReporteDeVentasDelDiaActual( salesDate, artPrecioMayorCero )
    }
  }


  static void paymentsByPeriod(){
    log.debug( 'imprime el reporte de Ingresos por Preiodo' )
    if( twoDateDialog == null ){
      twoDateDialog = new TwoDatesSelectionDialog()
    }
    twoDateDialog.setTitle( 'Ingresos por Periodo' )
    twoDateDialog.activate()
    Date dateStart = twoDateDialog.getSelectedDateStart()
    Date dateEnd = twoDateDialog.getSelectedDateEnd()
    if( dateStart != null && dateEnd != null ){
      reportService.obtenerReporteDeIngresosPorPeriodo( dateStart, dateEnd )
    }
  }

  // Public Methods
  static void fireReport( Report pReport ) {
    switch ( pReport ) {
      case Report.DailyClose: fireDailyCloseReport(); break;
      case Report.IncomePerBranch: fireIncomePerBranchReport(); break;
      case Report.SellerRevenue: fireSellerRevenueReport(); break;
      case Report.Sales: fireSalesReport(); break;
      case Report.SalesbySeller: fireSalesbySellerReport(); break;
      case Report.UndeliveredJobs: fireUndeliveredJobsReport(); break;
      case Report.Cancellations: fireCancellationsReport(); break;
      case Report.SalesbyLine: fireSalesbyLineReport(); break;
      case Report.SalesbyBrand: fireSalesbyBrandReport(); break;
      case Report.SalesbySellerbyBrand: fireSalesbySellerbyBrandReport(); break;
      case Report.StockbyBrand: fireStockbyBrandReport(); break;
      case Report.StockbyBrandColor: fireStockbyBrandColorReport(); break;
      case Report.JobControl: fireJobControlReport(); break;
      case Report.WorkSubmitted: fireWorkSubmittenReport(); break;
      case Report.TaxBills: fireTaxBillsReport(); break;
      case Report.Discounts: fireDiscountsReport(); break;
      case Report.PromotionsinSales: firePromotionsReport(); break;
      case Report.Payments: firePaymentsReport(); break;
      case Report.Quote: fireQuoteReport(); break;
      case Report.Exams: fireExamsReport(); break;
      case Report.OptometristSales: fireOptometristSalesReport(); break;
      case Report.Promotions: firePromotionsListReport(); break;
      case Report.Kardex: kardexByDateAndSkuReport(); break;
      case Report.SalesToday: todaySales(); break;
      case Report.PaymentsbyPeriod: paymentsByPeriod(); break;
    }
  }
}
