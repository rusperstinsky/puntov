package mx.lux.pos.repository.impl

import com.mysema.query.jpa.JPQLQuery
import com.mysema.query.types.Predicate
import mx.lux.pos.model.Pago
import mx.lux.pos.model.QPago
import mx.lux.pos.repository.custom.PagoRepositoryCustom
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport

class PagoRepositoryImpl extends QueryDslRepositorySupport implements PagoRepositoryCustom {

  @Override
  List<Pago> findBy_Fecha( Date fecha ) {
    if ( fecha != null ) {
      QPago pago = QPago.pago
      def predicates = [ ]
      Date fechaInicio = DateUtils.truncate( fecha, Calendar.DATE )
      Date fechaFin = DateUtils.addDays( fechaInicio, 1 )
      predicates.add( pago.fecha.between( fechaInicio, fechaFin ) )
      JPQLQuery query = from( pago )
      query.where( predicates as Predicate[] )
      return query.list( pago )
    }
    return [ ]
  }

  @Override
  List<Pago> findBy_Fecha_IdFactura_DescripcionTerminal( Date fecha, String descripcionTerminal, String plan ) {
    if ( fecha != null ) {
      QPago pago = QPago.pago
      def predicates = [ ]
      Date fechaInicio = fecha
      Date fechaFin = DateUtils.addDays( fecha, 1 )
      predicates.add( pago.fecha.between( fechaInicio, fechaFin ) )
      /*if ( StringUtils.isNotBlank( idFactura ) ) {
        predicates.add( pago.idFactura.eq( idFactura ) )
      }*/
      if( StringUtils.isNotBlank( plan ) ){
        predicates.add( pago.idPlan.eq( plan ) )
      }
      JPQLQuery query = from( pago )
      query.where( predicates as Predicate[] )
      List<Pago> pagos = query.list( pago )
      if ( StringUtils.isNotBlank( descripcionTerminal ) ) {
        pagos = pagos.findAll {
          it.terminal?.descripcion?.equals( descripcionTerminal.trim() )
        }
      }
      return pagos
    }
    return [ ]
  }

  @Override
  Pago findBy_Id( Integer id ) {
    QPago pago = QPago.pago
    def predicates = [ ]
    predicates.add( pago.id.eq( id ) )
    JPQLQuery query = from( pago )
    query.where( predicates as Predicate[] )
    query.singleResult( pago )
  }
}
