package mx.lux.pos.repository

import mx.lux.pos.model.GrupoArticuloDet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface GrupoArticuloDetRepository
extends JpaRepository<GrupoArticuloDet, GrupoArticuloDet>,
    QueryDslPredicateExecutor<GrupoArticuloDet> {

  List<GrupoArticuloDet> findByArticulo( String pArticulo )

  List<GrupoArticuloDet> findByIdGrupo( Integer pIdGrupo )

}
