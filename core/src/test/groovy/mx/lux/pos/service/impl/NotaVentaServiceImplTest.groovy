package mx.lux.pos.service.impl

import spock.lang.Shared
import spock.lang.Specification
import mx.lux.pos.model.*
import mx.lux.pos.repository.*

import static mx.lux.pos.assertions.Assert.*

class NotaVentaServiceImplTest extends Specification {

  private NotaVentaServiceImpl service
  private NotaVentaRepository notaVentaRepository
  private DetalleNotaVentaRepository detalleNotaVentaRepository
  private PagoRepository pagoRepository
  private SucursalRepository sucursalRepository
  private ArticuloRepository articuloRepository
  private PrecioRepository precioRepository
  private ParametroRepository parametroRepository
  @Shared private def detalles = [
      new DetalleNotaVenta(
          precioUnitFinal: 99.99,
          cantidadFac: 99
      ),
      new DetalleNotaVenta(
          precioUnitFinal: 33.33,
          cantidadFac: 33
      )
  ]
  @Shared private def pagos = [
      new Pago(
          monto: 999.99
      ),
      new Pago(
          monto: 333.33
      )
  ]

  def setup( ) {
    notaVentaRepository = Mock( NotaVentaRepository )
    detalleNotaVentaRepository = Mock( DetalleNotaVentaRepository )
    pagoRepository = Mock( PagoRepository )
    sucursalRepository = Mock( SucursalRepository )
    articuloRepository = Mock( ArticuloRepository )
    precioRepository = Mock( PrecioRepository )
    parametroRepository = Mock( ParametroRepository )
    service = new NotaVentaServiceImpl(
        notaVentaRepository: notaVentaRepository,
        detalleNotaVentaRepository: detalleNotaVentaRepository,
        pagoRepository: pagoRepository,
        sucursalRepository: sucursalRepository,
        articuloRepository: articuloRepository,
        precioRepository: precioRepository,
        parametroRepository: parametroRepository
    )
  }

  def 'obtiene null al obtener notaVenta con id nulo, vacio o no existente'( ) {
    when:
    def actual = service.obtenerNotaVenta( id as String )

    then:
    _ * notaVentaRepository.findOne( _ as String )

    then:
    0 * _

    then:
    null == actual

    where:
    id << [ null, ' ', '-' ]
  }

  def 'obtiene notaVenta al obtener notaVenta con id existente'( ) {
    given:
    def id = 'A1'
    def idSucursal = 1
    def expected = new NotaVenta(
        id: id,
        idSucursal: idSucursal
    )
    def result = new NotaVenta(
        id: id,
        idSucursal: idSucursal
    )

    when:
    def actual = service.obtenerNotaVenta( id )

    then:
    1 * notaVentaRepository.findOne( id ) >> result

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )
  }

  def 'obtiene null al registrar notaVenta con parametros invalidos'( ) {
    when:
    def actual = service.registrarNotaVenta( notaVenta )

    then:
    _ * notaVentaRepository.exists( _ )

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )

    where:
    expected | notaVenta
    null     | null
    null     | new NotaVenta()
    null     | new NotaVenta( id: ' ' )
    null     | new NotaVenta( id: '-' )
  }

  def 'obtiene notaVenta actualizada al registrar notaVenta existente sin detalles ni pagos'( ) {
    given:
    def idFactura = 'A1'
    def idSucursal = 1
    def expected = new NotaVenta(
        id: idFactura,
        idSucursal: idSucursal,
        ventaNeta: 0,
        ventaTotal: 0,
        sumaPagos: 0
    )
    def result = new NotaVenta(
        id: idFactura,
        idSucursal: idSucursal,
        ventaNeta: 0,
        ventaTotal: 0,
        sumaPagos: 0
    )

    when:
    def actual = service.registrarNotaVenta( new NotaVenta( id: idFactura ) )

    then:
    1 * notaVentaRepository.exists( idFactura ) >> true
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * detalleNotaVentaRepository.findByIdFactura( idFactura ) >> resultsDetalles
    1 * pagoRepository.findByIdFactura( idFactura ) >> resultsPagos
    1 * notaVentaRepository.save( { NotaVenta tmp ->
      tmp.id = idFactura
      assertNotaVentaEquals( expected, tmp )
      return tmp
    } ) >> result

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )

    where:
    resultsDetalles                                | resultsPagos
    null                                           | null
    [ ]                                            | [ ]
    [ null ]                                       | [ null ]
    [ new DetalleNotaVenta() ]                     | [ new Pago() ]
    [ new DetalleNotaVenta( precioUnitFinal: 0 ) ] | [ new Pago( monto: 0 ) ]
    [ new DetalleNotaVenta( cantidadFac: 0 ) ]     | [ new Pago( monto: 0 ) ]
  }

  def 'obtiene notaVenta actualizada al registrar notaVenta con detalles registrados sin pagos'( ) {
    given:
    def idFactura = 'A1'
    def idSucursal = 1
    def expected = new NotaVenta(
        id: idFactura,
        idSucursal: idSucursal,
        ventaNeta: 10998.90,
        ventaTotal: 10998.90,
        sumaPagos: 0
    )
    def result = new NotaVenta(
        id: idFactura,
        idSucursal: idSucursal,
        ventaNeta: 10998.90,
        ventaTotal: 10998.90,
        sumaPagos: 0
    )

    when:
    def actual = service.registrarNotaVenta( new NotaVenta( id: idFactura ) )

    then:
    1 * notaVentaRepository.exists( idFactura ) >> true
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * detalleNotaVentaRepository.findByIdFactura( idFactura ) >> [ detalles[ 0 ], detalles[ 1 ] ]
    1 * pagoRepository.findByIdFactura( idFactura ) >> [ ]
    1 * notaVentaRepository.save( { NotaVenta tmp ->
      tmp.id = idFactura
      assertNotaVentaEquals( expected, tmp )
      return tmp
    } ) >> result

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )
  }

  def 'obtiene notaVenta actualizada al registrar notaVenta con pagos registrados sin detalles'( ) {
    given:
    def idFactura = 'A1'
    def idSucursal = 1
    def expected = new NotaVenta(
        id: idFactura,
        idSucursal: idSucursal,
        ventaNeta: 0,
        ventaTotal: 0,
        sumaPagos: 1333.32
    )
    def result = new NotaVenta(
        id: idFactura,
        idSucursal: idSucursal,
        ventaNeta: 0,
        ventaTotal: 0,
        sumaPagos: 1333.32
    )

    when:
    def actual = service.registrarNotaVenta( new NotaVenta( id: idFactura ) )

    then:
    1 * notaVentaRepository.exists( idFactura ) >> true
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * detalleNotaVentaRepository.findByIdFactura( idFactura ) >> [ ]
    1 * pagoRepository.findByIdFactura( idFactura ) >> [ pagos[ 0 ], pagos[ 1 ] ]
    1 * notaVentaRepository.save( { NotaVenta tmp ->
      tmp.id = idFactura
      assertNotaVentaEquals( expected, tmp )
      return tmp
    } ) >> result

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )
  }

  def 'obtiene notaVenta actualizada al registrar notaVenta con detalles registrados y pagos registrados'( ) {
    given:
    def idFactura = 'A1'
    def idSucursal = 1
    def expected = new NotaVenta(
        id: idFactura,
        idSucursal: idSucursal,
        ventaNeta: 10998.90,
        ventaTotal: 10998.90,
        sumaPagos: 1333.32
    )
    def result = new NotaVenta(
        id: idFactura,
        idSucursal: idSucursal,
        ventaNeta: 10998.90,
        ventaTotal: 10998.90,
        sumaPagos: 1333.32
    )

    when:
    def actual = service.registrarNotaVenta( new NotaVenta( id: idFactura ) )

    then:
    1 * notaVentaRepository.exists( idFactura ) >> true
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * detalleNotaVentaRepository.findByIdFactura( idFactura ) >> [ detalles[ 0 ], detalles[ 1 ] ]
    1 * pagoRepository.findByIdFactura( idFactura ) >> [ pagos[ 0 ], pagos[ 1 ] ]
    1 * notaVentaRepository.save( { NotaVenta tmp ->
      tmp.id = idFactura
      assertNotaVentaEquals( expected, tmp )
      return tmp
    } ) >> result

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )
  }

  def 'obtiene nueva notaVenta al abrir notaVenta'( ) {
    given:
    def idSucursal = 1
    def idNotaVenta = 'A'
    def idCliente = 1
    def idParam = TipoParametro.ID_CLIENTE_GENERICO.value
    def expected = new NotaVenta(
        id: idNotaVenta,
        idSucursal: idSucursal,
        idCliente: idCliente
    )
    def result = new NotaVenta(
        id: idNotaVenta,
        idSucursal: idSucursal,
        idCliente: idCliente
    )

    when:
    def actual = service.abrirNotaVenta()

    then:
    1 * parametroRepository.findOne( idParam ) >> new Parametro( id: idParam, valor: idCliente )
    1 * notaVentaRepository.getNotaVentaSequence() >> idNotaVenta
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * notaVentaRepository.save( { NotaVenta tmp ->
      tmp.id = idNotaVenta
      assertNotaVentaEquals( expected, tmp )
      return tmp
    } ) >> result

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )
  }

  def 'obtiene null al abrir notaVenta con errores'( ) {
    when:
    def actual = service.abrirNotaVenta()

    then:
    1 * parametroRepository.findOne( _ as String )
    1 * notaVentaRepository.getNotaVentaSequence()
    1 * sucursalRepository.getCurrentSucursalId()
    1 * notaVentaRepository.save( _ )

    then:
    0 * _

    then:
    assertNotaVentaEquals( null, actual )
  }

  def 'obtiene null al registrar detalleNotaVenta en notaVenta con parametros nulos o vacios'( ) {
    when:
    def actual = service.registrarDetalleNotaVentaEnNotaVenta( idNotaVenta, detalleNotaVenta )

    then:
    _ * notaVentaRepository.findOne( idNotaVenta )

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | idNotaVenta | detalleNotaVenta
    null     | null        | null
    null     | ' '         | new DetalleNotaVenta()
    null     | ' '         | new DetalleNotaVenta( idArticulo: 0 )
    null     | ' '         | new DetalleNotaVenta( idArticulo: 1 )
    null     | '-'         | new DetalleNotaVenta( idArticulo: 0 )
  }

  def 'obtiene notaVenta actualizada al registrar detalleNotaVenta nuevo en notaVenta existente'( ) {
    given:
    def idFactura = 'A1'
    def idArticulo = 1
    def idDetalle = 1
    def idSucursal = 1
    def expected = new NotaVenta(
        id: idFactura,
        idSucursal: idSucursal,
        ventaNeta: 99.99,
        ventaTotal: 99.99,
        sumaPagos: 0
    )
    def currentNotaVenta = new NotaVenta(
        id: expected.id,
        idSucursal: idSucursal,
        ventaNeta: 0,
        ventaTotal: 0,
        sumaPagos: 0
    )
    def savedNotaVenta = new NotaVenta(
        id: expected.id,
        idSucursal: idSucursal,
        ventaNeta: expected.ventaNeta,
        ventaTotal: expected.ventaTotal,
        sumaPagos: expected.sumaPagos
    )
    def detalleNotaVenta = new DetalleNotaVenta(
        idArticulo: idArticulo,
        precioUnitFinal: 99.99,
        cantidadFac: 1
    )
    def savedDetalleNotaVenta = new DetalleNotaVenta(
        id: idDetalle,
        idFactura: idFactura,
        idArticulo: idArticulo,
        precioUnitFinal: 99.99,
        cantidadFac: 1
    )
    def articulo = new Articulo(
        id: idArticulo,
        articulo: 'A'
    )

    when:
    def actual = service.registrarDetalleNotaVentaEnNotaVenta( idFactura, detalleNotaVenta )

    then:
    1 * notaVentaRepository.findOne( idFactura ) >> currentNotaVenta
    2 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * detalleNotaVentaRepository.findByIdFacturaAndIdArticulo( idFactura, idArticulo )
    1 * articuloRepository.findOne( idArticulo ) >> articulo
    1 * precioRepository.findByArticulo( articulo.articulo )
    1 * detalleNotaVentaRepository.save( { DetalleNotaVenta tmp ->
      tmp.id = idDetalle
      assertDetalleNotaVentaEquals( savedDetalleNotaVenta, tmp )
      return tmp
    } ) >> savedDetalleNotaVenta
    1 * notaVentaRepository.exists( idFactura ) >> true
    1 * detalleNotaVentaRepository.findByIdFactura( idFactura ) >> [ savedDetalleNotaVenta ]
    1 * pagoRepository.findByIdFactura( _ )
    1 * notaVentaRepository.save( _ ) >> savedNotaVenta

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )
  }

  def 'obtiene notaVenta actualizada al registrar detalleNotaVenta existente en notaVenta existente'( ) {
    given:
    def idFactura = 'A1'
    def idArticulo = 1
    def idDetalle = 1
    def idSucursal = 1
    def expected = new NotaVenta(
        id: idFactura,
        idSucursal: 1,
        ventaNeta: 199.98,
        ventaTotal: 199.98,
        sumaPagos: 0
    )
    def currentNotaVenta = new NotaVenta(
        id: expected.id,
        idSucursal: expected.idSucursal,
        ventaNeta: 99.99,
        ventaTotal: 99.99,
        sumaPagos: 0
    )
    def savedNotaVenta = new NotaVenta(
        id: expected.id,
        idSucursal: expected.idSucursal,
        ventaNeta: expected.ventaNeta,
        ventaTotal: expected.ventaTotal,
        sumaPagos: expected.sumaPagos
    )
    def detalleNotaVenta = new DetalleNotaVenta(
        idArticulo: idArticulo,
        precioUnitFinal: 99.99,
        cantidadFac: 1
    )
    def currentDetalleNotaVenta = new DetalleNotaVenta(
        id: idDetalle,
        idFactura: idFactura,
        idArticulo: idArticulo,
        precioUnitFinal: 99.99,
        cantidadFac: 1
    )
    def savedDetalleNotaVenta = new DetalleNotaVenta(
        id: idDetalle,
        idFactura: idFactura,
        idArticulo: idArticulo,
        precioUnitFinal: 99.99,
        cantidadFac: 2
    )
    def articulo = new Articulo(
        id: idArticulo,
        articulo: 'A'
    )

    when:
    def actual = service.registrarDetalleNotaVentaEnNotaVenta( idFactura, detalleNotaVenta )

    then:
    1 * notaVentaRepository.findOne( idFactura ) >> currentNotaVenta
    2 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * detalleNotaVentaRepository.findByIdFacturaAndIdArticulo( idFactura, idArticulo ) >> currentDetalleNotaVenta
    1 * articuloRepository.findOne( idArticulo ) >> articulo
    1 * precioRepository.findByArticulo( articulo.articulo )
    1 * detalleNotaVentaRepository.save( { DetalleNotaVenta tmp ->
      tmp.id = idDetalle
      assertDetalleNotaVentaEquals( savedDetalleNotaVenta, tmp )
      return tmp
    } ) >> savedDetalleNotaVenta
    1 * notaVentaRepository.exists( idFactura ) >> true
    1 * detalleNotaVentaRepository.findByIdFactura( idFactura ) >> [ savedDetalleNotaVenta ]
    1 * pagoRepository.findByIdFactura( _ )
    1 * notaVentaRepository.save( _ ) >> savedNotaVenta

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )
  }

  def 'obtiene null al registrar pago en notaVenta con parametros nulos o vacios'( ) {
    when:
    def actual = service.registrarPagoEnNotaVenta( idNotaVenta, pago )

    then:
    _ * notaVentaRepository.findOne( _ as String )

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | idNotaVenta | pago
    null     | null        | null
    null     | ' '         | new Pago()
    null     | ' '         | new Pago( idFormaPago: ' ' )
    null     | ' '         | new Pago( monto: 0 )
    null     | ' '         | new Pago( idFormaPago: ' ', monto: 0 )
    null     | ' '         | new Pago( idFormaPago: '-', monto: 0 )
    null     | ' '         | new Pago( idFormaPago: ' ', monto: 1 )
    null     | '-'         | new Pago( idFormaPago: ' ', monto: 1 )
    null     | '-'         | new Pago( idFormaPago: '-', monto: 1 )
  }

  def 'obtiene notaVenta actualizada al registrar pago nuevo en notaVenta existente'( ) {
    given:
    def idFactura = 'A1'
    def idPago = 1
    def idSucursal = 1
    def fechaFactura = new Date()
    def expected = new NotaVenta(
        id: idFactura,
        fechaHoraFactura: fechaFactura,
        idSucursal: idSucursal,
        ventaNeta: 0,
        ventaTotal: 0,
        sumaPagos: 50
    )
    def currentNotaVenta = new NotaVenta(
        id: idFactura,
        fechaHoraFactura: fechaFactura,
        idSucursal: idSucursal,
        ventaNeta: 0,
        ventaTotal: 0,
        sumaPagos: 0
    )
    def savedNotaVenta = new NotaVenta(
        id: idFactura,
        fechaHoraFactura: fechaFactura,
        idSucursal: idSucursal,
        ventaNeta: expected.ventaNeta,
        ventaTotal: expected.ventaTotal,
        sumaPagos: expected.sumaPagos
    )
    def pago = new Pago(
        monto: 50,
        idFormaPago: 'EF'
    )
    def savedPago = new Pago(
        id: idPago,
        monto: 50,
        idFormaPago: 'EF',
        idFactura: idFactura,
        idSucursal: idSucursal,
        tipoPago: 'a'
    )

    when:
    def actual = service.registrarPagoEnNotaVenta( idFactura, pago )

    then:
    1 * notaVentaRepository.findOne( idFactura ) >> currentNotaVenta
    2 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * pagoRepository.findOne( _ as Integer )
    1 * pagoRepository.save( { Pago tmp ->
      tmp.id = idPago
      assertPagoEquals( savedPago, tmp )
      return tmp
    } ) >> savedPago
    1 * notaVentaRepository.exists( idFactura ) >> true
    1 * detalleNotaVentaRepository.findByIdFactura( _ )
    1 * pagoRepository.findByIdFactura( idFactura ) >> [ savedPago ]
    1 * notaVentaRepository.save( _ ) >> savedNotaVenta

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )
  }

  def 'obtiene null al eliminar detalleNotaVenta en notaVenta con parametros nulos o vacios'( ) {
    when:
    def actual = service.eliminarDetalleNotaVentaEnNotaVenta( idNotaVenta, idArticulo )

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | idArticulo | idNotaVenta
    null     | null       | null
    null     | 0          | ' '
    null     | 1          | ' '
    null     | 0          | '-'
  }

  def 'obtiene null al eliminar detalleNotaVenta no existente en notaVenta'( ) {
    given:
    def idArticulo = 1
    def idNotaVenta = 'A'

    when:
    def actual = service.eliminarDetalleNotaVentaEnNotaVenta( idNotaVenta, idArticulo )

    then:
    1 * detalleNotaVentaRepository.findByIdFacturaAndIdArticulo( idNotaVenta, idArticulo )

    then:
    0 * _

    then:
    null == actual
  }

  def 'obtiene null al eliminar detalleNotaVenta existente en notaVenta no existente'( ) {
    given:
    def idArticulo = 1
    def idNotaVenta = 'A'
    def currentDetalleNotaVenta = new DetalleNotaVenta(
        id: 1,
        idFactura: idNotaVenta,
        idArticulo: idArticulo,
        precioUnitFinal: 99.99,
        cantidadFac: 1
    )

    when:
    def actual = service.eliminarDetalleNotaVentaEnNotaVenta( idNotaVenta, idArticulo )

    then:
    1 * detalleNotaVentaRepository.findByIdFacturaAndIdArticulo( idNotaVenta, idArticulo ) >> currentDetalleNotaVenta
    1 * notaVentaRepository.findOne( idNotaVenta )

    then:
    0 * _

    then:
    null == actual
  }

  def 'obtiene notaVenta actualizada al eliminar detalleNotaVenta en notaVenta sin pagos'( ) {
    given:
    def idArticulo = 1
    def idNotaVenta = 'A'
    def idSucursal = 1
    def expected = new NotaVenta(
        id: idNotaVenta,
        idSucursal: 1,
        fechaHoraFactura: Date.parse( 'dd-MM-yyyy', '01-01-2010' ),
        fechaMod: Date.parse( 'dd-MM-yyyy', '02-01-2010' ),
        ventaNeta: 0,
        ventaTotal: 0,
        sumaPagos: 0
    )
    def currentDetalleNotaVenta = new DetalleNotaVenta(
        id: 1,
        idFactura: idNotaVenta,
        idArticulo: idArticulo,
        precioUnitFinal: 99.99,
        cantidadFac: 1
    )
    def currentNotaVenta = new NotaVenta(
        id: expected.id,
        idSucursal: expected.idSucursal,
        fechaHoraFactura: expected.fechaHoraFactura,
        ventaNeta: 99.99,
        ventaTotal: 99.99,
        sumaPagos: 0,
        detalles: [ currentDetalleNotaVenta ]
    )
    def savedNotaVenta = new NotaVenta(
        id: expected.id,
        idSucursal: expected.idSucursal,
        fechaHoraFactura: expected.fechaHoraFactura,
        fechaMod: expected.fechaMod,
        ventaNeta: expected.ventaNeta,
        ventaTotal: expected.ventaTotal,
        sumaPagos: expected.sumaPagos
    )

    when:
    def actual = service.eliminarDetalleNotaVentaEnNotaVenta( idNotaVenta, idArticulo )

    then:
    1 * detalleNotaVentaRepository.findByIdFacturaAndIdArticulo( idNotaVenta, idArticulo ) >> currentDetalleNotaVenta
    1 * notaVentaRepository.findOne( idNotaVenta ) >> currentNotaVenta
    1 * detalleNotaVentaRepository.delete( idArticulo )
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * notaVentaRepository.exists( idNotaVenta ) >> true
    1 * detalleNotaVentaRepository.findByIdFactura( _ )
    1 * pagoRepository.findByIdFactura( _ )
    1 * notaVentaRepository.save( _ ) >> savedNotaVenta

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )
  }

  def 'obtiene null al eliminar pago en notaVenta con parametros nulos o vacios'( ) {
    when:
    def actual = service.eliminarPagoEnNotaVenta( idNotaVenta, idPago )

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | idPago | idNotaVenta
    null     | null   | null
    null     | 0      | ' '
    null     | 1      | ' '
    null     | 0      | '-'
  }

  def 'obtiene null al eliminar pago no existente en notaVenta'( ) {
    given:
    def idPago = 1
    def idNotaVenta = 'A'

    when:
    def actual = service.eliminarPagoEnNotaVenta( idNotaVenta, idPago )

    then:
    1 * pagoRepository.findOne( idPago )

    then:
    0 * _

    then:
    null == actual
  }

  def 'obtiene null al eliminar pago existente en notaVenta no existente'( ) {
    given:
    def idPago = 1
    def idNotaVenta = 'A'
    def currentPago = new Pago(
        id: idPago,
        monto: 50,
        idFormaPago: 'EF',
        idFactura: idNotaVenta,
        idSucursal: 1,
        fecha: Date.parse( 'dd-MM-yyyy', '01-01-2010' )
    )

    when:
    def actual = service.eliminarPagoEnNotaVenta( idNotaVenta, idPago )

    then:
    1 * pagoRepository.findOne( idPago ) >> currentPago
    1 * notaVentaRepository.findOne( _ as String )

    then:
    0 * _

    then:
    null == actual
  }

  def 'obtiene notaVenta actualizada al eliminar pago en notaVenta sin detalles'( ) {
    given:
    def idPago = 1
    def idNotaVenta = 'A'
    def idSucursal = 1
    def expected = new NotaVenta(
        id: idNotaVenta,
        idSucursal: 1,
        fechaHoraFactura: Date.parse( 'dd-MM-yyyy', '01-01-2010' ),
        fechaMod: Date.parse( 'dd-MM-yyyy', '02-01-2010' ),
        ventaNeta: 0,
        ventaTotal: 0,
        sumaPagos: 0
    )
    def currentPago = new Pago(
        id: idPago,
        monto: 50,
        idFormaPago: 'EF',
        idFactura: idNotaVenta,
        idSucursal: 1,
        fecha: Date.parse( 'dd-MM-yyyy', '01-01-2010' )
    )
    def currentNotaVenta = new NotaVenta(
        id: expected.id,
        idSucursal: expected.idSucursal,
        fechaHoraFactura: expected.fechaHoraFactura,
        ventaNeta: 0,
        ventaTotal: 0,
        sumaPagos: 50,
        pagos: [ currentPago ]
    )
    def savedNotaVenta = new NotaVenta(
        id: expected.id,
        idSucursal: expected.idSucursal,
        fechaHoraFactura: expected.fechaHoraFactura,
        fechaMod: expected.fechaMod,
        ventaNeta: expected.ventaNeta,
        ventaTotal: expected.ventaTotal,
        sumaPagos: expected.sumaPagos
    )

    when:
    def actual = service.eliminarPagoEnNotaVenta( idNotaVenta, idPago )

    then:
    1 * pagoRepository.findOne( idPago ) >> currentPago
    1 * notaVentaRepository.findOne( idNotaVenta ) >> currentNotaVenta
    1 * pagoRepository.delete( idPago )
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * notaVentaRepository.exists( idNotaVenta ) >> true
    1 * detalleNotaVentaRepository.findByIdFactura( _ )
    1 * pagoRepository.findByIdFactura( _ )
    1 * notaVentaRepository.save( _ ) >> savedNotaVenta

    then:
    0 * _

    then:
    assertNotaVentaEquals( expected, actual )
  }

  def 'obtiene detalleNotaVenta original al establecer precios con idArticulo nulo o cero'( ) {
    when:
    def actual = service.establecerPrecios( detalle )

    then:
    0 * _

    then:
    assertDetalleNotaVentaEquals( expected, actual )

    where:
    expected                              | detalle
    null                                  | null
    new DetalleNotaVenta()                | new DetalleNotaVenta()
    new DetalleNotaVenta( idArticulo: 0 ) | new DetalleNotaVenta( idArticulo: 0 )
  }

  def 'obtiene detalleNotaVenta original al establecer precios con articulo inexistente'( ) {
    given:
    Integer idDetalle = 1
    Integer idArticulo = 1
    def detalleExpected = new DetalleNotaVenta(
        id: idDetalle,
        idArticulo: idArticulo,
        precioUnitFinal: 99.99,
        precioUnitLista: 99.99,
        precioCalcLista: 99.99,
        precioFactura: 99.99,
        precioCalcOferta: 0,
        precioConv: 0,
        cantidadFac: 1
    )
    def detalle = new DetalleNotaVenta(
        id: idDetalle,
        idArticulo: idArticulo,
        precioUnitFinal: 99.99,
        precioUnitLista: 99.99,
        precioCalcLista: 99.99,
        precioFactura: 99.99,
        precioCalcOferta: 0,
        precioConv: 0,
        cantidadFac: 1
    )

    when:
    def actual = service.establecerPrecios( detalle )

    then:
    1 * articuloRepository.findOne( idArticulo ) >> articulo

    then:
    0 * _

    then:
    assertDetalleNotaVentaEquals( detalleExpected, actual )

    where:
    articulo << [ null, new Articulo(), new Articulo( id: 0 ) ]
  }

  def 'obtiene detalleNotaVenta original al establecer precios con lista de precios nula o vacia'( ) {
    given:
    Integer idDetalle = 1
    Integer idArticulo = 1
    String codigo = 'A'
    def detalleExpected = new DetalleNotaVenta(
        id: idDetalle,
        idArticulo: idArticulo,
        precioUnitFinal: 99.99,
        precioUnitLista: 99.99,
        precioCalcLista: 99.99,
        precioFactura: 99.99,
        precioCalcOferta: 0,
        precioConv: 0
    )
    def detalle = new DetalleNotaVenta(
        id: idDetalle,
        idArticulo: idArticulo,
        precioUnitFinal: 99.99,
        precioUnitLista: 99.99,
        precioCalcLista: 99.99,
        precioFactura: 99.99,
        precioCalcOferta: 0,
        precioConv: 0
    )
    def articulo = new Articulo(
        id: idArticulo,
        articulo: codigo
    )

    when:
    def actual = service.establecerPrecios( detalle )

    then:
    1 * articuloRepository.findOne( idArticulo ) >> articulo
    1 * precioRepository.findByArticulo( codigo ) >> precios

    then:
    0 * _

    then:
    assertDetalleNotaVentaEquals( detalleExpected, actual )

    where:
    precios << [ null, [ ], [ null ] ]
  }

  def 'obtiene detalleNotaVenta modificada al establecer precios con precio de lista'( ) {
    given:
    Integer idDetalle = 1
    Integer idArticulo = 1
    String codigo = 'A'
    def detalleExpected = new DetalleNotaVenta(
        id: idDetalle,
        idArticulo: idArticulo,
        precioCalcLista: 100.00,
        precioCalcOferta: 0,
        precioUnitLista: 100.00,
        precioUnitFinal: 100.00,
        precioFactura: 100.00,
        precioConv: 0
    )
    def detalle = new DetalleNotaVenta(
        id: idDetalle,
        idArticulo: idArticulo
    )
    def articulo = new Articulo(
        id: idArticulo,
        articulo: codigo
    )
    def precios = [
        new Precio(
            lista: 'L',
            precio: 100
        )
    ]

    when:
    def actual = service.establecerPrecios( detalle )

    then:
    1 * articuloRepository.findOne( idArticulo ) >> articulo
    1 * precioRepository.findByArticulo( codigo ) >> precios

    then:
    0 * _

    then:
    assertDetalleNotaVentaEquals( detalleExpected, actual )
  }

  def 'obtiene detalleNotaVenta modificada al establecer precios con precio de lista y oferta'( ) {
    given:
    Integer idDetalle = 1
    Integer idArticulo = 1
    String codigo = 'A'
    def detalleExpected = new DetalleNotaVenta(
        id: idDetalle,
        idArticulo: idArticulo,
        precioCalcLista: 100.00,
        precioCalcOferta: 49.99,
        precioUnitLista: 49.99,
        precioUnitFinal: 49.99,
        precioFactura: 49.99,
        precioConv: 0
    )
    def detalle = new DetalleNotaVenta(
        id: idDetalle,
        idArticulo: idArticulo
    )
    def articulo = new Articulo(
        id: idArticulo,
        articulo: codigo
    )
    def precios = [
        new Precio(
            lista: 'L',
            precio: 100.00
        ),
        new Precio(
            lista: 'O',
            precio: 49.99
        )
    ]

    when:
    def actual = service.establecerPrecios( detalle )

    then:
    1 * articuloRepository.findOne( idArticulo ) >> articulo
    1 * precioRepository.findByArticulo( codigo ) >> precios

    then:
    0 * _

    then:
    assertDetalleNotaVentaEquals( detalleExpected, actual )
  }

  def 'obtiene null al generar predicado de ticket con ticket invalido'( ) {
    when:
    def actual = service.generarPredicadoTicket( ticket )

    then:
    !actual

    where:
    ticket << [ null, ' ', 'text', '-', '-text', 'text-' ]
  }

  def 'obtiene predicado al generar predicado de ticket'( ) {
    expect:
    service.generarPredicadoTicket( 'text-text' )
  }
}
