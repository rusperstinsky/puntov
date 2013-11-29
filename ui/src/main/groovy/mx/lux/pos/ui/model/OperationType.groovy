package mx.lux.pos.ui.model

enum OperationType {
  DEFAULT( 'P\u00fablico General' ),
  WALKIN( 'Cliente Estad\u00edstica' ),
  DOMESTIC( 'Cliente Nacional' ),
  FOREIGN( 'Cliente Extranjero' ),
  QUOTE( 'Cotización' ) ,
  AGREEMENT( 'Convenio' )

  final String value

  private OperationType( String value ) {
    this.value = value
  }

  static OperationType parse( String value ) {
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
