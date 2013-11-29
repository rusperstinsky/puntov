package mx.lux.pos.service.io

import mx.lux.pos.model.AdaptadorArticulo
import mx.lux.pos.model.ArticuloSunglassAdapter
import mx.lux.pos.model.ArticuloSunglassDescriptor

import mx.lux.pos.util.StringList
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils

import java.text.NumberFormat
import java.text.ParseException

class PartFileSunglass {

  private enum Section {
    Init, Global, Spec, Data
  }
  private static final String TAG_GLOBAL = "[TABLA]"
  private static final String TAG_SPEC = "[CAMPOS]"
  private static final String TAG_DATA = "[DATOS]"
  private static final String TAG_SKU = "IDCODIGO"
  private static final String TAG_PART_CODE = "NUMPARTE"
  private static final String TAG_PART_DESC = "DESCRIPCION"
  private static final String TAG_GENRE = "FAMILIA"
  private static final String TAG_PRICE = "PRECIOVENTA1"
  private static final String TAG_MODIFIED_DATE = "FECHAREVISION"
  private static final String TAG_MODIFIED_USER = "CLAVEUSUARIO"
  private static final String TAG_DESC_COLOR = "COLORARMAZON"
  private static final String TAG_BARCODE = "IDBARRAS"
  private static final String TAG_PART_TYPE = "TIPO"
  private static final String TAG_BRAND = "MARCA"
  private static final String TAG_SUPPLIER = "PROVEEDOR"



  private Section currentSection
  private File inFile
  private BufferedReader reader
  private Integer nLinesRead = 0
  private Integer nShadowsRead = 0
  private String currentLine
  private ArticuloSunglassAdapter currentShadow
  private String delimiter
  private Integer expectedRecords = 0
  private Integer currentFieldIndex

  PartFileSunglass( String pInputFile ) throws FileNotFoundException {
    this.init( pInputFile )
  }

  // Protected Methods
  protected void init( String pInputFile ) throws FileNotFoundException {
    this.currentSection = Section.Init;
    this.inFile = new File( pInputFile )
    this.reader = new BufferedReader( new FileReader( this.inFile ) )
  }

  protected void parseSpec( ) {

    String[] tokens = this.currentLine.split( "\\=" )
    if ( tokens.length >= 2 ) {
      for ( ArticuloSunglassDescriptor descriptor : ArticuloSunglassDescriptor.values() ) {
        if ( descriptor.equals( tokens[ 0 ] ) ) {
          descriptor.index = this.currentFieldIndex
          break
        }
      }
      this.currentFieldIndex++
    }
  }

  protected void parseGlobal( ) {
    final String TAG_SEPARATOR = "SeparadorCampos"
    final String TAG_RECORD_COUNT = "Registros"

    String[] tokens = this.currentLine.split( "\\=" )
    if ( tokens.length >= 2 ) {
      if ( TAG_SEPARATOR.equalsIgnoreCase( tokens[ 0 ] ) ) {
        this.setDelimiter( tokens[ 1 ] )
      } else if ( TAG_RECORD_COUNT.equalsIgnoreCase( tokens[ 0 ] ) ) {
        this.setExpectedRecords( tokens[ 1 ] )
      }
    }
  }

  protected boolean parseSection( ) {
    boolean parsed = false;
    this.currentLine = StringUtils.trimToEmpty( this.currentLine )
    if ( TAG_GLOBAL.equals( this.currentLine ) ) {
      this.currentSection = Section.Global
      parsed = true
    } else if ( TAG_SPEC.equals( this.currentLine ) ) {
      this.currentSection = Section.Spec
      this.currentFieldIndex = 0
      parsed = true
    } else if ( TAG_DATA.equals( this.currentLine ) ) {
      this.currentSection = Section.Data
      parsed = true
    }
    return parsed
  }

  protected void setDelimiter( String pDelimiter ) {
    this.delimiter = pDelimiter
  }

  protected void setExpectedRecords( String pRecordCount ) {
    this.expectedRecords = 0
    try {
      this.expectedRecords = NumberUtils.createInteger( pRecordCount )
    } catch ( ParseException e ) { }
  }

  // Public methods
  void close( ) {
    if ( this.reader != null ) {
      this.reader.close()
      this.reader = null
    }
  }

  String getCurrentLine( ) {
    return this.currentLine
  }

  ArticuloSunglassAdapter getCurrentShadow( ) {
    return this.currentShadow
  }

  int getLineCount( ) {
    return this.nLinesRead
  }

  int getPartCount( ) {
    return this.nShadowsRead
  }

  ArticuloSunglassAdapter read( ) {
    this.currentShadow = null
    this.currentLine = this.reader.readLine()
    while ( ( this.currentLine != null ) && ( this.currentShadow == null ) ) {
      this.nLinesRead++
      if ( !this.parseSection() ) {
        if ( Section.Data.equals( this.currentSection ) ) {
          this.currentShadow = new ArticuloSunglassAdapter( new StringList( this.currentLine, this.delimiter ) )
        } else if ( Section.Spec.equals( this.currentSection ) ) {
          this.parseSpec()
        } else if ( Section.Global.equals( this.currentSection ) ) {
          this.parseGlobal()
        }
      }
      if ( this.currentShadow == null ) {
        this.currentLine = this.reader.readLine()
      }
    }
    if ( this.currentShadow != null ) {
      this.nShadowsRead++
    }
    return this.currentShadow
  }


  String toString( ) {
    StringBuffer sb = new StringBuffer()
    sb.append( String.format( "[%s] Section:%s  LinesRead:%,d   PartsRead:%,d",
        this.inFile.absolutePath,
        this.currentSection.toString(),
        this.lineCount,
        this.partCount
    ) )
    sb.append( String.format( "\n    Delimiter:\"%s\"   ExpectedRecords:%,d", this.delimiter, this.expectedRecords ) )
    sb.append( String.format( "\n    Current Line:%s", this.currentLine ) )
    sb.append( String.format( "\n    Current Part:%s", this.currentShadow.toString() ) )
    return sb.toString()
  }


  AdaptadorArticulo readNew( String line ) {
     AdaptadorArticulo data = new AdaptadorArticulo()
      if( line.length() > 0 ){
      String[] dataTmp = line.split(/\|/)
      if(dataTmp[1].length() > 20){
        println "Articulo con mas de 20 caracteres ${dataTmp[0]}"
      }
        if( dataTmp.length >= 8 ){
          data.setSku(NumberFormat.getInstance().parse(dataTmp[0].toString()).intValue() )
          data.setArticulo( dataTmp[1] )
          data.setDescripcion( dataTmp[2] )
          try{
            data.setPrecio( NumberFormat.getInstance().parse(dataTmp[3].toString()).doubleValue() )
          } catch (ParseException e ){
            println "error de precio en articulo ${dataTmp[0]}"
            data.setArticulo( "error" )
          }
          data.setGenerico( dataTmp[4] )
          data.setTipo( dataTmp[5] )
          data.setSubtipo( dataTmp[6] )
          data.setMarca( dataTmp[7] )
        }
      }
      return data
  }


}
