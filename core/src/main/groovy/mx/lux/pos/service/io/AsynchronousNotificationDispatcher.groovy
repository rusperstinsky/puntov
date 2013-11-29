package mx.lux.pos.service.io

import mx.lux.pos.model.Acuse
import mx.lux.pos.model.Parametro
import mx.lux.pos.model.TipoParametro
import mx.lux.pos.model.TipoTransInv
import mx.lux.pos.model.TransInv
import mx.lux.pos.model.TransInvDetalle
import mx.lux.pos.repository.AcuseRepository
import mx.lux.pos.repository.TransInvRepository
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.business.Registry
import mx.lux.pos.service.impl.ServiceFactory
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.text.DateFormat
import java.text.SimpleDateFormat

class AsynchronousNotificationDispatcher implements Runnable {

  private static final String FMT_LOG_MESSAGE = '%s     %s [%s] %s'
  private static final String FMT_LOG_DISPATCH_ACK = 'Dispatching: %s'
  private static final String FMT_LOG_PENDING_QUEUE = 'Processing %d pending notifications.'
  private static final String MSG_START_CYCLE = 'Start notification cycle'
  private static final String MSG_NOTHING_TO_PROCESS = 'Processing queue is empty.'
  private static final String FMT_LOG_ACK_RCVD = 'Acknowledgment: %s(%d) <- %s'
  private static final String FMT_LOG_ACK_FAILED = 'Failed Acknowledge: %s(%d) <- %s'

  private static final DateFormat df = new SimpleDateFormat( 'yyyy-MM-dd HH:mm:ss' )

  Logger logger = LoggerFactory.getLogger( this.getClass().getSimpleName() )

  private static AsynchronousNotificationDispatcher instance

  private AsynchronousNotificationDispatcher( ) { }

  static AsynchronousNotificationDispatcher getInstance( ) {
    if ( instance == null ) {
      instance = new AsynchronousNotificationDispatcher()
    }
    return instance
  }

  // Internal methods
  private void debug( String pMessage ) {
    if ( Registry.isAckDebugEnabled() ) {
      println( this.format( 'DEBUG', pMessage ) )
    }
  }

  private void dispatch( Acuse pNotification ) {
    this.debug( String.format( FMT_LOG_DISPATCH_ACK, pNotification.toString() ) )
    Parametro p = Registry.find( TipoParametro.TRANS_INV_TIPO_SALIDA_ALMACEN )
    String url = Registry.getURL( pNotification.idTipo )
    if ( StringUtils.trimToNull( url ) != null ) {
      pNotification.intentos = pNotification.intentos + 1
      if( pNotification.idTipo.trim().equalsIgnoreCase(p.valor) ){
        url += String.format( '?arg=%s', URLEncoder.encode( String.format( '%s', pNotification.contenido ), 'UTF-8' ) )
      } else {
        url += String.format( '?%s', pNotification.contenido )
      }
      try {
        String response = url.toURL().text
        response = response?.find( /<XX>\s*(.*)\s*<\/XX>/ ) {m, r -> return r}
        pNotification.folio = response
        pNotification.fechaAcuso = new Date()
        this.debug( String.format( FMT_LOG_ACK_RCVD, pNotification.idTipo, pNotification.id, response ) )
      } catch ( Exception e ) {
        this.info( String.format( FMT_LOG_ACK_FAILED, pNotification.idTipo, pNotification.id, e.getMessage() ) )
      }
      ServiceFactory.ioServices.saveAcknowledgement( pNotification )
    }
  }

  private void dispatchTransaction( TransInv transaccion, TipoTransInv tipoTrans ) {
      String url = Registry.getURL( tipoTrans.idTipoTrans )
      if ( StringUtils.trimToNull( url ) != null ) {
          try {
              String variable = transaccion.sucursal + '>' + transaccion.sucursalDestino + '>' +
                      transaccion.folio + '>' +
                      transaccion.referencia + '>' +
                      transaccion.idEmpleado.trim() + '>'
              List<TransInvDetalle> lstDetalles = RepositoryFactory.getInventoryDetail().findByIdTipoTransAndFolio( transaccion.idTipoTrans, transaccion.folio )
              for (TransInvDetalle detalle : lstDetalles ) {
                  variable += detalle.sku + ',' + detalle.cantidad +'|'
              }
              url += String.format( '?arg=%s', URLEncoder.encode( String.format( '%s', variable ), 'UTF-8' ) )
              println 'url: '+url
              String response = url.toURL().text
              response = response?.find( /<XX>\s*(.*)\s*<\/XX>/ ) {m, r -> return r}
              println 'repuesta: '+response
              if( response != '' && response.isNumber() ){
                  transaccion.referencia = transaccion.referencia+'|'+response
              } /*else {
                  transaccion.referencia = aleatoria
              }*/
          } catch ( Exception e ) {
              this.info( String.format( FMT_LOG_ACK_FAILED, transaccion.idTipoTrans, transaccion.folio, e.getMessage() ) )
          }
          ServiceFactory.ioServices.saveAcknowledgementTrans( transaccion )
      }
  }

  private String format( String pLogLevel, String pMessage ) {
    return String.format( FMT_LOG_MESSAGE,   df.format( new Date() ), pLogLevel, this.name, pMessage )
  }

  private Collection<Acuse> getPendingNotifications( ) {
    AcuseRepository db = RepositoryFactory.acknowledgements
    return db.findPending()
  }

  private Collection<TransInv> getTransPendingNotifications( ){
      Collection<TransInv> collTrans = new ArrayList<TransInv>()
      Parametro p = Registry.find( TipoParametro.TRANS_INV_TIPO_SALIDA_ALMACEN )
      List<TransInv> transacciones = RepositoryFactory.inventoryMaster.findByIdTipoTrans( p.valor )
      for(TransInv transaccion : transacciones){
          if( !transaccion.referencia.contains( '|' ) ){
              collTrans.add( transaccion)
          }
      }
      return collTrans
  }

  private void info( String pMessage ) {
    println( this.format( 'INFO ', pMessage ) )
  }

  private void pause( ) {
    Integer milis = ( Integer ) ( Registry.ackDelay * 1000.0 )
    try {
      sleep( milis );
    } catch ( InterruptedException e ) {}
  }

  // Public methods
  String getName( ) {
    return this.getClass().getSimpleName()
  }

  void run( ) {
    while ( true ) {
      this.debug( MSG_START_CYCLE )
      Collection<Acuse> pending = this.getPendingNotifications()
      //Collection<TransInv> transPending = this.getTransPendingNotifications()
      /*if( transPending.size() > 0 ){
          for( TransInv trans : transPending ){
              TipoTransInv trType = RepositoryFactory.typeTransaction.findOne( trans.idTipoTrans.toUpperCase() )
              this.dispatchTransaction( trans, trType )
          }
      } else {
          this.debug( MSG_NOTHING_TO_PROCESS )
      }*/
      if ( pending.size() > 0 ) {
        this.debug( String.format( FMT_LOG_PENDING_QUEUE, pending.size() ) )
        for ( Acuse notification : pending ) {
          this.dispatch( notification )
        }
      } else {
        this.debug( MSG_NOTHING_TO_PROCESS )
      }
      this.pause()
    }
  }

}
