package mx.lux.pos.service.impl

import spock.lang.Specification
import org.springframework.test.context.ContextConfiguration
import mx.lux.pos.util.CustomDateUtils
import mx.lux.pos.service.CierreDiarioService
import mx.lux.pos.service.NotaVentaService
import mx.lux.pos.repository.NotaVentaRepository
import mx.lux.pos.repository.impl.RepositoryFactory
import org.apache.commons.lang3.time.DateUtils
import mx.lux.pos.repository.CierreDiarioRepository
import mx.lux.pos.model.CierreDiario

@ContextConfiguration( 'classpath:spring-config.xml' )
class CierreDiarioServiceIntegration extends Specification {

  def "Test Cierre Diario 12-05"( ) {
    setup:
    Date date = CustomDateUtils.parseDate( '2012-12-05', 'yyyy-MM-dd' )
    CierreDiarioService service = ServiceFactory.dailyClose
    NotaVentaRepository orders = RepositoryFactory.orders
    CierreDiarioRepository repository = RepositoryFactory.dailyClose
    CierreDiario day = repository.findOne( date )
    day.estado = 'a'
    repository.saveAndFlush( day )
    Date dateTo = DateUtils.addDays( date, 1 )
    Integer nOrdersBefore = orders.findByFechaHoraFacturaBetween( date, dateTo ).size( )

    when:
    service.cerrarCierreDiario( date, '' )
    Integer nOrdersAfter = orders.findByFechaHoraFacturaBetween( date, dateTo ).size( )
    println( String.format( 'DailyClose.close(%s)  OrdersBefore:%d  OrdersAfter:%d',
        CustomDateUtils.format( date ), nOrdersBefore, nOrdersAfter ) )

    then:
    nOrdersBefore >= nOrdersAfter

  }

  def "Test Cierre Diario 12-09"( ) {
    setup:
    Date date = CustomDateUtils.parseDate( '2012-12-09', 'yyyy-MM-dd' )
    CierreDiarioService service = ServiceFactory.dailyClose
    NotaVentaRepository orders = RepositoryFactory.orders
    CierreDiarioRepository repository = RepositoryFactory.dailyClose
    CierreDiario day = repository.findOne( date )
    day.estado = 'a'
    repository.saveAndFlush( day )
    Date dateTo = DateUtils.addDays( date, 1 )
    Integer nOrdersBefore = orders.findByFechaHoraFacturaBetween( date, dateTo ).size( )

    when:
    service.cerrarCierreDiario( date, '' )
    Integer nOrdersAfter = orders.findByFechaHoraFacturaBetween( date, dateTo ).size( )
    println( String.format( 'DailyClose.close(%s)  OrdersBefore:%d  OrdersAfter:%d',
        CustomDateUtils.format( date ), nOrdersBefore, nOrdersAfter ) )

    then:
    nOrdersBefore >= nOrdersAfter

  }

}
