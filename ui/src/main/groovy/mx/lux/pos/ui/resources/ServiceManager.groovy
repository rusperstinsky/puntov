package mx.lux.pos.ui.resources

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mx.lux.pos.service.*

@Component
class ServiceManager {

  private static EmpleadoService employeeService
  private static InventarioService invService
  private static NotaVentaService orderService
  private static ArticuloService partService
  private static PromotionService promotionService
  private static TicketService ticketEngine
  private static MonedaExtranjeraService fxRateService
  private static OpenSalesDayService salesDaysLog
  private static IOService ioServices
  private static ConvenioService convenioService
  private static CotizacionService cotizacionService
  private static SettingsService settingsService
  private static SucursalService sucursalService

  @Autowired
  ServiceManager( InventarioService pInventarioService, ArticuloService pArticuloService,
                  EmpleadoService pEmpMaster, TicketService pTicketEngine, NotaVentaService pNotaVentaService,
                  PromotionService pPromotionService, MonedaExtranjeraService pMonedaExtranjeraService,
                  OpenSalesDayService pOpenSalesDaysService, IOService pIOService,
                  ConvenioService pConvenioService, CotizacionService pCotizacionService,
                  SettingsService pSettingsService, SucursalService pSucursalService
  ) {
    employeeService = pEmpMaster
    invService = pInventarioService
    orderService = pNotaVentaService
    partService = pArticuloService
    promotionService = pPromotionService
    ticketEngine = pTicketEngine
    fxRateService = pMonedaExtranjeraService
    salesDaysLog = pOpenSalesDaysService
    ioServices = pIOService
    convenioService = pConvenioService
    cotizacionService = pCotizacionService
    settingsService = pSettingsService
    sucursalService = pSucursalService
  }

  static EmpleadoService getEmployeeService( ) {
    return employeeService
  }

  static InventarioService getInventoryService( ) {
    return invService
  }

  static NotaVentaService getOrderService( ) {
    return orderService
  }

  static ArticuloService getPartService( ) {
    return partService
  }

  static PromotionService getPromotionService( ) {
    return promotionService
  }

  static TicketService getTicketService( ) {
    return ticketEngine
  }

  static MonedaExtranjeraService getFxRateService( ) {
    return fxRateService
  }

  static OpenSalesDayService getSalesDayLog( ) {
    return salesDaysLog
  }

  static IOService getIoServices( ) {
    return ioServices
  }

  static ConvenioService getAgreementService( ) {
    return convenioService
  }

  static CotizacionService getRegistrarCotizacion( ) {
    return cotizacionService
  }

  static CotizacionService getQuote() {
    return cotizacionService
  }

  static SettingsService getSettingsService( ) {
    return settingsService
  }

  static SucursalService getStoreService( ) {
      return sucursalService
  }

}
