package mx.lux.pos.ui.controller

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.ListaPrecios
import mx.lux.pos.model.Sucursal
import mx.lux.pos.service.ListaPreciosService
import mx.lux.pos.service.SucursalService
import mx.lux.pos.service.TicketService
import mx.lux.pos.ui.model.Item
import mx.lux.pos.ui.model.PriceList
import mx.lux.pos.ui.model.PriceListLoadType
import spock.lang.Shared
import spock.lang.Specification

import static mx.lux.pos.ui.assertions.Assert.assertPriceListEquals

class PriceListControllerTest extends Specification {

  private PriceListController controller
  private ListaPreciosService listaPreciosService
  private SucursalService sucursalService
  private TicketService ticketService
  @Shared private File receiveDir = new File( System.getProperty( 'java.io.tmpdir' ), 'por_recibir' )
  @Shared private File priceListDir = new File( System.getProperty( 'java.io.tmpdir' ), 'lp' )
  @Shared private File receivedDir = new File( System.getProperty( 'java.io.tmpdir' ), 'recibidos' )

  def setup( ) {
    listaPreciosService = Mock( ListaPreciosService )
    sucursalService = Mock( SucursalService )
    ticketService = Mock( TicketService )
    receiveDir.mkdir()
    priceListDir.mkdir()
    receivedDir.mkdir()
    controller = new PriceListController( listaPreciosService, sucursalService, ticketService )
    controller.receivePath = receiveDir.path
    controller.priceListPath = priceListDir.path
    controller.receivedPath = receivedDir.path
  }

  def 'obtiene null al inicializar lista de precios con parametros nulos o vacios'( ) {
    expect:
    expected == controller.initializePriceList( header, filename )

    where:
    expected | header | filename
    null     | null   | null
    null     | null   | ' '
    null     | ' '    | null
    null     | ' '    | ' '
  }

  def 'obtiene valor al inicializar lista de precios'( ) {
    given:
    def header = '1549|12/06/2012|18/06/2012|0|'
    def filename = '3_1549_12-06-2012.LP'
    def expected = new PriceList(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        activated: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        autoActivated: Date.parse( 'dd/MM/yyyy', '18/06/2012' ),
        branch: '0'
    )

    when:
    def actual = controller.initializePriceList( header, filename )

    then:
    0 * _

    then:
    assertPriceListEquals( expected, actual )
  }

  def 'no se procesa listas de precios pendientes de cargar'( ) {
    given:
    def file = new File( receiveDir, '3_1549_12-06-2012.LP' )
    file.append( '1549|12/06/2012|18/06/2012|0|\n' )
    file.append( 'U|AS0003M|A|O|ARMAZON ARMAZONES DE SEGURIDAD AS0003M|P_SEARS|180||163257|AS|\n' )
    def newFile = new File( priceListDir, '3_1549_12-06-2012.LP' )
    def listaPrecios = new ListaPrecios(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        fechaAct: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        fechaActAuto: Date.parse( 'dd/MM/yyyy', '18/06/2012' )
    )

    when:
    controller.processPendingPriceLists()

    then:
    sucursalService.obtenSucursalActual() >> new Sucursal( id: 9999 )
    listaPreciosService.registrarListaPrecios( { ListaPrecios tmp ->
      assert tmp.id == listaPrecios.id
      assert tmp.filename == listaPrecios.filename
      assert tmp.fechaAct == listaPrecios.fechaAct
      assert tmp.fechaActAuto == listaPrecios.fechaActAuto
      return tmp
    } as ListaPrecios ) >> result

    then:
    0 * _

    then:
    expected == null
    file.exists()
    !newFile.exists()

    cleanup:
    newFile.delete()

    where:
    expected | result
    null     | null
    null     | new ListaPrecios()
    null     | new ListaPrecios( id: null )
  }

  def 'procesa listas de precios pendientes de cargar'( ) {
    given:
    def file = new File( receiveDir, '3_1549_12-06-2012.LP' )
    file.append( '1549|12/06/2012|18/06/2012|0|\n' )
    file.append( 'U|AS0003M|A|O|ARMAZON ARMAZONES DE SEGURIDAD AS0003M|P_SEARS|180||163257|AS|\n' )
    def content = file.text
    def newFile = new File( priceListDir, '3_1549_12-06-2012.LP' )
    def listaPrecios = new ListaPrecios(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        fechaAct: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        fechaActAuto: Date.parse( 'dd/MM/yyyy', '18/06/2012' )
    )
    def result = listaPrecios

    when:
    controller.processPendingPriceLists()

    then:
    sucursalService.obtenSucursalActual() >> new Sucursal( id: 9999 )
    listaPreciosService.registrarListaPrecios( { ListaPrecios tmp ->
      assert tmp.id == listaPrecios.id
      assert tmp.filename == listaPrecios.filename
      assert tmp.fechaAct == listaPrecios.fechaAct
      assert tmp.fechaActAuto == listaPrecios.fechaActAuto
      return tmp
    } as ListaPrecios ) >> result

    then:
    0 * _

    then:
    result.id == listaPrecios.id
    result.filename == listaPrecios.filename
    result.fechaAct == listaPrecios.fechaAct
    result.fechaActAuto == listaPrecios.fechaActAuto
    !file.exists()
    newFile.exists()
    content == newFile.text

    cleanup:
    newFile.delete()
  }

  def 'obtiene listas de precios'( ) {
    given:
    def file = new File( priceListDir, '3_1549_12-06-2012.LP' )
    file.append( '1549|12/06/2012|18/06/2012|0|\n' )
    file.append( 'U|AS0003M|A|O|ARMAZON ARMAZONES DE SEGURIDAD AS0003M|P_SEARS|180||163257|AS|\n' )
    file.append( 'A|RB2140Q|A|G|GOGLE RAY BAN RB2140Q|P_LUX|2480||172391|RB|\n' )
    def expected = new PriceList(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        activated: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        autoActivated: Date.parse( 'dd/MM/yyyy', '18/06/2012' ),
        branch: '0',
        items: [
            new Item(
                operation: 'U',
                name: 'AS0003M',
                type: 'A',
                genericType: 'O',
                reference: 'ARMAZON ARMAZONES DE SEGURIDAD AS0003M',
                priceType: 'P_SEARS',
                price: 180,
                id: 163257,
                genericSubType: 'AS'
            ),
            new Item(
                operation: 'A',
                name: 'RB2140Q',
                type: 'A',
                genericType: 'G',
                reference: 'GOGLE RAY BAN RB2140Q',
                priceType: 'P_LUX',
                price: 2480,
                id: 172391,
                genericSubType: 'RB'
            )
        ]
    )

    when:
    def actual = controller.getPendingPriceLists()

    then:
    0 * _

    then:
    actual.any()
    1 == actual.size()
    assertPriceListEquals( expected, actual.first() )

    cleanup:
    file.delete()
  }

  def 'obtiene lista vacia al validar lista de precios con parametros nulos o vacios'( ) {
    expect:
    expected == controller.validatePriceList( priceList )

    where:
    expected | priceList
    [ ]      | null
    [ ]      | new PriceList()
    [ ]      | new PriceList( id: 1 )
    [ ]      | new PriceList( items: [ ] )
    [ ]      | new PriceList( id: 1, items: [ ] )
    [ ]      | new PriceList( id: 1, items: [ null ] )
  }

  def 'obtiene lista de precios al validar lista de precios'( ) {
    given:
    def expected = [
        new Item(
            operation: 'U',
            name: 'AS0003M',
            color: '10',
            location: 'GU-1',
            type: 'A',
            genericType: 'O',
            reference: 'ARMAZON ARMAZONES DE SEGURIDAD AS0003M',
            priceType: 'P_SEARS',
            price: 180,
            id: 163257,
            genericSubType: 'AS'
        ),
        new Item(
            operation: 'U',
            name: 'RB2140Q',
            color: '70',
            location: 'CO-9',
            type: 'A',
            genericType: 'G',
            reference: 'GOGLE RAY BAN RB2140Q',
            priceType: 'P_LUX',
            price: 2480,
            id: 172391,
            genericSubType: 'RB'
        )
    ]
    def priceList = new PriceList(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        activated: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        autoActivated: Date.parse( 'dd/MM/yyyy', '18/06/2012' ),
        items: [
            new Item(
                operation: 'U',
                name: 'AS0003M',
                type: 'A',
                genericType: 'O',
                reference: 'ARMAZON ARMAZONES DE SEGURIDAD AS0003M',
                priceType: 'P_SEARS',
                price: 180,
                id: 163257,
                genericSubType: 'AS'
            ),
            new Item(
                operation: 'U',
                name: 'RB2140Q',
                type: 'A',
                genericType: 'G',
                reference: 'GOGLE RAY BAN RB2140Q',
                priceType: 'P_LUX',
                price: 2480,
                id: 172391,
                genericSubType: 'RB'
            )
        ]
    )
    def listaPrecios = new ListaPrecios(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        fechaAct: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        fechaActAuto: Date.parse( 'dd/MM/yyyy', '18/06/2012' )
    )
    def articulos = [
        new Articulo(
            id: 163257,
            articulo: 'AS0003M',
            descripcion: 'ARMAZON ARMAZONES DE SEGURIDAD AS0003M',
            idGenerico: 'A',
            idGenTipo: 'O',
            idGenSubtipo: 'AS',
            precio: 180,
            operacion: 'U',
            tipoPrecio: 'P_SEARS'
        ),
        new Articulo(
            id: 172391,
            articulo: 'RB2140Q',
            descripcion: 'GOGLE RAY BAN RB2140Q',
            idGenerico: 'A',
            idGenTipo: 'G',
            idGenSubtipo: 'RB',
            precio: 2480,
            operacion: 'U',
            tipoPrecio: 'P_LUX'
        )
    ]
    def results = [
        new Articulo(
            id: 163257,
            articulo: 'AS0003M',
            descripcion: 'ARMAZON ARMAZONES DE SEGURIDAD AS0003M',
            idGenerico: 'A',
            idGenTipo: 'O',
            idGenSubtipo: 'AS',
            precio: 180,
            operacion: 'U',
            tipoPrecio: 'P_SEARS',
            codigoColor: '10',
            ubicacion: 'GU-1'
        ),
        new Articulo(
            id: 172391,
            articulo: 'RB2140Q',
            descripcion: 'GOGLE RAY BAN RB2140Q',
            idGenerico: 'A',
            idGenTipo: 'G',
            idGenSubtipo: 'RB',
            precio: 2480,
            operacion: 'U',
            tipoPrecio: 'P_LUX',
            codigoColor: '70',
            ubicacion: 'CO-9'
        )
    ]

    when:
    def actual = controller.validatePriceList( priceList )

    then:
    listaPreciosService.validarListaPrecios(
        { ListaPrecios tmp ->
          assert tmp.id == listaPrecios.id
          assert tmp.filename == listaPrecios.filename
          assert tmp.fechaAct == listaPrecios.fechaAct
          assert tmp.fechaActAuto == listaPrecios.fechaActAuto
          return tmp
        } as ListaPrecios,
        {
          it.eachWithIndex { Articulo val, idx ->
            assert val.id == articulos[ idx ].id
            assert val.articulo == articulos[ idx ].articulo
            assert val.descripcion == articulos[ idx ].descripcion
            assert val.idGenerico == articulos[ idx ].idGenerico
            assert val.idGenTipo == articulos[ idx ].idGenTipo
            assert val.idGenSubtipo == articulos[ idx ].idGenSubtipo
            assert val.precio == articulos[ idx ].precio
            assert val.idDisenoLente == articulos[ idx ].idDisenoLente
          }
          return it
        } as List<Articulo>
    ) >> results

    then:
    0 * _

    then:
    expected == actual
  }

  def 'obtiene null al cargar lista de precios con parametros nulos o vacios'( ) {
    expect:
    expected == controller.loadPriceList( priceList, loadType )

    where:
    expected | priceList                               | loadType
    null     | null                                    | PriceListLoadType.MANUAL
    null     | new PriceList()                         | PriceListLoadType.MANUAL
    null     | new PriceList( id: 1 )                  | PriceListLoadType.MANUAL
    null     | new PriceList( items: [ ] )             | PriceListLoadType.MANUAL
    null     | new PriceList( id: 1, items: [ ] )      | PriceListLoadType.MANUAL
    null     | new PriceList( id: 1, items: [ null ] ) | PriceListLoadType.MANUAL
    null     | null                                    | PriceListLoadType.AUTO
    null     | new PriceList()                         | PriceListLoadType.AUTO
    null     | new PriceList( id: 1 )                  | PriceListLoadType.AUTO
    null     | new PriceList( items: [ ] )             | PriceListLoadType.AUTO
    null     | new PriceList( id: 1, items: [ ] )      | PriceListLoadType.AUTO
    null     | new PriceList( id: 1, items: [ null ] ) | PriceListLoadType.AUTO
  }


  def 'obtiene lista de precios al cargar lista de precios'( ) {
    given:
    def file = new File( priceListDir, '3_1549_12-06-2012.LP' )
    file.append( '1549|12/06/2012|18/06/2012|0|\n' )
    file.append( 'U|AS0003M|A|O|ARMAZON ARMAZONES DE SEGURIDAD AS0003M|P_SEARS|180||163257|AS|\n' )
    def content = file.text
    def newFile = new File( receivedDir, '3_1549_12-06-2012.LP' )
    def expected = new PriceList(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        activated: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        autoActivated: Date.parse( 'dd/MM/yyyy', '18/06/2012' ),
        loaded: Date.parse( 'dd/MM/yyyy', '16/06/2012' ),
        loadType: loadType,
        items: [
            new Item(
                operation: 'U',
                name: 'AS0003M',
                type: 'A',
                genericType: 'O',
                reference: 'ARMAZON ARMAZONES DE SEGURIDAD AS0003M',
                priceType: 'P_SEARS',
                price: 180,
                id: 163257,
                genericSubType: 'AS'
            ),
            new Item(
                operation: 'U',
                name: 'RB2140Q',
                type: 'A',
                genericType: 'G',
                reference: 'GOGLE RAY BAN RB2140Q',
                priceType: 'P_LUX',
                price: 2480,
                id: 172391,
                genericSubType: 'RB'
            )
        ]
    )
    def priceList = new PriceList(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        activated: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        autoActivated: Date.parse( 'dd/MM/yyyy', '18/06/2012' ),
        items: [
            new Item(
                operation: 'U',
                name: 'AS0003M',
                type: 'A',
                genericType: 'O',
                reference: 'ARMAZON ARMAZONES DE SEGURIDAD AS0003M',
                priceType: 'P_SEARS',
                price: 180,
                id: 163257,
                genericSubType: 'AS'
            ),
            new Item(
                operation: 'U',
                name: 'RB2140Q',
                type: 'A',
                genericType: 'G',
                reference: 'GOGLE RAY BAN RB2140Q',
                priceType: 'P_LUX',
                price: 2480,
                id: 172391,
                genericSubType: 'RB'
            )
        ]
    )
    def listaPrecios = new ListaPrecios(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        tipoCarga: loadType,
        fechaAct: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        fechaActAuto: Date.parse( 'dd/MM/yyyy', '18/06/2012' ),
        fechaCarga: Date.parse( 'dd/MM/yyyy', '16/06/2012' )
    )
    def articulos = [
        new Articulo(
            id: 163257,
            articulo: 'AS0003M',
            descripcion: 'ARMAZON ARMAZONES DE SEGURIDAD AS0003M',
            idGenerico: 'A',
            idGenTipo: 'O',
            idGenSubtipo: 'AS',
            precio: 180,
            operacion: 'U',
            tipoPrecio: 'P_SEARS'
        ),
        new Articulo(
            id: 172391,
            articulo: 'RB2140Q',
            descripcion: 'GOGLE RAY BAN RB2140Q',
            idGenerico: 'A',
            idGenTipo: 'G',
            idGenSubtipo: 'RB',
            precio: 2480,
            operacion: 'U',
            tipoPrecio: 'P_LUX'
        )
    ]

    when:
    def actual = controller.loadPriceList( priceList, loadType )

    then:
    listaPreciosService.cargarListaPrecios(
        { ListaPrecios tmp ->
          assert tmp.id == listaPrecios.id
          assert tmp.filename == listaPrecios.filename
          assert tmp.tipoCarga == listaPrecios.tipoCarga
          assert tmp.fechaAct == listaPrecios.fechaAct
          assert tmp.fechaActAuto == listaPrecios.fechaActAuto
          return tmp
        } as ListaPrecios,
        {
          it.eachWithIndex { Articulo val, idx ->
            assert val.id == articulos[ idx ].id
            assert val.articulo == articulos[ idx ].articulo
            assert val.descripcion == articulos[ idx ].descripcion
            assert val.idGenerico == articulos[ idx ].idGenerico
            assert val.idGenTipo == articulos[ idx ].idGenTipo
            assert val.idGenSubtipo == articulos[ idx ].idGenSubtipo
            assert val.precio == articulos[ idx ].precio
            assert val.idDisenoLente == articulos[ idx ].idDisenoLente
            assert val.operacion == articulos[ idx ].operacion
            assert val.tipoPrecio == articulos[ idx ].tipoPrecio
          }
          return it
        } as List<Articulo>
    ) >> listaPrecios

    then:
    0 * _

    then:
    assertPriceListEquals( expected, actual )
    !file.exists()
    newFile.exists()
    content == newFile.text

    cleanup:
    newFile.delete()

    where:
    loadType << [ PriceListLoadType.MANUAL, PriceListLoadType.AUTO ]
  }
}
