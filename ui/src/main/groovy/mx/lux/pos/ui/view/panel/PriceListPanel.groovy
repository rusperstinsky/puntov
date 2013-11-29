package mx.lux.pos.ui.view.panel

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.PriceListController
import mx.lux.pos.ui.model.PriceList
import mx.lux.pos.ui.model.PriceListLoadType
import mx.lux.pos.ui.view.dialog.AuthorizationDialog
import mx.lux.pos.ui.view.dialog.PendingPriceListDialog
import mx.lux.pos.ui.view.dialog.ValidatePriceListDialog
import mx.lux.pos.ui.view.renderer.DateCellRenderer
import net.miginfocom.swing.MigLayout

import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.ListSelectionModel

class PriceListPanel {

  JPanel panel
  private def sb = new SwingBuilder()
  private def pending = [ ]

  PriceListPanel( ) {
    pending = PriceListController.getPendingPriceLists()

    panel = sb.panel( layout: new MigLayout( 'fill, wrap', '[fill]', '[fill]' ) ) {
      panel( border: titledBorder( 'Listas de Precios Sin Cargar' ) ) {
        scrollPane {
          table( id: 'pendingTable', selectionMode: ListSelectionModel.SINGLE_SELECTION, mouseClicked: pendingTableClick ) {
            tableModel( list: pending ) {
              closureColumn( header: 'Id Lista', read: { PriceList pl -> pl?.id } )
              closureColumn( header: 'Activación', read: { PriceList pl -> pl?.activated }, cellRenderer: new DateCellRenderer() )
              closureColumn( header: 'Carga Automática', read: { PriceList pl -> pl?.autoActivated }, cellRenderer: new DateCellRenderer() )
            }
          }
        }
      }
    }
  }

  def pendingTableClick = { ev ->
    sb.build {
      def source = ev.source
      def selectedItem = ev.source.selectedElement
      if ( ev.button == 1 ) {
        if ( ev.clickCount == 2 ) {
          new PendingPriceListDialog( source, selectedItem )
        }
      } else if ( ev.button == 3 ) {
        popupMenu {
          menuItem {
            action( name: 'Validar', shortDescription: 'Validar Lista de Precios' ) {
              new ValidatePriceListDialog( source, selectedItem )
            }
          }
          menuItem {
            action( name: 'Cargar Lista', shortDescription: 'Cargar Lista de Precios' ) {
              /*def authDialog = new AuthorizationDialog( source, 'Requiere autorización para cargar Lista de Precios', false )
              authDialog.show()
              if ( authDialog.authorized ) {*/
                def priceList = PriceListController.loadPriceList( selectedItem, PriceListLoadType.MANUAL )
                if ( priceList?.loaded ) {
                  optionPane( message: "Lista de precios: ${priceList.id} cargada el: ${priceList.loaded.format( 'dd/MM/yyyy HH:mm' )}",
                      optionType: JOptionPane.DEFAULT_OPTION
                  ).createDialog( panel, "Carga Manual" ).show()
                } else {
                  optionPane( message: "No se puede cargar Lista de precios: ${priceList.id}",
                      optionType: JOptionPane.DEFAULT_OPTION
                  ).createDialog( panel, "Carga Manual" ).show()
                }
                pending.clear()
                pending.addAll( PriceListController.getPendingPriceLists() )
                pendingTable.model.fireTableDataChanged()
              //}
            }
          }
        }.show( source, ev.x, ev.y )
      }
    }
  }
}
