package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.model.Item
import java.awt.Component
import net.miginfocom.swing.MigLayout
import javax.swing.ListSelectionModel
import mx.lux.pos.ui.view.renderer.MoneyCellRenderer
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities
import javax.swing.JDialog

class SuggestedConveniosDialog extends JDialog {

    private SwingBuilder sb
    private String code
    private List<Item> suggestions
    private Item item


    SuggestedConveniosDialog(Component parent, String code, List<Item> suggestions ) {
        this.code = code
        this.suggestions = suggestions
        sb = new SwingBuilder()
        item = null
        buildUI( parent )
    }

    Item getItem( ) {
        return item
    }

    private void buildUI( Component parent ) {
        sb.dialog( this,
                title: "Convenios sugeridos con: ${code ?: ''}",
                location: parent.locationOnScreen,
                resizable: false,
                modal: true,
                pack: true,
                layout: new MigLayout( 'fill,wrap', '[fill]' )
        ) {
            if ( suggestions?.any() ) {
                label( " Se encontraron ${suggestions.size()} convenios similares a: ${code}" )
                scrollPane( constraints: 'h 250!' ) {
                    table( selectionMode: ListSelectionModel.SINGLE_SELECTION, mouseClicked: doItemClick ) {
                        tableModel( list: suggestions ) {
                            closureColumn( header: 'Convenio', read: {Item tmp -> tmp?.name}, maxWidth: 100 )
                        }
                    }
                }
            } else {
                label( "No se encontraron art\u00edculos con: ${code}" )
            }

            panel( layout: new MigLayout( 'right', '[fill,100!]' ) ) {
                button( 'Cerrar', defaultButton: true, actionPerformed: {dispose()} )
            }
        }
    }

    private def doItemClick = { MouseEvent ev ->
        if ( SwingUtilities.isLeftMouseButton( ev ) ) {
            if ( ev.clickCount == 2 ) {
                item = ev.source.selectedElement
                dispose()
            }
        }
    }
}
