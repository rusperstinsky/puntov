package mx.lux.pos.ui.model

enum DealType {
  DISCOUNT( 'Descuento' ),
  BUNDLE( 'Paquete' )

  final String value

  private DealType( String value ) {
    this.value = value
  }

  static DealType parse( String value ) {
    for ( item in values() ) {
      if ( item.value.equalsIgnoreCase( value?.trim() ) ) {
        return item
      }
    }
    return null
  }

  @Override
  String toString( ) {
    value
  }
}
