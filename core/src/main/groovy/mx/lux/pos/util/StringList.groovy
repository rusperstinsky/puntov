package mx.lux.pos.util

import org.apache.commons.lang.math.NumberUtils
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.StrTokenizer
import org.apache.commons.lang3.time.DateUtils

import java.text.ParseException
import java.text.NumberFormat

class StringList {

  private static final String FORMAT_DEFAULT_BOOLEAN = "true/false"
  private static final String FORMAT_DEFAULT_DATE = "yyyy/MM/dd"
  private static final String FORMAT_DEFAULT_DATE_TIME = "yyyy/MM/dd HH:mm:ss"
  private static final String FORMAT_DEFAULT_DOUBLE = "%.3f"
  private static final String FORMAT_DEFAULT_INTEGER = "%d"
  private static final String DEFAULT_DELIMITER = "!"
  private static final String MSG_UNABLE_TO_PARSE = "Unable to parse <%s> as %s"
  private static final Character QUOTE = '"'

  private static final NumberFormat NF = NumberFormat.getInstance( )

  private List<String> entryList = new ArrayList<String>()

  // Constructor
  StringList( ) {
    this.reset()
  }

  StringList( String pList ) {
    this.reset( pList )
  }

  StringList( String pList, String pDelimiter ) {
    this.reset( pList, pDelimiter )
  }

  // Internal methods
  String extractFalseFormat( String pBooleanFormat ) {
    String falseFormat = ""
    Integer slash = pBooleanFormat.indexOf( '/' )
    if ( ( slash >= 0 ) && ( slash < pBooleanFormat.length() ) ) {
      falseFormat = StringUtils.trimToEmpty( pBooleanFormat.substring( slash + 1 ) )
    }
    return falseFormat
  }

  String extractTrueFormat( String pBooleanFormat ) {
    String trueFormat = StringUtils.trimToEmpty( pBooleanFormat )
    Integer slash = pBooleanFormat.indexOf( '/' )
    if ( slash >= 0 ) {
      trueFormat = StringUtils.trimToEmpty( pBooleanFormat.substring( 0, slash ) )
    }
    return trueFormat
  }

  // Public methods
  void add( Object pObject ) {
    this.entryList.add( StringUtils.trimToEmpty( pObject.toString() ) )
  }

  void addBoolean( Boolean pBoolean ) {
    this.addBoolean( pBoolean, FORMAT_DEFAULT_BOOLEAN )
  }

  void addBoolean( Boolean pBoolean, String pFormat ) {
    String trueFormat = StringUtils.trimToEmpty( pFormat )
    String falseFormat = ""
    Integer slash = pFormat.indexOf( '/' )
    if ( slash >= 0 ) {
      trueFormat = StringUtils.trimToEmpty( pFormat.substring( 0, slash ) )
      if ( slash < pFormat.length() ) {
        falseFormat = StringUtils.trimToEmpty( pFormat.substring( slash + 1 ) )
      }
    }
    this.entryList.add( BooleanUtils.isTrue( pBoolean ) ? trueFormat : falseFormat )
  }

  void addDate( Date pDate ) {
    this.addDate( pDate, FORMAT_DEFAULT_DATE )
  }

  void addDate( Date pDate, String pFormat ) {
    this.entryList.add( CustomDateUtils.format( DateUtils.truncate( pDate, Calendar.DATE ), pFormat ) )
  }

  void addDateTime( Date pDate ) {
    this.addDateTime( pDate, FORMAT_DEFAULT_DATE_TIME )
  }

  void addDateTime( Date pDate, String pFormat ) {
    this.entryList.add( CustomDateUtils.format( pDate, pFormat ) )
  }

  void addDouble( Double pDouble ) {
    this.addDouble( pDouble, FORMAT_DEFAULT_DOUBLE )
  }

  void addDouble( Double pDouble, String pFormat ) {
    this.entryList.add( String.format( pFormat, pDouble ) )
  }

  void addFloat( Float pFloat ) {
    this.addDouble( pFloat as Double )
  }

  void addFloat( Float pFloat, String pFormat ) {
    this.addDouble( ( pFloat as Double ), pFormat )
  }

  void addInteger( Integer pInteger ) {
    this.addLong( pInteger )
  }

  void addInteger( Integer pInteger, String pFormat ) {
    this.addLong( pInteger, pFormat )
  }

  void addLong( Long pLong ) {
    this.addLong( pLong, FORMAT_DEFAULT_INTEGER )
  }

  void addLong( Long pLong, String pFormat ) {
    this.entryList.add( String.format( pFormat, pLong ) )
  }

  Date asDate( Integer ix ) throws ParseException {
    return this.asDate( ix, FORMAT_DEFAULT_DATE )
  }

  Date asDate( Integer ix, String pFormat ) throws ParseException {
    Date date = null
    try {
      date = DateUtils.truncate( DateUtils.parseDate( this.entry( ix ), pFormat ), Calendar.DATE )
    } catch ( Exception e ) {
      throw new ParseException( String.format( MSG_UNABLE_TO_PARSE, this.entry( ix ), "Date" ), 0 )
    }
    return date
  }

  Date asDateTime( Integer ix ) throws ParseException {
    return this.asDateTime( ix, FORMAT_DEFAULT_DATE_TIME )
  }

  Date asDateTime( Integer ix, String pFormat ) throws ParseException {
    Date date = null
    try {
      date = DateUtils.parseDate( this.entry( ix ), pFormat )
    } catch ( Exception e ) {
      throw new ParseException( String.format( MSG_UNABLE_TO_PARSE, this.entry( ix ), "Datetime" ), 0 )
    }
    return date
  }

  Double asDouble( Integer ix ) throws ParseException {
    Double value = null
    try {
      value = Double.parseDouble( this.entry( ix ) )
    } catch ( Exception e ) { }
    if (value == null) {
      try {
        NF.setParseIntegerOnly( false )
        value = NF.parse( this.entry( ix ) )
      } catch ( Exception e ) {
        throw new ParseException( String.format( MSG_UNABLE_TO_PARSE, this.entry( ix ), "Integer" ), 0 )
      }
    }
    return value
  }

  Float asFloat( Integer ix ) throws ParseException {
    Float value = null
    try {
      value = Float.parseFloat( this.entry( ix ) )
    } catch ( Exception e ) { }
    if (value == null) {
      try {
        NF.setParseIntegerOnly( false )
        value = NF.parse( this.entry( ix ) )
      } catch ( Exception e ) {
        throw new ParseException( String.format( MSG_UNABLE_TO_PARSE, this.entry( ix ), "Integer" ), 0 )
      }
    }
    return value
  }

  Integer asInteger( Integer ix ) throws ParseException {
    Integer value = null
    try {
      value = Integer.parseInt( this.entry( ix ) )
    } catch ( Exception e ) { }
    if (value == null) {
      try {
        NF.setParseIntegerOnly( true )
        value = NF.parse( this.entry( ix ) )
      } catch ( Exception e ) {
        throw new ParseException( String.format( MSG_UNABLE_TO_PARSE, this.entry( ix ), "Integer" ), 0 )
      }
    }
    return value
  }

  Long asLong( Integer ix ) throws ParseException {
    Long value = null
    try {
      value = Long.parseLong( this.entry( ix ) )
    } catch ( Exception e ) { }
    if (value == null) {
      try {
        NF.setParseIntegerOnly( true )
        value = NF.parse( this.entry( ix ) )
      } catch ( Exception e ) {
        throw new ParseException( String.format( MSG_UNABLE_TO_PARSE, this.entry( ix ), "Integer" ), 0 )
      }
    }
    return value
  }

  String entry( Integer ix ) {
    String entry = ""
    if ( ( ix >= 0 ) && ( ix < this.size ) ) {
      entry = entryList.get( ix )
    }
    return StringUtils.trimToEmpty( entry )
  }

  Integer getSize( ) {
    return entryList.size()
  }

  Boolean isFalse( Integer ix, String pFalseValue ) {
    return !this.isEquals( ix, pFalseValue )
  }

  Boolean isFalseAny( Integer ix, String pFalseList ) {
    return !this.isEqualsAny( ix, pFalseList )
  }

  Boolean isTrue( Integer ix ) {
    return BooleanUtils.toBoolean( this.entry( ix ) )
  }

  Boolean isEquals( Integer ix, String pValue ) {
    return StringUtils.trimToEmpty( pValue ).equals( this.entry( ix ) )
  }

  Boolean isEqualsAny( Integer ix, String pList ) {
    Boolean result = false
    for ( String entry : pList.split( "\\" + DEFAULT_DELIMITER ) ) {
      result = result || this.isEquals( ix, entry )
    }
    return result
  }

  void reset( ) {
    this.entryList.clear()
  }

  void reset( String pList ) {
    this.reset( pList, DEFAULT_DELIMITER )
  }

  void reset( String pList, String pDelimiter ) {
    this.reset()
    StrTokenizer tokenizer = new StrTokenizer()
    tokenizer.setDelimiterString( pDelimiter?.trim() )
    tokenizer.setQuoteChar( QUOTE )
    tokenizer.setIgnoreEmptyTokens( false )
    tokenizer.reset( pList )
    for ( String token : tokenizer.getTokenList() ) {
      this.add( token )
    }
  }

  String toString( ) {
    return this.toString( DEFAULT_DELIMITER )
  }

  String toString( String pDelimiter ) {
    StringBuffer sb = new StringBuffer()
    for ( String entry : entryList ) {
      if ( sb.length() > 0 ) {
        sb.append( pDelimiter )
      }
      sb.append( entry.length() > 0 ? entry : " " )
    }
    return sb.toString()
  }

}
