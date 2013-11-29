package mx.lux.pos.ui.view

import groovy.model.DefaultTableModel
import groovy.swing.SwingBuilder
import mx.lux.pos.ui.view.renderer.MoneyCellRenderer

import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.ListSelectionModel
import javax.swing.SwingUtilities

class PromotionEditorSample extends JFrame {

  private SwingBuilder sb = new SwingBuilder( )
  
  private DefaultTableModel browserPromotion
  
  PromotionEditorSample() {
    promotionList.add( new PromotionEditorDataSample( applied: true, description: "40% Articulos seleccionados", 
         partCode: "NK0033G", price: 440.00, discount: 176.00 ) )
    promotionList.add( new PromotionEditorDataSample( applied: false, 
         description: "En la compra del armazon, gratis lente plastico monofocal", 
         partCode: "N1G", price:1130.00, discount: 1130.0 ) ) 
    promotionList.add( new PromotionEditorDataSample( applied: false, 
         description: "50% descuento en articulo seleccionados", 
         partCode: "AT2101", price:5740.00, discount: 2870.0 ) )
    buildUI( )
  }

  List<PromotionEditorDataSample> promotionList = new ArrayList<PromotionEditorDataSample>()
  
  private void buildUI( ) {
    sb.build( ) {
      lookAndFeel( 'system' )
      frame( this,
          title: 'Promotions Panel',
          show: true,
          pack: true,
          resizable: true,
          preferredSize: [600, 200],
          defaultCloseOperation: EXIT_ON_CLOSE
      ) {
        panel( ) {
          borderLayout( )
          scrollPane( constraints: BorderLayout.CENTER,
              border: BorderFactory.createTitledBorder( "Promociones" )
          ) {
            table( selectionMode: ListSelectionModel.SINGLE_SELECTION ) {
              browserPromotion = sb.tableModel( list: promotionList ) {
                closureColumn( header: "", type: Boolean, maxWidth: 25, 
                  read: { it.applied }, write: { row, newValue -> onToggleSelection( row, newValue ) }
                 )
                propertyColumn( header: "Promocion", propertyName: "description", editable: false )
                propertyColumn( header: "Articulo", propertyName: "partCode", maxWidth: 100, editable: false )
                closureColumn( header: "Precio", read: { it.price }, maxWidth: 100,
                    cellRenderer: new MoneyCellRenderer( ) 
                )
                closureColumn( header: "Descto", read: { it.discount }, maxWidth: 100,
                    cellRenderer: new MoneyCellRenderer( ) 
                )
              }
            }
          }
        }
      }
    }
  }

  protected void onToggleSelection( PromotionEditorDataSample data, Boolean pNewValue ) {
    println "Data: ${ data }\n   ChangeAppliedTo: ${ pNewValue }"   
  }
  
  static void main( String[] args ) {
    SwingUtilities.invokeLater(
      new Runnable( ) {
        void run( ) {
          PromotionEditorSample sample = new PromotionEditorSample( )
        }
      }
    )
  }


}
