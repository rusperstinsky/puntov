package mx.lux.pos.service.impl

import spock.lang.Specification
import org.springframework.test.context.ContextConfiguration
import org.springframework.beans.factory.annotation.Autowired

import mx.lux.pos.service.MonedaExtranjeraService
import mx.lux.pos.model.Moneda
import mx.lux.pos.model.MonedaDetalle
import java.text.DateFormat
import java.text.SimpleDateFormat

@ContextConfiguration('classpath:spring-config.xml')
class MonedaExtranjeraServiceIntegration extends Specification {

  @Autowired
  MonedaExtranjeraService service

  private static DateFormat df = new SimpleDateFormat("yyyyMMdd")

  def "Test Find Currency"( ) {
    setup:
      List<String> list = ["USD", "CAD", "EUR" ]

    when:
      Boolean found = true
      for (String id in list) {
        Moneda curr = service.findCurrency(id)
        if (id != null)
          println String.format( "find(%s): %s", id, curr.toString())
        else
          found = false
      }

    then:
      found
  }

  def "Test Find active rate at date"( ) {
    setup:
      List<String> ids = [ "USD", "EUR", "CAD" ]
      List<Date> dates = [ "20120705", "20120910", "20121201", "20120831", "20121001" ]
      List<Double> expectedRates = [ 13.3514, 13.1899, 13.0954, 13.3162, 12.8630,
                                     16.9029, 16.5929, 16.9677, 16.3896, 16.5315,
                                     13.1100, 13.1100, 13.0800, 13.1100, 13.0800
      ]

    when:
      Integer nErr = 0
      for (Integer i = 0; i < ids.size(); i++) {
        for (Integer j = 0; j < dates.size(); j++) {
          Integer k = (i * dates.size()) + j
          BigDecimal expected = new BigDecimal( String.format( "%,.4f", expectedRates[k] ) )
          Date date = df.parse(dates[j])
          MonedaDetalle rate = service.findActiveRate( ids[i], date )
          if ( rate == null ) {
            println( String.format( "Rate(%s, %s): null(%.4f)", ids[i], dates[j], expectedRates[k] ) )
            nErr++
          } else if ( expected.compareTo(rate.tipoCambio) == 0 ) {
            println( String.format( "Rate(%s, %s): %,.4f", ids[i], dates[j], rate.tipoCambio))
          } else {
            println( String.format( "Rate(%s, %s): %.4f(%.4f)", ids[i], dates[j], rate.tipoCambio, expectedRates[k] ) )
            nErr++
          }
        }
      }

    then:
      nErr == 0
  }

  def "Test Find current rate"( ) {
    setup:
    List<String> ids = [ "USD", "EUR", "CAD" ]
    List<Double> expectedRates = [ 13.0954, 16.9677, 13.08 ]

    when:
    Integer nErr = 0
    for (Integer i = 0; i < ids.size(); i++) {
      BigDecimal expected = new BigDecimal( String.format( "%,.4f", expectedRates[i] ) )
      MonedaDetalle rate = service.findActiveRate( ids[i] )
      if ( rate == null ) {
        println( String.format( "Rate(%s): null(%.4f)", ids[i], expectedRates[i] ) )
        nErr++
      } else if ( expected.compareTo(rate.tipoCambio) == 0 ) {
        println( String.format( "Rate(%s): %,.4f", ids[i], rate.tipoCambio))
      } else {
        println( String.format( "Rate(%s): %.4f(%.4f)", ids[i], rate.tipoCambio, expectedRates[i] ) )
        nErr++
      }
    }

    then:
    nErr == 0
  }

}
