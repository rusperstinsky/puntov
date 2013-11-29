package mx.lux.pos.service.impl

import mx.lux.pos.model.Parametro
import mx.lux.pos.model.TipoPago
import mx.lux.pos.model.TipoParametro
import mx.lux.pos.repository.ParametroRepository
import mx.lux.pos.repository.TipoPagoRepository
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class TipoPagoServiceImplTest extends Specification {

  TipoPagoServiceImpl service
  TipoPagoRepository tipoPagoRepository
  ParametroRepository parametroRepository
  @Shared def tiposPago = [
      new TipoPago(
          id: 'EF',
          descripcion: 'EFECTIVO',
          tipoSoi: 'EF'
      ),
      new TipoPago(
          id: 'TC',
          descripcion: 'TARJETA CREDITO',
          tipoSoi: 'TC',
          f1: 'text',
          f2: 'text',
          f3: 'text',
          f4: 'text',
          f5: 'text'
      ),
      new TipoPago(
          id: 'TR',
          descripcion: 'TRANSFERENCIA',
          tipoSoi: 'TR',
          f1: 'text',
          f2: 'text'
      ),
      new TipoPago(
          id: 'VA',
          descripcion: 'VALE',
          tipoSoi: 'VA',
          f1: 'text',
          f2: 'text'
      )
  ]

  def setup( ) {
    tipoPagoRepository = Mock( TipoPagoRepository )
    parametroRepository = Mock( ParametroRepository )
    service = new TipoPagoServiceImpl(
        tipoPagoRepository: tipoPagoRepository,
        parametroRepository: parametroRepository
    )
  }

  @Unroll
  def 'obtiene lista vacia al listar tipos de pago con parametro nulo o vacio'( ) {
    when:
    def actual = service.listarTiposPago()

    then:
    1 * tipoPagoRepository.findAll() >> results
    expected == actual

    where:
    expected | results
    [ ]      | null
    [ ]      | [ ]
    [ ]      | [ null ]
    [ ]      | [ new TipoPago() ]
    [ ]      | [ new TipoPago( id: ' ' ) ]
  }

  def 'obtiene lista al listar tipos de pago'( ) {
    given:
    def expected = [ tiposPago[ 0 ], tiposPago[ 1 ] ]
    def results = [
        new TipoPago(
            id: tiposPago[ 0 ].id
        ),
        tiposPago[ 1 ],
        null
    ]

    when:
    def actual = service.listarTiposPago()

    then:
    1 * tipoPagoRepository.findAll() >> results
    expected*.id == actual*.id
  }

  @Unroll
  def 'obtiene lista vacia al listar tipos de pago activos con parametro nulo o vacio'( ) {
    when:
    def actual = service.listarTiposPagoActivos()

    then:
    1 * parametroRepository.findOne( _ ) >> parametro
    expected == actual

    where:
    expected | parametro
    [ ]      | null
    [ ]      | new Parametro()
    [ ]      | new Parametro( id: '' )
    [ ]      | new Parametro( id: TipoParametro.TIPO_PAGO.value )
    [ ]      | new Parametro( id: TipoParametro.TIPO_PAGO.value, valor: ' ' )
    [ ]      | new Parametro( id: TipoParametro.TIPO_PAGO.value, valor: ',' )
  }

  @Unroll
  def 'obtiene lista vacia al listar tipos de pago activos con resultados nulos o vacios'( ) {
    given:
    def idP = TipoParametro.TIPO_PAGO.value
    def valP = 'CH,EF,TC,TD,TR,VA,'

    when:
    def actual = service.listarTiposPagoActivos()

    then:
    1 * parametroRepository.findOne( idP ) >> new Parametro( id: idP, valor: valP )
    1 * tipoPagoRepository.findAll() >> results
    expected == actual

    where:
    expected | results
    [ ]      | null
    [ ]      | [ ]
    [ ]      | [ null ]
    [ ]      | [ null, null ]
    [ ]      | [ new TipoPago() ]
    [ ]      | [ new TipoPago( id: ' ' ) ]
    [ ]      | [ new TipoPago( id: 'XX' ) ]
  }

  def 'obtiene lista al listar tipos de pago activos'( ) {
    given:
    def idP = TipoParametro.TIPO_PAGO.value
    def valP = 'CH,EF,TC,TD,TR,VA,'
    def expected = [ tiposPago[ 0 ], tiposPago[ 1 ], tiposPago[ 2 ], tiposPago[ 3 ] ]

    when:
    def actual = service.listarTiposPagoActivos()

    then:
    1 * parametroRepository.findOne( idP ) >> new Parametro( id: idP, valor: valP )
    1 * tipoPagoRepository.findAll() >> tiposPago
    expected == actual
  }
}
