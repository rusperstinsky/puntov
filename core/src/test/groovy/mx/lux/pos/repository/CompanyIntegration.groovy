package mx.lux.pos.repository

import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import mx.lux.pos.model.Contribuyente
import mx.lux.pos.service.business.Registry
import com.sun.jndi.cosnaming.IiopUrl
import mx.lux.pos.model.AddressAdapter

@ContextConfiguration( 'classpath:spring-config.xml' )
class CompanyIntegration extends Specification {

  def "Test Find Default Company"( ) {
    when:
      AddressAdapter company = Registry.getCompanyAddress( )
      println company.toString()
      println String.format( "Has Customer Service? (%b)", company.hasCustomerService() )
      if ( company.hasCustomerService() ) println company.phone

    then:
      company != null
  }
}

