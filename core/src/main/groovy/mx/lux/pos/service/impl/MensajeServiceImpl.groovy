package mx.lux.pos.service.impl

import groovy.util.logging.Slf4j
import mx.lux.pos.service.MensajeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

import mx.lux.pos.model.*
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.business.MessageManager
import mx.lux.pos.service.io.MessageImportTask
import org.apache.commons.lang3.StringUtils

@Slf4j
@Service( 'mensajeService' )
@Transactional( readOnly = true )
class MensajeServiceImpl implements MensajeService {

  String obtenerMensajePorId( Integer id ){
    log.debug( "obtenerMensajePorId( )" )
    String mensaje
    if( id != null ){
      mensaje = MessageManager.getMessage( id )
    }
    return mensaje
  }

  String obtenerMensajePorClave( String clave ){
    log.debug( "obtenerMensajePorClave( )" )
    String mensaje
    if( clave != null ){
      mensaje = MessageManager.getMessage( clave )
    }
    return mensaje
  }

  @Transactional
  void importarArchivo( String pImportFile ) {
    MessageImportTask task = new MessageImportTask()
    if (StringUtils.trimToEmpty(pImportFile).length() > 0) {
      task.filename = pImportFile
    }
    log.debug( 'Importando archivo de mensajes:<%s>', task.getFilename() )
    task.run()
  }
}
