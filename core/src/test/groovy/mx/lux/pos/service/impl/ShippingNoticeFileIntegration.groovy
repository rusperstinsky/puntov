package mx.lux.pos.service.impl

import mx.lux.pos.model.InvTrType
import mx.lux.pos.model.Shipment
import mx.lux.pos.model.TransInv
import mx.lux.pos.service.io.ShippingNoticeFile
import mx.lux.pos.service.io.ShippingNoticeFileSunglass
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration( 'classpath:spring-config.xml' )
class ShippingNoticeFileIntegration extends Specification {

  def "Test Writing Lux Format"( ) {
    when:
    TransInv tr = ServiceFactory.inventory.obtenerTransaccion( InvTrType.ISSUE.trType, 1 )
    ShippingNoticeFile shippingFile = new ShippingNoticeFile()
    String path = shippingFile.write( tr )
    File f = new File( path )
    println String.format( "Output to: %s", path )
    f.eachLine { println it }

    then:
    f != null
    path != null
  }

  def "Test Reading Lux Format"( ) {
    when:
    Shipment shipment = new ShippingNoticeFile().read( "/home/rlago/lux/data/4.0015.REM.3237722A" )
    println shipment.toString()

    then:
    shipment != null
  }

  def "Test Writing Sunglass Format"( ) {
    when:
    TransInv tr = ServiceFactory.inventory.obtenerTransaccion( InvTrType.ISSUE.trType, 1 )
    ShippingNoticeFileSunglass shippingFile = new ShippingNoticeFileSunglass()
    String path = shippingFile.write( tr )
    File f = new File( path )
    println String.format( "Output to: %s", path )
    f.eachLine { println it }

    then:
    f != null
    path != null
  }

  def "Test Reading Sunglass Format"( ) {
    when:
    Shipment shipment = new ShippingNoticeFileSunglass().read( "/home/rlago/lux/runtime/paso/remision/ALM1141.S.1.txt" )
    println shipment.toString()

    then:
    shipment != null
  }

}
