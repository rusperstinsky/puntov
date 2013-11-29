package mx.lux.pos.ui.model.file

import mx.lux.pos.util.CustomDateUtils
import org.apache.commons.lang3.StringUtils

import java.util.regex.Matcher
import java.util.regex.Pattern
import org.apache.commons.lang3.time.DateUtils
import java.text.ParseException
import java.text.DateFormat
import java.text.SimpleDateFormat
import mx.lux.pos.ui.resources.ServiceManager
import mx.lux.pos.ui.controller.SettingsController

class FileNamePattern {

  private static final Character TAG_START_TAG = '{'
  private static final Character TAG_END_TAG = '}'
  private static final String TAG_ANY_CHAR = '*'
  private static final String TAG_DATE = 'FECHA'
  private static final String TAG_SITE = 'SUCURSAL'
  private static final String TAG_SEGMENT = 'REGION'
  private static final String TAG_SITE_2 = 'ID_SUCURSAL'
  private static final String TAG_SITE_3 = 'ID_SUC'

  private static final String REG_EXP_DATE_1 = '[0-9]{1,2}\\-[0-9]{1,2}\\-[0-9]{2,4}'
  private static final String REG_EXP_DATE_2 = '[0-9]{2,4}\\-[0-9]{1,2}\\-[0-9]{1,2}'
  private static final String REG_EXP_DATE_3 = '[0-9]{1,2}\\-[0-9]{1,2}\\-[0-9]{2}'
  private static final String REG_EXP_DATE_4 = '[0-9]{1,2}\\-[0-9]{1,2}\\-[0-9]{2,4}\\-[0-9]{4}'
  private static final String REG_EXP_DATE_5 = '[0-9]{2,4}\\-[0-9]{1,2}\\-[0-9]{1,2}\\-[0-9]{4}'
  private static final String REG_EXP_SITE = '[0-9]{1,5}'
  private static final String REG_ANY_CHAR = '[A-Za-z0-9]++'

  private static final String SPECIAL_CHARS = '<([{\\^-=$!|]})?*+.>'
  private static final Character ESCAPE_CHAR = '\\'

  private String filePattern
  private Pattern pattern_1, pattern_2, pattern_3, pattern_4, pattern_5
  private DateFormat df_1, df_2, df_3, df_4, df_5

  FileNamePattern( String pPattern ) {
    this.filePattern = pPattern
    df_1 = new SimpleDateFormat( 'dd-MM-yyyy' )
    df_2 = new SimpleDateFormat( 'yyyy-MM-dd' )
    df_3 = new SimpleDateFormat( 'dd-MM-yy' )
    df_4 = new SimpleDateFormat( 'dd-MM-yyyy-HHmm' )
    df_5 = new SimpleDateFormat( 'yyyy-MM-dd-HHmm' )
    this.compile()
  }

  protected String compile( ) {
    StringBuffer regExp_1 = new StringBuffer()
    StringBuffer regExp_2 = new StringBuffer()
    StringBuffer regExp_3 = new StringBuffer()
    StringBuffer regExp_4 = new StringBuffer()
    StringBuffer regExp_5 = new StringBuffer()

    StringBuffer currentTag = null;
    for ( Character c : this.filePattern.toCharArray() ) {
      if ( currentTag != null ) {
        if ( TAG_END_TAG.equals( c ) ) {
          String tag = StringUtils.trimToEmpty( currentTag.toString() ).toUpperCase()
          if ( TAG_SITE.equals( tag ) || TAG_SITE_2.equals( tag ) || TAG_SITE_3.equals( tag ) ) {
            regExp_1.append( REG_EXP_SITE )
            regExp_2.append( REG_EXP_SITE )
            regExp_3.append( REG_EXP_SITE )
            regExp_4.append( REG_EXP_SITE )
            regExp_5.append( REG_EXP_SITE )
          } else if ( TAG_SEGMENT.equals( tag ) ) {
            String segment = SettingsController.instance.getSiteSegment()
            regExp_1.append( segment.toLowerCase() )
            regExp_2.append( segment.toLowerCase() )
            regExp_3.append( segment.toLowerCase() )
            regExp_4.append( segment.toLowerCase() )
            regExp_5.append( segment.toLowerCase() )
          } else if ( TAG_DATE.equals( tag ) ) {
            regExp_1.append( REG_EXP_DATE_1 )
            regExp_2.append( REG_EXP_DATE_2 )
            regExp_3.append( REG_EXP_DATE_3 )
            regExp_4.append( REG_EXP_DATE_4 )
            regExp_5.append( REG_EXP_DATE_5 )
          } else if ( TAG_ANY_CHAR.equals( tag ) ) {
            regExp_1.append( REG_ANY_CHAR )
            regExp_2.append( REG_ANY_CHAR )
            regExp_3.append( REG_ANY_CHAR )
            regExp_4.append( REG_ANY_CHAR )
            regExp_5.append( REG_ANY_CHAR )
          }
          currentTag = null
        } else {
          currentTag.append( c )
        }
      } else {
        if ( TAG_START_TAG.equals( c ) ) {
          currentTag = new StringBuffer()
        } else if ( TAG_ANY_CHAR.contains( c.toString() ) ) {
          regExp_1.append( REG_ANY_CHAR )
          regExp_2.append( REG_ANY_CHAR )
          regExp_3.append( REG_ANY_CHAR )
          regExp_4.append( REG_ANY_CHAR )
          regExp_5.append( REG_ANY_CHAR )
        } else if ( SPECIAL_CHARS.contains( c.toString() ) ) {
          regExp_1.append( ESCAPE_CHAR )
          regExp_1.append( c )
          regExp_2.append( ESCAPE_CHAR )
          regExp_2.append( c )
          regExp_3.append( ESCAPE_CHAR )
          regExp_3.append( c )
          regExp_4.append( ESCAPE_CHAR )
          regExp_4.append( c )
          regExp_5.append( ESCAPE_CHAR )
          regExp_5.append( c )
        } else {
          regExp_1.append( c.toLowerCase() )
          regExp_2.append( c.toLowerCase() )
          regExp_3.append( c.toLowerCase() )
          regExp_4.append( c.toLowerCase() )
          regExp_5.append( c.toLowerCase() )
        }
      }

    }

    this.pattern_1 = Pattern.compile( regExp_1.toString() )
    this.pattern_2 = Pattern.compile( regExp_2.toString() )
    this.pattern_3 = Pattern.compile( regExp_3.toString() )
    this.pattern_4 = Pattern.compile( regExp_4.toString() )
    this.pattern_5 = Pattern.compile( regExp_5.toString() )
  }

  private Boolean matches( String pFilename ) {
    String lcFilename = StringUtils.trimToEmpty( pFilename ).toLowerCase()
    return ( lcFilename.matches( this.pattern_1 ) || lcFilename.matches( this.pattern_2 )
        || lcFilename.matches( this.pattern_3 ) || lcFilename.matches( this.pattern_4 )
        || lcFilename.matches( this.pattern_5 ) )
  }

  FileFiltered matches( File pFile ) {
    FileFiltered f = null
    if ( this.matches( pFile.getName() ) ) {
      String tagDate = TAG_START_TAG.toString() + TAG_DATE + TAG_END_TAG.toString()
      if ( this.filePattern.contains( tagDate ) ) {
        Date date = null

        Pattern datePattern = Pattern.compile( REG_EXP_DATE_4 )
        Matcher matcher = datePattern.matcher( pFile.getName() )
        while ( ( date == null ) && ( matcher.find() ) ) {
          try {
            date = df_4.parse( matcher.group() )
            int yr = date.toCalendar().get( Calendar.YEAR )
            if ( yr < 2000 ) {
              date = null
            }
          } catch ( ParseException e ) {

          }
        }

        if ( date == null ) {
          datePattern = Pattern.compile( REG_EXP_DATE_5 )
          matcher = datePattern.matcher( pFile.getName() )
          while ( ( date == null ) && ( matcher.find() ) ) {
            try {
              date = df_5.parse( matcher.group() )
              int yr = date.toCalendar().get( Calendar.YEAR )
              if ( yr < 2000 ) {
                date = null
              }
            } catch ( ParseException e ) {

            }
          }
        }

        if ( date == null ) {
          datePattern = Pattern.compile( REG_EXP_DATE_1 )
          matcher = datePattern.matcher( pFile.getName() )
          while ( ( date == null ) && ( matcher.find() ) ) {
            try {
              date = df_1.parse( matcher.group() )
              int yr = date.toCalendar().get( Calendar.YEAR )
              if ( yr < 2000 ) {
                date = null
              }
            } catch ( ParseException e ) {

            }
          }
        }
        if ( date == null ) {
          datePattern = Pattern.compile( REG_EXP_DATE_2 )
          matcher = datePattern.matcher( pFile.getName() )
          while ( ( date == null ) && ( matcher.find() ) ) {
            try {
              date = df_2.parse( matcher.group() )
              int yr = date.toCalendar().get( Calendar.YEAR )
              if ( yr < 2000 ) {
                date = null
              }
            } catch ( ParseException e ) {

            }
          }
        }
        if ( date == null ) {
          datePattern = Pattern.compile( REG_EXP_DATE_3 )
          matcher = datePattern.matcher( pFile.getName() )
          while ( ( date == null ) && ( matcher.find() ) ) {
            try {
              date = df_3.parse( matcher.group() )
              int yr = date.toCalendar().get( Calendar.YEAR )
              if ( yr < 2000 ) {
                DateUtils.addYears( date, 2000 )
              }
            } catch ( ParseException e ) {

            }
          }
        }
        if ( date != null ) {
          f = new DateFileFiltered( pFile, date )
        }
      }
      if ( f == null ) {
        f = new SimpleFileFiltered( pFile )
      }
    }
    return f
  }

  static void main( args ) {
    List<String> files = new ArrayList<String>()
    /*files.add( 'PROD_01-01-2013_01.TXT' )
    files.add( 'PROD.01-01-2013.01.TXT' )
    files.add( 'PRODUCTO_01.TXT' )
    files.add( 'PROD0117_01.TXT' )
    files.add( 'EMP.01-02-2013.TXT' )
    files.add( 'EMP_01-02-2013.TXT' )
    files.add( 'EMP.2013-02-01.TXT' )
    files.add( 'TC.USD.05-02-2013.TXT' )
    files.add( 'TC.usd.2013-02-25.TXT' )
    files.add( 'TC.eur.05-02-2013.TXT' )
    files.add( 'TC.2013-02-25.TXT' )
    files.add( 'TC.08-02-2013.TXT' )
    files.add( 'TC.2013-02-25.TXT' )
    files.add( 'TC.06-01-2013.TXT' )
    files.add( 'TC.2013-02-25.TXT' )*/
    files.add( 'TC.EUR.2013-02-25-1716.TXT' )
    files.add( 'TC.USD.25-02-2013-1718.TXT' )

    String fPattern = 'EMP.{FECHA}.TXT'
    FileNamePattern pattern = new FileNamePattern( fPattern )
    for ( String filename : files ) {
      System.out.println( String.format( '%s.matches(%s): %b', fPattern, filename, pattern.matches( filename ) ) )
    }
    System.out.println()

    fPattern = 'TC.*.{FECHA}.TXT'
    pattern = new FileNamePattern( fPattern )
    for ( String filename : files ) {
      System.out.println( String.format( '%s.matches(%s): %b', fPattern, filename, pattern.matches( filename ) ) )
    }
    System.out.println()
  }

}
