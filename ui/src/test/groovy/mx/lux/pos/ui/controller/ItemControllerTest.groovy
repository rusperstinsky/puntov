package mx.lux.pos.ui.controller

import mx.lux.pos.model.Articulo
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.ui.model.Item
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ItemControllerTest extends Specification {

  ItemController controller
  ArticuloService articuloService
  @Shared def articulo1 = new Articulo( id: 1, articulo: 'text', descripcion: 'text', precio: 1.00 )
  @Shared def articulo2 = new Articulo( id: 2, articulo: 'text', descripcion: 'text', precio: 2.00 )
  @Shared def item1 = new Item( id: 1, name: 'text', reference: 'text', price: 1.00 )
  @Shared def item2 = new Item( id: 2, name: 'text', reference: 'text', price: 2.00 )

  def setup( ) {
    articuloService = Mock( ArticuloService )
    controller = new ItemController( articuloService )
  }

  @Unroll
  def 'regresa item cuando conviente articulo a item'( ) {
    when:
    def actual = Item.toItem( articulo )

    then:
    id == actual?.id
    name == actual?.name
    reference == actual?.reference
    price == actual?.price

    where:
    id   | name   | reference | price | articulo
    null | null   | null      | null  | null
    null | null   | null      | null  | new Articulo()
    1    | null   | null      | null  | new Articulo( id: 1 )
    1    | 'text' | null      | null  | new Articulo( id: 1, articulo: 'text' )
    1    | 'text' | 'text'    | null  | new Articulo( id: 1, articulo: 'text', descripcion: 'text' )
    1    | 'text' | 'text'    | 1.00  | new Articulo( id: 1, articulo: 'text', descripcion: 'text', precio: 1.00 )
  }

  @Unroll
  def 'regresa lista de items cuando busca un articulo en especifico'( ) {
    given:
    articuloService.listarArticulosPorCodigo( _ ) >> results

    when:
    def actual = controller.findItems( articulo )

    then:
    expected == actual

    where:
    expected         | articulo | results
    [ ]              | null     | null
    [ ]              | ''       | null
    [ ]              | null     | [ ]
    [ ]              | ''       | [ ]
    [ null ]         | 'text'   | [ null ]
    [ null ]         | 'text'   | [ new Articulo() ]
    [ item1 ]        | 'text'   | [ articulo1 ]
    [ item1, item2 ] | 'text'   | [ articulo1, articulo2 ]
  }

  @Unroll
  def 'regresa lista de items cuando busca un articulo con codigo similar'( ) {
    given:
    articuloService.listarArticulosPorCodigoSimilar( _ ) >> results

    when:
    def actual = controller.findItemsLike( articulo )

    then:
    expected == actual

    where:
    expected         | articulo | results
    [ ]              | null     | null
    [ ]              | ''       | null
    [ ]              | null     | [ ]
    [ ]              | ''       | [ ]
    [ null ]         | 'text'   | [ null ]
    [ null ]         | 'text'   | [ new Articulo() ]
    [ item1 ]        | 'text'   | [ articulo1 ]
    [ item1, item2 ] | 'text'   | [ articulo1, articulo2 ]
  }

  @Unroll
  def 'regresa lista vacia de items cuando busca con query vacio o nulo'( ) {
    when:
    def actual = controller.findItemsByQuery( query )

    then:
    expected == actual

    where:
    expected | query
    [ ]      | null
    [ ]      | ''
  }

  def 'regresa lista con un solo item cuando busca query con id articulo'( ) {
    given:
    def articulo = new Articulo( id: 12587, articulo: 'AT1111', idGenerico: 'A' )
    def item = new Item( id: 12587, name: 'AT1111', type: 'A' )

    when:
    def actual = controller.findItemsByQuery( '12587' )

    then:
    1 * articuloService.obtenerArticulo( 12587, true ) >> articulo
    [ item ] == actual
  }

  def 'regresa lista vacia de items cuando busca query con id articulo incorrecto'( ) {
    when:
    def actual = controller.findItemsByQuery( '12587*' )

    then:
    [ ] == actual
  }

  def 'regresa lista con un solo item cuando busca query con codigo exacto'( ) {
    given:
    def articulo = new Articulo( id: 1, articulo: 'X1', idGenerico: 'B' )
    def item = new Item( id: 1, name: 'X1', type: 'B' )

    when:
    def actual = controller.findItemsByQuery( 'X1' )

    then:
    1 * articuloService.listarArticulosPorCodigo( 'X1', true ) >> [ articulo ]
    [ item ] == actual
  }

  def 'regresa lista con un solo item cuando busca query con codigo exacto y color'( ) {
    given:
    def articulo = new Articulo( id: 1, articulo: 'AT2101', codigoColor: '10', idGenerico: 'A' )
    def item = new Item( id: 1, name: 'AT2101', color: '10', type: 'A' )

    when:
    def actual = controller.findItemsByQuery( 'AT2101,10' )

    then:
    1 * articuloService.listarArticulosPorCodigo( 'AT2101', true ) >> [ articulo ]
    [ item ] == actual
  }

  def 'regresa lista con un solo item cuando busca query con codigo exacto y tipo'( ) {
    given:
    def articulo = new Articulo( id: 1, articulo: 'UXMPG', idGenerico: 'B' )
    def item = new Item( id: 1, name: 'UXMPG', type: 'B' )

    when:
    def actual = controller.findItemsByQuery( 'UXMPG+B' )

    then:
    1 * articuloService.listarArticulosPorCodigo( 'UXMPG', true ) >> [ articulo ]
    [ item ] == actual
  }

  def 'regresa lista con un solo item cuando busca query con codigo exacto, color y tipo'( ) {
    given:
    def articulo = new Articulo( id: 1, articulo: 'AT2101', codigoColor: '10', idGenerico: 'A' )
    def item = new Item( id: 1, name: 'AT2101', color: '10', type: 'A' )

    when:
    def actual = controller.findItemsByQuery( 'AT2101,10+A' )

    then:
    1 * articuloService.listarArticulosPorCodigo( 'AT2101', true ) >> [ articulo ]
    [ item ] == actual
  }

  def 'regresa lista con un solo item cuando busca query con codigo exacto, tipo y color'( ) {
    given:
    def articulo = new Articulo( id: 1, articulo: 'AT2101', codigoColor: '10', idGenerico: 'A' )
    def item = new Item( id: 1, name: 'AT2101', color: '10', type: 'A' )

    when:
    def actual = controller.findItemsByQuery( 'AT2101+A,10' )

    then:
    1 * articuloService.listarArticulosPorCodigo( 'AT2101', true ) >> [ articulo ]
    [ item ] == actual
  }

  def 'regresa lista de items cuando busca query con codigo similar'( ) {
    given:
    def a1 = new Articulo( id: 1, articulo: 'AT2101', codigoColor: '10', idGenerico: 'A' )
    def a2 = new Articulo( id: 2, articulo: 'AT2101', codigoColor: 'U9', idGenerico: 'A' )
    def i1 = new Item( id: 1, name: 'AT2101', color: '10', type: 'A' )
    def i2 = new Item( id: 2, name: 'AT2101', color: 'U9', type: 'A' )

    when:
    def actual = controller.findItemsByQuery( 'AT21*' )

    then:
    1 * articuloService.listarArticulosPorCodigoSimilar( 'AT21', true ) >> [ a1, a2 ]
    [ i1, i2 ] == actual
  }

  def 'regresa lista de items cuando busca query con codigo similar y color'( ) {
    given:
    def a1 = new Articulo( id: 1, articulo: 'AT2100', codigoColor: '10', idGenerico: 'A' )
    def a2 = new Articulo( id: 2, articulo: 'AT2101', codigoColor: '10', idGenerico: 'A' )
    def i1 = new Item( id: 1, name: 'AT2100', color: '10', type: 'A' )
    def i2 = new Item( id: 2, name: 'AT2101', color: '10', type: 'A' )

    when:
    def actual = controller.findItemsByQuery( 'AT21*,10' )

    then:
    1 * articuloService.listarArticulosPorCodigoSimilar( 'AT21', true ) >> [ a1, a2 ]
    [ i1, i2 ] == actual
  }

  def 'regresa lista de items cuando busca query con codigo similar y tipo'( ) {
    given:
    def a1 = new Articulo( id: 1, articulo: 'X1', idGenerico: 'B' )
    def a2 = new Articulo( id: 2, articulo: 'X1C', idGenerico: 'B' )
    def i1 = new Item( id: 1, name: 'X1', type: 'B' )
    def i2 = new Item( id: 2, name: 'X1C', type: 'B' )

    when:
    def actual = controller.findItemsByQuery( 'X1*+B' )

    then:
    1 * articuloService.listarArticulosPorCodigoSimilar( 'X1', true ) >> [ a1, a2 ]
    [ i1, i2 ] == actual
  }

  def 'regresa lista de items cuando busca query con codigo similar, color y tipo'( ) {
    given:
    def a1 = new Articulo( id: 1, articulo: 'AT2100', codigoColor: '10', idGenerico: 'A' )
    def a2 = new Articulo( id: 2, articulo: 'AT2101', codigoColor: '10', idGenerico: 'A' )
    def i1 = new Item( id: 1, name: 'AT2100', color: '10', type: 'A' )
    def i2 = new Item( id: 2, name: 'AT2101', color: '10', type: 'A' )

    when:
    def actual = controller.findItemsByQuery( 'AT21*,10+A' )

    then:
    1 * articuloService.listarArticulosPorCodigoSimilar( 'AT21', true ) >> [ a1, a2 ]
    [ i1, i2 ] == actual
  }

  def 'regresa lista de items cuando busca query con codigo similar, tipo y color'( ) {
    given:
    def a1 = new Articulo( id: 1, articulo: 'AT2100', codigoColor: '10', idGenerico: 'A' )
    def a2 = new Articulo( id: 2, articulo: 'AT2101', codigoColor: '10', idGenerico: 'A' )
    def i1 = new Item( id: 1, name: 'AT2100', color: '10', type: 'A' )
    def i2 = new Item( id: 2, name: 'AT2101', color: '10', type: 'A' )

    when:
    def actual = controller.findItemsByQuery( 'AT21*+A,10' )

    then:
    1 * articuloService.listarArticulosPorCodigoSimilar( 'AT21', true ) >> [ a1, a2 ]
    [ i1, i2 ] == actual
  }
}
