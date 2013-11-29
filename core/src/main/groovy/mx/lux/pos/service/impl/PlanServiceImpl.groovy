package mx.lux.pos.service.impl

import mx.lux.pos.model.Plan
import mx.lux.pos.repository.PlanRepository
import mx.lux.pos.service.PlanService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Service( 'planService' )
@Transactional( readOnly = true )
class PlanServiceImpl implements PlanService {

  private static final Logger log = LoggerFactory.getLogger( PlanServiceImpl.class )

  @Resource
  private PlanRepository planRepository

  @Override
  List<Plan> listarPlanesPorTerminal( String idTerminal ) {
    log.info( "listando planes por terminal: ${idTerminal}" )
    return planRepository.findByIdTerminal( idTerminal ) ?: [ ]
  }

  @Override
  List<Plan> listarPlanesPorBancoDeposito( Integer idBancoDeposito ) {
    log.info( "listando planes por bancoDeposito: ${idBancoDeposito}" )
    return planRepository.findByIdBancoDeposito( idBancoDeposito ) ?: [ ]
  }
}
