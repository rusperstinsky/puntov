package mx.lux.pos.service

import mx.lux.pos.model.Paises

interface PaisesService {

  List<Paises> obtenerPaises( )

  List<Paises> obtenerEstados( )

  void guardarOrdenPais( String pais )

  void guardarPais( String pais )

}
