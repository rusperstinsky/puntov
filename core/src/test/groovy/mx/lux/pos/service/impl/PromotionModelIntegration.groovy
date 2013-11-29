package mx.lux.pos.service.impl

import mx.lux.pos.service.business.PromotionCommit
import mx.lux.pos.service.business.PromotionEngine
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import mx.lux.pos.model.*
import mx.lux.pos.service.business.PromotionImportTask

@ContextConfiguration('classpath:spring-config.xml')
class PromotionModelIntegration extends Specification {

  def "Test PromotionOrder"( ) {
    setup:
      String vOrderNbr = "B28244"
    
    when:
      PromotionOrder order = new PromotionOrder( vOrderNbr )
      println order.toString()
      
    then:
      order.equals( vOrderNbr )
      order.regularAmount == 0
      order.finalSubtotalAmount == 0
      order.finalTotalAmount == 0
  }
  
  def "Test PromotionOrderDetail"( ) {
    setup:
      String vOrderNbr = "B28244"
      Integer vSku = 106243
      String vPartNbr = "AT2101"
      Integer vQtyOrder = 2
      Double vRegularPrice = 5740
      
    when:
      PromotionOrder order = new PromotionOrder( vOrderNbr )
      PromotionOrderDetail orderDetail = order.addOrderDetail( vSku )
      orderDetail.partNbr = vPartNbr
      orderDetail.qtyOrder = vQtyOrder
      orderDetail.regularPrice = vRegularPrice
      println order
      
    then:
      orderDetail.equals( vSku )
      orderDetail.partNbr.equals( vPartNbr )
      orderDetail.qtyOrder == vQtyOrder
      orderDetail.regularPrice == vRegularPrice
      orderDetail.finalPrice == vRegularPrice
      orderDetail.regularAmount == vQtyOrder * vRegularPrice
      orderDetail.finalAmount == vQtyOrder * vRegularPrice
      orderDetail.finalDiscountAmount == 0
      orderDetail.finalDiscountPercent == 0
      order.regularAmount == vQtyOrder * vRegularPrice
      order.finalSubtotalAmount == vQtyOrder * vRegularPrice
      order.finalTotalAmount == vQtyOrder * vRegularPrice

  }

  def "Test PromotionOrder Load from NotaVenta"( ) {
    setup:
      String vOrderNbr = "B28244"
      Double expectedSales = 7760
      PromotionModel model = new PromotionModel( )
      PromotionEngine engine = PromotionEngine.getInstance( )
      
    when:
      engine.updateOrder( model, vOrderNbr )
      PromotionOrder order = model.order
//      println model
      
    then:
      order.equals( vOrderNbr )
      order.regularAmount == expectedSales
      order.finalSubtotalAmount == expectedSales
      order.finalTotalAmount == expectedSales

  }

  def "Test Pricing List"( ) {
    setup:
      String vOrderNbr = "B28245"
      PromotionModel model = new PromotionModel( )
      PromotionEngine engine = PromotionEngine.getInstance( )
      
    when:
      engine.updateOrder( model, vOrderNbr )  
//      println model
      
    then:
      model.isOrderEquals( vOrderNbr )
      model.pricingSet.dataset.size( ) == 3
  }
  
  def "Test PromotionAvailable after Loading B28267"( ) {
    setup:
      String vOrderNbr = "B28267"
      PromotionModel model = new PromotionModel( )
      PromotionEngine engine = PromotionEngine.getInstance( )
    
    when:
      engine.updateOrder( model, vOrderNbr )
      PromotionAvailableList availableList = model.availablePromotionList
      engine.applyPromotion( model, availableList.find( 1, 930 ) )
      engine.applyPromotion( model, availableList.find( 2, 106249 ) )
      engine.applyOrderDiscount( model, "2601232", 0.35 )
      println model
      
    then:
      availableList.list.size( ) == 6
      Math.abs( model.order.finalTotalAmount - 3042.0) < 0.01 

    when:
      OrdenPromDetList opdl = new OrdenPromDetList( 1, model )
      OrdenPromList opl = new OrdenPromList( opdl )
      PromotionCommit.writePromotions( model )
      PromotionCommit.writeDiscounts( model )
      println opl
      println opdl
      
    then:
      opl.list.size( ) == 2
      opdl.list.size( ) == 2

  }


}
