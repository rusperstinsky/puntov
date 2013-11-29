package mx.lux.pos.util

import org.apache.commons.lang3.StringUtils

class FileFilterUtil {

  private static final Character TAG_START_TAG = '{'
  private static final Character TAG_END_TAG = '}'
  private static final String TAG_SITE = 'ID_SUC'
  private static final String TAG_SITE_2 = 'SUCURSAL'
  private static final String TAG_SITE_3 = 'SUC_NUM'
  private static final String TAG_NUMBER = 'NUMERO'
  private static final String TAG_YEAR = 'AÑO'
  private static final String TAG_MONTH = 'MES'
  private static final String TAG_DAY = 'DIA'

  private static final String REG_EXP_SITE = '[0-9]{1,5}'
  private static final String REG_EXP_NUMBER = '[0-9]++'
  private static final String REG_EXP_YEAR = '[0-9]{4}'
  private static final String REG_EXP_MONTH = '[0-9]{2}'
  private static final String REG_EXP_DAY = '[0-9]{2}'

  private static final String SPECIAL_CHARS = '<([{\\^-=$!|]})?*+.>'
  private static final Character ESCAPE_CHAR = '\\'

  static String toRegExpLowerCase( String pFilePattern ) {
    StringBuffer regExp = new StringBuffer()

    StringBuffer currentTag;
    for (Character c : pFilePattern.toCharArray()) {
      if ( currentTag != null ) {
        if ( TAG_END_TAG.equals(c) ) {
          String tag = StringUtils.trimToEmpty(currentTag.toString()).toUpperCase()
          if (TAG_SITE.equals(tag) || TAG_SITE_2.equals(tag) || TAG_SITE_3.equals(tag)) {
            regExp.append(REG_EXP_SITE)
          } else if (TAG_NUMBER.equals(tag)) {
            regExp.append(REG_EXP_NUMBER)
          } else if (TAG_YEAR.equals(tag)) {
            regExp.append(REG_EXP_YEAR)
          } else if (TAG_MONTH.equals(tag)) {
            regExp.append(REG_EXP_MONTH)
          } else if (TAG_DAY.equals(tag)) {
            regExp.append(REG_EXP_DAY)
          }
          currentTag = null
        } else {
          currentTag.append( c )
        }
      } else {
        if ( TAG_START_TAG.equals(c) ) {
          currentTag = new StringBuffer()
        } else if ( SPECIAL_CHARS.contains( c.toString() ) ) {
          regExp.append( ESCAPE_CHAR )
          regExp.append( c )
        } else {
          regExp.append(c.toLowerCase())
        }
      }
    }

    return StringUtils.trimToEmpty( regExp.toString() )
  }
}
