package mx.lux.pos.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.text.DateFormat
import java.text.SimpleDateFormat

class CustomDateUtils {

  private static Logger log = LoggerFactory.getLogger( CustomDateUtils.class )
  private static def pattern = "yyyy-MM-dd"

  static Date parseDate( String date, String pattern ) {
    try {
      DateFormat df = new SimpleDateFormat( pattern )
      df.setLenient( false )
      return df.parse( date )
    } catch ( Exception e ) {
      log.error( "Error al parsear la fecha ${ date } con formato ${ pattern }: ${ e.getMessage() }" )
      return null
    }
  }

  static Date parseDate( String date ) {
    return parseDate( date, pattern )
  }

  static String format( Date date ) {
    DateFormat df = new SimpleDateFormat( pattern )
    df.setLenient( false )
    return df.format( date )
  }

  static String format( Date date, String pattern ) {
    DateFormat df = new SimpleDateFormat( pattern )
    df.setLenient( false )
    return df.format( date )
  }

  static Date getCurrentDate( ) {
    return Calendar.getInstance( Constantes.TIMEZONE, Constantes.LOCALE ).getTime()
  }
}
