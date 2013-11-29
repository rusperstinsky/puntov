package mx.lux.pos.repository

import mx.lux.pos.model.OrdenPromDet
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration('classpath:spring-config.xml')
class OrdenPromDetIntegration extends Specification {
  
  @Resource
  private OrdenPromDetRepository source
  
  def "test listar OrdenPromDet"( ) {
    when:
      def list = source.findAll()
      for ( OrdenPromDet opd in list ) {
        println opd 
      }
    
    then:
      list.size >= 6
  }
  
  def "test Lista OrdenProm por Promocion"( ) {
    expect:
      source.findByIdPromocion( promocion ).size >= expectedOrdenProm
      
    where:
      promocion << [ 1, 2, 3, 4 ]
      expectedOrdenProm << [ 1, 2, 1, 2 ]
  }

  def "test Lista OrdenProm por Articulo"( ) {
    expect:
      source.findByIdArticulo( articulo ).size >= expectedOrdenProm
      
    where:
      articulo << [930, 2670, 106429, 2760 ]
      expectedOrdenProm << [ 1, 1, 0, 1 ]
  }

  def "test Lista OrdenProm por Factura"( ) {
    expect:
      source.findByIdFactura( factura ).size >= expectedOrdenProm
      
    where:
      factura << [ "B28244", "B28245", "B28247", "B28246" ]
      expectedOrdenProm << [ 2, 2, 0, 2 ]
  }


}
