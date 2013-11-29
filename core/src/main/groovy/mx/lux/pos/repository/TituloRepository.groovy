package mx.lux.pos.repository

import mx.lux.pos.model.Titulo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface TituloRepository extends JpaRepository<Titulo, String>, QueryDslPredicateExecutor<Titulo> {

  List<Titulo> findByTituloNotNullOrderByOrdenAsc( )

}
