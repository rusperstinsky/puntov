package mx.lux.pos.ui.view.renderer

import javax.swing.table.DefaultTableCellRenderer

class DateCellRenderer extends DefaultTableCellRenderer {

  DateCellRenderer( ) {
  }

  @Override
  public void setValue( Object value ) {
    if ( value instanceof Date ) {
      setText( value.format( 'dd-MM-yyyy' ) ?: '' )
    } else {
      setText( '' )
    }
  }
}
