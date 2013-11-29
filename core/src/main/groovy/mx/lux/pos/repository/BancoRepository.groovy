package mx.lux.pos.repository

import mx.lux.pos.model.Banco
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface BancoRepository extends JpaRepository<Banco, Integer>, QueryDslPredicateExecutor<Banco> {
}
