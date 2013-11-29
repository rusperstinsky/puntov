package mx.lux.pos.service

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.ListaPrecios

interface ListaPreciosService {

  String obtenRutaPorRecibir( )

  String obtenRutaListaPrecios( )

  String obtenRutaRecibidos( )

  ListaPrecios registrarListaPrecios( ListaPrecios listaPrecios )

  List<Articulo> validarListaPrecios( ListaPrecios listaPrecios, List<Articulo> articulos )

  ListaPrecios cargarListaPrecios( ListaPrecios listaPrecios, List<Articulo> articulos )

  Integer listasPreciosPendientes( )

}
