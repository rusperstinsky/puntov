package mx.lux.pos.service.impl

import mx.lux.pos.model.Parametro
import mx.lux.pos.model.Sucursal
import mx.lux.pos.repository.ParametroRepository
import mx.lux.pos.repository.SucursalRepository
import spock.lang.Specification

class SucursalServiceImplTest extends Specification {

  private SucursalServiceImpl service
  private SucursalRepository sucursalRepository
  private ParametroRepository parametroRepository

  def setup( ) {
    sucursalRepository = Mock( SucursalRepository )
    parametroRepository = Mock( ParametroRepository )
    service = new SucursalServiceImpl(
        sucursalRepository: sucursalRepository,
        parametroRepository: parametroRepository
    )
  }

  def 'regresa nulo cuando solicita sucursal actual con identificador nulo, vacio o menor a 1'( ) {
    when:
    def actual = service.obtenSucursalActual()

    then:
    1 * parametroRepository.findOne( _ as String ) >> parametro

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | parametro
    null     | null
    null     | new Parametro()
    null     | new Parametro( valor: '' )
    null     | new Parametro( valor: '0' )
    null     | new Parametro( valor: '0000' )
  }

  def 'regresa sucursal cuando solicita sucursal actual con identificador mayor a 1'( ) {
    when:
    def actual = service.obtenSucursalActual()

    then:
    1 * parametroRepository.findOne( _ as String ) >> parametro
    1 * sucursalRepository.findOne( _ as Integer ) >> sucursal

    then:
    0 * _

    then:
    expected == actual?.id

    where:
    expected | parametro                      | sucursal
    1        | new Parametro( valor: '1' )    | new Sucursal( id: 1 )
    1        | new Parametro( valor: '0001' ) | new Sucursal( id: 1 )
    1000     | new Parametro( valor: '1000' ) | new Sucursal( id: 1000 )
    1010     | new Parametro( valor: '1010' ) | new Sucursal( id: 1010 )
    1111     | new Parametro( valor: '1111' ) | new Sucursal( id: 1111 )
  }
}
