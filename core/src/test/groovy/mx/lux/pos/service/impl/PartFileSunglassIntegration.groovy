package mx.lux.pos.service.impl

import spock.lang.Specification
import mx.lux.pos.service.io.PartFileSunglass
import mx.lux.pos.model.ArticuloSunglassAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat

class PartFileSunglassIntegration extends Specification {

  // Internal Methods
  protected int assertNotNull(String pFieldName, Object pFieldValue, String pData) {
    if ( pFieldValue == null ) {
      println String.format("[%s is Null] %s", pFieldName, pData )
      return 1
    }
    return 0
  }

  protected int assertNull(String pFieldName, Object pFieldValue, String pData) {
    if ( pFieldValue != null ) {
      println String.format("[%s is not Null] %s", pFieldName, pData )
      return 1
    }
    return 0
  }

  // Test Methods
  def "Test Reading ShortFile"() {
    when:
      PartFileSunglass file = new PartFileSunglass("/home/rlago/lux/runtime/paso/ProductoSample.txt")
      println file.toString()
      ArticuloSunglassAdapter partSunglass = file.read( )
      while( partSunglass != null ) {
        partSunglass = file.read( )
      }
      println file.toString( )

    then:
      file.partCount == 92
      file.lineCount == 204
  }

  def "Test reading some lines"() {
    setup:
      DateFormat df = new SimpleDateFormat("dd/MM/yyyy")
      Map<Integer, ArticuloSunglassAdapter> partsRead = new HashMap<Integer, ArticuloSunglassAdapter>()
      Integer partsToPrint = 10

    when:
      PartFileSunglass file = new PartFileSunglass("/home/rlago/lux/runtime/paso/ProductoSample.txt")
      ArticuloSunglassAdapter partSunglass = file.read( )
      while( partSunglass != null ) {
        if (partSunglass.sku != null) {
          partsRead.put(partSunglass.sku, partSunglass)
        } else {
          assertNotNull("Sku", partSunglass.sku, partSunglass.data.toString())
        }
        partSunglass = file.read( )
      }
      Integer iPart = 0
      Integer nError = 0
      Integer nWarning = 0
      for (ArticuloSunglassAdapter part : partsRead.values()) {
        String data = part.data.toString()
        nError += assertNotNull("Sku", part.sku, data)
        nError += assertNotNull("PartNbr", part.partNbr, data)
        nError += assertNotNull("Desc", part.description, data)
        nError += assertNotNull("Genre", part.genre, data)
        nError += assertNotNull("Price", part.price, data)
        nError += assertNotNull("Color", part.colorDesc, data)
        nError += assertNotNull("Type", part.type, data)
        nError += assertNotNull("Brand", part.brand, data)
        nWarning += assertNotNull("Supplier", part.supplier, data)
        nWarning += assertNotNull("RevDate", part.revDate, data)
        nWarning += assertNotNull("RevUser", part.revUserid, data)

        nError += assertNull("ColorCode", part.colorCode, data)
        nError += assertNull("RedPrice", part.redPrice, data)
        nError += assertNull("SubType", part.subtype, data)

        if (iPart < partsToPrint) {
          println String.format( "Sku:%06d  Part:[%s]%s  Color:[%s]%s",
              part.sku, part.partNbr, part.description, part.colorCode, part.colorDesc
          )
          println String.format( "    GTSB:(%s, %s, %s, %s)  Price:%,.2f(%,.2f)",
              part.genre, part.type, part.subtype, part.brand, part.price, part.redPrice
          )
          println String.format( "    Supplier:%s  Rev:%s(%s)",
              part.supplier, (part.revDate != null ? df.format(part.revDate) : null), part.revUserid
          )
        }
        iPart ++
      }
      println( String.format( "Warning:%,d  Errors:%,d", nWarning, nError ) )
    
    then:
      partsRead.size() == 92
      nError == 0
  }
}
