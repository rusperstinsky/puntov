package mx.lux.pos.service.impl

import mx.lux.pos.repository.EstadoRepository
import mx.lux.pos.repository.ParametroRepository
import spock.lang.Specification

class EstadoServiceImplTest extends Specification {

  private EstadoServiceImpl service
  private EstadoRepository estadoRepository
  private ParametroRepository parametroRepository

  def setup( ) {
    estadoRepository = Mock( EstadoRepository )
    parametroRepository = Mock( ParametroRepository )
    service = new EstadoServiceImpl(
        estadoRepository: estadoRepository,
        parametroRepository: parametroRepository
    )
  }

  def 'es lista vacia al listar estados sin resultados'( ) {
    when:
    def actual = service.listaEstados()

    then:
    1 * estadoRepository.findByNombreNotNullOrderByNombreAsc()

    then:
    0 * _

    then:
    [ ] == actual
  }
}
