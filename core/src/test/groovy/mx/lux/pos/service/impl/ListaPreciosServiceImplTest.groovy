package mx.lux.pos.service.impl

import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource

import mx.lux.pos.model.*
import mx.lux.pos.repository.*

import static mx.lux.pos.assertions.Assert.assertListaPreciosEquals

class ListaPreciosServiceImplTest extends Specification {

  private ListaPreciosServiceImpl service
  private ListaPreciosRepository listaPreciosRepository
  private ParametroRepository parametroRepository
  private SucursalRepository sucursalRepository
  private ArticuloRepository articuloRepository
  private PrecioRepository precioRepository
  private DataSource invDataSource
  @Shared def articulos = [
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
      ),
      new Articulo(
          id: 135651,
          articulo: 'AS45810',
          descripcion: 'ARMAZON ARMAZONES DE SEGURIDAD AS45810',
          idGenerico: 'A',
          idGenTipo: 'O',
          idGenSubtipo: 'AS',
          precio: 0,
          operacion: 'D',
          tipoPrecio: 'P_LUX_O'
      ),
      new Articulo(
          id: 163257,
          articulo: 'AS0003M',
          descripcion: 'ARMAZON ARMAZONES DE SEGURIDAD AS0003M',
          idGenerico: 'A',
          idGenTipo: 'O',
          idGenSubtipo: 'AS',
          precio: 180,
          operacion: 'D',
          tipoPrecio: 'P_SEARS'
      ),
      new Articulo(
          id: 135652,
          articulo: 'AS45811',
          descripcion: 'ARMAZON ARMAZONES DE SEGURIDAD AS45811',
          idGenerico: 'A',
          idGenTipo: 'O',
          idGenSubtipo: 'AS',
          precio: 0,
          operacion: 'D',
          tipoPrecio: 'P_SEARS_O'
      ),
      new Articulo(
          id: 162641,
          articulo: 'HA2637',
          descripcion: 'ARMAZON HAVRE HA2637',
          idGenerico: 'A',
          idGenTipo: 'O',
          idGenSubtipo: 'HA',
          precio: 480,
          operacion: 'U',
          tipoPrecio: '9'
      )
  ]

  def setup( ) {
    listaPreciosRepository = Mock( ListaPreciosRepository )
    parametroRepository = Mock( ParametroRepository )
    sucursalRepository = Mock( SucursalRepository )
    articuloRepository = Mock( ArticuloRepository )
    precioRepository = Mock( PrecioRepository )
    invDataSource = Mock( DataSource )
    service = new ListaPreciosServiceImpl(
        listaPreciosRepository: listaPreciosRepository,
        parametroRepository: parametroRepository,
        sucursalRepository: sucursalRepository,
        articuloRepository: articuloRepository,
        precioRepository: precioRepository,
        invDataSource: invDataSource
    )
  }

  def 'regresa null al obtener ruta por recibir con parametro nulo'( ) {
    when:
    def actual = service.obtenRutaPorRecibir()

    then:
    1 * parametroRepository.findOne( _ as String ) >> null

    then:
    0 * _

    then:
    null == actual
  }

  def 'regresa valor al obtener ruta por recibir'( ) {
    given:
    def rId = TipoParametro.RUTA_POR_RECIBIR.value
    def expected = '/home/paso/por_recibir'

    when:
    def actual = service.obtenRutaPorRecibir()

    then:
    1 * parametroRepository.findOne( rId ) >> new Parametro( id: rId, valor: expected )

    then:
    0 * _

    then:
    expected == actual
  }

  def 'regresa null al obtener ruta lista de precios con parametro nulo'( ) {
    when:
    def actual = service.obtenRutaListaPrecios()

    then:
    1 * parametroRepository.findOne( _ as String ) >> null

    then:
    0 * _

    then:
    null == actual
  }

  def 'regresa valor al obtener ruta lista de precios'( ) {
    given:
    def rId = TipoParametro.RUTA_LISTA_PRECIOS.value
    def expected = '/home/paso/lp'

    when:
    def actual = service.obtenRutaListaPrecios()

    then:
    1 * parametroRepository.findOne( rId ) >> new Parametro( id: rId, valor: expected )

    then:
    0 * _

    then:
    expected == actual
  }

  def 'regresa null al obtener ruta recibidos con parametro nulo'( ) {
    when:
    def actual = service.obtenRutaRecibidos()

    then:
    1 * parametroRepository.findOne( _ as String ) >> null

    then:
    0 * _

    then:
    null == actual
  }

  def 'regresa valor al obtener ruta recibidos'( ) {
    given:
    def rId = TipoParametro.RUTA_RECIBIDOS.value
    def expected = '/home/paso/recibidos'

    when:
    def actual = service.obtenRutaRecibidos()

    then:
    1 * parametroRepository.findOne( rId ) >> new Parametro( id: rId, valor: expected )

    then:
    0 * _

    then:
    expected == actual
  }

  def 'regresa null al obtener sucursal actual con parametro nulo'( ) {
    when:
    def actual = service.obtenSucursalActual()

    then:
    1 * parametroRepository.findOne( _ as String ) >> null

    then:
    0 * _

    then:
    null == actual
  }

  def 'regresa id de sucursal como cadena de 4 digitos al obtener sucursal actual'( ) {
    given:
    def sId = TipoParametro.ID_SUCURSAL.value

    when:
    def actual = service.obtenSucursalActual()

    then:
    1 * parametroRepository.findOne( sId ) >> new Parametro( id: sId, valor: idSucursal )

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | idSucursal
    null     | null
    '0001'   | '1'
    '0001'   | '01'
    '0001'   | '001'
    '0001'   | '0001'
    '0011'   | '11'
    '0111'   | '111'
    '1111'   | '1111'
  }

  def 'regresa null al generar url de servicio web con parametros nulos, vacios o incorrectos'( ) {
    when:
    def actual = service.generaUrlServicioWeb( tipoParametro, idCambio, tipoCarga )

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | tipoParametro                          | idCambio | tipoCarga
    null     | null                                   | null     | null
    null     | null                                   | null     | ''
    null     | null                                   | ''       | null
    null     | null                                   | ''       | ''
    null     | TipoParametro.RUTA_POR_RECIBIR         | null     | null
    null     | TipoParametro.URL_RECIBE_LISTA_PRECIOS | null     | null
    null     | TipoParametro.URL_RECIBE_LISTA_PRECIOS | ''       | null
    null     | TipoParametro.URL_CARGA_LISTA_PRECIOS  | null     | null
    null     | TipoParametro.URL_CARGA_LISTA_PRECIOS  | ''       | null
  }

  def 'regresa url de recibe lista precios al generar url de servicio web'( ) {
    given:
    def uId = TipoParametro.URL_RECIBE_LISTA_PRECIOS.value
    def baseUrl = 'http://app.olux.com.mx/cgi-bin/wspd_cgi.sh/WService=LUX3/e-recibi_lp.r'
    def sId = TipoParametro.ID_SUCURSAL.value
    def date = new Date().format( 'ddMMyyyy' )

    when:
    def url = service.generaUrlServicioWeb( TipoParametro.URL_RECIBE_LISTA_PRECIOS, '1560', null )

    then:
    1 * parametroRepository.findOne( uId ) >> new Parametro( id: uId, valor: baseUrl )
    1 * parametroRepository.findOne( sId ) >> new Parametro( id: sId, valor: '1' )

    then:
    0 * _

    then:
    "${baseUrl}?id_suc=0001&id_cambio=1560&fecha=${date}" == url
  }

  def 'regresa url de carga lista precios al generar url de servicio web'( ) {
    given:
    def uId = TipoParametro.URL_CARGA_LISTA_PRECIOS.value
    def baseUrl = 'http://app.olux.com.mx/cgi-bin/wspd_cgi.sh/WService=LUX3/e-carga_lp.r'
    def sId = TipoParametro.ID_SUCURSAL.value
    def date = new Date().format( 'ddMMyyyy' )

    when:
    def url = service.generaUrlServicioWeb( TipoParametro.URL_CARGA_LISTA_PRECIOS, '1560', tipoCarga )

    then:
    1 * parametroRepository.findOne( uId ) >> new Parametro( id: uId, valor: baseUrl )
    1 * parametroRepository.findOne( sId ) >> new Parametro( id: sId, valor: '1' )

    then:
    0 * _

    then:
    "${baseUrl}?id_suc=0001&id_cambio=1560&fecha=${date}&tipo=${expectedTipoCarga}" == url

    where:
    expectedTipoCarga | tipoCarga
    'M'               | 'MANUAL'
    'A'               | 'AUTO'
  }

  def 'regresa null al registrar lista de precios con parametros nulos o vacios'( ) {
    when:
    def actual = service.registrarListaPrecios( listaPrecios )

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | listaPrecios
    null     | null
    null     | new ListaPrecios()
    null     | new ListaPrecios( id: ' ' )
    null     | new ListaPrecios( filename: ' ' )
    null     | new ListaPrecios( id: ' ', filename: ' ' )
  }

  def 'regresa null al registrar lista de precios por error al guardar'( ) {
    given:
    def listaPrecios = new ListaPrecios( id: '1549', filename: '3_1549_12-06-2012.LP' )

    when:
    def actual = service.registrarListaPrecios( listaPrecios )

    then:
    1 * listaPreciosRepository.save( listaPrecios ) >> null

    then:
    0 * _

    then:
    null == actual
  }

  def 'regresa valor al registrar lista de precios'( ) {
    given:
    def expected = new ListaPrecios( id: '1549', filename: '3_1549_12-06-2012.LP' )
    def listaPrecios = new ListaPrecios( id: '1549', filename: '3_1549_12-06-2012.LP' )
    def uId = TipoParametro.URL_RECIBE_LISTA_PRECIOS.value
    def baseUrl = "file://${System.getProperty( 'java.io.tmpdir' )}"
    def sId = TipoParametro.ID_SUCURSAL.value

    when:
    def actual = service.registrarListaPrecios( listaPrecios )

    then:
    1 * listaPreciosRepository.save( listaPrecios ) >> listaPrecios
    1 * parametroRepository.findOne( uId ) >> new Parametro( id: uId, valor: baseUrl )
    1 * parametroRepository.findOne( sId ) >> new Parametro( id: sId, valor: '1' )

    then:
    0 * _

    then:
    assertListaPreciosEquals( expected, actual )
  }

  def 'regresa lista vacia al validar lista de precios con parametros nulos o vacios'( ) {
    when:
    def actual = service.validarListaPrecios( listaPrecios, listaArticulos )

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | listaPrecios                | listaArticulos
    [ ]      | null                        | null
    [ ]      | new ListaPrecios()          | null
    [ ]      | new ListaPrecios( id: ' ' ) | null
    [ ]      | null                        | [ ]
    [ ]      | null                        | [ null ]
    [ ]      | new ListaPrecios()          | [ ]
    [ ]      | new ListaPrecios( id: ' ' ) | [ null ]
  }

  def 'regresa lista de articulos al validar lista de precios sucursal lux'( ) {
    given:
    def expected = [
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
            codigoColor: 10,
            existencia: new Existencia( cantidad: 1 )
        ),
        new Articulo(
            id: 135651,
            articulo: 'AS45810',
            descripcion: 'ARMAZON ARMAZONES DE SEGURIDAD AS45810',
            idGenerico: 'A',
            idGenTipo: 'O',
            idGenSubtipo: 'AS',
            precio: 0,
            operacion: 'D',
            tipoPrecio: 'P_LUX_O',
            codigoColor: 10,
            existencia: new Existencia( cantidad: 1 )
        ),
        new Articulo(
            id: 162641,
            articulo: 'HA2637',
            descripcion: 'ARMAZON HAVRE HA2637',
            idGenerico: 'A',
            idGenTipo: 'O',
            idGenSubtipo: 'HA',
            precio: 480,
            operacion: 'U',
            tipoPrecio: '9',
            codigoColor: 10,
            existencia: new Existencia( cantidad: 1 )
        )
    ]
    def listaPrecios = new ListaPrecios(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        fechaAct: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        fechaActAuto: Date.parse( 'dd/MM/yyyy', '18/06/2012' )
    )
    def existencias = [ ]
    def sId = TipoParametro.ID_SUCURSAL.value
    def sucursal = new Sucursal( id: 1, sears: false )

    when:
    def actual = service.validarListaPrecios( listaPrecios, articulos )

    then:
    1 * parametroRepository.findOne( sId ) >> new Parametro( id: sId, valor: '1' )
    1 * sucursalRepository.findOne( 1 ) >> sucursal
    3 * articuloRepository.findAll( { query ->
      def item = articulos.find { art ->
        art.articulo.equalsIgnoreCase( query.args[ 0 ].args[ 1 ].constant )
      }
      item.codigoColor = 10
      item.existencia = new Existencia( cantidad: 1 )
      existencias.clear()
      existencias.add( item )
      return query
    } ) >> existencias

    then:
    expected*.id == actual*.id
    expected*.articulo == actual*.articulo
    expected*.descripcion == actual*.descripcion
    expected*.idGenerico == actual*.idGenerico
    expected*.idGenTipo == actual*.idGenTipo
    expected*.idGenSubtipo == actual*.idGenSubtipo
    expected*.precio == actual*.precio
    expected*.operacion == actual*.operacion
    expected*.tipoPrecio == actual*.tipoPrecio
    expected*.codigoColor == actual*.codigoColor
    expected*.existencia.cantidad == actual*.existencia.cantidad
  }

  def 'regresa lista de articulos al validar lista de precios sucursal sears'( ) {
    given:
    def expected = [
        new Articulo(
            id: 163257,
            articulo: 'AS0003M',
            descripcion: 'ARMAZON ARMAZONES DE SEGURIDAD AS0003M',
            idGenerico: 'A',
            idGenTipo: 'O',
            idGenSubtipo: 'AS',
            precio: 180,
            operacion: 'D',
            tipoPrecio: 'P_SEARS',
            codigoColor: 10,
            existencia: new Existencia( cantidad: 1 )
        ),
        new Articulo(
            id: 135652,
            articulo: 'AS45811',
            descripcion: 'ARMAZON ARMAZONES DE SEGURIDAD AS45811',
            idGenerico: 'A',
            idGenTipo: 'O',
            idGenSubtipo: 'AS',
            precio: 0,
            operacion: 'D',
            tipoPrecio: 'P_SEARS_O',
            codigoColor: 10,
            existencia: new Existencia( cantidad: 1 )
        ),
        new Articulo(
            id: 162641,
            articulo: 'HA2637',
            descripcion: 'ARMAZON HAVRE HA2637',
            idGenerico: 'A',
            idGenTipo: 'O',
            idGenSubtipo: 'HA',
            precio: 480,
            operacion: 'U',
            tipoPrecio: '9',
            codigoColor: 10,
            existencia: new Existencia( cantidad: 1 )
        )
    ]
    def listaPrecios = new ListaPrecios(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        fechaAct: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        fechaActAuto: Date.parse( 'dd/MM/yyyy', '18/06/2012' )
    )
    def existencias = [ ]
    def sId = TipoParametro.ID_SUCURSAL.value
    def sucursal = new Sucursal( id: 1, sears: true )

    when:
    def actual = service.validarListaPrecios( listaPrecios, articulos )

    then:
    1 * parametroRepository.findOne( sId ) >> new Parametro( id: sId, valor: '1' )
    1 * sucursalRepository.findOne( 1 ) >> sucursal
    3 * articuloRepository.findAll( { query ->
      def item = articulos.find { art ->
        art.articulo.equalsIgnoreCase( query.args[ 0 ].args[ 1 ].constant )
      }
      item.codigoColor = 10
      item.existencia = new Existencia( cantidad: 1 )
      existencias.clear()
      existencias.add( item )
      return query
    } ) >> existencias

    then:
    expected*.id == actual*.id
    expected*.articulo == actual*.articulo
    expected*.descripcion == actual*.descripcion
    expected*.idGenerico == actual*.idGenerico
    expected*.idGenTipo == actual*.idGenTipo
    expected*.idGenSubtipo == actual*.idGenSubtipo
    expected*.precio == actual*.precio
    expected*.operacion == actual*.operacion
    expected*.tipoPrecio == actual*.tipoPrecio
    expected*.codigoColor == actual*.codigoColor
    expected*.existencia.cantidad == actual*.existencia.cantidad
  }

  def 'regresa null al cargar lista de precios con parametros nulos o vacios'( ) {
    when:
    def actual = service.cargarListaPrecios( listaPrecios, listaArticulos )

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | listaPrecios                                | listaArticulos
    null     | null                                        | null
    null     | new ListaPrecios()                          | null
    null     | new ListaPrecios( id: ' ' )                 | null
    null     | new ListaPrecios( id: ' ', tipoCarga: ' ' ) | null
    null     | null                                        | [ ]
    null     | null                                        | [ null ]
    null     | new ListaPrecios()                          | [ ]
    null     | new ListaPrecios( id: ' ' )                 | [ null ]
    null     | new ListaPrecios( id: ' ', tipoCarga: ' ' ) | [ null ]
  }

  def 'regresa lista de precios al cargar lista de precios manual'( ) {
    given:
    def expected = new ListaPrecios(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        fechaAct: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        fechaActAuto: Date.parse( 'dd/MM/yyyy', '18/06/2012' ),
        tipoCarga: 'MANUAL',
        fechaCarga: new Date()
    )
    def listaPrecios = new ListaPrecios(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        fechaAct: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        fechaActAuto: Date.parse( 'dd/MM/yyyy', '18/06/2012' ),
        tipoCarga: 'MANUAL'
    )
    def result = new ListaPrecios(
        id: '1549',
        filename: '3_1549_12-06-2012.LP',
        fechaAct: Date.parse( 'dd/MM/yyyy', '12/06/2012' ),
        fechaActAuto: Date.parse( 'dd/MM/yyyy', '18/06/2012' )
    )
    def sId = TipoParametro.ID_SUCURSAL.value
    def sucursal = new Sucursal( id: 1, sears: sears )
    def uId = TipoParametro.URL_CARGA_LISTA_PRECIOS.value
    def baseUrl = "file://${System.getProperty( 'java.io.tmpdir' )}"

    when:
    def actual = service.cargarListaPrecios( listaPrecios, articulos )

    then:
    1 * listaPreciosRepository.findOne( result.id ) >> result
    1 * listaPreciosRepository.save( { ListaPrecios tmp ->
      assertListaPreciosEquals( expected, tmp )
      return tmp
    } ) >> expected
    2 * parametroRepository.findOne( sId ) >> new Parametro( id: sId, valor: '1' )
    1 * sucursalRepository.findOne( 1 ) >> sucursal
    1 * parametroRepository.findOne( uId ) >> new Parametro( id: uId, valor: baseUrl )

    then:
    assertListaPreciosEquals( expected, actual )

    where:
    sears << [ true, false ]
  }
}
