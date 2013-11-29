package mx.lux.pos.ui.controller

import mx.lux.pos.service.EmpleadoService
import mx.lux.pos.service.SucursalService
import spock.lang.Specification

import static mx.lux.pos.ui.assertions.Assert.assertUserEquals

class AccessControllerTest extends Specification {

  private AccessController controller
  private EmpleadoService empleadoService
  private SucursalService sucursalService

  def setup( ) {
    empleadoService = Mock( EmpleadoService )
    sucursalService = Mock( SucursalService )
    controller = new AccessController(
        empleadoService,
        sucursalService
    )
  }

  def 'obtiene null cuando invoca login con parametros nulos o vacios'( ) {
    when:
    def actual = controller.logIn( username, password )

    then:
    0 * _

    then:
    assertUserEquals( expected, actual )

    where:
    expected | username | password
    null     | null     | null
    null     | ''       | null
    null     | null     | ''
    null     | ''       | ''
    null     | ' '      | ' '
  }
}
