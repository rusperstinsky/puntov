package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.model.Item
import mx.lux.pos.ui.model.PriceList
import mx.lux.pos.ui.view.renderer.MoneyCellRenderer
import net.miginfocom.swing.MigLayout

import java.awt.Component

class PendingPriceListDialog {

  private def sb = new SwingBuilder()
  private def items = [ ]

  PendingPriceListDialog( Component component, PriceList priceList ) {
    items = priceList?.items
    sb.dialog( title: "Lista de Precios: ${priceList?.id}",
        locationRelativeTo: component,
        resizable: false,
        modal: true,
        pack: true,
        show: true,
        layout: new MigLayout( 'wrap', '[fill]' )
    ) {
      label( "Contenido Lista de Precios: ${priceList?.id}" )

      scrollPane {
        table() {
          tableModel( list: items ) {
            closureColumn( header: 'SKU', read: {Item em -> em?.id} )
            closureColumn( header: 'Artículo', read: {Item em -> em?.name} )
            closureColumn( header: 'Descripción', read: {Item em -> em?.reference} )
            //closureColumn( header: 'Tipo Precio', read: {Item em -> em?.priceType} )
            closureColumn( header: 'Precio', read: {Item em -> em?.price}, cellRenderer: new MoneyCellRenderer() )
          }
        }
      }

      panel( layout: new MigLayout( 'fill', '[right]' ) ) {
        button( 'Cerrar', defaultButton: true, actionPerformed: {dispose()} )
      }
    }
  }
}
