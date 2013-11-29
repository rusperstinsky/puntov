package mx.lux.pos.service.impl

import spock.lang.Specification
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration( 'classpath:spring-config.xml' )
class CotizacionServiceIntegration extends Specification {

  def "Test Order To Quote"() {
    when:
    Integer quoteNbr = ServiceFactory.quotes.copyFromOrder('A00161', 104, '9999')
    println quoteNbr

    then:
    quoteNbr != null
    quoteNbr > 0

  }

  def "Test Print Ticket Cotizacion"() {
    when:
    ServiceFactory.ticketEngine.imprimeCotizacion( 9 )

    then:
    true
  }
}
