package mx.lux.pos.service

import mx.lux.pos.model.Estado

interface EstadoService {

  Estado obtenerEstado( String id )

  List<Estado> listaEstados( )

  Estado obtenEstadoPorDefecto( )

  Estado obtenEstadoPorNombre( String nombre )

}
