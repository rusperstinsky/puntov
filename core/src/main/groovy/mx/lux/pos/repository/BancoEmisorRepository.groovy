package mx.lux.pos.repository

import mx.lux.pos.model.BancoEmisor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface BancoEmisorRepository extends JpaRepository<BancoEmisor, Integer>, QueryDslPredicateExecutor<BancoEmisor> {

  List<BancoEmisor> findByDescripcionNotNullOrderByDescripcionAsc( )

}
