package mx.lux.pos.service.business

import mx.lux.pos.model.Mensaje
import mx.lux.pos.repository.impl.RepositoryFactory
import groovy.util.logging.Slf4j
import mx.lux.pos.model.QMensaje
import mx.lux.pos.model.MensajesPorParametro
import org.apache.commons.lang3.StringUtils

@Slf4j
class MessageManager {

  private static final String MENSAJE_ERROR = 'MENSAJE [%s]'

  static String getMessage( Integer idMensaje ){
    log.debug( "getMessage( )" )
    String mensaje
    if( idMensaje != null ){
      Mensaje message = RepositoryFactory.getMessageCatalog().findOne( idMensaje )
      if( message != null ){
        mensaje = message.toString()
      } else {
        mensaje = String.format( MENSAJE_ERROR, idMensaje )
      }
    }
    return mensaje
  }


  static String getMessage( String clave ){
    log.debug( "getMessage( )" )
    MensajesPorParametro mensajeParam = MensajesPorParametro.parse( StringUtils.trimToEmpty( clave ) )
    if( mensajeParam != null){
      Integer id = mensajeParam.id
      return getMessage( id )
    } else {
      return String.format( MENSAJE_ERROR, clave )
    }
  }

}
