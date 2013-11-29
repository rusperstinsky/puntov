package mx.lux.pos.repository

import mx.lux.pos.model.Terminal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface TerminalRepository extends JpaRepository<Terminal, String>, QueryDslPredicateExecutor<Terminal> {

  List<Terminal> findByDescripcionNotNullOrderByDescripcionAsc( )

}
