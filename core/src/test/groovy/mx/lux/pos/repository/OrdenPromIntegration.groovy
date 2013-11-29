package mx.lux.pos.repository

import mx.lux.pos.model.OrdenProm
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration('classpath:spring-config.xml')
class OrdenPromIntegration extends Specification {
  
  @Resource
  private OrdenPromRepository source
  @Resource
  private OrdenPromDetRepository detail

  def "test Lista OrdenProm"( ) {
    when:
      def list = source.findAll( )
      for ( OrdenProm op in list ) {
        println op
      } 
    
    then:
      list.size >= 5
  }
  
  def "test Lista OrdenProm por Promocion"( ) {
    expect:
      source.findByIdPromocion( promocion ).size >= expectedOrdenProm
      
    where:
      promocion << [ 1, 2, 3, 4 ] 
      expectedOrdenProm << [ 1, 1, 1, 2 ]
  }

  def "test Lista OrdenProm por Nota Venta"( ) {
    expect:
      source.findByIdFactura( factura ).size >= expectedOrdenProm
      
    where:
      factura << [ "B28244", "B28245", "B28246" ]
      expectedOrdenProm << [ 2, 2, 1 ]
  }

  def "test Listar OrdenProm con Detalle"( ) {
    when:
    println "\nOrden-Promocion con Detalle"
    def list = source.findAll()
    for ( OrdenProm ord in list ) {
      ord.add( detail.findByIdOrdenProm( ord.getId( ) ) )
      println ord
    }
    
  then:
    list.size >= 4

  }

}
