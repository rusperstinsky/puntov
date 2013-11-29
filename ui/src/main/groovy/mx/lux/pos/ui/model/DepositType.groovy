package mx.lux.pos.ui.model


enum DepositType {

  CASH( 'EFECTIVO' ),
  USD( 'DOLARES' )

  final String value

  private DepositType( String value ) {
    this.value = value
  }

  static DepositType parse( String value ) {
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
