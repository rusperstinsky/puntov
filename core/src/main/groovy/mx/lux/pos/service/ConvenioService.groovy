package mx.lux.pos.service

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.ArticuloSombra
import mx.lux.pos.model.Generico
import mx.lux.pos.model.InstitucionIc

interface ConvenioService {

    List<InstitucionIc> obtenerConvenios( String clave  )

}
