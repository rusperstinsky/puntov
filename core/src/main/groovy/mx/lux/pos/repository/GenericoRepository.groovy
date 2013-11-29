package mx.lux.pos.repository

import mx.lux.pos.model.Generico
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface GenericoRepository extends JpaRepository<Generico, String>, QueryDslPredicateExecutor<Generico> {
  
  List<Generico> findByIdIn( Collection<String> pIdGenericoSet )
  
}
