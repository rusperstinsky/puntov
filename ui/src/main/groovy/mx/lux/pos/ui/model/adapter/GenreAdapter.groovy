package mx.lux.pos.ui.model.adapter

import mx.lux.pos.model.Generico
import org.apache.commons.lang3.StringUtils

class GenreAdapter extends StringAdapter<Generico> {

  String getText( Generico pGenerico ) {
    return String.format( "(%s) %s", pGenerico.id, StringUtils.trimToEmpty( pGenerico.descripcion ) )
  }
}
