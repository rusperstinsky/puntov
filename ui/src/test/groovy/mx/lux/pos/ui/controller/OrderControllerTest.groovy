package mx.lux.pos.ui.controller

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.DetalleNotaVenta
import mx.lux.pos.model.NotaVenta
import mx.lux.pos.model.Pago
import spock.lang.Specification
import mx.lux.pos.service.*
import mx.lux.pos.ui.model.*

import static mx.lux.pos.ui.assertions.Assert.assertOrderEquals

class OrderControllerTest extends Specification {

  private OrderController controller
  private NotaVentaService notaVentaService
  private DetalleNotaVentaService detalleNotaVentaService
  private PagoService pagoService
  private TicketService ticketService
  private BancoService bancoService
  private InventarioService inventarioService

  def setup( ) {
    notaVentaService = Mock( NotaVentaService )
    detalleNotaVentaService = Mock( DetalleNotaVentaService )
    pagoService = Mock( PagoService )
    ticketService = Mock( TicketService )
    bancoService = Mock( BancoService )
    inventarioService = Mock( InventarioService )
    controller = new OrderController(
        notaVentaService,
        detalleNotaVentaService,
        pagoService,
        ticketService,
        bancoService,
        inventarioService
    )
  }

  def 'obtiene null al agregar item a la orden con parametros nulos o vacios'( ) {
    when:
    def actual = controller.addItemToOrder( orderId, item )

    then:
    0 * _

    then:
    assertOrderEquals( expected, actual )

    where:
    expected | orderId | item
    null     | null    | null
    null     | null    | new Item()
    null     | ' '     | new Item()
    null     | ' '     | new Item( id: 0 )
  }

  def 'obtiene orden actualizada al agregar item a la orden'( ) {
    given:
    def orderId = 'A1'
    def item = new Item( id: 1, price: 99.99 )
    def expected = new Order(
        id: orderId,
        status: 'N',
        total: 99.99,
        paid: 0,
        due: 99.99,
        items: [
            new OrderItem( quantity: 1, item: item )
        ]
    )
    def notaVenta = new NotaVenta(
        id: orderId,
        ventaNeta: 99.99,
        ventaTotal: 99.99,
        sumaPagos: 0,
        detalles: [
            new DetalleNotaVenta(
                id: 1,
                idArticulo: 1,
                precioUnitFinal: 99.99,
                cantidadFac: 1,
                articulo: new Articulo(
                    id: 1,
                    precio: 99.99
                )
            )
        ]
    )

    when:
    def actual = controller.addItemToOrder( orderId, item )

    then:
    1 * notaVentaService.obtenerNotaVenta( orderId ) >> new NotaVenta( id: orderId )
    1 * notaVentaService.registrarDetalleNotaVentaEnNotaVenta( _, _ ) >> notaVenta

    then:
    0 * _

    then:
    assertOrderEquals( expected, actual )
  }

  def 'obtiene orden actualizada al agregar item a la orden con pagos'( ) {
    given:
    def orderId = 'A1'
    def item = new Item( id: 1, price: 99.99 )
    def expected = new Order(
        id: orderId,
        status: 'N',
        total: 99.99,
        paid: 50,
        due: 49.99,
        items: [
            new OrderItem( quantity: 1, item: item )
        ]
    )
    def notaVenta = new NotaVenta(
        id: orderId,
        ventaNeta: 99.99,
        ventaTotal: 99.99,
        sumaPagos: 50,
        detalles: [
            new DetalleNotaVenta(
                id: 1,
                idArticulo: 1,
                precioUnitFinal: 99.99,
                cantidadFac: 1,
                articulo: new Articulo(
                    id: 1,
                    precio: 99.99
                )
            )
        ]
    )

    when:
    def actual = controller.addItemToOrder( orderId, item )

    then:
    1 * notaVentaService.obtenerNotaVenta( orderId ) >> new NotaVenta( id: orderId )
    1 * notaVentaService.registrarDetalleNotaVentaEnNotaVenta( _, _ ) >> notaVenta

    then:
    0 * _

    then:
    assertOrderEquals( expected, actual )
  }

  def 'obtiene orden actualizada al agregar item a la orden con el mismo item'( ) {
    given:
    def orderId = 'A1'
    def item = new Item( id: 1, price: 99.99 )
    def expected = new Order(
        id: orderId,
        status: 'N',
        total: 199.98,
        paid: 0,
        due: 199.98,
        items: [
            new OrderItem( quantity: 2, item: item )
        ]
    )
    def notaVenta = new NotaVenta(
        id: orderId,
        ventaNeta: 199.98,
        ventaTotal: 199.98,
        sumaPagos: 0,
        detalles: [
            new DetalleNotaVenta(
                id: 1,
                idArticulo: 1,
                precioUnitFinal: 99.99,
                cantidadFac: 2,
                articulo: new Articulo(
                    id: 1,
                    precio: 99.99
                )
            )
        ]
    )

    when:
    def actual = controller.addItemToOrder( orderId, item )

    then:
    1 * notaVentaService.obtenerNotaVenta( orderId ) >> new NotaVenta( id: orderId )
    1 * notaVentaService.registrarDetalleNotaVentaEnNotaVenta( _, _ ) >> notaVenta

    then:
    0 * _

    then:
    assertOrderEquals( expected, actual )
  }

  def 'obtiene orden actualizada al agregar item a la orden con item distinto'( ) {
    given:
    def orderId = 'A1'
    def item1 = new Item( id: 1, price: 99.99 )
    def item2 = new Item( id: 2, price: 33.33 )
    def expected = new Order(
        id: orderId,
        status: 'N',
        total: 133.32,
        paid: 0,
        due: 133.32,
        items: [
            new OrderItem( quantity: 1, item: item1 ),
            new OrderItem( quantity: 1, item: item2 )
        ]
    )
    def notaVenta = new NotaVenta(
        id: orderId,
        ventaNeta: 133.32,
        ventaTotal: 133.32,
        sumaPagos: 0,
        detalles: [
            new DetalleNotaVenta(
                id: 1,
                idArticulo: 1,
                precioUnitFinal: 99.99,
                cantidadFac: 1,
                articulo: new Articulo(
                    id: 1,
                    precio: 99.99
                )
            ),
            new DetalleNotaVenta(
                id: 2,
                idArticulo: 2,
                precioUnitFinal: 33.33,
                cantidadFac: 1,
                articulo: new Articulo(
                    id: 2,
                    precio: 33.33
                )
            )
        ]
    )

    when:
    def actual = controller.addItemToOrder( orderId, item2 )

    then:
    1 * notaVentaService.obtenerNotaVenta( orderId ) >> new NotaVenta( id: orderId )
    1 * notaVentaService.registrarDetalleNotaVentaEnNotaVenta( _, _ ) >> notaVenta

    then:
    0 * _

    then:
    assertOrderEquals( expected, actual )
  }

  def 'obtiene null al agregar pago a la orden con parametros nulos o vacios'( ) {
    when:
    def actual = controller.addPaymentToOrder( orderId, payment )

    then:
    0 * _

    then:
    assertOrderEquals( expected, actual )

    where:
    expected | orderId | payment
    null     | null    | null
    null     | null    | null
    null     | ' '     | new Payment()
    null     | ' '     | new Payment( amount: 0 )
    null     | ' '     | new Payment( paymentTypeId: ' ' )
    null     | ' '     | new Payment( amount: 1, paymentTypeId: ' ' )
    null     | ' '     | new Payment( amount: 0, paymentTypeId: 'EF' )
    null     | '-'     | new Payment( amount: 1, paymentTypeId: ' ' )
    null     | '-'     | new Payment( amount: 0, paymentTypeId: 'EF' )
  }

  def 'obtiene orden actualizada al agregar pago a la orden sin pagos'( ) {
    given:
    def orderId = 'A1'
    def payment = new Payment( amount: 999.99, paymentTypeId: 'EF' )
    def expected = new Order(
        id: orderId,
        status: 'N',
        total: 0,
        paid: 999.99,
        due: -999.99,
        payments: [
            new Payment(
                id: 1,
                amount: 999.99
            )
        ]
    )
    def notaVenta = new NotaVenta(
        id: orderId,
        ventaNeta: 0,
        ventaTotal: 0,
        sumaPagos: 999.99,
        pagos: [
            new Pago(
                id: 1,
                monto: 999.99
            )
        ]
    )

    when:
    def actual = controller.addPaymentToOrder( orderId, payment )

    then:
    1 * notaVentaService.registrarPagoEnNotaVenta( _, _ ) >> notaVenta

    then:
    0 * _

    then:
    assertOrderEquals( expected, actual )
  }

  def 'obtiene orden actualizada al agregar pago a la orden con pagos'( ) {
    given:
    def orderId = 'A1'
    Session.put( SessionItem.USER, new User( username: '0000' ) )
    def payment = new Payment( amount: 999.99, paymentTypeId: 'EF' )
    def expected = new Order(
        id: orderId,
        status: 'N',
        total: 10998.90,
        paid: 1333.32,
        due: 9665.58,
        payments: [
            new Payment(
                id: 1,
                amount: 999.99
            ),
            new Payment(
                id: 2,
                amount: 333.33
            )
        ]
    )
    def notaVenta = new NotaVenta(
        id: orderId,
        ventaNeta: 10998.90,
        ventaTotal: 10998.90,
        sumaPagos: 1333.32,
        pagos: [
            new Pago(
                id: 1,
                monto: 999.99
            ),
            new Pago(
                id: 2,
                monto: 333.33
            )
        ]
    )

    when:
    def actual = controller.addPaymentToOrder( orderId, payment )

    then:
    1 * notaVentaService.registrarPagoEnNotaVenta( _, _ ) >> notaVenta

    then:
    0 * _

    then:
    assertOrderEquals( expected, actual )
  }

  def 'obtiene null al registrar orden con orden invalida'( ) {
    when:
    def actual = controller.placeOrder( orden )

    then:
    0 * _

    then:
    assertOrderEquals( expected, actual )

    where:
    expected | orden
    null     | null
    null     | new Order()
    null     | new Order( id: ' ' )
    null     | new Order( customer: new Customer() )
    null     | new Order( id: ' ', customer: new Customer( id: 0 ) )
  }

  def 'obtiene null al registrar orden con notaVenta no existente'( ) {
    given:
    String orderId = 'A'
    def orden = new Order(
        id: orderId,
        customer: new Customer( id: 1 )
    )

    when:
    def actual = controller.placeOrder( orden )

    then:
    1 * notaVentaService.obtenerNotaVenta( orderId )

    then:
    0 * _

    then:
    assertOrderEquals( null, actual )
  }

  def 'obtiene orden al registrar orden con transaccion venta true o false'( ) {
    given:
    String orderId = 'A'
    Integer idCliente = 1
    Date fechaFactura = new Date()
    String comments = 'text'
    String factura = 'text'
    User user = new User( username: '0' )
    Session.put( SessionItem.USER, user )
    def expectedOrden = new Order(
        id: orderId,
        status: 'N',
        date: new Date(),
        bill: factura,
        comments: comments
    )
    def orden = new Order(
        id: orderId,
        customer: new Customer( id: idCliente ),
        comments: comments
    )
    def notaVenta = new NotaVenta(
        id: orderId,
        fechaHoraFactura: fechaFactura,
        idEmpleado: user.username,
        idCliente: idCliente,
        observacionesNv: comments,
        tipoNotaVenta: 'F',
        tipoDescuento: 'N',
        tipoEntrega: 'S',
        fExpideFactura: true,
        fechaEntrega: new Date(),
        horaEntrega: new Date(),
        empEntrego: user.username,
        factura: factura
    )

    when:
    def actual = controller.placeOrder( orden )

    then:
    1 * notaVentaService.obtenerNotaVenta( orderId ) >> notaVenta
    1 * notaVentaService.cerrarNotaVenta( notaVenta ) >> notaVenta
    1 * inventarioService.solicitarTransaccionVenta( notaVenta ) >> inventario

    then:
    0 * _

    then:
    assertOrderEquals( expectedOrden, actual )

    where:
    inventario << [ true, false ]
  }

  def 'obtiene orden al registrar orden con notaVenta valida y atributos validos'( ) {
    given:
    String orderId = 'A'
    Integer idCliente = 1
    Date fechaFactura = new Date()
    String idEmpleado = '0'
    String comments = 'text'
    String factura = 'text'
    Session.put( SessionItem.USER, new User( username: '0' ) )
    def expectedOrden = new Order(
        id: orderId,
        status: 'N',
        date: new Date(),
        bill: factura,
        comments: comments
    )
    def orden = new Order(
        id: orderId,
        customer: new Customer( id: idCliente ),
        comments: comments
    )
    def notaVenta = new NotaVenta(
        id: orderId,
        fechaHoraFactura: fechaFactura,
        idEmpleado: idEmpleado,
        idCliente: idCliente,
        observacionesNv: comments,
        tipoNotaVenta: 'F',
        tipoDescuento: 'N',
        tipoEntrega: 'S',
        fExpideFactura: true,
        fechaEntrega: new Date(),
        horaEntrega: new Date(),
        empEntrego: idEmpleado,
        factura: factura
    )

    when:
    def actual = controller.placeOrder( orden )

    then:
    1 * notaVentaService.obtenerNotaVenta( orderId ) >> notaVenta
    1 * notaVentaService.cerrarNotaVenta( notaVenta ) >> notaVenta
    1 * inventarioService.solicitarTransaccionVenta( notaVenta ) >> true

    then:
    0 * _

    then:
    assertOrderEquals( expectedOrden, actual )
  }

  def 'obtiene null al solicitar orden con orderId nulo o vacio'( ) {
    when:
    def actual = controller.getOrder( orderId )

    then:
    1 * notaVentaService.obtenerNotaVenta( orderId )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | orderId
    null     | null
    null     | ' '
  }

  def 'obtiene null al solicitar orden con orderId invalido'( ) {
    given:
    String orderId = 'A'

    when:
    def actual = controller.getOrder( orderId )

    then:
    1 * notaVentaService.obtenerNotaVenta( orderId )

    then:
    0 * _

    then:
    actual == null
  }

  def 'obtiene orden al solicitar orden con orderId valido'( ) {
    given:
    String orderId = 'A'
    Date date = new Date()
    def orderExpected = new Order(
        id: orderId,
        date: date,
        status: 'N'
    )
    def notaVenta = new NotaVenta(
        id: orderId,
        fechaHoraFactura: date
    )

    when:
    def actual = controller.getOrder( orderId )

    then:
    1 * notaVentaService.obtenerNotaVenta( orderId ) >> notaVenta
    1 * detalleNotaVentaService.listarDetallesNotaVentaPorIdFactura( orderId )
    1 * pagoService.listarPagosPorIdFactura( orderId )

    then:
    0 * _

    then:
    assertOrderEquals( orderExpected, actual )
  }
}
