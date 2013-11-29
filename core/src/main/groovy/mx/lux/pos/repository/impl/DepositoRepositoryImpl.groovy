package mx.lux.pos.repository.impl

import com.mysema.query.jpa.JPQLQuery
import com.mysema.query.types.Predicate
import mx.lux.pos.model.Deposito
import mx.lux.pos.model.QDeposito
import mx.lux.pos.repository.custom.DepositoRepositoryCustom
import org.apache.commons.lang3.time.DateUtils
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport

class DepositoRepositoryImpl extends QueryDslRepositorySupport implements DepositoRepositoryCustom {

  @Override
  List<Deposito> findBy_Fecha( Date pFecha ) {
    if ( pFecha != null ) {
      Date fecha = DateUtils.truncate( pFecha, Calendar.DATE )
      QDeposito deposito = QDeposito.deposito
      def predicates = [ ]
      predicates.add( deposito.fechaCierre.eq( fecha ) )
      JPQLQuery query = from( deposito )
      query.where( predicates as Predicate[] )
      return query.list( deposito ).collect { it }
    }
    [ ]
  }

  @Override
  Deposito findBy_Id( Integer id ) {
    QDeposito deposito = QDeposito.deposito
    def predicates = [ ]
    predicates.add( deposito.id.eq( id ) )
    JPQLQuery query = from( deposito )
    query.where( predicates as Predicate[] )
    return query.singleResult( deposito )
  }
}
