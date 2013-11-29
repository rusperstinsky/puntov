package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.model.Taxpayer
import net.miginfocom.swing.MigLayout

import java.awt.Component
import java.awt.event.MouseEvent
import javax.swing.JDialog
import javax.swing.ListSelectionModel
import javax.swing.SwingUtilities

class SuggestedTaxpayersDialog extends JDialog {

  private SwingBuilder sb
  private String rfc
  private List<Taxpayer> suggestions
  private Taxpayer taxpayer

  SuggestedTaxpayersDialog( Component component, String rfc, List<Taxpayer> suggestions ) {
    this.rfc = rfc
    this.suggestions = suggestions
    sb = new SwingBuilder()
    taxpayer = null
    buildUI( component )
  }

  Taxpayer getTaxpayer( ) {
    return taxpayer
  }

  private void buildUI( Component component ) {
    sb.dialog( this,
        title: "RFC sugeridos con: ${rfc ?: ''}",
        location: component.locationOnScreen,
        resizable: false,
        modal: true,
        pack: true,
        layout: new MigLayout( 'fill,wrap', '[fill]' )
    ) {
      if ( suggestions?.any() ) {
        label( " Se encontraron ${suggestions.size()} RFC similares a: ${rfc}" )
        scrollPane( constraints: 'h 250!' ) {
          table( selectionMode: ListSelectionModel.SINGLE_SELECTION, mouseClicked: doClick ) {
            tableModel( list: suggestions ) {
              closureColumn( header: 'RFC', read: {Taxpayer tmp -> tmp?.rfc}, maxWidth: 130 )
              closureColumn( header: 'Nombre', read: {Taxpayer tmp -> tmp?.name}, maxWidth: 340 )
            }
          }
        }
      } else {
        label( "No se encontraron RFC con: ${rfc}" )
      }

      panel( layout: new MigLayout( 'right', '[fill,100!]' ) ) {
        button( 'Cerrar', defaultButton: true, actionPerformed: {dispose()} )
      }
    }
  }

  private def doClick = { MouseEvent ev ->
    if ( SwingUtilities.isLeftMouseButton( ev ) ) {
      if ( ev.clickCount == 2 ) {
        taxpayer = ev.source.selectedElement
        dispose()
      }
    }
  }
}
