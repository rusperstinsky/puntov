package mx.lux.pos.ui.model

import groovy.model.DefaultTableModel
import mx.lux.pos.model.IPromotionAvailable

interface IPromotionDrivenPanel {

  Order getOrder( )
  
  DefaultTableModel getPromotionModel( )
 
  List<IPromotionAvailable> getPromotionList( )

  void refreshData( )
}
