package mx.lux.pos.repository

import mx.lux.pos.model.BancoDeposito
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface BancoDepositoRepository extends JpaRepository<BancoDeposito, Integer>, QueryDslPredicateExecutor<BancoDeposito> {

  List<BancoDeposito> findByNombreNotNullOrderByNombreAsc( )

}
