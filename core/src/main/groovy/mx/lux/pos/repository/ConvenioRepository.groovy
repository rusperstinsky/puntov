package mx.lux.pos.repository

import mx.lux.pos.model.Dominio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor
import mx.lux.pos.model.InstitucionIc

interface ConvenioRepository extends JpaRepository<InstitucionIc, String>, QueryDslPredicateExecutor<InstitucionIc> {

  List<InstitucionIc> findById( String convenio )

}
