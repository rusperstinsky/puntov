package mx.lux.pos.repository

import mx.lux.pos.model.TipoTransInv
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface TipoTransInvRepository
extends JpaRepository<TipoTransInv, String>,
    QueryDslPredicateExecutor<TipoTransInv> {

}
