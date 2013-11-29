package mx.lux.pos.ui.view.driver

import mx.lux.pos.model.IPromotionAvailable
import mx.lux.pos.model.PromotionAvailable
import mx.lux.pos.model.PromotionModel
import mx.lux.pos.service.PromotionService
import mx.lux.pos.ui.model.ICorporateKeyVerifier
import mx.lux.pos.ui.model.IPromotionDrivenPanel
import mx.lux.pos.ui.resources.ServiceManager
import mx.lux.pos.ui.view.dialog.DiscountDialog
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.swing.JComponent
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import mx.lux.pos.model.PromotionDiscount

class PromotionDriver implements TableModelListener, ICorporateKeyVerifier {

  private static final String MSG_POST_DISCOUNT_FAILED = "Hubo un error al habilitar descuento.\nNotifique a soporte técnico."
  private static final String MSG_POST_FAILED = "Hubo un error al registrar promociones.\nNotifique a soporte técnico."
  private static final String TXT_POST_DISCOUNT_TITLE = "Aplicacion de Descuentos"
  private static final String TXT_POST_TITLE = "Registro de Promociones"

  private static final Logger log = LoggerFactory.getLogger( PromotionDriver.class )

  private static PromotionDriver instance

  IPromotionDrivenPanel view
  PromotionModel promotionModel
  DiscountDialog dlgDiscount
  Boolean itemsTableEventsEnabled = true

  private PromotionDriver( ) {  }

  // Internal methods
  protected void setDlgDiscount( DiscountDialog pDialog ) {
    this.dlgDiscount = pDialog
  }

  protected void setItemsTableEventsEnabled( Boolean pEnabled ) {
    this.itemsTableEventsEnabled = pEnabled
  }

  protected void updatePromotionList( ) {
    view.promotionList.clear()
    view.promotionList.addAll( this.model.listAvailablePromotions() )
    if ( this.model.hasOrderDiscountApplied() ) {
      view.promotionList.add( this.model.orderDiscount )
      Collections.sort( view.promotionList )
    }
    view.promotionModel.fireTableDataChanged()
  }

  // Properties
  static PromotionDriver getInstance() {
    if (instance == null) {
      instance = new PromotionDriver()
    }
    return instance
  }

  void enableItemsTableEvents( Boolean pEnabled ) {
    this.setItemsTableEventsEnabled( pEnabled )
  }

  PromotionModel getModel( ) {
    return this.promotionModel
  }

  PromotionService getService( ) {
    return ServiceManager.promotionService
  }

  Boolean isDiscountEnabled( ) {
    boolean enabled = ( ( !this.model.isAnyApplied() )
        && ( !this.model.hasOrderDiscountApplied() )
        && ( view.order.total.compareTo( BigDecimal.ZERO ) > 0 )
    )
    return enabled
  }

  Boolean isCorporateDiscountEnabled( ) {
    boolean enabled = ( ( !this.model.hasOrderDiscountApplied() )
        && ( view.order.total.compareTo( BigDecimal.ZERO ) > 0 )
    )
    return enabled
  }

  void init( IPromotionDrivenPanel pOrderPanel ) {
    this.view = pOrderPanel
    this.promotionModel = new PromotionModel()
  }

  // Commands
  void requestApplyPromotion( IPromotionAvailable pPromotion ) {
    log.debug( String.format( "Apply Promotion: %s", pPromotion.toString() ) )
    if ( pPromotion instanceof PromotionAvailable ) {
      service.requestApplyPromotion( this.model, pPromotion )
      this.updatePromotionList()
      view.refreshData()
    }
  }

  void requestCancelPromotion( IPromotionAvailable pPromotion ) {
    log.debug( String.format( "Cancel Promotion: %s", pPromotion.toString() ) )
    if ( pPromotion instanceof PromotionAvailable ) {
      service.requestCancelPromotion( this.model, pPromotion )
      this.updatePromotionList()
      view.refreshData()
    }

    if( pPromotion instanceof PromotionDiscount ){
        service.requestCancelPromotionDiscount( this.model, pPromotion )
        this.updatePromotionList()
        view.refreshData()
    }
  }

  void requestDiscount( Boolean authorizationManager, Boolean needAuthorization ) {
    log.debug( "Discount Selected" )
    DiscountDialog dlgDiscount = new DiscountDialog( false, authorizationManager, needAuthorization )
    dlgDiscount.setOrderTotal( view.order.total )
    dlgDiscount.setMaximumDiscount( service.requestTopStoreDiscount() )
    dlgDiscount.activate()
    if ( dlgDiscount.getDiscountSelected() ) {
      log.debug( String.format( "Discount Selected: %,.2f (%,.1f%%)", dlgDiscount.getDiscountAmt(),
          dlgDiscount.getDiscountPct() )
      )
      Double discount = dlgDiscount.getDiscountAmt() / view.order.total
      if ( service.requestOrderDiscount( this.model, "", discount ) ) {
        log.debug( this.model.orderDiscount.toString() )
        this.updatePromotionList()
        view.refreshData()
      } else {
        JOptionPane.showMessageDialog( view as JComponent, MSG_POST_DISCOUNT_FAILED, TXT_POST_DISCOUNT_TITLE,
            JOptionPane.ERROR_MESSAGE
        )
      }
    }
  }

  void requestCorporateDiscount( Boolean authorizationManager, Boolean needAuthorization ) {
    log.debug( "Corporate Discount Selected" )
    DiscountDialog dlgDiscount = new DiscountDialog( true, authorizationManager, needAuthorization )
    dlgDiscount.setOrderTotal( view.order.total )
    dlgDiscount.setVerifier( this )
    dlgDiscount.activate()
    if ( dlgDiscount.getDiscountSelected() ) {
      log.debug( String.format( "Corporate Discount Selected: %,.2f (%,.1f%%)", dlgDiscount.getDiscountAmt(),
          dlgDiscount.getDiscountPct() ) )
      Double discount = dlgDiscount.getDiscountAmt() / view.order.total
      if ( service.requestOrderDiscount( this.model, dlgDiscount.corporateKey, discount ) ) {
        log.debug( this.model.orderDiscount.toString() )
        this.updatePromotionList()
        view.refreshData()
      } else {
        JOptionPane.showMessageDialog( view as JComponent, MSG_POST_DISCOUNT_FAILED, TXT_POST_DISCOUNT_TITLE,
            JOptionPane.ERROR_MESSAGE
        )
      }
    }
  }

  void requestPromotionSave( ) {
    log.debug( "Request promotion persist" )
    service.requestPersist( this.model )
  }

  Boolean requestVerify( String pCorporateKey, Double pDiscountPct ) {
    log.debug( String.format( "RequestVerify(%s, %.1f%%)", pCorporateKey, pDiscountPct ) )
    return this.service.requestVerify( pCorporateKey, pDiscountPct )
  }

  // Listening for events
  void tableChanged( TableModelEvent pEvent ) {
    if ( itemsTableEventsEnabled ) {
      log.debug( "Driver notified of TableModel changes" )
      log.debug( String.format( "Order: %s", view.order.id ) )
      String orderNbr = ""
      if ( view.order != null ) {
        orderNbr = StringUtils.trimToEmpty( view.order.id )
      }
      service.updateOrder( this.model, orderNbr )
      log.debug( this.model.availablePromotionList.toString() )
      SwingUtilities.invokeLater( new Runnable() {
        void run( ) {
          PromotionDriver.this.updatePromotionList()
          view.refreshData()
        }
      } )
    }
  }
}
