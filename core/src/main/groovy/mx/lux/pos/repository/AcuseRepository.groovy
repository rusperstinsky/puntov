package mx.lux.pos.repository

import mx.lux.pos.model.Acuse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor
import org.springframework.data.jpa.repository.Query

interface AcuseRepository extends JpaRepository<Acuse, Integer>, QueryDslPredicateExecutor<Acuse> {

  @Query( value = "SELECT NEXTVAL('acuses_id_acuse_seq')", nativeQuery = true )
  BigInteger nextIdAcuse( )

  @Query( value = "SELECT * FROM acuses WHERE fecha_acuso IS NULL", nativeQuery = true)
  List<Acuse> findPending()

}

