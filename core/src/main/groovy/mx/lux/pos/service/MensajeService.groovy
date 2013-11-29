package mx.lux.pos.service

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.ArticuloSombra
import mx.lux.pos.model.Generico

interface MensajeService {

  String obtenerMensajePorId( Integer id )

  String obtenerMensajePorClave( String clave )

  void importarArchivo( String pImportFile )
}
