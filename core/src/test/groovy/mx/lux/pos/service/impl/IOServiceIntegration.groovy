package mx.lux.pos.service.impl

import spock.lang.Specification
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration('classpath:spring-config.xml')
class IOServiceIntegration extends Specification {

  def "Test Notify Daily Sales"() {
    when:
    ServiceFactory.ioServices.notifySales( 'A00042' )
    ServiceFactory.ioServices.notifyAdjustment( 1 )

    then:
    true
  }

}
