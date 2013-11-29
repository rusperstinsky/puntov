package mx.lux.pos.service.impl

import mx.lux.pos.model.InvTrType
import mx.lux.pos.model.TransInv
import mx.lux.pos.service.io.InvTrFile
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration( 'classpath:spring-config.xml' )
class InvTrFileIntegration extends Specification {

  def "Test Write Sales Lux Format"( ) {
    when:
    TransInv tr = ServiceFactory.inventory.obtenerTransaccion( InvTrType.SALES.trType, 10 );
    InvTrFile trFile = new InvTrFile()
    String path = trFile.write( tr )
    File f = new File( path )
    println String.format( "Output to: %s", path )
    f.eachLine { println it }

    then:
    f != null
    path != null

  }

  def "Test Write Issue Lux Format"( ) {
    when:
    TransInv tr = ServiceFactory.inventory.obtenerTransaccion( InvTrType.ISSUE.trType, 1 )
    InvTrFile trFile = new InvTrFile()
    String path = trFile.write( tr )
    File f = new File( path )
    println String.format( "Output to: %s", path )
    f.eachLine { println it }

    then:
    f != null
    path != null

  }


}
