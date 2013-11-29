package mx.lux.pos.ui.controller

import mx.lux.pos.model.Terminal as CoreTerminal
import mx.lux.pos.model.Plan as CorePlan

import groovy.util.logging.Slf4j
import mx.lux.pos.model.BancoEmisor
import mx.lux.pos.model.Pago
import mx.lux.pos.model.Mensaje
import mx.lux.pos.model.TipoPago
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mx.lux.pos.service.*
import mx.lux.pos.ui.model.*
import mx.lux.pos.model.MensajesPorParametro

@Slf4j
@Component
class PaymentController {

  private static PagoService pagoService
  private static TipoPagoService tipoPagoService
  private static BancoService bancoService
  private static TerminalService terminalService
  private static PlanService planService
  private static MensajeService mensajeService

  @Autowired
  PaymentController(
      PagoService pagoService,
      TipoPagoService tipoPagoService,
      BancoService bancoService,
      TerminalService terminalService,
      PlanService planService,
      MensajeService mensajeService
  ) {
    this.pagoService = pagoService
    this.tipoPagoService = tipoPagoService
    this.bancoService = bancoService
    this.terminalService = terminalService
    this.planService = planService
    this.mensajeService = mensajeService
  }

  static PaymentType findDefaultPaymentType( ) {
    log.info( "obteniendo tipo de pago por defecto" )
    TipoPago result = tipoPagoService.obtenerTipoPagoPorDefecto()
    PaymentType.toPaymentType( result )
  }

  static List<PaymentType> findActivePaymentTypes( ) {
    log.info( "obteniendo tipos de pago activos" )
    List<TipoPago> results = tipoPagoService.listarTiposPagoActivos()
    results?.collect { TipoPago tipoPago ->
      PaymentType.toPaymentType( tipoPago )
    }
  }

  static List<Bank> findIssuingBanks( ) {
    log.info( "obteniendo bancos emisores" )
    List<BancoEmisor> results = bancoService.listarBancosEmisores()
    results?.collect { BancoEmisor banco ->
      Bank.toBank( banco )
    }
  }

  static List<Terminal> findTerminals( ) {
    log.info( "obteniendo terminales" )
    List<CoreTerminal> results = terminalService.listarTerminales()
    results?.collect { CoreTerminal terminal ->
      Terminal.toTerminal( terminal )
    }
  }

  static List<Plan> findPlansByTerminal( String terminalId ) {
    log.info( "obteniendo planes por terminal: ${terminalId}" )
    List<CorePlan> results = planService.listarPlanesPorTerminal( terminalId )
    results?.collect { CorePlan plan ->
      Plan.toPlan( plan )
    }
  }

  static List<Payment> findPaymentsByOrderId( String orderId ) {
    log.info( "obteniendo pagos por orden id: ${orderId}" )
    List<Pago> results = pagoService.listarPagosPorIdFactura( orderId )
    if ( results?.any() ) {
      return results.collect { Pago pago ->
        Payment.toPaymment( pago )
      }
    }
    return [ ]
  }

  static String obtenerMensaje( String clave ){
    log.debug( 'obtenerMensaje( clave )' )
    return mensajeService.obtenerMensajePorClave( clave )
  }

  static String obtenerMensaje( MensajesPorParametro mensaje ){
    log.debug( 'obtenerMensaje( mensaje )' )
    String clave = mensaje.clave
    return mensajeService.obtenerMensajePorClave( clave )
  }


  static Boolean findTypePaymentsDollar( String formaPago ){
    log.debug( 'findTypePaymentsDollar( )' )
    return pagoService.obtenerTipoPagosDolares( formaPago )
  }

  static String findDefaultPlanCreditCard(){
      log.debug( 'findDefaultPlanCreditCard( )' )
      return pagoService.obtenerPlanNormalTarjetaCredito()
  }

  static List<Terminal> findActiveTerminals( String idTipoPago ) {
      log.info( "obteniendo terminales Activas para la terminal" )
      List<CoreTerminal> results = terminalService.listarTerminalesActivas( idTipoPago )
      results?.collect { CoreTerminal terminal ->
          Terminal.toTerminal( terminal )
      }
  }

}