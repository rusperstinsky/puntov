package mx.lux.pos.service.impl

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.Precio
import mx.lux.pos.repository.ArticuloRepository
import mx.lux.pos.repository.PrecioRepository
import spock.lang.Specification

import static mx.lux.pos.assertions.Assert.assertArticuloEquals
import org.springframework.test.context.ContextConfiguration

class ArticuloServiceImplTest extends Specification {

  private ArticuloServiceImpl service
  private ArticuloRepository articuloRepository
  private PrecioRepository precioRepository

  def setup( ) {
    articuloRepository = Mock( ArticuloRepository )
    precioRepository = Mock( PrecioRepository )
    service = new ArticuloServiceImpl(
        articuloRepository: articuloRepository,
        precioRepository: precioRepository
    )
  }

  def 'obtiene articulo original al establecer precio con parametros nulos o vacios'( ) {
    when:
    def actual = service.establecerPrecio( articulo )

    then:
    0 * _

    then:
    assertArticuloEquals( expected, actual )

    where:
    expected              | articulo
    null                  | null
    new Articulo()        | new Articulo()
    new Articulo( id: 0 ) | new Articulo( id: 0 )
  }

  def 'obtiene articulo actualizado al establecer precio con precio de lista valido y precio oferta cero'( ) {
    given:
    def idArticulo = 1
    def codigo = 'A'
    def expected = new Articulo(
        id: idArticulo,
        articulo: codigo,
        precio: 99.99,
        precioO: 0
    )
    def articulo = new Articulo(
        id: idArticulo,
        articulo: codigo,
        precio: 0,
        precioO: 0
    )
    def precioLista = new Precio(
        id: 1,
        lista: 'L',
        precio: 99.99
    )
    def precioOferta = new Precio(
        id: 2,
        lista: 'O',
        precio: 0
    )

    when:
    def actual = service.establecerPrecio( articulo )

    then:
    1 * precioRepository.findByArticulo( codigo ) >> [ precioLista, precioOferta ]

    then:
    0 * _

    then:
    assertArticuloEquals( expected, actual )
  }

  def 'obtiene articulo actualizado al establecer precio con precio de lista mayor y precio oferta menor'( ) {
    given:
    def idArticulo = 1
    def codigo = 'A'
    def expected = new Articulo(
        id: idArticulo,
        articulo: codigo,
        precio: 95.95,
        precioO: 95.95
    )
    def articulo = new Articulo(
        id: idArticulo,
        articulo: codigo,
        precio: 0,
        precioO: 0
    )
    def precioLista = new Precio(
        id: 1,
        lista: 'L',
        precio: 99.99
    )
    def precioOferta = new Precio(
        id: 2,
        lista: 'O',
        precio: 95.95
    )

    when:
    def actual = service.establecerPrecio( articulo )

    then:
    1 * precioRepository.findByArticulo( codigo ) >> [ precioLista, precioOferta ]

    then:
    0 * _

    then:
    assertArticuloEquals( expected, actual )
  }

  def 'obtiene articulo actualizado al establecer precio con precio de lista menor y precio oferta mayor'( ) {
    given:
    def idArticulo = 1
    def codigo = 'A'
    def expected = new Articulo(
        id: idArticulo,
        articulo: codigo,
        precio: 95.95,
        precioO: 0
    )
    def articulo = new Articulo(
        id: idArticulo,
        articulo: codigo,
        precio: 0,
        precioO: 0
    )
    def precioLista = new Precio(
        id: 1,
        lista: 'L',
        precio: 95.95
    )
    def precioOferta = new Precio(
        id: 2,
        lista: 'O',
        precio: 99.99
    )

    when:
    def actual = service.establecerPrecio( articulo )

    then:
    1 * precioRepository.findByArticulo( codigo ) >> [ precioLista, precioOferta ]

    then:
    0 * _

    then:
    assertArticuloEquals( expected, actual )
  }
}
