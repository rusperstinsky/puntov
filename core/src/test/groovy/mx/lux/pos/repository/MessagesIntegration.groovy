package mx.lux.pos.repository

import mx.lux.pos.model.Mensaje
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource
import mx.lux.pos.model.QMensaje
import mx.lux.pos.service.business.MessageManager
import mx.lux.pos.model.MensajesPorParametro
import mx.lux.pos.model.TipoParametro
import mx.lux.pos.service.MensajeService

@ContextConfiguration('classpath:spring-config.xml')
class MessagesIntegration extends Specification {
  
  @Resource
  private MensajeRepository source

  @Resource
  private MensajeService mensajeService
  

  def "test Listar Mensajes"(  ) {
    when:
      def list = source.findAll(  )
      for ( Mensaje m in list ) {
        println m.dump()
      }
      
    then:
      list.size()== 10
  }

  def "prueba de clase MessageManager"( ){
    when:
    String mensaje = MessageManager.getMessage( 1 )
    String message = MessageManager.getMessage( 'CL' )
    String msj = MessageManager.getMessage( MensajesPorParametro.CANCELACION_SATISFACTORIA.id )
    println mensaje
    println message
    println msj

    then:
    mensaje != null
    message != null
    msj != null
  }

  def "prueba de Servicio de mensajes"( ){
    when:
    String mensaje = mensajeService.obtenerMensajePorClave( MensajesPorParametro.CANCELACION_SATISFACTORIA.clave )
    String message = mensajeService.obtenerMensajePorId( MensajesPorParametro.CANCELACION_SATISFACTORIA.id )
    println mensaje
    println message

    then:
    mensaje != null
    message != null
  }

}
