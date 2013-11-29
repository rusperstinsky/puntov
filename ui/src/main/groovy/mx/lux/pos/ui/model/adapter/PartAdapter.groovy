package mx.lux.pos.ui.model.adapter

import mx.lux.pos.model.Articulo
import org.apache.commons.lang3.StringUtils

@Singleton
class PartAdapter extends StringAdapter<Articulo> {
  
  static final String FLD_INV_DESC = "Inventory:Description"
  
  // Private Methods
  private String formatDescription( Articulo pPart ) {
    String desc = ""
    if (StringUtils.isEmpty(pPart.descripcion)) {
      if (StringUtils.isEmpty(pPart.codigoColor)) {
        desc = String.format( "%s", pPart.articulo )
      } else {
        desc = String.format( "[%s %s] %s", pPart.articulo, pPart.codigoColor,
               (StringUtils.isEmpty(pPart.descripcionColor) ? "" : pPart.descripcionColor) )
      }
    } else {
      if (StringUtils.isEmpty(pPart.codigoColor)) {
        desc = String.format( "[%s] %s", pPart.articulo, pPart.descripcion )
       } else {
        desc = String.format( "[%s] %s [%s] %s", pPart.articulo, pPart.descripcion, pPart.codigoColor,
               (StringUtils.isEmpty(pPart.descripcionColor) ? "" : pPart.descripcionColor) )
      }
    }
    return desc
  } 
  
  // Public Methods
  String getText( Articulo pPart ) {
    return String.format( "(Sku:%d) %s", pPart.id, getText( pPart, FLD_INV_DESC ) )
  }

  public String getText( Articulo pPart, String pField ) {
    String text = super.getText( pPart, pField )
    if ( pField.equalsIgnoreCase( FLD_INV_DESC ) ) {
      text = formatDescription( pPart )
    } 
    return text
  }
    
}
