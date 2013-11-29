package mx.lux.pos.service.impl

import spock.lang.Specification
import org.springframework.test.context.ContextConfiguration
import mx.lux.pos.repository.NotaVentaRepository
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.model.NotaVenta
import org.apache.commons.lang3.time.DateUtils
import org.apache.commons.lang3.StringUtils
import mx.lux.pos.util.CustomDateUtils
import mx.lux.pos.service.business.EliminarNotaVentaTask

@ContextConfiguration('classpath:spring-config.xml')
class EliminarNotaVentaTaskIntegration extends Specification {

  private Collection<NotaVenta> listOpenOrders( Date pDate ) {
    Date dateFrom = DateUtils.truncate( pDate, Calendar.DATE )
    Date dateTo = DateUtils.addDays( dateFrom, 1)
    NotaVentaRepository catalog = RepositoryFactory.orders
    Collection<NotaVenta> orders = catalog.findByFechaHoraFacturaBetween( dateFrom, dateTo )
    Collection<NotaVenta> selected = new ArrayList<NotaVenta>( )
    for ( NotaVenta order : orders ) {
      if ( StringUtils.trimToNull( order.factura ) == null ) {
        selected.add( order )
      }
    }
    return selected
  }

  def "test EliminarNotaVenta task"( ) {
    when:
    Date date = CustomDateUtils.parseDate( "2012-12-06", "yyyy-MM-dd" )
    Collection<NotaVenta> orders = listOpenOrders( date )
    EliminarNotaVentaTask task = new EliminarNotaVentaTask()
    for ( NotaVenta order : orders ) {
      println( String.format("Order:%s  Ticket:%s", order.id, order.factura ) )
      task.addNotaVenta( order.id )
    }
    println( task )
    task.run( )
    println( task )

    then:
    true
  }
}
