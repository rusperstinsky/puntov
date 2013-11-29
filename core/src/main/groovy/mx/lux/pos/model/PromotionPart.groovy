package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils
import mx.lux.pos.service.business.Registry

class PromotionPart {

  private static final String ALL_ITEMS = "*"
  private static final String LIST_DELIMITER = ","
  private static final String GROUP_TAG = "G:"

  String genre
  String type
  String subtype
  List<String> brandList
  List<String> partList

  Integer group
  private Boolean requiresGroup

  PromotionPriceType priceType
  Double discountAmount
  Double discountPercent

  protected PromotionPart( ) {
    brandList = new ArrayList<String>()
    partList = new ArrayList<String>()
    this.setGenre( "" )
    this.setType( "" )
    this.setSubtype( "" )
    this.setBrandList( "" )
    this.setPartList( "" )
    priceType = PromotionPriceType.Undefined
    discountAmount = 0
    discountPercent = 0

  }

  static PromotionPart createPromotionPart( Promocion pPromocion, Boolean pUseCombo ) {
    PromotionPart part = new PromotionPart()
    part.setGenre( pUseCombo ? pPromocion.idGenericoC : pPromocion.idGenerico )
    part.setType( pUseCombo ? pPromocion.tipoC : pPromocion.tipo )
    part.setSubtype( pUseCombo ? pPromocion.subtipoC : pPromocion.subtipo )
    part.setBrandList( pUseCombo ? pPromocion.marcaC : pPromocion.marca )
    part.setPartList( pUseCombo ? pPromocion.articuloC : pPromocion.articulo )
    part.setPriceType( pUseCombo ? pPromocion.tipoPrecioC : pPromocion.tipoPrecio )
    part.setDiscountAmount( pUseCombo ? pPromocion.precioDescontadoC : pPromocion.precioDescontado )
    part.setDiscountPercent( pUseCombo ? pPromocion.descuentoC : pPromocion.descuento )
    return part
  }

  // Internal Methods
  protected void setGenre( String pGenre ) {
    genre = StringUtils.trimToEmpty( pGenre )
    if ( genre.length() == 0 ) {
      genre = ALL_ITEMS
    }
  }

  protected void setType( String pType ) {
    type = StringUtils.trimToEmpty( pType )
    if ( type.length() == 0 ) {
      type = ALL_ITEMS
    }
  }

  protected void setSubtype( String pSubtype ) {
    subtype = StringUtils.trimToEmpty( pSubtype )
    if ( subtype.length() == 0 ) {
      subtype = ALL_ITEMS
    }
  }

  protected void setBrandList( String pBrandList ) {
    brandList.clear()
    String brand = StringUtils.trimToEmpty( pBrandList ).toUpperCase()
    if ( brand.length() == 0 ) {
      brand = ALL_ITEMS
    }
    if ( brand.contains( LIST_DELIMITER ) ) {
      for ( String b in brand.split( LIST_DELIMITER ) ) {
        if ( !StringUtils.isEmpty( b ) ) {
          brandList.add( StringUtils.trimToEmpty( b ) )
        }
      }
    } else {
      brandList.add( brand )
    }
  }

  protected void setPartList( String pPartList ) {
    partList.clear()
    group = 0
    requiresGroup = false
    String part = StringUtils.trimToEmpty( pPartList ).toUpperCase()
    if ( part.length() == 0 ) {
      part = ALL_ITEMS
    }
    if ( part.startsWith( GROUP_TAG ) ) {
      String strGroup = StringUtils.trimToEmpty( part.substring( GROUP_TAG.length() ) )
      if ( StringUtils.isNumeric( strGroup ) ) {
        group = Integer.valueOf( strGroup )
        requiresGroup = true
      }
    } else if ( part.contains( LIST_DELIMITER ) ) {
      for ( String b in part.split( LIST_DELIMITER ) ) {
        if ( !StringUtils.isEmpty( b ) ) {
          partList.add( StringUtils.trimToEmpty( b ) )
        }
      }
    } else {
      partList.add( part )
    }
  }

  protected void setPriceType( String pPriceType ) {
    priceType = PromotionPriceType.parse( StringUtils.trimToEmpty( pPriceType ).toUpperCase() )
  }

  protected void setDiscountAmount( BigDecimal pDiscountAmount ) {
    discountAmount = pDiscountAmount.doubleValue()
  }

  protected void setDiscountPercent( BigDecimal pDiscountPercent ) {
    discountPercent = pDiscountPercent.doubleValue()
  }

  // Public Methods
  void addPart( String pPartNbr ) {
    String partNbr = StringUtils.trimToEmpty( pPartNbr )
    if ( ( partNbr.length() > 0 ) && ( !partList.contains( partNbr ) ) ) {
      partList.add( partNbr )
    }
  }

  Boolean allGenre( ) {
    return genre.equals( ALL_ITEMS )
  }

  Boolean listGenres( Articulo pPart ) {
    Boolean valid = false
    String[] listGenres = genre.split( ',' )
    if( listGenres.length > 1 ){
      for(int i=0;i<listGenres.length;i++){
        if(pPart.idGenerico.equalsIgnoreCase(listGenres[i].trim())){
          valid = true
        }
      }
    }
    return valid
  }

  Boolean allType( ) {
    return type.equals( ALL_ITEMS )
  }

  Boolean allSubtype( ) {
    return subtype.equals( ALL_ITEMS )
  }

  Boolean allBrand( ) {
    return ( ( getBrandList().size == 0 ) || ( getBrandList().get( 0 ).equalsIgnoreCase( ALL_ITEMS ) ) )
  }

  Boolean allPart( ) {
    return ( ( getPartList().size == 0 ) || ( getPartList().get( 0 ).equalsIgnoreCase( ALL_ITEMS ) ) )
  }

  Boolean appliesToPart( Articulo pPart ) {
    Boolean applies = allGenre() || genre.equalsIgnoreCase( pPart.idGenerico )  || listGenres( pPart )
    if ( applies )
      applies = allPart() || partList.contains( pPart.articulo.toUpperCase() )
    if ( applies )
      applies = allBrand() || brandList.contains( pPart.marca.toUpperCase() )
    if ( applies )
      applies = allType() || type.equalsIgnoreCase( pPart.tipo )
    if ( applies )
      applies = allSubtype() || subtype.equalsIgnoreCase( pPart.subtipo )
    if ( applies )
      applies = !( Registry.getManualPriceTypeList().contains( pPart.idGenerico ) )
    return applies
  }

  Boolean isGroupRequired( ) {
    return requiresGroup
  }

  // Entity
  String toString( ) {
    String str = String.format( "Part:%s  Brand:%s  Genre: %s  Type:%s.%s", partList.toString(), brandList.toString(),
        genre, type, subtype )
    return str
  }

}
