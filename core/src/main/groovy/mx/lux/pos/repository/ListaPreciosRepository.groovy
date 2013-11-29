package mx.lux.pos.repository

import mx.lux.pos.model.ListaPrecios
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ListaPreciosRepository extends JpaRepository<ListaPrecios, String>, QueryDslPredicateExecutor<ListaPrecios> {
}
