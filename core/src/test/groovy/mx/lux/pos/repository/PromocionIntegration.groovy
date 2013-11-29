package mx.lux.pos.repository

import mx.lux.pos.model.Promocion
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.annotation.Resource

@ContextConfiguration('classpath:spring-config.xml')
class PromocionIntegration extends Specification {
  
  @Resource
  private PromocionRepository source
  
  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  
  def "test Listar Promociones"(  ) {
    when:
      def list = source.findAll()
      for ( Promocion p in list ) {
        println p
      }
      
    then:
      list.size >= 6
  }

  def "test Validar Campos"(  ) {
    when:
      def list = source.findAll()
      for ( Promocion p in list ) {
        StringBuffer sb = new StringBuffer()
        sb.append( String.format( "[%d] %s   Prioridad:<%d> ArtProm:<%s> Convenio:<%s> PrecOferta:<%s>\n   ", 
          p.idPromocion, p.descripcion, p.prioridad, p.articuloProm, ( p.aplicaConvenio ? "si" : "no" ),
          ( p.precioOferta ? "si" : "no") ) )
        sb.append( String.format( "Vigencia:<%s-%s> GpoTienda:<%s> Auto:<%s> Obligatoria:<%s> Tipo:<%s>\n   ", 
          df.format( p.vigenciaIni ), df.format( p.vigenciaFin ), p.idGrupoTienda , 
          ( p.aplicaAuto ? "si" : "no" ), ( p.obligatoria ? "si" : "no" ), p.tipoPromocion ) )
        sb.append( String.format( "Generico:<%s> Tipo:<%s.%s> Marcas:<%s> Articulo:<%s> ", 
          p.idGenerico, p.tipo, p.subtipo, p.marca, p.articulo )  )
        sb.append( String.format( "Precio:<%,.2f (%s)> Descuento:<%.2f>\n    ", p.precioDescontado, p.tipoPrecio, 
          p.descuento )  )
        if ( p.tipoPromocion.equalsIgnoreCase( "Combo" ) ) {
          sb.append( String.format( "(Combo) Generico:<%s> Tipo:<%s.%s> Marcas:<%s> Articulo:<%s> ", 
            p.idGenericoC, p.tipoC, p.subtipoC, p.marcaC, p.articuloC )  )
          sb.append( String.format( "Precio:<%,.2f (%s)> Descuento:<%.2f>", p.precioDescontadoC, p.tipoPrecioC, 
            p.descuentoC )  )
        }
        sb.append( "\n\n" )
        println sb.toString( )
      }
      
    then:
      list.size >= 6
  }
  
  def "test Listar Promociones VigenciaFin después de fecha"( ) {
    expect:
      source.findByVigenciaFinAfter( df.parse(fecha) ).size >= promocionesExpected
      
    where:
      fecha << [ "20/08/2012", "10/09/2012", "20/09/2012", "10/10/2012" ]
      promocionesExpected << [ 6, 6, 5, 3 ]
  }

}
