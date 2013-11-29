package mx.lux.pos.repository

import mx.lux.pos.model.GrupoArticulo
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration('classpath:spring-config.xml')
class GrupoArticuloIntegration extends Specification {

  @Resource
  private GrupoArticuloRepository source

  @Resource
  private GrupoArticuloDetRepository detail

  def "test Listar Grupo Articulo"( ) {
    when:
    println "\nGrupoArticulo sin Detalle"
      def list = source.findAll()
      for ( GrupoArticulo gpo in list ) {
        println gpo
      }
      
    then:
      list.size >= 4
  }

  def "test Listar GrupoArticulo con Detalle"( ) {
    when:
    println "\nGrupoArticulo con Detalle"
    def list = source.findAll()
    for ( GrupoArticulo gpo in list ) {
      gpo.add( detail.findByIdGrupo( gpo.idGrupo ) )
      println gpo
    }
    
  then:
    list.size >= 4

  }
}
