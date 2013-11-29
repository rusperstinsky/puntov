package mx.lux.pos.repository

import mx.lux.pos.model.ClasifCliente
import mx.lux.pos.repository.custom.ClasifCliRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ClasifCliRepository extends JpaRepository<ClasifCliente, Integer>, QueryDslPredicateExecutor<ClasifCliente>, ClasifCliRepositoryCustom {

}
