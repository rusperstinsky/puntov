package mx.lux.pos.repository.impl

import com.mysema.query.jpa.impl.JPAQuery
import mx.lux.pos.model.NotaVenta
import mx.lux.pos.model.QNotaVenta
import mx.lux.pos.repository.custom.NotaVentaRepositoryCustom
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport

class NotaVentaRepositoryImpl extends QueryDslRepositorySupport implements NotaVentaRepositoryCustom {

  @Override
  List<NotaVenta> findByFacturaNotEmptyLimitingLatestResults( Integer results ) {
    if ( results ) {
      QNotaVenta qNotaVenta = QNotaVenta.notaVenta
      JPAQuery query = new JPAQuery( entityManager )
      query.from( qNotaVenta )
      query.where( qNotaVenta.factura.isNotEmpty() )
      query.orderBy( qNotaVenta.fechaHoraFactura.desc() )
      query.limit( results )
      List<NotaVenta> items = query.list( qNotaVenta )
      return items?.any() ? items : [ ]
    }
    return [ ]
  }
}
