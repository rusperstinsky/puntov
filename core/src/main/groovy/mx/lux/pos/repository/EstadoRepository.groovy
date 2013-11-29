package mx.lux.pos.repository

import mx.lux.pos.model.Estado
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface EstadoRepository extends JpaRepository<Estado, String>, QueryDslPredicateExecutor<Estado> {

  List<Estado> findByNombreNotNullOrderByNombreAsc( )

  Estado findByNombre( String nombre )

}
