package mx.lux.pos.service.business

import mx.lux.pos.repository.impl.RepositoryFactory
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import mx.lux.pos.model.*

class PromotionQuery {

  static final Logger log = LoggerFactory.getLogger( PromotionQuery.class ) 
  
  static void fillActivePromotions( PromotionList pActivePromotionList, Date pActiveOn ) {
    Date activeOn = DateUtils.truncate( pActiveOn, Calendar.DATE )
    Date dueAfter = DateUtils.addSeconds( activeOn, -1 )
    for ( Promocion p in RepositoryFactory.promotionCatalog.findByVigenciaFinAfter( dueAfter ) ) {
      if ( p.vigenciaIni.compareTo( activeOn ) <= 0 ) {
        pActivePromotionList.add( p )
      }
    }
    List<PromotionPart> groupRequiredList = pActivePromotionList.listGroupRequired( )
    for ( PromotionPart part : groupRequiredList ) {
      for ( GrupoArticuloDet gpoPart : RepositoryFactory.groupPartDetail.findByIdGrupo( part.group ) ) {
        part.addPart( gpoPart.articulo )
      }
    }
  }
  
  static void fillPart( PartDataset pPartSet ) {
    List<Integer> skuList = pPartSet.listRequiredParts( )
    if ( skuList.size( ) > 0 ) {
      log.debug( String.format( "fillPartNbr( %d OrderDetails )", skuList.size( ) ) )
      pPartSet.add( RepositoryFactory.partMaster.findByIdIn( skuList ) ) 
    }
  }
  
  static void fillPricing( PricingDataset pPricingSet, PartDataset pPartSet ) {
    List<Pricing> pricingRequiredList = pPricingSet.listPricingRequired( )
    if ( pricingRequiredList.size( ) > 0 ) {
      for ( Pricing pricing : pricingRequiredList ) {
        log.debug( String.format( "ObtenerPrecio( Sku:<%d>, Convenio:<%s> )", pricing.sku, pPricingSet.agreement ) )
        Articulo part = pPartSet.find( pricing.sku )
        if ( part != null ) {
          List<Precio> precios = RepositoryFactory.priceCatalog.findByArticulo( part.articulo )
          pricing.listPrice = part.precio
          if ( precios?.any() ) {
            Precio precioLista = precios.find { Precio tmp -> "L".equalsIgnoreCase( tmp?.lista ) }
            pricing.listPrice = precioLista?.precio ?: part.precio
            Precio precioOferta = precios.find { Precio tmp -> "O".equalsIgnoreCase( tmp?.lista ) }
            pricing.redPrice = precioOferta?.precio ?: 0
            Precio precioConv = precios.find { Precio tmp -> pPricingSet.agreement.equalsIgnoreCase( tmp?.lista ) }
            pricing.agreementPrice = precioConv?.precio ?: 0
          }
        }
      }
    }
  }

  static String findEmpId( String pOrderNbr ) {
    String empId = ""
    NotaVenta dbOrder = RepositoryFactory.orders.findOne( StringUtils.trimToEmpty( pOrderNbr ) )
    if ( dbOrder != null ) {
      empId = dbOrder.idEmpleado
    }
    return empId
  } 
  
  static Integer findSiteNbr( String pOrderNbr ) {
    Integer siteNbr = 0
    NotaVenta dbOrder = RepositoryFactory.orders.findOne( StringUtils.trimToEmpty( pOrderNbr ) )
    if ( dbOrder != null ) {
      siteNbr = dbOrder.idSucursal
    }
    return siteNbr
  } 
  
  static Double getTopStoreDiscount( ) {
    return ( Registry.asDouble( TipoParametro.MAX_DISCOUNT_STORE ) / 100.0 )
  }
}
