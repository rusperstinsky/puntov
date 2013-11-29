package mx.lux.pos.repository

import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration('classpath:spring-config.xml')
class TransInvDetalleIntegration extends Specification {

  @Resource 
  private TransInvDetalleRepository source

  def "test ListarTransacciones por Tipo y Folio"(  ) {
    when:
      def trType = "VENTA"
      def folio = 3
      def list = source.findByIdTipoTransAndFolio( trType, folio )
      println "\nTransacciones por Tipo and Folio"
      list.each() { println it }
      
    then:
      list.size() == 1
  }


}
