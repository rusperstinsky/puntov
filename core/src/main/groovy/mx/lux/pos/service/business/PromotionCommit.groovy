package mx.lux.pos.service.business

import mx.lux.pos.repository.GrupoArticuloDetRepository
import mx.lux.pos.repository.GrupoArticuloRepository
import mx.lux.pos.repository.PromocionRepository
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.io.PromotionsAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.Resource

import mx.lux.pos.model.*

class PromotionCommit {

  @Resource
  private PromocionRepository promocionRepository

  private static Logger log = LoggerFactory.getLogger( PromotionCommit.class )

  static final BigDecimal asAmount( Double pDoubleValue ) {
    return new BigDecimal( String.format( "%.2f", pDoubleValue ) )
  }

  static final BigDecimal asPercent( Double pDoubleValue ) {
    return new BigDecimal( String.format( "%.1f", pDoubleValue ) )
  }

  static final void deleteDiscounts( String pOrderNbr ) {
    List<Descuento> discountList = RepositoryFactory.discounts.findByIdFactura( pOrderNbr )
    if ( discountList.size() > 0 ) {
      RepositoryFactory.discounts.deleteInBatch( discountList )
    }
    RepositoryFactory.discounts.flush()
  }

  static final void deletePromotions( String pOrderNbr ) {
    List<OrdenPromDet> ordenPromDetList = RepositoryFactory.orderLinePromotionDetail.findByIdFactura( pOrderNbr )
    if ( ordenPromDetList.size() > 0 ) {
      RepositoryFactory.orderLinePromotionDetail.deleteInBatch( ordenPromDetList )
    }
    RepositoryFactory.orderLinePromotionDetail.flush()

    List<OrdenProm> ordenPromList = RepositoryFactory.orderPromotionDetail.findByIdFactura( pOrderNbr )
    if ( ordenPromList.size() > 0 ) {
      RepositoryFactory.orderPromotionDetail.deleteInBatch( ordenPromList )
    }
    RepositoryFactory.orderPromotionDetail.flush()
  }

  static final void writeDiscounts( PromotionModel pModel ) {
    deleteDiscounts( pModel.order.orderNbr )
    if ( pModel.hasOrderDiscountApplied() ) {
      String empId = PromotionQuery.findEmpId( pModel.order.orderNbr )
      Descuento descuento = new Descuento()
      descuento.idFactura = pModel.order.orderNbr
      descuento.clave = pModel.orderDiscount.corporateKey
      if ( pModel.orderDiscount.discountPercent < 1 ) {
        descuento.porcentaje = String.format( "%.0f", pModel.orderDiscount.discountPercent * 100.0 )
      } else {
        descuento.porcentaje = "100"
      }
      descuento.idEmpleado = empId
      descuento.idTipoD = pModel.orderDiscount.discountType.idType
      descuento.tipoClave = pModel.orderDiscount.discountType.description
      RepositoryFactory.discounts.saveAndFlush( descuento )
    }
  }

  static final void writePromotions( PromotionModel pModel ) {
    deletePromotions( pModel.order.orderNbr )
    if ( pModel.isAnyApplied() ) {
      Integer siteNbr = PromotionQuery.findSiteNbr( pModel.order.orderNbr )
      OrdenPromDetList opdl = new OrdenPromDetList( siteNbr, pModel )
      if ( opdl.list.size() > 0 ) {
        OrdenPromList opl = new OrdenPromList( opdl )
        List<OrdenProm> commited = new ArrayList<OrdenProm>()
        for ( OrdenProm op : opl.list ) {
          commited.add( RepositoryFactory.orderPromotionDetail.save( op ) )
        }
        RepositoryFactory.orderPromotionDetail.flush()
        opdl.setRelation( commited )
        RepositoryFactory.orderLinePromotionDetail.save( opdl.list )
        RepositoryFactory.orderLinePromotionDetail.flush()
      }
    }
  }

  static final void writeOrder( PromotionModel pModel ) {
    NotaVenta dbOrder = RepositoryFactory.orders.findOne( pModel.order.orderNbr )
    Double netAmount = 0
    for ( DetalleNotaVenta dbOrderLine : dbOrder.detalles ) {
      PromotionOrderDetail orderDetail = pModel.order.orderDetailSet.get( dbOrderLine.idArticulo )
      if ( orderDetail != null ) {
        dbOrderLine.precioUnitFinal = asAmount( orderDetail.finalPrice )
      } else {
        dbOrderLine.precioUnitFinal = dbOrderLine.precioUnitLista
      }
      dbOrderLine.precioFactura = dbOrderLine.precioUnitFinal
      netAmount += dbOrderLine.precioUnitFinal.doubleValue() * dbOrderLine.cantidadFac
      RepositoryFactory.orderLines.save( dbOrderLine )
    }
    RepositoryFactory.orderLines.flush()
    dbOrder.ventaNeta = asAmount( netAmount )
    dbOrder.ventaTotal = asAmount( netAmount )
    if ( pModel.hasOrderDiscountApplied() ) {
      dbOrder.montoDescuento = asAmount( pModel.orderDiscount.discountAmount )
      dbOrder.por100Descuento = Math.round( pModel.orderDiscount.discountPercent * 100.0 ) as Integer
    } else {
      dbOrder.montoDescuento = BigDecimal.ZERO
      dbOrder.por100Descuento = 0
    }
    RepositoryFactory.orders.saveAndFlush( dbOrder )
  }

  static void updatePromotions( List<PromotionsAdapter> lstPromociones ) {

    PromotionsAdapter promotionsAdapter = new PromotionsAdapter()
    PromocionRepository promociones = RepositoryFactory.promotionCatalog
    for ( PromotionsAdapter p : lstPromociones ) {
      /*Promocion promocion = promociones.findOne( p.idPromocion )
      if ( promocion != null ) {
        promociones.delete( promocion.idPromocion )
        promociones.flush()
      } else {*/
        Promocion promocion = new Promocion()
        promocion.idPromocion = p.idPromocion
      //}
      p.assignInto( promocion )
      promociones.save( promocion )
    }
    promociones.flush()
  }


  static void updateGroupPromotions( List<String> lstPromociones ) {
    GrupoArticuloRepository groups = RepositoryFactory.groupPartMaster
    GrupoArticuloDetRepository parts = RepositoryFactory.groupPartDetail

    List<GrupoArticuloImportLine> records = GrupoArticuloImportLine.adaptList( lstPromociones )
    Map<String, GrupoArticuloDet> buffer = new HashMap<String, GrupoArticuloDet>()
    GrupoArticulo currGroup = null
    for ( GrupoArticuloImportLine line : records ) {
      if ( ( currGroup == null ) || ( ! currGroup.equals( line.groupId ) ) ) {
        List<GrupoArticuloDet> partList = parts.findByIdGrupo( line.groupId )
        if ( partList.size() > 0 ) {
          try {
            parts.delete( partList )
            parts.flush()
          } catch ( Exception e ) {
            log.error( e.getMessage(), e )
          }
        }

        currGroup = groups.findOne( line.groupId )
        if ( currGroup == null ) {
          currGroup = new GrupoArticulo()
          currGroup.idGrupo = line.groupId
        }
        currGroup.descripcion = line.description
        try {
          groups.saveAndFlush( currGroup )
        } catch ( Exception e ) {
          log.error( e.getMessage(), e )
        }
      }

      String key = String.format( '%d:%s', line.groupId, line.partNbr )
      GrupoArticuloDet part = buffer.get( key )
      if ( part == null ) {
        part = new GrupoArticuloDet()
        part.idGrupo = line.groupId
        part.articulo = line.partNbr
        buffer.put( key, part )
      }
    }

    if ( buffer.size() > 0 ) {
      try {
        parts.save( buffer.values() )
        parts.flush()
      } catch ( Exception e ) {
        log.error( e.getMessage(), e )
      }
    }
  }

}
