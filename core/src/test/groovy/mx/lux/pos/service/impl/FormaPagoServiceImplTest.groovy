package mx.lux.pos.service.impl

import mx.lux.pos.model.FormaPago
import mx.lux.pos.model.Parametro
import mx.lux.pos.model.TipoParametro
import mx.lux.pos.repository.FormaPagoRepository
import mx.lux.pos.repository.ParametroRepository
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class FormaPagoServiceImplTest extends Specification {

  FormaPagoServiceImpl service
  FormaPagoRepository formaPagoRepository
  ParametroRepository parametroRepository
  @Shared def formasPago = [
      new FormaPago(
          id: 'EF',
          descripcion: 'EFECTIVO',
          columnaReporteCi: 1,
          defaultFormaDevolucion: 'EF',
          fechaModificacion: Date.parse( 'dd/MM/yyyy', '01/01/2012' ),
          idSucursal: 1,
          aceptaDevoluciones: true
      ),
      new FormaPago(
          id: 'TC',
          descripcion: 'T. CREDITO',
          columnaReporteCi: 1,
          defaultFormaDevolucion: 'TB',
          fechaModificacion: Date.parse( 'dd/MM/yyyy', '02/01/2012' ),
          idSucursal: 1,
          aceptaDevoluciones: true,
          pedirBanco: true
      ),
      new FormaPago(
          id: 'TD',
          descripcion: 'T. DEBITO',
          columnaReporteCi: 1,
          defaultFormaDevolucion: 'EF',
          fechaModificacion: Date.parse( 'dd/MM/yyyy', '03/01/2012' ),
          idSucursal: 1,
          pedirBanco: true
      ),
      new FormaPago(
          id: 'CN',
          descripcion: 'CERTIFICADO',
          columnaReporteCi: 1,
          defaultFormaDevolucion: 'CN',
          fechaModificacion: Date.parse( 'dd/MM/yyyy', '04/01/2012' ),
          idSucursal: 1,
          pedirReferencia: true
      )
  ]

  def setup( ) {
    formaPagoRepository = Mock( FormaPagoRepository )
    parametroRepository = Mock( ParametroRepository )
    service = new FormaPagoServiceImpl(
        formaPagoRepository: formaPagoRepository,
        parametroRepository: parametroRepository
    )
  }

  @Unroll
  def 'regresa lista vacia al obtener formas de pago con parametros nulos o vacios'( ) {
    when:
    def actual = service.listarFormasPago()

    then:
    1 * formaPagoRepository.findAll() >> results
    expected == actual

    where:
    expected | results
    [ ]      | null
    [ ]      | [ ]
    [ ]      | [ null ]
    [ ]      | [ new FormaPago() ]
    [ ]      | [ new FormaPago( id: ' ' ) ]
  }

  def 'regresa lista al obtener formas de pago'( ) {
    given:
    def expected = [ formasPago[ 0 ], formasPago[ 1 ] ]
    def results = [ formasPago[ 0 ], formasPago[ 1 ], null ]

    when:
    def actual = service.listarFormasPago()

    then:
    1 * formaPagoRepository.findAll() >> results
    expected == actual
  }

  @Unroll
  def 'regresa lista vacia al obtener formas de pago activas con parametro nulo o vacio'( ) {
    when:
    def actual = service.listarFormasPagoActivas()

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
  def 'regresa lista vacia al obtener formas de pago activas con resultados nulos o vacios'( ) {
    given:
    def idP = TipoParametro.TIPO_PAGO.value
    def valP = 'CH,EF,TC,TD,TR,VA,'

    when:
    def actual = service.listarFormasPagoActivas()

    then:
    1 * parametroRepository.findOne( idP ) >> new Parametro( id: idP, valor: valP )
    1 * formaPagoRepository.findAll() >> results
    expected == actual

    where:
    expected | results
    [ ]      | null
    [ ]      | [ ]
    [ ]      | [ null ]
    [ ]      | [ null, null ]
    [ ]      | [ new FormaPago() ]
    [ ]      | [ new FormaPago( id: ' ' ) ]
    [ ]      | [ new FormaPago( id: 'XX' ) ]
  }

  def 'regresa lista al obtener formas de pago activas'( ) {
    given:
    def idP = TipoParametro.TIPO_PAGO.value
    def valP = 'CH,EF,TC,TD,TR,VA,'
    def expected = [ formasPago[ 0 ], formasPago[ 1 ], formasPago[ 2 ] ]

    when:
    def actual = service.listarFormasPagoActivas()

    then:
    1 * parametroRepository.findOne( idP ) >> new Parametro( id: idP, valor: valP )
    1 * formaPagoRepository.findAll() >> formasPago
    expected == actual
  }
}
