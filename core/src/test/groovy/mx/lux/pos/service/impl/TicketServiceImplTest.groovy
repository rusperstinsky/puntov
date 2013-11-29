package mx.lux.pos.service.impl

import mx.lux.pos.repository.ParametroRepository
import org.apache.velocity.app.VelocityEngine
import spock.lang.Specification

class TicketServiceImplTest extends Specification {

  private TicketServiceImpl service
  private ParametroRepository parametroRepository
  private VelocityEngine velocityEngine

  def setup( ) {
    parametroRepository = Mock( ParametroRepository )
    velocityEngine = new VelocityEngine()
    velocityEngine.addProperty( 'resource.loader', 'class' )
    velocityEngine.addProperty( 'class.resource.loader.class', 'org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader' )
    service = new TicketServiceImpl(
        parametroRepository: parametroRepository,
        velocityEngine: velocityEngine
    )
  }

  def 'obtiene null al generar ticket con parametros nulos o vacios'( ) {
    when:
    def actual = service.generaTicket( template, items )

    then:
    expected == actual

    where:
    expected | template | items
    null     | null     | null
    null     | ' '      | null
    null     | '-'      | null
    null     | '-'      | [ : ]
  }

  def 'obtiene null al generar ticket con plantilla que no existe'( ) {
    given:
    def template = 'error.vm'
    def items = [ '': null ]

    when:
    def actual = service.generaTicket( template, items )

    then:
    null == actual
  }

  def 'obtiene archivo al generar ticket'( ) {
    given:
    def template = 'template/error.vm'
    def items = [ '': null ]

    when:
    def actual = service.generaTicket( template, items )

    then:
    actual.exists()
    actual.canRead()

    cleanup:
    actual.delete()
  }

  def 'error al imprimir ticket con parametros nulos o vacios'( ) {
    when:
    service.imprimeTicket( template, items )

    then:
    true

    where:
    template            | items
    null                | null
    ' '                 | null
    '-'                 | null
    '-'                 | [ : ]
    'error.vm'          | [ '': null ]
    'template/error.vm' | [ '': null ]
  }
}
