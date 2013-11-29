package mx.lux.pos.service.impl

import org.springframework.core.io.ClassPathResource
import spock.lang.Specification
import mx.lux.pos.model.*
import mx.lux.pos.repository.*

import static mx.lux.pos.assertions.Assert.assertComprobanteEquals
import static mx.lux.pos.assertions.Assert.assertDetalleComprobanteEquals

class ComprobanteServiceImplTest extends Specification {

  private ComprobanteServiceImpl service
  private ComprobanteRepository comprobanteRepository
  private DetalleComprobanteRepository detalleComprobanteRepository
  private NotaVentaRepository notaVentaRepository
  private DetalleNotaVentaRepository detalleNotaVentaRepository
  private ArticuloRepository articuloRepository
  private PagoRepository pagoRepository
  private SucursalRepository sucursalRepository
  private ParametroRepository parametroRepository
  private ReimpresionRepository reimpresionRepository

  def setup( ) {
    comprobanteRepository = Mock( ComprobanteRepository )
    detalleComprobanteRepository = Mock( DetalleComprobanteRepository )
    notaVentaRepository = Mock( NotaVentaRepository )
    detalleNotaVentaRepository = Mock( DetalleNotaVentaRepository )
    articuloRepository = Mock( ArticuloRepository )
    pagoRepository = Mock( PagoRepository )
    sucursalRepository = Mock( SucursalRepository )
    parametroRepository = Mock( ParametroRepository )
    reimpresionRepository = Mock( ReimpresionRepository )
    service = new ComprobanteServiceImpl(
        comprobanteRepository: comprobanteRepository,
        detalleComprobanteRepository: detalleComprobanteRepository,
        notaVentaRepository: notaVentaRepository,
        detalleNotaVentaRepository: detalleNotaVentaRepository,
        articuloRepository: articuloRepository,
        pagoRepository: pagoRepository,
        sucursalRepository: sucursalRepository,
        parametroRepository: parametroRepository,
        reimpresionRepository: reimpresionRepository
    )
  }

  def 'es null al obtener comprobante con idFiscal invalido'( ) {
    when:
    def actual = service.obtenerComprobante( idFiscal )

    then:
    _ * comprobanteRepository.findByIdFiscal( _ )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | idFiscal
    null     | null
    null     | ' '
    null     | '-'
  }

  def 'es comprobante al obtener comprobante'( ) {
    given:
    def idFiscal = '1'
    def expected = new Comprobante(
        id: 1,
        idFiscal: idFiscal,
        fechaImpresion: new Date()
    )
    def comprobante = new Comprobante(
        id: 1,
        idFiscal: idFiscal,
        fechaImpresion: new Date()
    )

    when:
    def actual = service.obtenerComprobante( idFiscal )

    then:
    _ * comprobanteRepository.findByIdFiscal( idFiscal ) >> comprobante

    then:
    0 * _

    then:
    assertComprobanteEquals( expected, actual )
  }

  def 'es false al validar ticket con ticket invalido'( ) {
    when:
    def actual = service.esTicketValido( ticket )

    then:
    !actual

    where:
    ticket << [ null, ' ', 'text', '-', '-text', 'text-' ]
  }

  def 'es true al validar ticket'( ) {
    when:
    def actual = service.esTicketValido( 'text-text' )

    then:
    actual
  }

  def 'es lista vacia al listar comprobantes por ticket con ticket invalido'( ) {
    when:
    def actual = service.listarComprobantesPorTicket( ticket )

    then:
    _ * comprobanteRepository.findByTicketOrderByFechaImpresionDesc( ticket )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | ticket
    [ ]      | null
    [ ]      | ' '
    [ ]      | '-'
    [ ]      | '-text'
    [ ]      | 'text-'
  }

  def 'es lista al listar comprobantes por ticket'( ) {
    given:
    def ticket = 'text-text'
    def idC = 1
    def expected = new Comprobante(
        id: idC,
        ticket: ticket
    )
    def comprobantes = [
        new Comprobante(
            id: idC,
            ticket: ticket
        )
    ]

    when:
    def actual = service.listarComprobantesPorTicket( ticket )

    then:
    1 * comprobanteRepository.findByTicketOrderByFechaImpresionDesc( ticket ) >> comprobantes

    then:
    0 * _

    then:
    assertComprobanteEquals( expected, actual.first() )
  }

  def 'es null al generar parametros servicio web con parametros invalidos'( ) {
    when:
    def actual = service.generarParametrosServicioWeb( comprobante, detalles )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | comprobante                            | detalles
    null     | null                                   | null
    null     | new Comprobante()                      | null
    null     | new Comprobante( ticket: ' ' )         | null
    null     | new Comprobante( ticket: 'text' )      | null
    null     | new Comprobante( ticket: '-text' )     | null
    null     | new Comprobante( ticket: 'text-' )     | null
    null     | new Comprobante( ticket: 'text-text' ) | null
    null     | new Comprobante( ticket: 'text-text' ) | [ ]
    null     | new Comprobante( ticket: 'text-text' ) | [ null ]
  }

  def 'es cadena failsafe al generar parametros servicio web'( ) {
    given:
    def ticket = '9999-999999'
    def expected = '|9999-999999|ingreso||0.00|0.00||0.00|||||||||||||N|>|||0.00|0.00|>'
    def idParamEmpresa = TipoParametro.EMP_ELECTRONICO.value
    def idParamTasa = TipoParametro.IVA_VIGENTE.value
    def comprobante = new Comprobante(
        ticket: ticket
    )
    def detalles = [ new DetalleComprobante() ]

    when:
    def actual = service.generarParametrosServicioWeb( comprobante, detalles )

    then:
    1 * sucursalRepository.getCurrentSucursalId()
    1 * parametroRepository.findOne( idParamEmpresa )
    1 * parametroRepository.findOne( idParamTasa )

    then:
    0 * _

    then:
    actual == expected
  }

  def 'es cadena parametros al generar parametros servicio web'( ) {
    given:
    def ticket = '9999-999999'
    def expected = '9999|9999-999999|ingreso|UNA SOLA EXHIBICION|10.00|11.60|EFECTIVO|1.60|16|XAXX010101000|' +
        'PRUEBA PRUEBA PRUEBA|CALLE PRUEBA N 1|COLONIA PRUEBA|BENITO JUAREZ|DF|MEXICO|03100|' +
        'prueba@prueba.com||9999|N|>1|ARTICULO|DESCRIPCION ARTICULO|10.00|10.00|>'
    def idFactura = 'A'
    def idSucursal = 9999
    def idParamEmpresa = TipoParametro.EMP_ELECTRONICO.value
    def idParamTasa = TipoParametro.IVA_VIGENTE.value
    def comprobante = new Comprobante(
        rfc: 'XAXX010101000',
        ticket: ticket,
        idFactura: idFactura,
        formaPago: 'UNA SOLA EXHIBICION',
        metodoPago: 'EFECTIVO',
        estatus: 'N',
        razon: 'PRUEBA PRUEBA PRUEBA',
        calle: 'CALLE PRUEBA N 1',
        colonia: 'COLONIA PRUEBA',
        municipio: 'BENITO JUAREZ',
        estado: 'DF',
        pais: 'MEXICO',
        codigoPostal: '03100',
        importe: '$11.60',
        subtotal: 10,
        impuestos: 1.6,
        email: 'prueba@prueba.com'
    )
    def detalles = [
        new DetalleComprobante(
            cantidad: 1,
            articulo: 'ARTICULO',
            descripcion: 'DESCRIPCION ARTICULO',
            precioUnitario: 10,
            importe: 10
        )
    ]

    when:
    def actual = service.generarParametrosServicioWeb( comprobante, detalles )

    then:
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * parametroRepository.findOne( idParamEmpresa ) >> new Parametro( id: idParamEmpresa, valor: '9999' )
    1 * parametroRepository.findOne( idParamTasa ) >> new Parametro( id: idParamTasa, valor: '16' )

    then:
    0 * _

    then:
    actual == expected
  }

  def 'es null al solicitar comprobante servicio web con parametros invalidos'( ) {
    when:
    def actual = service.solicitarComprobanteServicioWeb( comprobante, detalles )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | comprobante                            | detalles
    null     | null                                   | null
    null     | new Comprobante()                      | null
    null     | new Comprobante( ticket: ' ' )         | null
    null     | new Comprobante( ticket: 'text' )      | null
    null     | new Comprobante( ticket: '-text' )     | null
    null     | new Comprobante( ticket: 'text-' )     | null
    null     | new Comprobante( ticket: 'text-text' ) | null
    null     | new Comprobante( ticket: 'text-text' ) | [ ]
    null     | new Comprobante( ticket: 'text-text' ) | [ null ]
  }

  def 'es null al solicitar comprobante servicio web con parametro url invalido'( ) {
    given:
    def comprobante = new Comprobante( ticket: 'text-text' )
    def detalles = [ new DetalleComprobante() ]
    def idParametro = TipoParametro.PIDE_FACTURA.value

    when:
    def actual = service.solicitarComprobanteServicioWeb( comprobante, detalles )

    then:
    1 * parametroRepository.findOne( idParametro ) >> parametroUrl

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | parametroUrl
    null     | null
    null     | new Parametro()
    null     | new Parametro( valor: ' ' )
  }

  def 'es comprobante al solicitar comprobante servicio web'( ) {
    given:
    def ticket = '9999-999999'
    def idFactura = 'A1'
    def idSucursal = 9999
    def idParamURL = TipoParametro.PIDE_FACTURA.value
    def valorURL = new ClassPathResource( 'cfd-result.txt' ).URL
    def idParamEmpresa = TipoParametro.EMP_ELECTRONICO.value
    def idParamTasa = TipoParametro.IVA_VIGENTE.value
    def expected = new Comprobante(
        idFiscal: '100',
        url: 'http://cfdv2.lux.mx/cfdv2/9999-PRUE-100/9999-999999.pdf',
        xml: 'http://cfdv2.lux.mx/cfdv2/9999-PRUE-100/9999-999999.xml',
        rfc: 'XAXX010101000',
        ticket: ticket,
        idFactura: idFactura,
        formaPago: 'UNA SOLA EXHIBICION',
        metodoPago: 'EFECTIVO',
        estatus: 'N',
        razon: 'PRUEBA PRUEBA PRUEBA',
        calle: 'CALLE PRUEBA N 1',
        colonia: 'COLONIA PRUEBA',
        municipio: 'BENITO JUAREZ',
        estado: 'DF',
        pais: 'MEXICO',
        codigoPostal: '03100',
        importe: '$11.60',
        subtotal: 10,
        impuestos: 1.6,
        email: 'prueba@prueba.com'
    )
    def comprobante = new Comprobante(
        rfc: 'XAXX010101000',
        ticket: ticket,
        idFactura: idFactura,
        formaPago: 'UNA SOLA EXHIBICION',
        metodoPago: 'EFECTIVO',
        estatus: 'N',
        razon: 'PRUEBA PRUEBA PRUEBA',
        calle: 'CALLE PRUEBA N 1',
        colonia: 'COLONIA PRUEBA',
        municipio: 'BENITO JUAREZ',
        estado: 'DF',
        pais: 'MEXICO',
        codigoPostal: '03100',
        importe: '$11.60',
        subtotal: 10,
        impuestos: 1.6,
        email: 'prueba@prueba.com'
    )
    def detalles = [
        new DetalleComprobante(
            cantidad: 1,
            articulo: 'ARTICULO',
            descripcion: 'DESCRIPCION ARTICULO',
            precioUnitario: 10,
            importe: 10
        )
    ]

    when:
    def actual = service.solicitarComprobanteServicioWeb( comprobante, detalles )

    then:
    1 * parametroRepository.findOne( idParamURL ) >> new Parametro( id: idParamURL, valor: valorURL )
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * parametroRepository.findOne( idParamEmpresa ) >> new Parametro( id: idParamEmpresa, valor: '9999' )
    1 * parametroRepository.findOne( idParamTasa ) >> new Parametro( id: idParamTasa, valor: '16' )

    then:
    0 * _

    then:
    assertComprobanteEquals( expected, actual )
  }

  def 'es null al registrar comprobante fiscal con comprobante invalido'( ) {
    when:
    def actual = service.registrarComprobante( comprobante )

    then:
    _ * notaVentaRepository.findOne( _ as String )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | comprobante
    null     | null
    null     | new Comprobante()
    null     | new Comprobante( ticket: '-' )
    null     | new Comprobante( ticket: 'text-' )
    null     | new Comprobante( ticket: '-text' )
    null     | new Comprobante( ticket: 'text-text' )
    null     | new Comprobante( ticket: 'text-text', idFactura: ' ' )
    null     | new Comprobante( ticket: 'text-text', idFactura: '-' )
    null     | new Comprobante( ticket: 'text-text', idFactura: 'text' )
  }

  def 'es comprobante al registrar comprobante fiscal con comprobante nuevo'( ) {
    given:
    def rfc = 'XAXX010101000'
    def ticket = '9999-999999'
    def idFactura = 'A1'
    def factura = '999999'
    def idParamTasa = TipoParametro.IVA_VIGENTE.value
    def idSucursal = 9999
    def idParamURL = TipoParametro.PIDE_FACTURA.value
    def valorURL = new ClassPathResource( 'cfd-result.txt' ).URL
    def idParamEmpresa = TipoParametro.EMP_ELECTRONICO.value
    def expectedDetalle = new DetalleComprobante(
        id: 1,
        idFiscal: '100',
        idArticulo: 10,
        articulo: 'XX',
        idGenerico: 'A',
        descripcion: 'XX',
        cantidad: 2,
        precioUnitario: 12.06,
        importe: 24.12
    )
    def expected = new Comprobante(
        id: 1,
        idFiscal: '100',
        rfc: rfc,
        ticket: ticket,
        idEmpleado: '9999',
        idFactura: idFactura,
        factura: factura,
        tipo: 'O',
        idCliente: 9,
        razon: 'text',
        estatus: 'N',
        tipoFactura: 'N',
        calle: 'text',
        colonia: 'text',
        municipio: 'text',
        estado: 'text',
        pais: 'text',
        codigoPostal: 'text',
        importe: '$27.98',
        subtotal: 24.121,
        impuestos: 3.859,
        email: 'text',
        url: 'http://cfdv2.lux.mx/cfdv2/9999-PRUE-100/9999-999999.pdf',
        xml: 'http://cfdv2.lux.mx/cfdv2/9999-PRUE-100/9999-999999.xml',
        rx: '0',
        paciente: '0',
        formaPago: 'UNA SOLA EXHIBICION',
        metodoPago: 'TARJETA CREDITO 1234',
        detalles: [ expectedDetalle ]
    )
    def comprobante = new Comprobante(
        rfc: rfc,
        ticket: ticket,
        idEmpleado: '9999',
        idFactura: idFactura,
        razon: 'text',
        tipoFactura: 'N',
        calle: 'text',
        colonia: 'text',
        municipio: 'text',
        estado: 'text',
        pais: 'text',
        codigoPostal: 'text',
        email: 'text',
        rx: '0',
        paciente: '0'
    )
    def notaVenta = new NotaVenta(
        id: idFactura,
        factura: factura,
        idCliente: 9,
        ventaNeta: 27.98
    )
    def pago = new Pago(
        id: 1,
        idFormaPago: 'TC',
        referenciaPago: '1234',
        eTipoPago: new TipoPago(
            descripcion: 'TARJETA CREDITO'
        )
    )
    def detalleNotaVenta = new DetalleNotaVenta(
        id: 1,
        idFactura: idFactura,
        idArticulo: 10,
        precioUnitFinal: 13.99,
        cantidadFac: 2
    )
    def articulo = new Articulo(
        id: 10,
        articulo: 'XX',
        idGenerico: 'A',
        descripcion: 'XX'
    )

    when:
    def actual = service.registrarComprobante( comprobante )

    then:
    1 * notaVentaRepository.findOne( idFactura ) >> notaVenta
    2 * parametroRepository.findOne( idParamTasa ) >> new Parametro( id: idParamTasa, valor: '16' )
    1 * comprobanteRepository.findByTicketOrderByFechaImpresionDesc( ticket ) >> null
    1 * pagoRepository.findByIdFacturaOrderByFechaAsc( idFactura ) >> [ pago ]
    1 * detalleNotaVentaRepository.findByIdFacturaOrderByIdArticuloAsc( idFactura ) >> [ detalleNotaVenta ]
    1 * articuloRepository.findOne( articulo.id ) >> articulo
    1 * parametroRepository.findOne( idParamURL ) >> new Parametro( id: idParamURL, valor: valorURL )
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * parametroRepository.findOne( idParamEmpresa ) >> new Parametro( id: idParamEmpresa, valor: '9999' )
    1 * comprobanteRepository.save( { Comprobante tmp ->
      tmp.id = 1
      assertComprobanteEquals( expected, tmp )
      return tmp
    } ) >> expected
    1 * detalleComprobanteRepository.save( { List<DetalleComprobante> dets ->
      dets.each { DetalleComprobante tmp ->
        tmp.id = 1
        assertDetalleComprobanteEquals( expectedDetalle, tmp )
      }
      return dets
    } )

    then:
    0 * _

    then:
    assertComprobanteEquals( expected, actual )
  }
}
