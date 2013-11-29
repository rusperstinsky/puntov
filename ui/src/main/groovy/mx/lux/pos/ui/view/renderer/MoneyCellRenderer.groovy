package mx.lux.pos.ui.view.renderer

import mx.lux.pos.ui.model.Item

import java.text.NumberFormat
import javax.swing.table.DefaultTableCellRenderer

class MoneyCellRenderer extends DefaultTableCellRenderer {

  MoneyCellRenderer( ) {
    setHorizontalAlignment( RIGHT )
  }

  @Override
  public void setValue( Object value ) {
    Object tmp = null
    if ( value instanceof String ) {
      tmp = value
    } else if ( value instanceof Number ) {
      tmp = value
    } else if ( value instanceof Item ) {
      tmp = value.price
    }
    setText( NumberFormat.getCurrencyInstance( Locale.US ).format( tmp ?: 0 ) )
  }
}
