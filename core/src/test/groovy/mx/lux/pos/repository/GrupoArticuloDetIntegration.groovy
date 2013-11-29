package mx.lux.pos.repository

import mx.lux.pos.model.GrupoArticuloDet
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration('classpath:spring-config.xml')
class GrupoArticuloDetIntegration extends Specification {
  
  @Resource
  private GrupoArticuloDetRepository source
  
  def "test Listar GrupoArticuloDet"( ) {
    when:
      def list = source.findAll() 
      for ( GrupoArticuloDet gad in list ) {
        println gad  
      }
      
    then:
      list.size >= 14
  }
  
  def "test Listar GrupoArticuloDet por Grupo"( ) {
    expect:
      source.findByIdGrupo( idGrupo ).size >= expectedGadCount

    where:
      idGrupo << [ 1, 2, 3, 4 ]
      expectedGadCount << [ 4, 3, 3, 4 ]
  }

  def "test Listar GrupoArticuloDet por Articulo"( ) {
    expect:
      source.findByArticulo( articulo ).size >= expectedGadCount

    where:
      articulo << [ "NK0178G", "PL2805G", "TH0821", "NK0033G", "SU2805G", "NK0008" ]
      expectedGadCount << [ 1, 1, 1, 1, 1, 0 ]
  }

}
