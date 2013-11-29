package mx.lux.pos.model

import spock.lang.Specification
import mx.lux.pos.util.StringList
import org.apache.commons.lang3.time.DateUtils

class ArticuloSunglassAdapterTest extends Specification {

  def "Test Adapter"() {
    setup:
      String record = "1234\\Descrip\\-\\Part\\A\\100\\2012-10-31\\rlago"
      ArticuloSunglassDescriptor.reset()
      ArticuloSunglassDescriptor.sku.index = 0
      ArticuloSunglassDescriptor.description.index = 1
      ArticuloSunglassDescriptor.partNbr.index = 3
      ArticuloSunglassDescriptor.family.index = 4
      ArticuloSunglassDescriptor.price.index = 5
      ArticuloSunglassDescriptor.revDate.index = 6
      ArticuloSunglassDescriptor.revUser.index = 7

    when:
      ArticuloSunglassAdapter part = new ArticuloSunglassAdapter(new StringList(record, "\\"))
      println part

    then:
      part.sku.equals( Integer.valueOf(1234) )
      part.description.equals( "Descrip" )
      part.partNbr.equals( "Part" )
      part.genre.equals( "E" )
      Math.abs( part.price - 100.0f ) < 0.001f
      part.revDate.equals( DateUtils.parseDate( "31/10/2012", "dd/MM/yyyy" ) )
      part.revUserid.equals( "rlago" )
      part.brand == null
      part.colorCode == null
      part.colorDesc == null

  }
}
