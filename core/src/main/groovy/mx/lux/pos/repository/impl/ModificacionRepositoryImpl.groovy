package mx.lux.pos.repository.impl

import com.mysema.query.jpa.JPQLQuery
import com.mysema.query.types.Predicate
import mx.lux.pos.model.Modificacion
import mx.lux.pos.model.QModificacion
import mx.lux.pos.repository.custom.ModificacionRepositoryCustom
import org.apache.commons.lang3.time.DateUtils
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport

class ModificacionRepositoryImpl extends QueryDslRepositorySupport implements ModificacionRepositoryCustom {

  @Override
  List<Modificacion> findBy_Fecha( Date fecha ) {
    if ( fecha != null ) {
      QModificacion modificacion = QModificacion.modificacion
      def predicates = [ ]
      Date fechaInicio = DateUtils.truncate( fecha, Calendar.DATE )
      Date fechaFin = DateUtils.addDays( fechaInicio, 1 )
      predicates.add( modificacion.fecha.between( fechaInicio, fechaFin ) )
      JPQLQuery query = from( modificacion )
      query.where( predicates as Predicate[] )
      return query.list( modificacion )
    }
    [ ]
  }

  @Override
  Modificacion getBy_IdFactura( String idFactura ) {
    if ( idFactura != null ) {
      QModificacion modificacion = QModificacion.modificacion
      def predicates = [ ]
      predicates.add( modificacion.idFactura.eq( idFactura.trim() ) )
      JPQLQuery query = from( modificacion )
      query.where( predicates as Predicate[] )
      List<Modificacion> ms = query.list( modificacion )
      if ( ms.size() > 0 ) { return ms.first() }
    }
    null
  }

}
