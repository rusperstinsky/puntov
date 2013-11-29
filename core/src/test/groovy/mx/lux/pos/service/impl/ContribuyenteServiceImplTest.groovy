package mx.lux.pos.service.impl

import mx.lux.pos.model.Cliente
import mx.lux.pos.model.Contribuyente
import mx.lux.pos.model.Estado
import mx.lux.pos.repository.ClienteRepository
import mx.lux.pos.repository.ContribuyenteRepository
import mx.lux.pos.repository.EstadoRepository
import mx.lux.pos.repository.SucursalRepository
import spock.lang.IgnoreRest
import spock.lang.Specification

import static mx.lux.pos.assertions.Assert.assertContribuyenteEquals

class ContribuyenteServiceImplTest extends Specification {

  private ContribuyenteServiceImpl service
  private ContribuyenteRepository contribuyenteRepository
  private EstadoRepository estadoRepository
  private ClienteRepository clienteRepository
  private SucursalRepository sucursalRepository

  def setup( ) {
    contribuyenteRepository = Mock( ContribuyenteRepository )
    estadoRepository = Mock( EstadoRepository )
    clienteRepository = Mock( ClienteRepository )
    sucursalRepository = Mock( SucursalRepository )
    service = new ContribuyenteServiceImpl(
        contribuyenteRepository: contribuyenteRepository,
        estadoRepository: estadoRepository,
        clienteRepository: clienteRepository,
        sucursalRepository: sucursalRepository
    )
  }

  def 'es null al obtener contribuyente por rfc con rfc invalido'( ) {
    when:
    def actual = service.obtenerContribuyentePorRfc( rfc )

    then:
    _ * contribuyenteRepository.findByRfcOrderByFechaRegistroDesc( rfc ) >> resultados

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | resultados | rfc
    null     | null       | null
    null     | null       | ' '
    null     | null       | '-'
    null     | [ ]        | 'text'
    null     | [ null ]   | 'text'
  }

  def 'es contribuyente al obtener contribuyente por rfc'( ) {
    given:
    def rfc = 'XAXX010101000'
    def expected = new Contribuyente(
        id: 2,
        idCliente: 1,
        rfc: rfc,
        nombre: 'text',
        domicilio: 'text',
        colonia: 'text',
        ciudad: 'text',
        idEstado: '01',
        codigoPostal: '00000',
        telefono: 'text',
        fechaModificacion: new Date(),
        idSucursal: 1,
        fechaRegistro: new Date(),
        email: 'text'
    )
    def tmp = new Contribuyente(
        id: 1,
        idCliente: 2,
        rfc: rfc,
        nombre: 'text',
        domicilio: 'text',
        colonia: 'text',
        ciudad: 'text',
        idEstado: '01',
        codigoPostal: '00000',
        telefono: 'text',
        fechaModificacion: new Date(),
        idSucursal: 1,
        fechaRegistro: new Date(),
        email: 'text'
    )

    when:
    def actual = service.obtenerContribuyentePorRfc( rfc )

    then:
    1 * contribuyenteRepository.findByRfcOrderByFechaRegistroDesc( rfc ) >> [ expected, tmp ]

    then:
    0 * _

    then:
    assertContribuyenteEquals( expected, actual )
  }

  def 'es lista vacia al listar contribuyentes por rfc similar con rfc invalido'( ) {
    when:
    def actual = service.listarContribuyentesPorRfcSimilar( rfc )

    then:
    _ * contribuyenteRepository.findByRfcStartingWithOrderByRfcAsc( rfc )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | rfc
    [ ]      | null
    [ ]      | ' '
    [ ]      | '-'
    [ ]      | 'text'
  }

  def 'es lista de contribuyentes al listar contribuyentes por rfc similar'( ) {
    given:
    def rfc = 'XAXX'
    def expected1 = new Contribuyente(
        id: 1,
        rfc: 'XAXXA'
    )
    def expected2 = new Contribuyente(
        id: 3,
        rfc: 'XAXXB'
    )
    def resultados = [
        expected1,
        new Contribuyente(
            id: 2,
            rfc: 'XAXXA'
        ),
        expected2,
        new Contribuyente(
            id: 4,
            rfc: 'XAXXB'
        ),
        new Contribuyente(
            id: 5,
            rfc: 'XAXXB'
        )
    ]

    when:
    def actual = service.listarContribuyentesPorRfcSimilar( rfc )

    then:
    1 * contribuyenteRepository.findByRfcStartingWithOrderByRfcAsc( rfc ) >> resultados

    then:
    0 * _

    then:
    assertContribuyenteEquals( expected1, actual.first() )
    assertContribuyenteEquals( expected2, actual.last() )
  }

  def 'es false al validar rfc invalido'( ) {
    when:
    def actual = service.esRfcValido( rfc )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | rfc
    false    | null
    false    | ' '
    false    | 'text'
    false    | 'XXXXXXXXXXXXX'
    false    | '1111111111XXX'
    false    | '1111XXXXXXXXX'
    false    | 'XXXX000000'
    false    | 'XXXX000000X'
    false    | 'XXXX000000XX'
  }

  def 'es true al validar rfc'( ) {
    when:
    def actual = service.esRfcValido( rfc )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | rfc
    true     | 'XEXX010101000'
    true     | 'XAXX010101000'
    true     | 'XXX111111XXX'
    true     | 'XXXX111111XXX'
    true     | '&XXX000000X1X'
    true     | '/XXX000000X1X'
  }

  def 'es null al registrar contribuyente con parametro invalido'( ) {
    when:
    def actual = service.registrarContribuyente( contribuyente )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | contribuyente
    null     | null
    null     | new Contribuyente()
    null     | new Contribuyente( rfc: ' ' )
    null     | new Contribuyente( rfc: 'XXXX000000' )
  }

  @IgnoreRest
  def 'es contribuyente al registrar contribuyente nuevo'( ) {
    given:
    def idCliente = 9
    def rfc = 'XAXX010101000'
    def idSucursal = 1
    def expected = new Contribuyente(
        id: 1,
        idCliente: idCliente,
        rfc: rfc,
        nombre: 'text',
        domicilio: 'text',
        colonia: 'text',
        ciudad: 'text',
        idEstado: '01',
        codigoPostal: 'text',
        idSucursal: idSucursal,
        email: 'text'
    )
    def contribuyente = new Contribuyente(
        idCliente: idCliente,
        rfc: rfc,
        nombre: 'text',
        domicilio: 'text',
        colonia: 'text',
        ciudad: 'text',
        idEstado: 'MEXICO',
        codigoPostal: 'text',
        email: 'text'
    )

    when:
    def actual = service.registrarContribuyente( contribuyente )

    then:
    1 * sucursalRepository.getCurrentSucursalId() >> idSucursal
    1 * estadoRepository.findByNombre( contribuyente.idEstado ) >> new Estado( id: '01' )
    1 * contribuyenteRepository.findByRfcOrderByFechaRegistroDesc( rfc ) >> null
    1 * contribuyenteRepository.save( { Contribuyente tmp ->
      tmp.id = 1
      assertContribuyenteEquals( expected, tmp )
      return tmp
    } ) >> expected
    1 * clienteRepository.findOne( idCliente ) >> new Cliente( id: idCliente, rfc: rfc )
    1 * clienteRepository.save( { Cliente tmp ->
      assert tmp.id == idCliente
      assert tmp.rfc == rfc
      return tmp
    } )

    then:
    0 * _

    then:
    assertContribuyenteEquals( expected, actual )
  }
}
