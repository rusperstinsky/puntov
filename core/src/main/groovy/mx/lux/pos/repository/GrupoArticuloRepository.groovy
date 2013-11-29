package mx.lux.pos.repository

import mx.lux.pos.model.GrupoArticulo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface GrupoArticuloRepository
extends JpaRepository<GrupoArticulo, Integer>,
    QueryDslPredicateExecutor<GrupoArticulo> {

}
