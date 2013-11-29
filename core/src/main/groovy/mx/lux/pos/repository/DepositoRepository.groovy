package mx.lux.pos.repository

import mx.lux.pos.model.Deposito
import mx.lux.pos.repository.custom.DepositoRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface DepositoRepository extends JpaRepository<Deposito, Integer>, QueryDslPredicateExecutor<Deposito>, DepositoRepositoryCustom {

  List<Deposito> findByFechaCierre( Date fecha )

}
