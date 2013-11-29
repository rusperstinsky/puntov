package mx.lux.pos.ui.view

import groovy.model.DefaultTableModel
import groovy.swing.SwingBuilder
import mx.lux.pos.model.IPromotionAvailable
import mx.lux.pos.model.PromotionAvailable
import mx.lux.pos.ui.controller.OrderController
import mx.lux.pos.ui.model.IPromotionDrivenPanel
import mx.lux.pos.ui.model.Order
import mx.lux.pos.ui.model.OrderItem
import mx.lux.pos.ui.view.component.DiscountContextMenu
import mx.lux.pos.ui.view.driver.PromotionDriver
import mx.lux.pos.ui.view.renderer.MoneyCellRenderer
import net.miginfocom.swing.MigLayout

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.swing.*

class PromotionDrivenPanelSample extends JFrame implements IPromotionDrivenPanel {

  private static final String TXT_DISCOUNT_LABEL = "Discount Amount (%.1f%%)"

  private SwingBuilder sb = new SwingBuilder()

  private PromotionDriver driver
  private Order order

  private JPanel viewSample
  private JPanel mainPanel
  private JTextField txtOrderNbr, txtOrderDate, txtOrderSubtotal, txtOrderDiscount, txtOrderTotal, txtTotal
  private JLabel lblDiscount
  private List<OrderItem> itemList
  private List<IPromotionAvailable> promotionList
  DefaultTableModel itemsModel, promotionModel
  DiscountContextMenu discountMenu

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )

  PromotionDrivenPanelSample( ) {
    init()
    buildUI()
    activate()
  }

  // Internal Methods
  protected void buildUI( ) {
    sb.build() {
      lookAndFeel( 'system' )
      frame( this,
          title: 'Sample Promotion Driven Panel to test PromotionDriver Capabilities',
          show: true,
          pack: true,
          resizable: true,
          preferredSize: [ 800, 600 ] as Dimension,
          defaultCloseOperation: EXIT_ON_CLOSE
      ) {
        mainPanel = panel() {
          borderLayout()
          composeOrderHeaderPane()
          composeOrderDetailPane()
          panel( constraints: BorderLayout.PAGE_END, preferredSize: [ 800, 200 ] as Dimension ) {
            borderLayout()
            composePromotionsPane()
            composeSummaryPane()
          }
        }
      }
    }
  }

  protected JComponent composeOrderHeaderPane( ) {
    sb.panel( constraints: BorderLayout.PAGE_START,
        border: BorderFactory.createTitledBorder( " Order " ),
        layout: new MigLayout( "wrap 2", "20[]10[fill,grow]500" )
    ) {
      label( text: "Order Number" )
      txtOrderNbr = textField( text: "B28244", actionPerformed: { onOrderNbrValueChanged() } )
      label( text: "Order Date" )
      txtOrderDate = textField( text: "10/09/2012", editable: false, background: Color.WHITE )
    }
  }

  protected JComponent composeOrderDetailPane( ) {
    sb.scrollPane( constraints: BorderLayout.CENTER,
        border: BorderFactory.createTitledBorder( " Order Lines " )
    ) {
      table( selectionMode: ListSelectionModel.SINGLE_SELECTION ) {
        itemsModel = tableModel( list: itemList ) {
          closureColumn(
              header: 'Art\u00edculo',
              read: {OrderItem tmp -> "${tmp?.item?.name} ${tmp?.item?.color ?: ''}"},
              minWidth: 80,
              maxWidth: 100
          )
          closureColumn(
              header: 'Descripci\u00f3n',
              read: {OrderItem tmp -> tmp?.description}
          )
          closureColumn(
              header: 'Cantidad',
              read: {OrderItem tmp -> tmp?.quantity},
              minWidth: 70,
              maxWidth: 70
          )
          closureColumn(
              header: 'Precio',
              read: {OrderItem tmp -> tmp?.item?.price},
              minWidth: 80,
              maxWidth: 100,
              cellRenderer: new MoneyCellRenderer()
          )
          closureColumn(
              header: 'Total',
              read: {OrderItem tmp -> tmp?.item?.price * tmp?.quantity},
              minWidth: 80,
              maxWidth: 100,
              cellRenderer: new MoneyCellRenderer()
          )
        } as DefaultTableModel
      }
    }
  }

  protected JComponent composePromotionsPane( ) {
    sb.scrollPane( constraints: BorderLayout.CENTER,
        border: BorderFactory.createTitledBorder( " Promotions " )
    ) {
      table( selectionMode: ListSelectionModel.SINGLE_SELECTION,
          mouseClicked: { MouseEvent ev -> onMouseClicked( ev ) },
          mouseReleased: { MouseEvent ev -> onMouseClicked( ev ) }
      ) {
        promotionModel = sb.tableModel( list: promotionList ) {
          closureColumn( header: "", type: Boolean, maxWidth: 25,
              read: { row -> row.applied },
              write: { row, newValue -> onTogglePromotion( row, newValue ) }
          )
          propertyColumn( header: "Promocion", propertyName: "description", editable: false )
          propertyColumn( header: "Articulo", propertyName: "partNbrList", maxWidth: 100, editable: false )
          closureColumn( header: "Precio Base",
              read: { IPromotionAvailable promotion -> promotion.baseAmount },
              maxWidth: 80,
              cellRenderer: new MoneyCellRenderer()
          )
          closureColumn( header: "Descto",
              read: { IPromotionAvailable promotion -> promotion.discountAmount },
              maxWidth: 80,
              cellRenderer: new MoneyCellRenderer()
          )
          closureColumn( header: "Promocion",
              read: { IPromotionAvailable promotion -> promotion.promotionAmount },
              maxWidth: 80,
              cellRenderer: new MoneyCellRenderer()
          )
        } as DefaultTableModel
      }
    }
  }

  protected JComponent composeSummaryPane( ) {
    sb.panel( constraints: BorderLayout.LINE_END,
        border: BorderFactory.createTitledBorder( " Summary " ),
        layout: new MigLayout( "wrap 2", "20[]10[fill,grow,120!]20" )
    ) {
      label( text: "Regular Amount" )
      txtOrderSubtotal = textField( text: "10,000.00", editable: false, background: Color.WHITE )
      lblDiscount = label( text: "Discount Amount (20.0%)" )
      txtOrderDiscount = textField( text: "2,000.00", editable: false, background: Color.WHITE )
      label( text: "Promotion Order Total" )
      txtOrderTotal = textField( text: "8,000.00", editable: false, background: Color.WHITE )
      label( text: "Order Total" )
      txtTotal = textField( text: "8,000.00", editable: false, background: Color.WHITE )
    }
  }

  protected void init( ) {
    itemList = new ArrayList<OrderItem>()
    promotionList = new ArrayList<PromotionAvailable>()
    order = OrderController.getOrder( "B28267" )
    driver = new PromotionDriver( this )
  }

  protected void activate( ) {
    itemsModel.addTableModelListener( driver )
    this.updateUI()
  }

  // Public Methods
  Order getOrder( ) {
    return order
  }

  List<IPromotionAvailable> getPromotionList( ) {
    return this.promotionList
  }

  DefaultTableModel getPromotionModel( ) {
    return this.promotionModel
  }

  void updateUI( ) {
    this.txtOrderNbr.text = order.id
    this.txtOrderDate.text = df.format( order.date )
    this.txtTotal.text = String.format( "%,.2f", order.total )

    this.itemList.clear()
    this.itemList.addAll( order.items )
    this.itemsModel.fireTableDataChanged()

    if ( order.promotions.order != null ) {
      this.lblDiscount.text = String.format( TXT_DISCOUNT_LABEL, order.promotions.order.discountPercent * 100.0 )
      this.txtOrderSubtotal.text = String.format( "%,.2f", order.promotions.order.regularAmount )
      this.txtOrderDiscount.text = String.format( "%,.2f", -1 * order.promotions.order.discountAmount )
      this.txtOrderTotal.text = String.format( "%,.2f", order.promotions.order.finalTotalAmount )
    } else {
      this.lblDiscount.text = String.format( TXT_DISCOUNT_LABEL, 0.0 )
      this.txtOrderSubtotal.text = "N/A"
      this.txtOrderDiscount.text = "N/A"
      this.txtOrderTotal.text = "N/A"
    }
  }

  void refreshData( ) {
    OrderController.updateOrder( order )
    sb.doLater {
      driver.enableItemsTableEvents( false )
      this.updateUI()
      //      itemsModel.fireTableDataChanged( )
      driver.enableItemsTableEvents( true )
    }
  }

  // UI Response
  protected void onMouseClicked( MouseEvent pEvent ) {
    if ( pEvent.isPopupTrigger() ) {
      if ( discountMenu == null ) {
        discountMenu = new DiscountContextMenu( driver )
      }
      discountMenu.activate( pEvent )

    }
  }

  protected void onOrderNbrValueChanged( ) {
    Order o = OrderController.getOrder( txtOrderNbr.text )
    if ( o != null ) {
      order = o
    }
    sb.doLater {
      updateUI()
    }
  }

  protected void onTogglePromotion( IPromotionAvailable pPromotion, Boolean pNewValue ) {
    if ( pNewValue ) {
      driver.requestApplyPromotion( pPromotion )
    } else {
      driver.requestCancelPromotion( pPromotion )
    }
  }

}
