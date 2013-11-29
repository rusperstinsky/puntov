package mx.lux.pos.service.impl

import spock.lang.Specification
import org.springframework.test.context.ContextConfiguration
import mx.lux.pos.model.TransInv
import mx.lux.pos.service.business.InventorySearch
import mx.lux.pos.util.CustomDateUtils
import mx.lux.pos.service.io.ZInFile

@ContextConfiguration( 'classpath:spring-config.xml' )
class ZInFileIntegration extends Specification {

  def "Test ZInFile"() {
    setup:
    Date from = CustomDateUtils.parseDate( "2012-10-01", "yyyy-MM-dd" )
    Date to = new Date( )
    List<TransInv> list = InventorySearch.listarTransaccionesPorFecha( from, to )

    when:
    ZInFile file = new ZInFile( from )
    file.setInvTrList( list )
    file.write( )

    then:
    true
  }

}
