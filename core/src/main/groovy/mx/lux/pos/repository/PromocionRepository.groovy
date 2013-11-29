package mx.lux.pos.repository

import mx.lux.pos.model.Promocion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface PromocionRepository
extends JpaRepository<Promocion, Integer>,
    QueryDslPredicateExecutor<Promocion> {

  List<Promocion> findByVigenciaFinAfter( Date pDate )

}
