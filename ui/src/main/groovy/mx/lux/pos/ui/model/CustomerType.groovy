package mx.lux.pos.ui.model

enum CustomerType {
  DOMESTIC( 'Nacional', 'XAXX010101000' ),
  FOREIGN( 'Extranjero', 'XEXX010101000' )

  final String value
  final String rfc

  private CustomerType( String value, String rfc ) {
    this.value = value
    this.rfc = rfc
  }

  static CustomerType parse( String value ) {
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
