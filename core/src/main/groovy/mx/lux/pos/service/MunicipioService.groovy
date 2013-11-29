package mx.lux.pos.service

import mx.lux.pos.model.Municipio

interface MunicipioService {

  List<Municipio> listaMunicipiosPorEstado( String estado )

}
