package mx.lux.pos.repository.impl

import com.mysema.query.jpa.JPQLQuery
import com.mysema.query.types.Predicate
import mx.lux.pos.model.Devolucion
import mx.lux.pos.model.QDevolucion
import mx.lux.pos.repository.custom.DevolucionRepositoryCustom
import org.apache.commons.lang3.time.DateUtils
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport

class DevolucionRepositoryImpl extends QueryDslRepositorySupport implements DevolucionRepositoryCustom {

  @Override
  List<Devolucion> findBy_Fecha( Date fecha ) {
    if ( fecha != null ) {
      QDevolucion devolucion = QDevolucion.devolucion
      def predicates = [ ]
      Date fechaInicio = DateUtils.truncate( fecha, Calendar.DATE )
      Date fechaFin = DateUtils.addDays( fechaInicio, 1 )
      predicates.add( devolucion.fecha.between( fechaInicio, fechaFin ) )
      JPQLQuery query = from( devolucion )
      query.where( predicates as Predicate[] )
      return query.list( devolucion )
    }
    [ ]
  }
}
