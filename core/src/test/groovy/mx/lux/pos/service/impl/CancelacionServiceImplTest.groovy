package mx.lux.pos.service.impl

import com.mysema.query.types.OrderSpecifier
import com.mysema.query.types.Predicate
import mx.lux.pos.service.NotaVentaService
import spock.lang.Specification
import mx.lux.pos.model.*
import mx.lux.pos.repository.*

import static mx.lux.pos.assertions.Assert.*

class CancelacionServiceImplTest extends Specification {

  private CancelacionServiceImpl service
  private CausaCancelacionRepository causaCancelacionRepository
  private NotaVentaRepository notaVentaRepository
  private NotaVentaService notaVentaService
  private ParametroRepository parametroRepository
  private ModificacionRepository modificacionRepository
  private ModificacionCanRepository modificacionCanRepository
  private PagoRepository pagoRepository
  private DevolucionRepository devolucionRepository

  def setup( ) {
    causaCancelacionRepository = Mock( CausaCancelacionRepository )
    notaVentaRepository = Mock( NotaVentaRepository )
    notaVentaService = Mock( NotaVentaService )
    parametroRepository = Mock( ParametroRepository )
    modificacionRepository = Mock( ModificacionRepository )
    modificacionCanRepository = Mock( ModificacionCanRepository )
    pagoRepository = Mock( PagoRepository )
    devolucionRepository = Mock( DevolucionRepository )
    service = new CancelacionServiceImpl(
        causaCancelacionRepository: causaCancelacionRepository,
        notaVentaRepository: notaVentaRepository,
        notaVentaService: notaVentaService,
        parametroRepository: parametroRepository,
        modificacionRepository: modificacionRepository,
        modificacionCanRepository: modificacionCanRepository,
        pagoRepository: pagoRepository,
        devolucionRepository: devolucionRepository
    )
  }

  def 'obtiene lista vacia al listar causas de cancelacion sin datos en repositorio'( ) {
    expect:
    [ ] == service.listarCausasCancelacion()
  }

  def 'obtiene lista de causas al listar causas de cancelacion'( ) {
    given:
    def fecha = Date.parse( 'dd/MM/yyyy', '01/01/2010' )
    def sucursal = 1
    def causas = [
        new CausaCancelacion(
            id: 1,
            descripcion: 'text 1',
            fechaModificacion: fecha,
            idSucursal: sucursal
        ),
        new CausaCancelacion(
            id: 2,
            descripcion: 'text 2',
            fechaModificacion: fecha,
            idSucursal: sucursal
        ),
        new CausaCancelacion(
            id: 3,
            descripcion: 'text 3',
            fechaModificacion: fecha,
            idSucursal: sucursal
        ),
        new CausaCancelacion(
            id: 4,
            descripcion: 'text 4',
            fechaModificacion: fecha,
            idSucursal: sucursal
        )
    ]

    when:
    def actual = service.listarCausasCancelacion()

    then:
    1 * causaCancelacionRepository.findByDescripcionNotNullOrderByDescripcionAsc() >> causas

    then:
    0 * _

    then:
    actual.any()
    actual.eachWithIndex { it, idx ->
      assertCausaCancelacionEquals( causas.get( idx ), it )
    }
  }

  def 'obtiene true al solicitar autorizacion para cancelacion extemporanea con parametro nulo o vacio'( ) {
    when:
    def actual = service.permitirCancelacionExtemporanea( null )

    then:
    1 * parametroRepository.findOne( _ as String ) >> parametro

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | parametro
    true     | null
    true     | new Parametro()
    true     | new Parametro( valor: ' ' )
    true     | new Parametro( valor: '-' )
  }

  def 'obtiene true al solicitar autorizacion para cancelacion extemporanea con parametro inactivo'( ) {
    given:
    String idP = TipoParametro.CAN_MISMO_DIA
    Parametro parametro = new Parametro( id: idP, valor: 'f' )

    when:
    def actual = service.permitirCancelacionExtemporanea( null )

    then:
    1 * parametroRepository.findOne( idP ) >> parametro

    then:
    0 * _

    then:
    actual
  }

  def 'obtiene false al solicitar autorizacion para cancelacion extemporanea con parametro activo y notaVenta nula o vacia'( ) {
    given:
    String idP = TipoParametro.CAN_MISMO_DIA
    Parametro parametro = new Parametro( id: idP, valor: 't' )

    when:
    def actual = service.permitirCancelacionExtemporanea( idNotaVenta )

    then:
    1 * parametroRepository.findOne( idP ) >> parametro
    _ * notaVentaService.obtenerNotaVenta( _ ) >> notaVenta

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | idNotaVenta | notaVenta
    false    | null        | null
    false    | ''          | null
    false    | 'A'         | null
    false    | 'A'         | new NotaVenta()
    false    | 'A'         | new NotaVenta( id: 'A' )
  }

  def 'obtiene false al solicitar autorizacion para cancelacion extemporanea con parametro activo y fecha distinta de actual'( ) {
    given:
    String idP = TipoParametro.CAN_MISMO_DIA
    Parametro parametro = new Parametro( id: idP, valor: 't' )
    String idFactura = 'A'
    NotaVenta notaVenta = new NotaVenta(
        id: idFactura,
        fechaHoraFactura: fecha
    )

    when:
    def actual = service.permitirCancelacionExtemporanea( idFactura )

    then:
    1 * parametroRepository.findOne( idP ) >> parametro
    1 * notaVentaService.obtenerNotaVenta( idFactura ) >> notaVenta

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | fecha
    false    | new Date().previous()
    false    | new Date().next()
  }

  def 'obtiene true al solicitar autorizacion para cancelacion extemporanea con parametro activo y fecha actual'( ) {
    given:
    String idP = TipoParametro.CAN_MISMO_DIA
    Parametro parametro = new Parametro( id: idP, valor: 't' )
    String idFactura = 'A'
    NotaVenta notaVenta = new NotaVenta(
        id: idFactura,
        fechaHoraFactura: new Date()
    )

    when:
    def actual = service.permitirCancelacionExtemporanea( idFactura )

    then:
    1 * parametroRepository.findOne( idP ) >> parametro
    1 * notaVentaService.obtenerNotaVenta( idFactura ) >> notaVenta

    then:
    0 * _

    then:
    actual
  }

  def 'obtiene null al registrar cancelacion de notaVenta con idNotaVenta y/o modificacion nulos o vacios'( ) {
    when:
    def actual = service.registrarCancelacionDeNotaVenta( idNotaVenta, modificacion )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | idNotaVenta | modificacion
    null     | null        | null
    null     | null        | new Modificacion()
    null     | ' '         | new Modificacion( idFactura: ' ' )
  }

  def 'obtiene null al registrar cancelacion de notaVenta con notaVenta invalida'( ) {
    given:
    def idNotaVenta = 'A'
    def modificacion = new Modificacion(
        idFactura: idNotaVenta
    )

    when:
    def actual = service.registrarCancelacionDeNotaVenta( idNotaVenta, modificacion )

    then:
    1 * notaVentaService.obtenerNotaVenta( idNotaVenta ) >> notaVenta

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | notaVenta
    null     | null
    null     | new NotaVenta()
    null     | new NotaVenta( id: ' ' )
    null     | new NotaVenta( id: 'A', sFactura: ' ' )
    null     | new NotaVenta( id: 'A', sFactura: 'T' )
  }

  def 'obtiene modificacion registrada al registar cancelacion de notaVenta con notaVenta valida'( ) {
    given:
    String idNotaVenta = 'A'
    Integer idMod = 1
    String tipo = 'can'
    String idEmpleado = '0'
    String causa = 'text'
    String obs = 'text'
    def expectedModificacion = new Modificacion(
        id: idMod,
        idFactura: idNotaVenta,
        tipo: tipo,
        idEmpleado: idEmpleado,
        causa: causa,
        observaciones: obs
    )
    def modificacion = new Modificacion(
        idFactura: idNotaVenta,
        tipo: tipo,
        idEmpleado: idEmpleado,
        causa: causa,
        observaciones: obs
    )
    def expectedNotaVenta = new NotaVenta(
        id: idNotaVenta,
        fechaHoraFactura: new Date(),
        sFactura: 'T'
    )
    def notaVenta = new NotaVenta(
        id: idNotaVenta,
        fechaHoraFactura: new Date(),
        sFactura: 'N'
    )
    def can = new ModificacionCan(
        id: idMod,
        estadoAnterior: notaVenta.sFactura
    )
    def pagos = [
        new Pago(
            id: 1,
            idFactura: idNotaVenta,
            monto: 100
        ),
        new Pago(
            id: 2,
            idFactura: idNotaVenta,
            monto: 200
        )
    ]

    when:
    def actual = service.registrarCancelacionDeNotaVenta( idNotaVenta, modificacion )

    then:
    1 * notaVentaService.obtenerNotaVenta( idNotaVenta ) >> notaVenta
    1 * pagoRepository.findByIdFactura( idNotaVenta ) >> pagos
    1 * modificacionRepository.save( { Modificacion tmp ->
      tmp.id = idMod
      return tmp
    } ) >> modificacion
    1 * modificacionCanRepository.save( { ModificacionCan tmp ->
      assert tmp.id == can.id
      assert tmp.estadoAnterior == can.estadoAnterior
      return tmp
    } ) >> can
    1 * notaVentaRepository.save( { NotaVenta tmp ->
      assert tmp.sFactura == 'T'
      return tmp
    } ) >> notaVenta
    1 * pagoRepository.save( { List<Pago> tmp ->
      tmp.each { Pago pTmp ->
        assert pTmp.porDevolver == pTmp.monto
      }
      return tmp
    } )

    then:
    0 * _

    then:
    assertModificacionEquals( expectedModificacion, actual )
    assertNotaVentaEquals( expectedNotaVenta, notaVenta )
  }

  def 'obtiene lista vacia al listar devoluciones de notaVenta con idNotaVenta nulo o vacio'( ) {
    when:
    def actual = service.listarDevolucionesDeNotaVenta( idNotaVenta )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | idNotaVenta
    [ ]      | null
    [ ]      | ' '
  }

  def 'obtiene lista vacia al listar devoluciones de notaVenta sin modificaciones'( ) {
    given:
    String idNotaVenta = 'A'

    when:
    def actual = service.listarDevolucionesDeNotaVenta( idNotaVenta )

    then:
    1 * modificacionRepository.findByIdFacturaOrderByFechaAsc( idNotaVenta ) >> modificaciones

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | modificaciones
    [ ]      | null
    [ ]      | [ ]
    [ ]      | [ null ]
  }

  def 'obtiene lista vacia al listar devoluciones de notaVenta sin devoluciones'( ) {
    given:
    String idNotaVenta = 'A'
    List<Modificacion> modificaciones = [
        new Modificacion(
            id: 1,
            idFactura: idNotaVenta,
            tipo: 'text'
        ),
        new Modificacion(
            id: 2,
            idFactura: idNotaVenta,
            tipo: 'text'
        )
    ]

    when:
    def actual = service.listarDevolucionesDeNotaVenta( idNotaVenta )

    then:
    1 * modificacionRepository.findByIdFacturaOrderByFechaAsc( idNotaVenta ) >> modificaciones
    1 * devolucionRepository.findByIdModInOrderByFechaAsc( modificaciones*.id ) >> devoluciones

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | devoluciones
    [ ]      | null
    [ ]      | [ ]
    [ ]      | [ null ]
  }

  def 'obtiene lista de devoluciones al listar devoluciones de notaVenta'( ) {
    given:
    String idNotaVenta = 'A'
    List<Modificacion> modificaciones = [
        new Modificacion(
            id: 1,
            idFactura: idNotaVenta,
            tipo: 'text'
        ),
        new Modificacion(
            id: 2,
            idFactura: idNotaVenta,
            tipo: 'text'
        )
    ]
    List<Devolucion> devoluciones = [
        new Devolucion(
            id: 1,
            idMod: 1,
            idPago: 1,
            idFormaPago: 'EF',
            monto: 10
        ),
        new Devolucion(
            id: 2,
            idMod: 1,
            idPago: 1,
            idFormaPago: 'EF',
            monto: 10
        ),
        new Devolucion(
            id: 3,
            idMod: 2,
            idPago: 2,
            idFormaPago: 'EF',
            monto: 10
        ),
        new Devolucion(
            id: 4,
            idMod: 2,
            idPago: 2,
            idFormaPago: 'EF',
            monto: 10
        )
    ]

    when:
    def actual = service.listarDevolucionesDeNotaVenta( idNotaVenta )

    then:
    1 * modificacionRepository.findByIdFacturaOrderByFechaAsc( idNotaVenta ) >> modificaciones
    1 * devolucionRepository.findByIdModInOrderByFechaAsc( modificaciones*.id ) >> devoluciones

    then:
    0 * _

    then:
    actual.eachWithIndex { it, idx ->
      assertDevolucionEquals( devoluciones.get( idx ), it )
    }
  }

  def 'obtiene lista vacia al registrar devoluciones de notaVenta con idNotaVenta y/o devolucionesPagos vacias o nulas'( ) {
    when:
    def actual = service.registrarDevolucionesDeNotaVenta( idNotaVenta, devolucionesPagos )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | idNotaVenta | devolucionesPagos
    [ ]      | null        | null
    [ ]      | ' '         | null
    [ ]      | ' '         | [ : ]
    [ ]      | ' '         | [ ( 0 ): null ]
    [ ]      | ' '         | [ ( 1 ): null ]
    [ ]      | 'A'         | [ ( 0 ): null ]
  }

  def 'obtiene lista vacia al registrar devoluciones de notaVenta con pagos invalidos'( ) {
    given:
    String idNotaVenta = 'A'
    Integer idPago1 = 1
    Integer idPago2 = 2
    def devolucionesPagos = [
        ( idPago1 ): 'text',
        ( idPago2 ): 'text'
    ]

    when:
    def actual = service.registrarDevolucionesDeNotaVenta( idNotaVenta, devolucionesPagos )

    then:
    1 * modificacionRepository.findByIdFacturaAndTipo( idNotaVenta, 'can' ) >> [ new Modificacion( id: 1 ) ]
    1 * pagoRepository.findOne( idPago1 ) >> pago1
    _ * pagoRepository.findOne( idPago2 ) >> pago2

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | pago1             | pago2
    [ ]      | null              | null
    [ ]      | new Pago()        | null
    [ ]      | new Pago()        | new Pago()
    [ ]      | new Pago( id: 1 ) | null
    [ ]      | new Pago( id: 1 ) | new Pago()
  }

  def 'obtiene lista vacia al registrar devoluciones de notaVenta con cancelacion invalida'( ) {
    given:
    String idNotaVenta = 'A'
    Integer idPago1 = 1
    Integer idPago2 = 2
    def devolucionesPagos = [
        ( idPago1 ): 'text',
        ( idPago2 ): 'text'
    ]

    when:
    def actual = service.registrarDevolucionesDeNotaVenta( idNotaVenta, devolucionesPagos )

    then:
    1 * modificacionRepository.findByIdFacturaAndTipo( idNotaVenta, 'can' ) >> mods

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | mods
    [ ]      | null
    [ ]      | [ ]
    [ ]      | [ null ]
    [ ]      | [ new Modificacion() ]
    [ ]      | [ new Modificacion( id: 0 ) ]
    [ ]      | [ new Modificacion( id: 0 ), new Modificacion( id: 1 ) ]
  }

  def 'obtiene lista de devoluciones registradas al registrar devoluciones de notaVenta'( ) {
    given:
    String idNotaVenta = 'A'
    Integer idPago1 = 1
    Integer idPago2 = 2
    Integer idPago3 = 3
    Integer idPago4 = 4
    Integer idMod = 6
    def devolucionesPagos = [
        ( idPago1 ): 'EFECTIVO',
        ( idPago2 ): 'ORIGINAL',
        ( idPago3 ): 'ORIGINAL',
        ( idPago4 ): 'ORIGINAL'
    ]
    def pago1 = new Pago(
        id: idPago1,
        idFactura: idNotaVenta,
        porDevolver: 100,
        idFPago: 'EF'
    )
    def pago2 = new Pago(
        id: idPago2,
        idFactura: idNotaVenta,
        porDevolver: 200,
        idFPago: 'TD',
        idBancoEmisor: 1
    )
    def pago3 = new Pago(
        id: idPago3,
        idFactura: idNotaVenta,
        porDevolver: 300,
        idFPago: 'TC',
        idBancoEmisor: 2
    )
    def pago4 = new Pago(
        id: idPago4,
        idFactura: idNotaVenta,
        porDevolver: 400,
        idFPago: 'TR',
        clave: '1234'
    )
    def devoluciones = [
        new Devolucion(
            id: 1,
            idMod: idMod,
            idPago: idPago1,
            idFormaPago: 'EF',
            monto: 100,
            tipo: 'd'
        ),
        new Devolucion(
            id: 2,
            idMod: idMod,
            idPago: idPago2,
            idFormaPago: 'TD',
            idBanco: 1,
            monto: 200,
            tipo: 'd'
        ),
        new Devolucion(
            id: 3,
            idMod: idMod,
            idPago: idPago3,
            idFormaPago: 'TC',
            idBanco: 2,
            monto: 300,
            tipo: 'd'
        ),
        new Devolucion(
            id: 4,
            idMod: idMod,
            idPago: idPago4,
            idFormaPago: '1234',
            monto: 400,
            tipo: 'd'
        )
    ]

    when:
    def actual = service.registrarDevolucionesDeNotaVenta( idNotaVenta, devolucionesPagos )

    then:
    1 * modificacionRepository.findByIdFacturaAndTipo( idNotaVenta, 'can' ) >> [ new Modificacion( id: idMod ) ]
    1 * pagoRepository.findOne( idPago1 ) >> pago1
    1 * pagoRepository.findOne( idPago2 ) >> pago2
    1 * pagoRepository.findOne( idPago3 ) >> pago3
    1 * pagoRepository.findOne( idPago4 ) >> pago4
    1 * pagoRepository.save( { pmts ->
      pmts.each { Pago pmt ->
        assert pmt.porDevolver == 0
      }
      return pmts
    } )
    1 * devolucionRepository.save( { devs ->
      devs.eachWithIndex { Devolucion dev, Integer idx ->
        dev.id = idx + 1
        assertDevolucionEquals( devoluciones.get( idx ), dev )
      }
      return devs
    } ) >> devoluciones

    then:
    0 * _

    then:
    actual.eachWithIndex { Devolucion dev, Integer idx ->
      assertDevolucionEquals( devoluciones.get( idx ), dev )
    }
  }

  def 'obtiene lista vacia al registrar transferencias para notaVenta con ids de notaVenta y/o transferenciasPagos nulos o vacios'( ) {
    when:
    def actual = service.registrarTransferenciasParaNotaVenta( idOrigen, idDestino, devolucionesPagos )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | idOrigen | idDestino | devolucionesPagos
    [ ]      | null     | null      | null
    [ ]      | null     | ' '       | null
    [ ]      | ' '      | null      | null
    [ ]      | ' '      | ' '       | null
    [ ]      | ' '      | ' '       | [ : ]
    [ ]      | ' '      | ' '       | [ ( 0 ): null ]
    [ ]      | 'A'      | ' '       | [ ( 0 ): null ]
    [ ]      | ' '      | 'A'       | [ ( 0 ): null ]
    [ ]      | 'A'      | 'A'       | [ ( 0 ): null ]
    [ ]      | ' '      | ' '       | [ ( 1 ): null ]
    [ ]      | 'A'      | ' '       | [ ( 1 ): null ]
    [ ]      | ' '      | 'A'       | [ ( 1 ): null ]
  }

  def 'obtiene lista vacia al registrar transferencias para notaVenta con notaVenta origen/destino y/o cancelacion invalidos'( ) {
    given:
    def idOrigen = 'A'
    def idDestino = 'B'
    def devolucionesPagos = [
        ( 1 ): 1000,
        ( 2 ): 2000
    ]

    when:
    def actual = service.registrarTransferenciasParaNotaVenta( idOrigen, idDestino, devolucionesPagos )

    then:
    1 * notaVentaService.obtenerNotaVenta( idDestino ) >> destino
    1 * modificacionRepository.findByIdFacturaAndTipo( idOrigen, 'can' ) >> mods

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | origen                   | destino                  | mods
    [ ]      | null                     | null                     | null
    [ ]      | new NotaVenta()          | new NotaVenta()          | null
    [ ]      | new NotaVenta( id: 'A' ) | new NotaVenta( id: 'B' ) | null
    [ ]      | new NotaVenta( id: 'A' ) | new NotaVenta( id: 'B' ) | [ ]
    [ ]      | new NotaVenta( id: 'A' ) | new NotaVenta( id: 'B' ) | [ null ]
    [ ]      | new NotaVenta( id: 'A' ) | new NotaVenta( id: 'B' ) | [ new Modificacion() ]
    [ ]      | new NotaVenta( id: 'A' ) | new NotaVenta( id: 'B' ) | [ new Modificacion( id: 0 ) ]
    [ ]      | new NotaVenta( id: 'A' ) | new NotaVenta( id: 'B' ) | [ new Modificacion( id: 0 ), new Modificacion( id: 1 ) ]
  }

  def 'obtiene lista vacia al registrar transferencias para notaVenta con pagos invalidos'( ) {
    given:
    def idOrigen = 'A'
    def idDestino = 'B'
    Integer idPago1 = 1
    Integer idPago2 = 2
    Integer idMod = 3
    def devolucionesPagos = [
        ( idPago1 ): 1000,
        ( idPago2 ): 2000
    ]
    def destino = new NotaVenta(
        id: idDestino,
        fechaHoraFactura: new Date()
    )
    def modificacion = new Modificacion(
        id: idMod
    )

    when:
    def actual = service.registrarTransferenciasParaNotaVenta( idOrigen, idDestino, devolucionesPagos )

    then:
    1 * notaVentaService.obtenerNotaVenta( idDestino ) >> destino
    1 * modificacionRepository.findByIdFacturaAndTipo( idOrigen, 'can' ) >> [ modificacion ]
    1 * pagoRepository.findOne( idPago1 ) >> pago1
    _ * pagoRepository.findOne( idPago2 ) >> pago2

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | pago1                                | pago2
    [ ]      | null                                 | null
    [ ]      | new Pago()                           | null
    [ ]      | new Pago()                           | new Pago()
    [ ]      | new Pago( id: 1, porDevolver: 1000 ) | null
    [ ]      | new Pago( id: 1, porDevolver: 1000 ) | new Pago()
  }

  def 'obtiene lista vacia al registrar transferencias para notaVenta con pagos de transferencias invalidos'( ) {
    given:
    def idOrigen = 'A'
    def idDestino = 'B'
    Integer idPago1 = 1
    Integer idPago2 = 2
    Integer idMod = 3
    def devolucionesPagos = [
        ( idPago1 ): 1000,
        ( idPago2 ): 2000
    ]
    def destino = new NotaVenta(
        id: idDestino,
        fechaHoraFactura: new Date()
    )
    def modificacion = new Modificacion(
        id: idMod
    )
    def pago1 = new Pago(
        id: idPago1,
        porDevolver: 1000
    )
    def pago2 = new Pago(
        id: idPago2,
        porDevolver: 2000
    )

    when:
    def actual = service.registrarTransferenciasParaNotaVenta( idOrigen, idDestino, devolucionesPagos )

    then:
    1 * notaVentaService.obtenerNotaVenta( idDestino ) >> destino
    1 * modificacionRepository.findByIdFacturaAndTipo( idOrigen, 'can' ) >> [ modificacion ]
    1 * pagoRepository.findOne( idPago1 ) >> pago1
    1 * pagoRepository.findOne( idPago2 ) >> pago2
    1 * pagoRepository.save( [ pago1, pago2 ] )
    1 * pagoRepository.save( _ ) >> transferencias

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | transferencias
    [ ]      | null
    [ ]      | [ ]
    [ ]      | [ new Pago(), new Pago() ]
    [ ]      | [ new Pago( referenciaClave: 'A:1' ), new Pago( referenciaClave: 'A:2' ) ]
    [ ]      | [ new Pago( id: 3, referenciaClave: 'A:1' ), new Pago() ]
    [ ]      | [ new Pago( id: 5, referenciaClave: 'B:1' ), new Pago( id: 4, referenciaClave: 'A:2' ) ]
  }

  def 'obtiene lista de transferencias registradas al registrar transferencias para notaVenta'( ) {
    given:
    def idOrigen = 'A'
    def idDestino = 'B'
    Integer idPago1 = 1
    Integer idPago2 = 2
    Integer idMod = 3
    Integer idSucursal = 1
    String idEmpleado = '0'
    def devolucionesPagos = [
        ( idPago1 ): 1000,
        ( idPago2 ): 2000
    ]
    def expectedNotaVenta = new NotaVenta(
        id: idDestino,
        fechaHoraFactura: new Date(),
        idEmpleado: idEmpleado
    )
    def destino = new NotaVenta(
        id: idDestino,
        fechaHoraFactura: new Date(),
        idEmpleado: idEmpleado
    )
    def modificacion = new Modificacion(
        id: idMod
    )
    def pago1 = new Pago(
        id: idPago1,
        idFormaPago: 'EF',
        idSucursal: idSucursal,
        idFPago: 'EF',
        porDevolver: 1500
    )
    def pago2 = new Pago(
        id: idPago2,
        idFormaPago: 'TC',
        idSucursal: idSucursal,
        idFPago: 'TC',
        idBancoEmisor: '1',
        idTerminal: '0001',
        idPlan: '6M',
        porDevolver: 2500
    )
    def transferencias = [
        new Pago(
            id: 3,
            idFactura: idDestino,
            idFormaPago: 'EF',
            referenciaPago: idOrigen,
            monto: 1000,
            idEmpleado: idEmpleado,
            idSucursal: idSucursal,
            idFPago: 'TR',
            clave: 'EF',
            referenciaClave: 'A:1',
            idSync: '2',
            tipoPago: 'a'
        ),
        new Pago(
            id: 4,
            idFactura: idDestino,
            idFormaPago: 'TC',
            referenciaPago: idOrigen,
            monto: 2000,
            idEmpleado: idEmpleado,
            idSucursal: idSucursal,
            idFPago: 'TR',
            clave: 'TC',
            referenciaClave: 'A:2',
            idSync: '2',
            tipoPago: 'a',
            idBancoEmisor: '1',
            idTerminal: '0001',
            idPlan: '6M'
        )
    ]
    def devoluciones = [
        new Devolucion(
            id: 1,
            idMod: idMod,
            idPago: idPago1,
            idFormaPago: 'EF',
            monto: 1000,
            tipo: 't',
            transf: idDestino,
            referencia: 3
        ),
        new Devolucion(
            id: 2,
            idMod: idMod,
            idPago: idPago2,
            idFormaPago: 'TC',
            idBanco: 1,
            monto: 2000,
            tipo: 't',
            transf: idDestino,
            referencia: 4
        )
    ]

    when:
    def actual = service.registrarTransferenciasParaNotaVenta( idOrigen, idDestino, devolucionesPagos )

    then:
    1 * notaVentaService.obtenerNotaVenta( idDestino ) >> destino
    1 * modificacionRepository.findByIdFacturaAndTipo( idOrigen, 'can' ) >> [ modificacion ]
    1 * pagoRepository.findOne( idPago1 ) >> pago1
    1 * pagoRepository.findOne( idPago2 ) >> pago2
    1 * pagoRepository.save( [ pago1, pago2 ] )
    1 * pagoRepository.save( { trans ->
      trans.eachWithIndex { Pago tmp, Integer idx ->
        tmp.id = idx + 3
        assertPagoEquals( transferencias.get( idx ), tmp )
      }
      return trans
    } ) >> transferencias
    1 * devolucionRepository.save( { devs ->
      devs.eachWithIndex { Devolucion dev, Integer idx ->
        dev.id = idx + 1
        assertDevolucionEquals( devoluciones.get( idx ), dev )
      }
      return devs
    } ) >> devoluciones
    1 * notaVentaService.registrarNotaVenta( { NotaVenta tmp ->
      assertNotaVentaEquals( expectedNotaVenta, tmp )
      return tmp
    } as NotaVenta )

    then:
    0 * _

    then:
    actual.any()
  }

  def 'obtiene lista vacia al obtener notasVenta origen de notaVenta con parametros invalidos'( ) {
    when:
    def actual = service.listarNotasVentaOrigenDeNotaVenta( idNotaVenta )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | idNotaVenta
    [ ]      | null
    [ ]      | ' '
  }

  def 'obtiene lista vacia al obtener notasVenta origen de notaVenta con notaVenta sin transferencias'( ) {
    when:
    def actual = service.listarNotasVentaOrigenDeNotaVenta( 'A' )

    then:
    1 * pagoRepository.findAll( _ as Predicate ) >> transferencias

    then:
    0 * _

    then:
    actual == [ ]

    where:
    transferencias << [ null, [ ], [ null ] ]
  }

  def 'obtiene lista de notasVenta al obtener notasVenta origen de notaVenta'( ) {
    given:
    def idOrigen1 = 'A'
    def idOrigen2 = 'B'
    def idDestino = 'C'
    def transferencias = [
        new Pago( id: 1, idFactura: idDestino, referenciaPago: idOrigen1 ),
        new Pago( id: 2, idFactura: idDestino, referenciaPago: idOrigen2 )
    ]
    def nota1 = new NotaVenta( id: idOrigen1 )
    def nota2 = new NotaVenta( id: idOrigen2 )

    when:
    def actual = service.listarNotasVentaOrigenDeNotaVenta( idDestino )

    then:
    1 * pagoRepository.findAll( _ as Predicate ) >> transferencias
    1 * notaVentaRepository.findAll( _ as Predicate, _ as OrderSpecifier ) >> [ nota1, nota2 ]

    then:
    0 * _

    then:
    actual.first().id == idOrigen1
    actual.last().id == idOrigen2
  }

  def 'obtiene null al obtener credito de notaVenta con parametros invalidos'( ) {
    when:
    def actual = service.obtenerCreditoDeNotaVenta( idNotaVenta )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | idNotaVenta
    null     | null
    null     | ' '
  }

  def 'obtiene null al obtener credito de notaVenta con notaVenta no cancelada'( ) {
    given:
    def idNotaVenta = 'A'

    when:
    def actual = service.obtenerCreditoDeNotaVenta( idNotaVenta )

    then:
    1 * modificacionRepository.findByIdFacturaAndTipo( idNotaVenta, 'can' )

    then:
    0 * _

    then:
    actual == null
  }

  def 'obtiene credito en 0 al obtener credito de notaVenta con notaVenta sin saldo por devover'( ) {
    given:
    def idNotaVenta = 'A'

    when:
    def actual = service.obtenerCreditoDeNotaVenta( idNotaVenta )

    then:
    1 * modificacionRepository.findByIdFacturaAndTipo( idNotaVenta, 'can' ) >> [ new Modificacion( id: 1 ) ]
    1 * pagoRepository.findByIdFactura( idNotaVenta ) >> [ new Pago( porDevolver: 0 ), new Pago( porDevolver: 0 ) ]

    then:
    0 * _

    then:
    actual == 0
  }

  def 'obtiene credito positivo al obtener credito de notaVenta'( ) {
    given:
    def idNotaVenta = 'A'

    when:
    def actual = service.obtenerCreditoDeNotaVenta( idNotaVenta )

    then:
    1 * modificacionRepository.findByIdFacturaAndTipo( idNotaVenta, 'can' ) >> [ new Modificacion( id: 1 ) ]
    1 * pagoRepository.findByIdFactura( idNotaVenta ) >> [ new Pago( porDevolver: 333.33 ), new Pago( porDevolver: 999.99 ) ]

    then:
    0 * _

    then:
    actual == 1333.32
  }
}
