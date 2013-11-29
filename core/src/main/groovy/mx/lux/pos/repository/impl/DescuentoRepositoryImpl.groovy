package mx.lux.pos.repository.impl

import com.mysema.query.jpa.JPQLQuery
import com.mysema.query.types.Predicate
import mx.lux.pos.model.Descuento
import mx.lux.pos.model.QDescuento
import mx.lux.pos.repository.custom.DescuentoRepositoryCustom
import org.apache.commons.lang3.StringUtils
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport

class DescuentoRepositoryImpl extends QueryDslRepositorySupport implements DescuentoRepositoryCustom {

  @Override
  Descuento getBy_IdFactura( String idFactura ) {
    if ( StringUtils.isNotBlank( idFactura ) ) {
      QDescuento descuento = QDescuento.descuento
      def predicates = [ ]
      predicates.add( descuento.idFactura.eq( idFactura.trim() ) )
      JPQLQuery query = from( descuento )
      query.where( predicates as Predicate[] )
      return query.singleResult( descuento )
    }
    null
  }

}
