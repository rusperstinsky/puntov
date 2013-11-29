package mx.lux.pos.service

import mx.lux.pos.model.Localidad

interface LocalidadService {

  List<Localidad> listaLocalidadesPorEstadoYMunicipio( String estado, String municipio )

  List<Localidad> listaLocalidadesPorCodigoYNombre( String codigo, String nombre )

  List<Localidad> listaLocalidadesPorCodigo( String codigo )

}
