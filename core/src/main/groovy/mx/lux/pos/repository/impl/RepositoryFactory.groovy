package mx.lux.pos.repository.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mx.lux.pos.repository.*

@Component
class RepositoryFactory {

  private static ClienteRepository customerCatalog
  private static DescuentoRepository discounts
  private static GenericoRepository genres
  private static GrupoArticuloRepository groupPartMaster
  private static GrupoArticuloDetRepository groupPartDetail
  private static TransInvRepository inventoryMaster
  private static TransInvDetalleRepository inventoryDetail
  private static TipoTransInvRepository trTypes
  private static NotaVentaRepository orders
  private static DetalleNotaVentaRepository orderLines
  private static OrdenPromDetRepository orderLinePromotionDetail
  private static OrdenPromRepository orderPromotionDetail
  private static ArticuloRepository partMaster
  private static PagoRepository payments
  private static PromocionRepository promotionCatalog
  private static PrecioRepository priceCatalog
  private static ParametroRepository registry
  private static ImpuestoRepository taxMaster
  private static ContribuyenteRepository rfcMaster
  private static EstadoRepository states
  private static MonedaRepository currencyCatalog
  private static MonedaDetalleRepository fxRates
  private static TerminalRepository posCatalog
  private static ModificacionRepository orderModifications
  private static CierreDiarioRepository dailyClose
  private static MensajeRepository msgCatalog
  private static EmpleadoRepository employeeCatalog
  private static AcuseRepository acknowledgements
  private static ModificacionRepository adjustments
  private static CotizacionRepository quotes
  private static CotizaDetRepository quoteDetail
  private static TipoTransInvRepository typeTransaction

  @Autowired
  RepositoryFactory( ArticuloRepository pArticuloRepository,
                     ClienteRepository pClienteRepository,
                     PrecioRepository pPrecioRepository,
                     NotaVentaRepository pNotaVentaRepository,
                     DetalleNotaVentaRepository pDetalleNotaVentaRepository,
                     PromocionRepository pPromocionRepository,
                     GrupoArticuloRepository pGrupoArticuloRepository,
                     GrupoArticuloDetRepository pGrupoArticuloDetRepository,
                     OrdenPromRepository pOrdenPromRepository,
                     OrdenPromDetRepository pOrdenPromDetRepository,
                     PagoRepository pPagoRepository,
                     ParametroRepository pParametroRepository,
                     DescuentoRepository pDescuentoRepository,
                     GenericoRepository pGenericoRepository,
                     TransInvRepository pTransInvRepository,
                     TransInvDetalleRepository pTransInvDetalleRepository,
                     TipoTransInvRepository pTipoTransInvRepository,
                     ImpuestoRepository pImpuestoRepository,
                     ContribuyenteRepository pContribuyenteRepository,
                     EstadoRepository pEstadoRepository,
                     MonedaRepository pMonedaRepository,
                     MonedaDetalleRepository pMonedaDetalleRepository,
                     TerminalRepository pTerminalRepository,
                     ModificacionRepository pModificationRepository,
                     CierreDiarioRepository pCierreDiarioRepository,
                     MensajeRepository pMensajeRepository,
                     EmpleadoRepository pEmpleadoRepository,
                     AcuseRepository pAcuseRepository,
                     ModificacionRepository pModificacionRepository,
        CotizacionRepository pCotizacionRepository, CotizaDetRepository pCotizaDetRepository,
        TipoTransInvRepository pTypeTransaction
  ) {
    customerCatalog = pClienteRepository
    discounts = pDescuentoRepository
    groupPartMaster = pGrupoArticuloRepository
    groupPartDetail = pGrupoArticuloDetRepository
    orderLines = pDetalleNotaVentaRepository
    orders = pNotaVentaRepository
    partMaster = pArticuloRepository
    priceCatalog = pPrecioRepository
    promotionCatalog = pPromocionRepository
    orderPromotionDetail = pOrdenPromRepository
    orderLinePromotionDetail = pOrdenPromDetRepository
    payments = pPagoRepository
    registry = pParametroRepository
    genres = pGenericoRepository
    inventoryMaster = pTransInvRepository
    inventoryDetail = pTransInvDetalleRepository
    trTypes = pTipoTransInvRepository
    taxMaster = pImpuestoRepository
    rfcMaster = pContribuyenteRepository
    states = pEstadoRepository
    currencyCatalog = pMonedaRepository
    fxRates = pMonedaDetalleRepository
    posCatalog = pTerminalRepository
    orderModifications = pModificationRepository
    dailyClose = pCierreDiarioRepository
    msgCatalog = pMensajeRepository
    employeeCatalog = pEmpleadoRepository
    acknowledgements = pAcuseRepository
    adjustments = pModificacionRepository
    quotes = pCotizacionRepository
    quoteDetail = pCotizaDetRepository
    typeTransaction = pTypeTransaction
  }

  static ClienteRepository getCustomerCatalog( ) {
    return customerCatalog
  }


  static DescuentoRepository getDiscounts( ) {
    return discounts
  }

  static GenericoRepository getGenres( ) {
    return genres
  }

  static GrupoArticuloRepository getGroupPartMaster( ) {
    return groupPartMaster
  }

  static GrupoArticuloDetRepository getGroupPartDetail( ) {
    return groupPartDetail
  }

  static TransInvDetalleRepository getInventoryDetail( ) {
    return inventoryDetail
  }

  static TransInvRepository getInventoryMaster( ) {
    return inventoryMaster
  }

  static TipoTransInvRepository getTrTypes( ) {
    return trTypes
  }

  static DetalleNotaVentaRepository getOrderLines( ) {
    return orderLines
  }

  static NotaVentaRepository getOrders( ) {
    return orders
  }

  static OrdenPromRepository getOrderPromotionDetail( ) {
    return orderPromotionDetail
  }

  static OrdenPromDetRepository getOrderLinePromotionDetail( ) {
    return orderLinePromotionDetail
  }

  static ArticuloRepository getPartMaster( ) {
    return partMaster
  }

  static PagoRepository getPayments( ) {
    return payments
  }

  static PrecioRepository getPriceCatalog( ) {
    return priceCatalog
  }

  static PromocionRepository getPromotionCatalog( ) {
    return promotionCatalog
  }

  static ParametroRepository getRegistry( ) {
    return registry
  }

  static ImpuestoRepository getTaxMaster( ) {
    return taxMaster
  }

  static ContribuyenteRepository getRfcMaster( ) {
    return rfcMaster
  }

  static EstadoRepository getStates( ) {
    return states
  }

  static MonedaRepository getCurrencyCatalog( ) {
    return currencyCatalog
  }

  static MonedaDetalleRepository getFxRates( ) {
    return fxRates
  }

  static TerminalRepository getPosCatalog( ) {
    return posCatalog
  }

  static ModificacionRepository getOrderModifications( ) {
    return orderModifications
  }

  static CierreDiarioRepository getDailyClose( ) {
    return dailyClose
  }

  static MensajeRepository getMessageCatalog( ) {
    return msgCatalog
  }

  static EmpleadoRepository getEmployeeCatalog( ) {
    return employeeCatalog
  }

  static AcuseRepository getAcknowledgements( ) {
    return acknowledgements
  }

  static ModificacionRepository getAdjustments( ) {
    return adjustments
  }

  static CotizacionRepository getQuotes() {
    return quotes
  }

  static CotizaDetRepository getQuoteDetail() {
    return quoteDetail
  }

  static TipoTransInvRepository getTypeTransaction(){
      return typeTransaction
  }

}
