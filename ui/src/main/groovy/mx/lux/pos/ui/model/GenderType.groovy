package mx.lux.pos.ui.model

enum GenderType {
  MALE( 'Masculino' ),
  FEMALE( 'Femenino' )

  final String value

  private GenderType( String value ) {
    this.value = value
  }

  static GenderType parse( String value ) {
    for ( item in values() ) {
      if ( item.value.equalsIgnoreCase( value?.trim() ) ) {
        return item
      }
    }
    return null
  }

  static GenderType parse( boolean value ) {
    return value ? MALE : FEMALE
  }

  @Override
  String toString( ) {
    value
  }
}
