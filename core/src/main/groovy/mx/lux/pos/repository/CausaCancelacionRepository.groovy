package mx.lux.pos.repository

import mx.lux.pos.model.CausaCancelacion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface CausaCancelacionRepository extends JpaRepository<CausaCancelacion, Integer>, QueryDslPredicateExecutor<CausaCancelacion> {

  List<CausaCancelacion> findByDescripcionNotNullOrderByDescripcionAsc( )

}
