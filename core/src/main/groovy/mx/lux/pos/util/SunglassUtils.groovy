package mx.lux.pos.util

import com.ibm.icu.text.NumberFormat
import org.apache.commons.lang3.StringUtils

import java.text.ParseException

class SunglassUtils {

  private static final NumberFormat NF = NumberFormat.getInstance()
  private static final String MSG_UNABLE_TO_PARSE = 'Unable to parse <%s> as %s.'

  static final String PREFIX_SITE = "ALM"

  static String formatSite( Integer pSite ) {
    String site = "NINGUNA"
    if ( pSite != null ) {
      site = String.format( "%s%02d", PREFIX_SITE, pSite )
    }
    return site
  }

  static Integer parseSite( String pSunglassSite ) {
    Integer site = null
    String sunglassSite = StringUtils.trimToEmpty( pSunglassSite )
    if ( sunglassSite.startsWith( PREFIX_SITE ) ) {
      sunglassSite = StringUtils.trimToEmpty( sunglassSite.substring( PREFIX_SITE.length() ) )
    }
    try {
      site = Integer.parseInt( sunglassSite )
    } catch ( Exception ignore ) { }
    if ( site == null ) {
      try {
        NF.setParseIntegerOnly( true )
        site = NF.parse( sunglassSite )
      } catch ( Exception ignore ) {
        throw new ParseException( String.format( MSG_UNABLE_TO_PARSE, sunglassSite, "Integer" ), 0 )
      }
    }
    return site
  }

}
