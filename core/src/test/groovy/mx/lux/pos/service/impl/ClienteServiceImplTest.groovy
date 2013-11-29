package mx.lux.pos.service.impl

import mx.lux.pos.model.Cliente
import mx.lux.pos.model.ClientePais
import mx.lux.pos.model.Parametro
import mx.lux.pos.model.TipoParametro
import spock.lang.Specification
import mx.lux.pos.repository.*

import static mx.lux.pos.assertions.Assert.assertClienteEquals

class ClienteServiceImplTest extends Specification {

  private ClienteServiceImpl service
  private ClienteRepository clienteRepository
  private TituloRepository tituloRepository
  private DominioRepository dominioRepository
  private ParametroRepository parametroRepository
  private ClientePaisRepository clientePaisRepository
  private SucursalRepository sucursalRepository

  def setup( ) {
    clienteRepository = Mock( ClienteRepository )
    tituloRepository = Mock( TituloRepository )
    dominioRepository = Mock( DominioRepository )
    parametroRepository = Mock( ParametroRepository )
    clientePaisRepository = Mock( ClientePaisRepository )
    sucursalRepository = Mock( SucursalRepository )
    service = new ClienteServiceImpl(
        clienteRepository: clienteRepository,
        tituloRepository: tituloRepository,
        dominioRepository: dominioRepository,
        parametroRepository: parametroRepository,
        clientePaisRepository: clientePaisRepository,
        sucursalRepository: sucursalRepository
    )
  }

  def 'regresa lista vacia al buscar cliente con parametros nulos o vacios'( ) {
    when:
    def actual = service.buscarCliente( nombre, apellidoPaterno, apellidoMaterno )

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | nombre | apellidoPaterno | apellidoMaterno
    [ ]      | null   | null            | null
    [ ]      | ''     | null            | null
    [ ]      | ''     | ''              | null
    [ ]      | ''     | ''              | ''
    [ ]      | null   | ''              | null
    [ ]      | null   | ''              | ''
    [ ]      | null   | null            | ''
    [ ]      | ''     | ''              | ''
  }

  def 'regresa null al obtener cliente por defecto con parametro nulo o vacio'( ) {
    when:
    def actual = service.obtenerClientePorDefecto()

    then:
    1 * parametroRepository.findOne( _ ) >> parametro

    then:
    0 * _

    then:
    expected == actual

    where:
    expected | parametro
    null     | null
    null     | new Parametro()
    null     | new Parametro( id: ' ' )
    null     | new Parametro( id: ' ', valor: ' ' )
  }

  def 'regresa null al obtener cliente por defecto con resultado nulo'( ) {
    given:
    def pId = TipoParametro.ID_CLIENTE_GENERICO.value
    def cId = 1

    when:
    def actual = service.obtenerClientePorDefecto()

    then:
    1 * parametroRepository.findOne( pId ) >> new Parametro( id: pId, valor: cId )
    1 * clienteRepository.findOne( cId ) >> null

    then:
    0 * _

    then:
    null == actual
  }

  def 'regresa cliente al obtener cliente por defecto'( ) {
    given:
    def pId = TipoParametro.ID_CLIENTE_GENERICO.value
    def cId = 1
    def expected = new Cliente(
        id: 1,
        nombre: 'Publico General',
        sexo: true,
        fechaAlta: Date.parse( 'dd/MM/yyyy', '01/01/2012' ),
        fechaModificado: Date.parse( 'dd/MM/yyyy', '02/01/2012' )
    )
    def result = new Cliente(
        id: 1,
        nombre: 'Publico General',
        sexo: true,
        fechaAlta: Date.parse( 'dd/MM/yyyy', '01/01/2012' ),
        fechaModificado: Date.parse( 'dd/MM/yyyy', '02/01/2012' )
    )

    when:
    def actual = service.obtenerClientePorDefecto()

    then:
    1 * parametroRepository.findOne( pId ) >> new Parametro( id: pId, valor: cId )
    1 * clienteRepository.findOne( cId ) >> result

    then:
    0 * _

    then:
    assertClienteEquals( expected, actual )
  }

  def 'regresa null al agregar cliente con parametros invalidos'( ) {
    when:
    def actual = service.agregarCliente( cliente )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | cliente
    null     | null
    null     | new Cliente()
    null     | new Cliente( nombre: ' ' )
  }

  def 'regresa cliente al agregar cliente'( ) {
    given:
    def idSucursal = 1
    def expected = new Cliente(
        id: 1,
        nombre: 'Juan',
        apellidoPaterno: 'Perez',
        apellidoMaterno: 'Perez',
        idSucursal: idSucursal
    )
    def cliente = new Cliente(
        nombre: 'Juan',
        apellidoPaterno: 'Perez',
        apellidoMaterno: 'Perez'
    )

    when:
    def actual = service.agregarCliente( cliente )

    then:
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * clienteRepository.save( { Cliente tmp ->
      tmp.id = 1
      assertClienteEquals( expected, tmp )
      return tmp
    } ) >> expected

    then:
    0 * _

    then:
    assertClienteEquals( expected, actual )
  }

  def 'regresa cliente actualizado al agregar cliente existente'( ) {
    given:
    def idSucursal = 1
    def expected = new Cliente(
        id: 1,
        nombre: 'Juan',
        apellidoPaterno: 'Perez',
        apellidoMaterno: 'Perez',
        idSucursal: idSucursal
    )
    def cliente = new Cliente(
        nombre: 'Juan',
        apellidoPaterno: 'Perez',
        apellidoMaterno: 'Perez'
    )

    when:
    def actual = service.agregarCliente( cliente )

    then:
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * clienteRepository.save( { Cliente tmp ->
      tmp.id = 1
      assertClienteEquals( expected, tmp )
      return tmp
    } ) >> expected

    then:
    0 * _

    then:
    assertClienteEquals( expected, actual )
  }

  def 'regresa null al agregar cliente pais con parametros nulos o vacios'( ) {
    when:
    def actual = service.agregarClientePais( clientePais )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | clientePais
    null     | null
    null     | new ClientePais()
    null     | new ClientePais( id: 0 )
  }

  def 'regresa cliente pais al agregar cliente pais de cliente extranjero'( ) {
    given:
    def expected = new ClientePais(
        id: 1,
        ciudad: 'NEW YORK',
        pais: 'USA'
    )
    def clientePais = new ClientePais(
        id: 1,
        ciudad: 'NEW YORK',
        pais: 'USA'
    )

    when:
    def actual = service.agregarClientePais( clientePais )

    then:
    1 * clientePaisRepository.save( {
      assert it.id == expected.id
      assert it.ciudad == expected.ciudad
      assert it.pais == expected.pais
      assert it.fecha?.dateString == expected.fecha?.dateString
      return it
    } ) >> new ClientePais(
        id: 1,
        ciudad: 'NEW YORK',
        pais: 'USA'
    )

    then:
    0 * _

    then:
    expected.id == actual.id
    expected.ciudad == actual.ciudad
    expected.pais == actual.pais
    expected.fecha?.dateString == actual.fecha?.dateString
  }
}
