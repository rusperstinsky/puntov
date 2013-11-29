package mx.lux.pos.service.io

import mx.lux.pos.util.StringList
import org.apache.commons.lang3.StringUtils
import java.text.ParseException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import mx.lux.pos.model.Articulo
import mx.lux.pos.model.ArticuloSombra

class ArticuloClassAdapter implements Comparable<ArticuloClassAdapter> {

  private static final String MSG_UNABLE_TO_PARSE_SKU = 'Error! No se puede interpretar <%s> como sku.'

  private static enum Field {
    PartNbr, Desc, Fam, Type, SubType, Brand, Sku, Genre
  }
  private static final String DELIMITER = ','

  private static String delimiter
  private StringList record
  private Logger logger = LoggerFactory.getLogger( this.getClass() )

  ArticuloClassAdapter( String pRecord ) {
    this.record = new StringList( pRecord, getDelimiter() )
  }

  // Private Methods
  protected static String getDelimiter( ) {
    String del = StringUtils.trimToNull( delimiter )
    if ( del == null ) {
      del = DELIMITER
    }
    return del
  }

  protected Boolean isSkuValid( ) {
    return this.sku > 0
  }

  protected Boolean isDescValid( ) {
    return StringUtils.trimToNull( this.desc ) != null
  }

  protected Boolean isIdGenericoValid( ) {
    return StringUtils.trimToNull( this.family ) != null
  }

  protected Boolean isTipoValid( ) {
    return StringUtils.trimToNull( this.type ) != null
  }

  protected Boolean isIdGenTipoValid( Articulo pPart ) {
    return StringUtils.trimToNull( pPart?.tipo ) != null
  }

  protected Boolean isSubtipoValid( ) {
    return StringUtils.trimToNull( this.subtype ) != null
  }

  protected Boolean isMarcaValid( ) {
    return StringUtils.trimToNull( this.brand ) != null
  }

  protected Boolean isIdGenSubtipoValid( Articulo pPart ) {
    return StringUtils.trimToNull( pPart?.marca ) != null
  }

  // Public methods
  String getPartNbr( ) {
    return StringUtils.trimToEmpty( this.record.entry( Field.PartNbr.ordinal() ) ).toUpperCase()
  }

  String getDesc( ) {
    return StringUtils.trimToEmpty( this.record.entry( Field.Desc.ordinal() ) )
  }

  String getFamily( ) {
    return StringUtils.trimToEmpty( this.record.entry( Field.Fam.ordinal() ) ).toUpperCase()
  }

  String getType( ) {
    return StringUtils.trimToEmpty( this.record.entry( Field.Type.ordinal() ) ).toUpperCase()
  }

  String getSubtype( ) {
    return StringUtils.trimToEmpty( this.record.entry( Field.SubType.ordinal() ) ).toUpperCase()
  }

  String getBrand( ) {
    return StringUtils.trimToEmpty( this.record.entry( Field.Brand.ordinal() ) ).toUpperCase()
  }

  Integer getSku( ) {
    Integer sku = 0
    try {
      sku = this.record.asInteger( Field.Sku.ordinal() )
    } catch ( ParseException e ) {
      this.logger.debug( String.format( MSG_UNABLE_TO_PARSE_SKU,
          this.record.entry( Field.Sku.ordinal( ) ) ) )
    }
    return sku
  }

  Boolean isValid( ) {
    return ( ( this.record.size > Field.Sku.ordinal() ) &&  this.isSkuValid() )
  }

  String getGenre( ) {
    return StringUtils.trimToEmpty( this.record.entry( Field.Genre.ordinal() ) ).toUpperCase()
  }

  static void setDelimiter( String pDelimiter ) {
    delimiter = StringUtils.trimToNull( pDelimiter )
  }

  void updatePart( Articulo pPart ) {
    if ( this.isDescValid( ) ) {
      pPart.setDescripcion( this.desc )
    }
    if ( this.isIdGenericoValid( ) ){
      pPart.idGenerico = this.family
    }
    if( this.isTipoValid( ) ){
      pPart.tipo = StringUtils.trimToEmpty( this.type )
    }
    if( this.isIdGenTipoValid( pPart ) ) {
      pPart.idGenTipo = ( pPart.tipo.length( ) >  2 ? pPart.tipo.substring(0, 2) : pPart.tipo )
    }
    if( this.isSubtipoValid() ){
      pPart.subtipo = this.subtype
    }
    if( this.isMarcaValid( ) ){
      pPart.marca = StringUtils.trimToEmpty( this.brand )
    }
    if( this.isIdGenSubtipoValid( ) ){
      pPart.idGenSubtipo = ( pPart.marca.length( ) >  2 ? pPart.marca.substring(0, 2) : pPart.marca )
    }
  }

  // Identity Methods
  int compareTo( ArticuloClassAdapter adapter ) {
    return this.sku.compareTo( adapter.sku )
  }

  boolean equals( Object pObject ) {
    boolean result = false
    if ( pObject instanceof ArticuloClassAdapter ) {
      result = this.sku.equals( (pObject as ArticuloClassAdapter ).sku)
    } else if ( pObject instanceof Integer ) {
      result = this.sku.equals( pObject as Integer )
    }
    return result
  }

  int hashCode( ) {
    return this.sku.hashCode()
  }

  String toString( ) {
    return String.format( '[%d] %s - %s (%s)', this.sku, this.partNbr, this.desc, this.family )
  }

}
