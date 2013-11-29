package mx.lux.pos.ui.controller

import mx.lux.pos.model.Articulo
import mx.lux.pos.ui.model.Item
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration('classpath:spring-config.xml')
class ItemControllerIntegration extends Specification {
  
  @Resource
  ItemController parts 
  
  def "Test Search For Articulos" ( ) {
    expect: 
      parts.searchForArticulo( query ).size() == sizeOfListExpected
      parts.searchForArticulo( query ) [0] instanceof Articulo
      
    where:
      query << [ "LVA8X", "EA00*", "NK*", "NK*,001", "EA*+A,001" ]
      sizeOfListExpected << [ 1, 138, 984, 129, 302 ] 
  }

  def "Test Search For Items" ( ) {
    expect:
      parts.searchFor( query ).size() == sizeOfListExpected
      parts.searchFor( query ) [0] instanceof Item
      
    where:
      query << [ "LVA8X", "EA00*", "NK*", "NK*,001", "EA*+A,001" ]
      sizeOfListExpected << [ 1, 138, 984, 129, 302 ]
  }
  

}
