package mx.lux.pos.repository

import mx.lux.pos.model.Trabajo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface TrabajoRepository extends JpaRepository<Trabajo, String>, QueryDslPredicateExecutor<Trabajo> {

  List<Trabajo> findByFechaPromesaIsNullAndEstadoNotLike( String estado )

}
