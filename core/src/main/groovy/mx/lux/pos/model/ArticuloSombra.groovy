package mx.lux.pos.model

import mx.lux.pos.service.impl.ServiceFactory
import mx.lux.pos.service.io.ArticuloClassAdapter
import org.apache.commons.lang3.StringUtils

class ArticuloSombra {

  def Integer id_articulo = null
  def String articulo = null
  def String generico = null
  def String descripcion = null
  def String codigoColor = null
  def String descripcionColor = null
  def String tipo = null
  def String marca = null
  def String cb = null
  def Integer existencia = 0

  ArticuloSombra( Integer pSku ) {
    id_articulo = pSku
  }

  // Public Methods
  Articulo createArticulo( ) {
    Articulo part = null
    if ( isValidForNew() ) {
      part = new Articulo( id: id_articulo )
      part.idSucursal = ServiceFactory.sites.obtenSucursalActual().id
      updateArticulo( part )
    }
    return part
  }

  Boolean isValidForNew( ) {
    boolean valid = true
    valid &= ( id_articulo != null )
    valid &= ( articulo != null )
    valid &= ( generico != null )
    return valid
  }

  void setArticulo( String pValue ) {
    articulo = StringUtils.trimToNull( pValue )
  }

  void setGenerico( String pValue ) {
    generico = StringUtils.trimToNull( pValue )
  }

  void setDescripcion( String pValue ) {
    descripcion = StringUtils.trimToNull( pValue )
  }

  void setCodigoColor( String pValue ) {
    codigoColor = StringUtils.trimToNull( pValue )
  }

  void setDescripcionColor( String pValue ) {
    descripcionColor = StringUtils.trimToNull( pValue )
  }

  void setTipo( String pValue ) {
    tipo = StringUtils.trimToNull( pValue )
  }

  void setMarca( String pValue ) {
    marca = StringUtils.trimToNull( pValue )
  }

  void setCB( String pValue ) {
    cb = StringUtils.trimToNull( pValue )
  }

  void updateArticulo( Articulo pPart ) {
    if ( articulo != null )
      pPart.articulo = articulo
    if ( generico != null )
      pPart.idGenerico = generico
    if ( descripcion != null )
      pPart.descripcion = descripcion
    if ( codigoColor != null )
      pPart.codigoColor = codigoColor
    if ( descripcionColor != null )
      pPart.descripcionColor = descripcionColor
    if ( tipo != null ) {
      pPart.tipo = tipo
      pPart.idGenTipo = tipo
    }
    if ( marca != null ) {
      pPart.marca = marca
      pPart.idGenSubtipo = marca.trim().length() > 2?marca.substring(0, 2) : marca
    }
    if ( cb != null )
      pPart.idCb = cb


  }
}
