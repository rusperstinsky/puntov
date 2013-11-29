package mx.lux.pos.model

enum MensajesPorParametro {
  CAMBIO( 'CAMB', 1 ),
  CIERRE_CORRECTO( 'CL', 2 ),
  CANCELACION_SATISFACTORIA( 'CANC', 3 ),
  DEVOLUCION_SATISFACTORIA( 'DEV', 4 ),
  CORRECCION_TEMINAL_SATISFACTORIA( 'CT', 5 ),
  CARGA_LP_SATISFACTORIA( 'LP', 6 ),

  final String clave
  final Integer id

  private MensajesPorParametro( String clave ) {
    this( clave, '' )
  }

  private MensajesPorParametro( String clave, Integer id ) {
    this.clave = clave
    this.id = id
  }

  static MensajesPorParametro parse( String clave ) {
    for ( item in values() ) {
      if ( item.clave.equalsIgnoreCase( clave?.trim() ) ) {
        return item
      }
    }
    return null
  }

  @Override
  String toString( ) {
    clave
  }
}
