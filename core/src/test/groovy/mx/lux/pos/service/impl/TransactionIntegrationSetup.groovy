package mx.lux.pos.service.impl

import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.model.Articulo
import mx.lux.pos.service.InventarioService
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.model.Shipment
import mx.lux.pos.model.ShipmentLine
import org.apache.commons.lang3.StringUtils

@ContextConfiguration( 'classpath:spring-config.xml' )
class TransactionIntegrationSetup extends Specification {

  // Internal methods
  static String partToString( Articulo part ) {
    String str = "null"
    if ( part != null ) {
      if (StringUtils.trimToEmpty(part.descripcion).length() > 0) {
        if (StringUtils.trimToEmpty(part.codigoColor).length() > 0) {
          str = String.format( "[%s %s] %s%s", part.articulo, part.codigoColor, part.descripcion,
              (StringUtils.trimToEmpty(part.descripcionColor).length() > 0 ? " @ " + part.descripcionColor : "")
          )
        } else {
          str = String.format( "[%s] %s%s", part.articulo, part.descripcion,
              (StringUtils.trimToEmpty(part.descripcionColor).length() > 0 ? " @ " + part.descripcionColor : "")
          )
        }
      } else {
        if (StringUtils.trimToEmpty(part.codigoColor).length() > 0) {
          str = String.format( "[%s %s] %s", part.articulo, part.codigoColor,
              (StringUtils.trimToEmpty(part.descripcionColor).length() > 0 ? " @ " + part.descripcionColor : "")
          )
        } else {
          str = String.format( "Articulo: %s", part.articulo )
        }
      }
    }
    return str
  }
  // TO setup DB, first backup and delete records with commands
  // found in resources.SetupTransactionIntegration.sql_
  def "Test Transaction Behavior"( ) {
    setup:
      ArticuloService partMaster = ServiceFactory.partMaster
      InventarioService inventory = ServiceFactory.inventory
    def Map<Integer, String> before = new TreeMap<Integer, String>()
    def Map<Integer, String> after = new TreeMap<Integer, String>()

    when:
      // Read Shipment
      Shipment shipment = inventory.leerArchivoRemesa( "/home/rlago/lux/data/4.0015.REM.3241646A")
      println shipment.toString()
      // Query Articulos
      for (ShipmentLine line: shipment.lines) {
        Articulo part = partMaster.obtenerArticulo( line.sku, false )
        before.put( line.sku,partToString( part ) )
      }
      // Update Parts
      partMaster.actualizarArticulosConSombra( shipment.partShadows )
      // Query Articulos again
      for (ShipmentLine line: shipment.lines) {
        Articulo part = partMaster.obtenerArticulo( line.sku, false )
        after.put( line.sku, partToString( part ))
      }
      for( Integer sku : before.keySet() ) {
        println( String.format( "findBefore(%d):<%s>\tfindAfter(%d):<%s>", sku, before[sku], sku, after[sku]))
      }


    then:
      true
  }

}
