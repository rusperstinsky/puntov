package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

class InvAdjustLine {

  Integer sku
  String partCode
  String colorCode
  String colorDesc
  String brand
  String type
  String barcode
  Integer qty = 0
  
  ArticuloSombra getPartShadow() {
    ArticuloSombra shadow = new ArticuloSombra( sku )
    if (StringUtils.isNotBlank(this.barcode))
      shadow.setArticulo( partCode )
    if (StringUtils.isNotBlank(this.barcode))
      shadow.setCodigoColor( colorCode )
    if (StringUtils.isNotBlank(this.barcode))
      shadow.setDescripcionColor( colorDesc )
    if (StringUtils.isNotBlank(this.barcode))
      shadow.setTipo( type )
    if (StringUtils.isNotBlank(this.barcode))
      shadow.setMarca( brand )
    if (StringUtils.isNotBlank(this.barcode))
    shadow.setCB( barcode )
    return shadow
  }

  void setBarcode( String pBarcode ) {
    this.barcode = StringUtils.trimToEmpty( pBarcode ).toUpperCase( )
  } 
   
  void setBrand( String pBrand ) {
    this.brand = StringUtils.trimToEmpty( pBrand ).toUpperCase( )
  }
  
  void setColorCode( String pColorCode ) {
    this.colorCode = StringUtils.trimToEmpty( pColorCode ).toUpperCase( )
  } 
   
  void setColorDesc( String pColorDesc ) {
    this.colorDesc = StringUtils.trimToEmpty( pColorDesc )
  } 
   
  void setPartCode( String pPartCode ) {
    this.partCode = StringUtils.trimToEmpty( pPartCode ).toUpperCase( )
  } 
  
  void setType( String pType ) {
    this.type = StringUtils.trimToEmpty( pType ).toUpperCase( )
  }
  
  String toString() {
    String.format( "Sku(%d): %s [%s: %s] Marca:<%s> Tipo:<%s> CB:<%s> Cant:<%d>", sku, partCode, colorCode,
        colorDesc, brand, type, barcode, qty )
  }
  
}
