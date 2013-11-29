package mx.lux.pos.ui.controller

import mx.lux.pos.service.ComboService
import mx.lux.pos.ui.model.Bank
import mx.lux.pos.ui.model.Plan
import mx.lux.pos.ui.model.Terminal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ComboBoxController {

  private static ComboService comboService

  @Autowired
  DailyCloseController( ComboService comboService ) {
    this.comboService = comboService
  }

  static List<Bank> getAllBanks( ) {
    def results = comboService.obtenerTodosBancos()
    results.collect {
      Bank.toBank( it )
    }
  }

  static List<Terminal> getAllTerminals( ) {
    def results = comboService.obtenerTodosTerminales()
    results.collect {
      Terminal.toTerminal( it )
    }
  }

  static List<Terminal> getAllTerminalsPlusTodas( ) {
    def results = [ ]
    results.add( new Terminal( id: 0, description: 'TODAS' ) )
    results.addAll( getAllTerminals() )
    results
  }

  static List<Plan> getAllPlans( ) {
    def results = comboService.obtenerTodosPlanes()
    results.collect {
      Plan.toPlan( it )
    }
  }

  static List<Plan> getPlansByBank( Integer idBank ) {
    def results = comboService.obtenerPlanesPorBanco( idBank )
    results.collect {
      Plan.toPlan( it )
    }
  }
}
