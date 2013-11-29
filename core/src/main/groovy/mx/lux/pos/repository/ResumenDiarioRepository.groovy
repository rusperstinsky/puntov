package mx.lux.pos.repository

import mx.lux.pos.model.ResumenDiario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ResumenDiarioRepository extends JpaRepository<ResumenDiario, Integer>, QueryDslPredicateExecutor<ResumenDiario> {

  List<ResumenDiario> findByFechaCierre( Date fecha )

  List<ResumenDiario> findByFechaCierreAndIdTerminal( Date fecha, String idTerminal )

  List<ResumenDiario> findByFechaCierreAndIdTerminalIn( Date fecha, Collection<String> idTerminal )

}
