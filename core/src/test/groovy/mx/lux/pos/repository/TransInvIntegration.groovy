package mx.lux.pos.repository

import mx.lux.pos.model.TransInv
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration('classpath:spring-config.xml')
class TransInvIntegration extends Specification {
  
  @Resource
  private TransInvRepository repository
  
  def "test ListarTransacciones por Fecha"(  ) {
    when:
      def Date dateFrom = java.sql.Date.valueOf( "2012-07-23" )
      def Date dateTo = java.sql.Date.valueOf( "2012-07-24" )
      def list = repository.findByFechaBetween( dateFrom, dateTo )
      println "\nTransacciones por Fecha"
      list.each() { println it }
      
    then:
      list.size() == 13
  }
  
  def "test ListarTransacciones por Tipo"(  ) {
    when:
      def tipo = "VENTA"
      def list = repository.findByIdTipoTrans( tipo )
      println "\nTransacciones por Tipo"
      list.each() { println it }
      
    then:
      list.size() == 10
  }
  
  def "test ListarTransacciones por Sucursal Destino"(  ) {
    when:
      def sucursal = 1
      def list = repository.findBySucursalDestino( sucursal )
      println "\nTransacciones por Sucursal"
      list.each() { println it }
      
    then:
      list.size() == 4
  }

  def "test ListarTransacciones por Tipo y Folio"(  ) {
    when:
      def trType = "VENTA"
      def folio = 3
      def list = repository.findByIdTipoTransAndFolio( trType, folio )
      println "\nTransacciones por Tipo and Folio"
      list.each() { println it }
      
    then:
      list.size() == 1
  }

  def "test ObtenerLastTransaction por Fecha"(  ) {
    when:
      def fechaSort = new Sort( Sort.Direction.DESC, [ "fecha" ] )
      Page<TransInv> list = repository.findAll( new PageRequest(0, 1, fechaSort) ) 
      println "\n Ultima Transaccion por Fecha"
      list.each() { println it }
      
    then:
      list.numberOfElements == 1 
  }
  
  
}
