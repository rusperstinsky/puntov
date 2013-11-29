package mx.lux.pos.ui.model

enum PriceListLoadType {
  MANUAL,
  AUTO

  static PriceListLoadType parse( String value ) {
    return values().find {
      it.name().equalsIgnoreCase( value?.trim() )
    } as PriceListLoadType
  }
}
