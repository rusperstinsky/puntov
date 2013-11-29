package mx.lux.pos.ui.view

class PromotionEditorDataSample {

  Boolean applied
  String description
  String partCode
  BigDecimal price
  BigDecimal discount

  void setApplied( Boolean pApplied ) {
    applied = pApplied
    println String.format( "Applied: %b", applied )
  }
}
