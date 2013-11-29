package mx.lux.pos.repository.impl

import com.mysema.query.jpa.JPQLQuery
import com.mysema.query.types.Predicate
import mx.lux.pos.model.OrdenPromDet
import mx.lux.pos.model.QNotaFactura
import mx.lux.pos.repository.custom.NotaFacturaRepositoryCustom
import org.apache.commons.lang.time.DateUtils
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport
import mx.lux.pos.repository.custom.OrdenPromDetRepositoryCustom
import mx.lux.pos.model.QOrdenPromDet

class OrdenPromDetRepositoryImpl extends QueryDslRepositorySupport implements OrdenPromDetRepositoryCustom {
  
  @Override
  List<OrdenPromDet> findByFechaMod( Date fecha ) {
	if ( fecha != null ) {
	  QOrdenPromDet ordenPromDet = QOrdenPromDet.ordenPromDet
	  def predicates = [ ]
	  Date fechaInicio = fecha
	  Date fechaFin = DateUtils.addDays( fecha, 1 )
	  predicates.add( ordenPromDet.fechaMod.between( fechaInicio, fechaFin ) )
	  JPQLQuery query = from( ordenPromDet )
	  query.where( predicates as Predicate[] )
	  return query.list( ordenPromDet )
	}
	[ ]
  }
}
