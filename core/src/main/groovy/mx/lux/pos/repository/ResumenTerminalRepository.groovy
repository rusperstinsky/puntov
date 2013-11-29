package mx.lux.pos.repository

import mx.lux.pos.model.ResumenTerminal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ResumenTerminalRepository extends JpaRepository<ResumenTerminal, Integer>, QueryDslPredicateExecutor<ResumenTerminal> {

  List<ResumenTerminal> findByFechaCierreGreaterThanAndFechaCierreLessThan( Date fechaInicio, Date fechaFin )

  List<ResumenTerminal> findByFechaCierre( Date fecha )

}
