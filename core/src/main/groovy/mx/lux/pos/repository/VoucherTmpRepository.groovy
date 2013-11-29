package mx.lux.pos.repository

import mx.lux.pos.model.VoucherTmp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface VoucherTmpRepository extends JpaRepository<VoucherTmp, Integer>, QueryDslPredicateExecutor<VoucherTmp> {

  List<VoucherTmp> findByFechaCierre( Date fecha )

}
