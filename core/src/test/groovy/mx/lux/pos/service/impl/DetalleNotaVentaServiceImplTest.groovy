package mx.lux.pos.service.impl

import mx.lux.pos.repository.DetalleNotaVentaRepository
import spock.lang.Specification

class DetalleNotaVentaServiceImplTest extends Specification {

  private DetalleNotaVentaServiceImpl service
  private DetalleNotaVentaRepository detalleNotaVentaRepository

  def setup( ) {
    detalleNotaVentaRepository = Mock( DetalleNotaVentaRepository )
    service = new DetalleNotaVentaServiceImpl(
        detalleNotaVentaRepository: detalleNotaVentaRepository
    )
  }
}
