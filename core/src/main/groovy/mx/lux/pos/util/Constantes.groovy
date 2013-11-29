package mx.lux.pos.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Constantes {
  private static Logger log = LoggerFactory.getLogger( Constantes.class )

  private static final ResourceBundle APPCONFIG = ResourceBundle.getBundle( "appconfig" )

  public static final Locale LOCALE = new Locale( APPCONFIG.getString( "locale_l" ), APPCONFIG.getString( "locale_c" ) )
  public static final TimeZone TIMEZONE = TimeZone.getTimeZone( APPCONFIG.getString( "timezone" ) )
}
