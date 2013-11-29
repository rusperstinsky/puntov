package mx.lux.pos.ui.controller

import groovy.util.logging.Slf4j
import mx.lux.pos.service.business.Registry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mx.lux.pos.model.*
import mx.lux.pos.service.*
import mx.lux.pos.ui.model.*

import java.text.NumberFormat

@Slf4j
@Component
class DailyCloseController {

  private static CierreDiarioService cierreDiarioService
  private static TicketService ticketService
  private static EmpleadoService empleadoService
  private static BancoService bancoService
  private static PagoService pagoService
  private static PromotionService promotionService
  private static NotaVentaService notaVentaService

  @Autowired
  DailyCloseController(
      CierreDiarioService cierreDiarioService,
      TicketService ticketService,
      EmpleadoService empleadoService,
      BancoService bancoService,
      PagoService pagoService,
      PromotionService promotionService,
      NotaVentaService notaVentaService
  ) {
    this.cierreDiarioService = cierreDiarioService
    this.ticketService = ticketService
    this.empleadoService = empleadoService
    this.bancoService = bancoService
    this.pagoService = pagoService
    this.promotionService = promotionService
    this.notaVentaService = notaVentaService
  }

  static List<DailyClose> findWithStatusOpened( ) {
    log.info( "obteniendo lista de dias abiertos" )
    def results = cierreDiarioService.buscarConEstadoAbierto()
    results.collect {
      DailyClose.toDailyClose( it )
    }
  }

  static DailyClose findByDate( Date date ) {
    log.info( "obteniendo dia por fecha: ${date.format( 'dd-MM-yyyy' )}" )
    CierreDiario cierreDiario = cierreDiarioService.buscarPorFecha( date )
    DailyClose.toDailyClose( cierreDiario )
  }

  static List<Deposit> findDepositsByDay( Date date ) {
    List<Deposito> results = cierreDiarioService.buscarDepositosPorFecha( date ) ?: [ ]
    return results.collect { Deposito tmp ->
      BancoDeposito banco = bancoService.obtenerBancoDeposito( tmp?.idBanco?.toInteger() )
      Deposit deposit = Deposit.toDeposit( tmp )
      deposit.bank = banco?.nombre
      return deposit
    }
  }

  static Deposit findDepositById( Integer id ) {
    Deposit deposit = Deposit.toDeposit( cierreDiarioService.buscarDepositoPorId( id ) )
    BancoDeposito banco = bancoService.obtenerBancoDeposito( deposit?.bankId?.toInteger() )
    deposit.bank = banco?.nombre
    return deposit
  }

  static boolean saveDeposit( Deposit deposit, boolean edit ) {
      if( edit ){
          cierreDiarioService.eliminarDeposito( deposit.id )
      }
    if ( deposit != null ) {
      User user = Session.get( SessionItem.USER ) as User
      Deposito deposito = new Deposito(
          fechaCierre: deposit.closeDate,
          fechaIngreso: deposit.enterDate,
          fechaDeposito: deposit.depositDate,
          numeroDeposito: deposit.number,
          tipoDeposito: deposit.depositType,
          idBanco: deposit.bankId,
          referencia: deposit.reference,
          idEmpleado: user?.username,
          monto: deposit.ammount
      )
      try {
        cierreDiarioService.guardarDeposito( deposito )
        return true
      } catch ( Exception e ) {
        log.error( "Error al guardar Deposito: ${e.getMessage()}" )
      }
    }
    return false
  }

  static boolean deleteDeposit( Integer id ) {
    try {
      log.debug( "Eliminando Deposito ID: ${id}" )
      cierreDiarioService.eliminarDeposito( id )
    } catch ( Exception e ) {
      log.error( "Error al eliminar Deposito: ${e.getMessage()}" )
      return false
    }
    true
  }

  static List<Payment> findPaymentsByDayByInvoiceByTerminal( Date date, String terminal, String plan ) {
    def resultados = cierreDiarioService.buscarPagosPorFechaCierrePorFacturaPorTerminal( date, terminal, plan )
    resultados.collect {
      Payment.toPaymment( it )
    }
  }

  static boolean printDailyClose( Date closeDate, String description ) {
    List<ResumenDiario> results = cierreDiarioService.buscarResumenDiarioPorFechaPorTerminal( closeDate, description )
    log.debug( "Se han encontrado ${results?.size()} resultados" )
    User user = Session.get( SessionItem.USER ) as User
    Empleado employee = empleadoService.obtenerEmpleado( user.username )
    boolean terminal = ticketService.imprimeCierreTerminales( closeDate, results, employee, description )

      return terminal
  }

  static void printDailyDigest( Date closeDate ) {
    User user = Session.get( SessionItem.USER ) as User
    Empleado employee = empleadoService.obtenerEmpleado( user.username )
    ticketService.imprimeResumenDiario( closeDate, employee )
  }

  static boolean closeDailyClose( Date closeDate, String observations, Boolean cierre ) {
    try {
      Map<Integer, String> creditRefunds = [ : ]
      List<Pago> payments = new ArrayList<Pago>()
      List<NotaVenta> notas = notaVentaService.obtenerDevolucionesPendientes( closeDate )
      for(NotaVenta nota : notas){
          creditRefunds = new HashMap<>()
          for(Pago payment : nota.pagos){
              payments.add( payment )
          }
          payments.each { Pago pmt ->
              creditRefunds.put( pmt?.id, 'ORIGINAL' )
          }
          if( CancellationController.refundPaymentsCreditFromOrder( nota.id, creditRefunds ) ){
            CancellationController.printOrderCancellation( nota.id )
          }
          payments = new ArrayList<Pago>()
      }
      cierreDiarioService.cargarDatosCierreDiario( closeDate )
      cierreDiarioService.cerrarCierreDiario( closeDate, observations, cierre )
	  User user = Session.get( SessionItem.USER ) as User
	  Empleado employee = empleadoService.obtenerEmpleado( user.username )
	  ticketService.imprimeResumenDiario( closeDate, employee )
    } catch ( Exception e ) {
      log.error( "Error al cerrar el Cierre Diario: ${e.getMessage()}" )
      return false
    }
    true
  }

  static boolean loadDayData( DailyClose dailyClose ) {
    log.info( "cargando datos del dia: ${dailyClose?.date?.format( 'dd-MM-yyyy' )}" )
    if ( dailyClose?.date ) {
      return cierreDiarioService.cargarDatosCierreDiario( dailyClose.date )
    }
    return false
  }

  static Payment updatePayment( Payment payment ) {
    log.info( "actualizando pago id: ${payment?.id}, idFactura: ${payment?.order}" )
    Pago pago = pagoService.obtenerPago( payment?.id )
    if ( pago?.id ) {
      pago.idTerminal = payment.terminalId
      pago.idPlan = payment.planId
      try {
        pago = pagoService.actualizarPago( pago )
        return Payment.toPaymment( pago )
      } catch ( ex ) {
        log.error( "error al actualizar pago: ${payment?.dump()}", ex )
      }
    } else {
      log.warn( "no se puede actualizar pago, no existe" )
    }
    return null
  }

  static DailyClose openDay( ) {
    log.info( "abriendo el dia para cierre diario" )
    CierreDiario cierreDiario = cierreDiarioService.abrirCierreDiario()
    DailyClose.toDailyClose( cierreDiario )
  }

  static List<Bank> findDepositBanks( ) {
    log.info( "obteniendo bancos de deposito" )
    List<BancoDeposito> results = bancoService.listarBancosDeposito()
    results?.collect { BancoDeposito banco ->
      Bank.toBank( banco )
    }
  }

    static void RegistrarPromociones( ) {
        log.info( "Registrando Promociones" )
        promotionService.RegistrarPromociones()
    }

  static void regenerarArchivosZ( Date fecha ){
    log.debug( "Regenerando Archivos Z" )
    cierreDiarioService.regenerarArchivosZ( fecha )
  }

  static void eliminarVentasSinFactura(){
    log.debug( "Eliminando ventas sin factura" )
    cierreDiarioService.eliminarVentasAbiertas()
  }

  static List<DailyClose> findByDatesBetween( Date dateInicio, Date dateFin ) {
    log.info( "obteniendo dias por dias entre ${dateInicio.format( 'dd-MM-yyyy' )} y ${dateFin.format( 'dd-MM-yyyy' )}" )
    //List<DailyClose> lstDailyClose = new ArrayList<DailyClose>()
    List<CierreDiario> lstCierreDiario = cierreDiarioService.buscarPorFechasEntre( dateInicio, dateFin )
    lstCierreDiario.collect {
      DailyClose.toDailyClose( it )
    }
  }

  static Boolean updateTerminal( Date fecha ) {
      Boolean vauchers = cierreDiarioService.cargarVouchersResumenes( fecha )
      Boolean terminales = cierreDiarioService.cargarResumenTerminales( fecha )
      if( vauchers && terminales ){
          return true
      } else {
          return false
      }
  }


  static Long timeWait(  ) {
    String seconds = Registry.timeToWait
    Long time = 0L
    Long newTime = 0L
    try{
      time = NumberFormat.getInstance().parse( seconds.trim() ).toLong()
    } catch ( Exception e ){
      println( e )
    }
    newTime = time*1000L
    return newTime
  }


}
