package mx.lux.pos.service.impl

import mx.lux.pos.repository.BancoRepository
import mx.lux.pos.repository.PlanRepository
import mx.lux.pos.repository.SucursalRepository
import mx.lux.pos.repository.TerminalRepository
import mx.lux.pos.service.ComboService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

import mx.lux.pos.model.*

@Service( 'comboService' )
@Transactional( readOnly = true )
class ComboServiceImpl implements ComboService {

  private final Logger log = LoggerFactory.getLogger( this.class )

  @Resource
  private BancoRepository bancoRepository

  @Resource
  private TerminalRepository terminalRepository

  @Resource
  private PlanRepository planRepository

  @Resource
  private SucursalRepository sucursalRepository

  @Override
  List<Banco> obtenerTodosBancos( ) {
    List<Banco> bancos = bancoRepository.findAll( QBanco.banco.id.isNotNull(), QBanco.banco.nombre.asc() ) as List<Banco>
    return bancos
  }

  @Override
  List<Terminal> obtenerTodosTerminales( ) {
    terminalRepository.findAll( QTerminal.terminal.id.isNotNull(), QTerminal.terminal.descripcion.asc() ) as List<Terminal>
  }

  @Override
  List<Plan> obtenerTodosPlanes( ) {
    planRepository.findAll( QPlan.plan.id.isNotNull(), QPlan.plan.id.asc() ) as List<Plan>
  }

  @Override
  List<Plan> obtenerPlanesPorBanco( Integer idBanco ) {
    planRepository.findBy_IdBanco( idBanco )
  }
}
