package mx.lux.pos.repository.impl

import com.mysema.query.jpa.JPQLQuery
import com.mysema.query.types.Predicate
import mx.lux.pos.repository.custom.PlanRepositoryCustom
import org.apache.commons.lang3.StringUtils
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport
import mx.lux.pos.model.*

class PlanRepositoryImpl extends QueryDslRepositorySupport implements PlanRepositoryCustom {

  @Override
  List<Plan> findBy_IdBanco( Integer idBanco ) {
    if ( idBanco ) {
      QRelacionPlan relacionPlan = QRelacionPlan.relacionPlan
      def predicates = [ ]
      predicates.add( relacionPlan.idBanco.eq( idBanco.toString() ) )
      JPQLQuery query = from( relacionPlan )
      query.where( predicates as Predicate[] )
      List<String> lista = query.list( relacionPlan ).collect {
        it.idPlan
      }

      QPlan plan = QPlan.plan
      predicates = [ ]
      predicates.add( plan.id.in( lista ) )
      query = from( plan )
      query.where( predicates as Predicate[] )
      return query.list( plan )
    }
    return [ ]
  }

  @Override
  List<Plan> findByIdTerminal( String idTerminal ) {
    if ( StringUtils.isNotBlank( idTerminal ) ) {
      QPlan plan = QPlan.plan
      QRelacionPlan relacionPlan = QRelacionPlan.relacionPlan
      QTerminal terminal = QTerminal.terminal
      JPQLQuery query = from( plan, relacionPlan, terminal )
      query.where(
          terminal.idBancoDep.eq( relacionPlan.idBanco.castToNum( Integer ) )
              .and( relacionPlan.idPlan.eq( plan.id ) )
              .and( relacionPlan.activo.eq( 'si' ) )
              .and( terminal.id.eq( idTerminal ) )
      )
      return query.list( plan )
    }
    return [ ]
  }

  @Override
  List<Plan> findByIdBancoDeposito( Integer idBancoDeposito ) {
    if ( idBancoDeposito ) {
      QPlan plan = QPlan.plan
      QRelacionPlan relacionPlan = QRelacionPlan.relacionPlan
      QBancoDeposito bancoDeposito = QBancoDeposito.bancoDeposito
      JPQLQuery query = from( plan, relacionPlan, bancoDeposito )
      query.where(
          bancoDeposito.id.eq( relacionPlan.idBanco.castToNum( Integer ) )
              .and( relacionPlan.idPlan.eq( plan.id ) )
              .and( relacionPlan.activo.eq( 'si' ) )
              .and( bancoDeposito.id.eq( idBancoDeposito ) )
      )
      return query.list( plan )
    }
    return [ ]
  }
}
