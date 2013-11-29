package mx.lux.pos.model

import mx.lux.pos.util.StringList
import mx.lux.pos.service.impl.ServiceFactory
import org.apache.commons.lang3.math.NumberUtils
import java.text.ParseException
import mx.lux.pos.service.business.Registry

class ArticuloSunglassAdapter implements ArticuloSunglass {

  private static final String ID_GENRE_DEFAULT = "E"
  private static final String ID_GENRE_CONTACTS = "C"
  private static final String ID_GENRE_GLASSES = "A"

  private static final String TAG_GENRE_CONTACTS = "LENTES DE CONTACTO"
  private static final String TAG_GENRE_SUNGLASSES = "LENTES SOLARES"

  private static final String TYPE_DEFAULT = ""
  private static final String TYPE_SUNGLASSES = "E"

  private StringList data
  private static Double vat

  ArticuloSunglassAdapter(StringList pStringList) {
    this.data = pStringList
  }

  // Internal Methods
  protected Double addVat( Double pPrice ) {
      if ( vat == null ) {
          vat = 1.0 + ( Registry.currentVAT / 100.0 )
      }
      Double withVat = pPrice * vat
      return NumberUtils.createDouble( String.format( "%.2f", withVat ) )
  }

  protected Integer asInteger( ArticuloSunglassDescriptor pField ) {
    Integer value  = null
    if ((pField.indexValid) && (this.data.getSize() > ArticuloSunglassDescriptor.maxIndex) ){
      try {
        value = data.asInteger( pField.index )
      } catch (ParseException e) { }
    }
    return value
  }

  protected Date asDate( ArticuloSunglassDescriptor pField ) {
    Date value = null
    if ((pField.indexValid) && (this.data.getSize() > ArticuloSunglassDescriptor.maxIndex) ){
      try {
        value = data.asDate( pField.index, "yyyy-MM-dd" )
      } catch (ParseException e) { }
    }
    return value
  }

  protected Double asDouble( ArticuloSunglassDescriptor pField ) {
    Double value  = null
    if ((pField.indexValid) && (this.data.getSize() > ArticuloSunglassDescriptor.maxIndex) ){
      try {
        value = data.asDouble( pField.index )
      } catch (ParseException e) { }
    }
    return value
  }

  protected String asString( ArticuloSunglassDescriptor pField ) {
    String value  = null
    if ((pField.indexValid) && (this.data.getSize() > ArticuloSunglassDescriptor.maxIndex) ){
      value = data.entry( pField.index )
    }
    return value
  }

  protected Double getVat( ) {
  }

  protected Boolean isGroupContacts() {
    return TAG_GENRE_CONTACTS.equalsIgnoreCase( this.group )
  }

  protected Boolean isGroupSunglasses() {
    return TAG_GENRE_SUNGLASSES.equalsIgnoreCase( this.group )
  }

  // Public Methods
  StringList getData() {
    return this.data
  }

  Integer getSku() {
    return this.asInteger( ArticuloSunglassDescriptor.sku )
  }

  String getPartNbr() {
    return this.asString( ArticuloSunglassDescriptor.partNbr )
  }

  String getColorCode() {
    return this.asString( ArticuloSunglassDescriptor.colorCode )
  }

  String getDescription() {
    return this.asString( ArticuloSunglassDescriptor.description )
  }

  String getGenre() {
    String genre = ID_GENRE_DEFAULT
    if ( this.isGroupContacts( ) ) {
      genre = ID_GENRE_CONTACTS
    } else if ( this.isGroupSunglasses( ) ) {
      genre = ID_GENRE_GLASSES
    }
    return genre
  }

  String getGroup() {
    return this.asString( ArticuloSunglassDescriptor.group )
  }

  Double getPrice() {
    return this.addVat( this.asDouble( ArticuloSunglassDescriptor.price ) )
  }

  Double getRedPrice() {
    return this.asDouble( ArticuloSunglassDescriptor.redPrice )
  }

  String getColorDesc() {
    return this.asString( ArticuloSunglassDescriptor.colorDesc )
  }

  String getType() {
    String type = TYPE_DEFAULT
    if (this.isGroupSunglasses() ) {
      type = TYPE_SUNGLASSES
    }
    return type
  }

  String getSubtype() {
    return this.asString( ArticuloSunglassDescriptor.subtype )
  }

  String getBrand() {
    return this.asString( ArticuloSunglassDescriptor.brand )
  }

  String getSupplier() {
    return this.asString( ArticuloSunglassDescriptor.supplier )
  }

  Date getRevDate() {
    return this.asDate( ArticuloSunglassDescriptor.revDate )
  }

  String getRevUserid() {
    return this.asString( ArticuloSunglassDescriptor.revUser )
  }

  String toString() {
    StringBuffer sb = new StringBuffer()
    sb.append( String.format( "%06d: [%s] %s ", this.sku, this.partNbr, this.description ) )
    sb.append( String.format( "(%s)  Price:%,.2f", this.genre, this.price) )
    return sb.toString()
  }

}
