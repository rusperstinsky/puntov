package mx.lux.pos.repository.impl

import com.mysema.query.jpa.JPQLQuery
import com.mysema.query.types.Predicate
import mx.lux.pos.model.ClasifCliente
import mx.lux.pos.model.QClasifCliente
import mx.lux.pos.repository.custom.ClasifCliRepositoryCustom
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport

class ClasifCliRepositoryImpl extends QueryDslRepositorySupport implements ClasifCliRepositoryCustom {

  private final Logger log = LoggerFactory.getLogger( this.class )

  @Override
  ClasifCliente getBy_IdFactura( String idFactura ) {
    if ( StringUtils.isNotBlank( idFactura ) ) {
      QClasifCliente clasifCliente = QClasifCliente.clasifCliente
      def predicates = [ ]
      predicates.add( clasifCliente.idFactura.eq( idFactura.trim() ) )
      JPQLQuery query = from( clasifCliente )
      query.where( predicates as Predicate[] )
      try {
        return query.singleResult( clasifCliente )
      } catch ( Exception e ) {
        log.error( "Se ha producido un error al buscar ClasifCliente por IdFactura (${idFactura}): ${e.getMessage()}" )
      }
    }
    null
  }

}
