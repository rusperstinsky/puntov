package mx.lux.pos.repository

import mx.lux.pos.model.Dominio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface DominioRepository extends JpaRepository<Dominio, Integer>, QueryDslPredicateExecutor<Dominio> {

  List<Dominio> findByNombreNotNull( )

}
