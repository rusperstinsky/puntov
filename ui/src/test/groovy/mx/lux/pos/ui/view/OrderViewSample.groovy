package mx.lux.pos.ui.view

import groovy.swing.SwingBuilder
import mx.lux.pos.service.business.PromotionEngine
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.dialog.DiscountDialog
import mx.lux.pos.ui.view.driver.PromotionDriver
import mx.lux.pos.ui.view.renderer.MoneyCellRenderer

import java.awt.BorderLayout
import java.awt.Font
import java.awt.event.MouseEvent
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.AbstractTableModel
import javax.swing.*

class OrderViewSample extends JPanel implements TableModelListener {
  
  private SwingBuilder sb = new SwingBuilder( )
  
  JTextField txtPart
  AbstractTableModel browserPart, browserPromotion
  JPopupMenu discountMenu
  PromotionDriver driver = new PromotionDriver( )
  
  def partList = [
    [ partCode: "NK0033G 008", description: "NIKE TARJ [008] PLATA/LENTE GRIS OBSCURO (S)", quantity: 1, price: 264.0 ],
    [ partCode: "FR0008G", description: "GOGLE SALVADOR FERRAGAMO FR0008G", quantity: 1, price: 1390.0 ],
    [ partCode: "N1G", description: "NUCLONIC MONOFOCAL GRADASOL AA Y UV 0.00 A 6.00", quantity: 1, price: 0.0 ],
    [ partCode: "AT2101 ST", description: "AIR TITANIUM", quantity: 1, price: 2870.0 ]
  ]

  def promotionList = [
    [ selected: true, text: "40% Articulos seleccionados", partCode: "NK0033G", price: 440.00, discount: 176.00 ],
    [ selected: false, text: "En la compra del armazon, gratis lente plastico monofocal", partCode: "N1G", price:1130.00, discount: 1130.0 ],  
    [ selected: false, text: "50% descuento en articulo seleccionados", partCode: "AT2101", price:5740.00, discount: 2870.0 ]  
  ]
  
  OrderViewSample( ) {
    buildUI( )
    browserPromotion.addTableModelListener( this )
  }
  
  private void buildUI() {
    sb.panel( this ) {
      borderLayout( )
      composeTopSection( )
      composeMidSection( )
      composeBottomSection( )  
    }
    discountMenu = sb.popupMenu( ) {
      menuItem( text: "Descuento Tienda", visible: true, actionPerformed: { onDiscountSelected( ) } )
      menuItem( text: "Descuento Corporativo", visible: true, actionPerformed: { onDiscountCorporate( ) } )
    }
  }
  
  private JComponent composeBottomSection() {
    sb.panel( constraints: BorderLayout.PAGE_END, 
        border: BorderFactory.createLoweredBevelBorder( ),
        background: UI_Standards.LOCKED_BACKGROUND
    ) {
      label( text: " ", preferredSize: [ 20, 85 ] )
    }
  }
  
  private JComponent composeMidSection( ) {
    sb.panel( constraints: BorderLayout.CENTER ) {
      borderLayout( ) 
      composePartDetailPanel( ) 
      panel( constraints: BorderLayout.CENTER ) {
        borderLayout( )
        composePromotionPanel( ) 
        panel( constraints: BorderLayout.LINE_END,
            border: BorderFactory.createLoweredBevelBorder( ),
            background: UI_Standards.LOCKED_BACKGROUND
        ) {
          label( text: " ", preferredSize: [ 240, 130 ])
        }
      }
    }
  }

  private JComponent composePartDetailPanel( ) {
    sb.panel( constraints: BorderLayout.PAGE_START ) {
      borderLayout( )
      txtPart = textField( constraints: BorderLayout.PAGE_START,
          font: new Font( "", Font.BOLD, 16 ),
          actionPerformed: { event -> println event.source.text  }
      )
      scrollPane( constraints: BorderLayout.CENTER,
          border:BorderFactory.createTitledBorder( "Artículos" ),
          preferredSize: [ 780, 150 ],
      ) {
        table( selectionMode: ListSelectionModel.SINGLE_SELECTION, 
        ) {
          browserPart = sb.tableModel( list: partList ) {
            propertyColumn( header: "Artículo", propertyName: "partCode", maxWidth: 100, editable: false )
            propertyColumn( header: "Descripción", propertyName: "description", editable: false )
            propertyColumn( header: "Cantidad", propertyName: "quantity", maxWidth: 70, editable: false )
            closureColumn( header: 'Precio', read: { it.price }, maxWidth: 100,
                cellRenderer: new MoneyCellRenderer( )
            )
            closureColumn( header: 'Total', read: { it.price * it.quantity }, maxWidth: 100,
                cellRenderer: new MoneyCellRenderer( )
            )
          }
        }
      }
    }
  }  
  
  private JComponent composePromotionPanel( ) {
    sb.scrollPane( constraints: BorderLayout.CENTER,
        border: BorderFactory.createTitledBorder( "Promociones" )
    ) {
      table( selectionMode: ListSelectionModel.SINGLE_SELECTION,
            mouseClicked: { MouseEvent ev -> onMouseClicked( ev ) },
            mouseReleased: { MouseEvent ev -> onMouseClicked( ev ) } 
      ) {
        browserPromotion = sb.tableModel( list: promotionList ) {
          propertyColumn( header: "", propertyName: "selected", type: Boolean, maxWidth: 25, editable: true )
          propertyColumn( header: "Promocion", propertyName: "text", editable: false )
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

  private JComponent composeTopSection( ) {
    sb.panel( constraints: BorderLayout.PAGE_START, 
        border: BorderFactory.createLoweredBevelBorder( ),
        background: UI_Standards.LOCKED_BACKGROUND
    ) {
      label( text: " ", preferredSize: [ 20, 125 ])
    }
  }
  
  protected void onMouseClicked( MouseEvent pEvent ) { 
    if ( pEvent.isPopupTrigger( ) ) {
      discountMenu.show( pEvent.getComponent( ), pEvent.getX( ), pEvent.getY( )) 
    }  
  }

  protected void onDiscountCorporate( ) {
    DiscountDialog dlgDiscount = new DiscountDialog( true )
    dlgDiscount.setOrderTotal( 10000 )
    dlgDiscount.setDiscountPct( 45.0 )
    dlgDiscount.setMaximumDiscount( 50.0 ) 
    dlgDiscount.setKeyVerifier( new PromotionEngine( ) )   
    dlgDiscount.activate( )
    if ( dlgDiscount.getDiscountSelected( ) ) {
      println String.format( "Discount Selected: %,.1f%% (%,.2f)", dlgDiscount.getDiscountPct( ),
          dlgDiscount.getDiscountAmt( ) )
    }
  }

  protected void onDiscountSelected( ) {
    DiscountDialog dlgDiscount = new DiscountDialog( false )
    dlgDiscount.setOrderTotal( 10000 )
    dlgDiscount.setDiscountAmt( 852.40 )
    dlgDiscount.setMaximumDiscount( 10.0 )    
    dlgDiscount.activate( )
    if ( dlgDiscount.getDiscountSelected( ) ) {
      println String.format( "Discount Selected: %,.2f (%,.1f%%)", dlgDiscount.getDiscountAmt( ),
          dlgDiscount.getDiscountPct( ) )
    }
  } 
   
  public void tableChanged( TableModelEvent pEvent ) {
    println String.format( "Row:%d, Column:%d, Type:%d", pEvent.firstRow, pEvent.column, pEvent.type )
  }
  
}
