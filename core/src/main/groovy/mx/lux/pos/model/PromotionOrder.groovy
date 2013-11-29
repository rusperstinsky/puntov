package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

class PromotionOrder implements Comparable<PromotionOrder> {
  
  private static final Double ZERO_TOLERANCE = PromotionModel.ZERO_TOLERANCE
  
  String orderNbr
  Map<Integer, PromotionOrderDetail> orderDetailSet
  
  private PromotionOrder( ) {
    orderDetailSet = new TreeMap<Integer, PromotionOrderDetail>( )
  }
  PromotionOrder( String pOrderNbr ) {
    this( )
    setOrderNbr( pOrderNbr )
  }
  PromotionOrder( NotaVenta pOrder ) {
    this( pOrder.id )
    for ( DetalleNotaVenta orderLine : pOrder.detalles ) {
      this.addOrderDetail( orderLine )
    }
  }
  
  // Internal Methods
  protected void setOrderNbr( String pOrderNbr ) {
    clear( )
    this.orderNbr = StringUtils.trimToEmpty( pOrderNbr ).toUpperCase( )
  }

  // Public methods
  PromotionOrderDetail addOrderDetail( Integer pSku ) {
    PromotionOrderDetail orderDetail = this.orderDetailSet.get( pSku )
    if ( orderDetail == null ) {
      orderDetail = new PromotionOrderDetail( pSku )
      this.orderDetailSet.put( orderDetail.sku, orderDetail )
    }
    return orderDetail
  }
   
  PromotionOrderDetail addOrderDetail( DetalleNotaVenta pOrderLine ) {
    PromotionOrderDetail orderDetail = null
    if ( this.equals( pOrderLine.idFactura ) ) {
      orderDetail = this.orderDetailSet.get( pOrderLine.idArticulo )
      if ( orderDetail == null ) {
        orderDetail = new PromotionOrderDetail( pOrderLine )
        this.orderDetailSet.put( orderDetail.sku, orderDetail )
      }
      orderDetail.assign( pOrderLine )
    }
    return orderDetail
  }
   
  void clear( ) {
    orderDetailSet.clear( )
  }

  Double getRegularAmount( ) {
    Double amount = 0
    for ( PromotionOrderDetail det : orderDetailSet.values() ) {
      amount += det.regularAmount
    }  
    return amount
  }
  
  Double getDiscountAmount( ) {
    return this.getDiscountAmount( false )
  }
  
  Double getDiscountAmount( boolean pPromotionOnly ) {
    return ( this.regularAmount - ( pPromotionOnly ? this.finalSubtotalAmount : this.finalTotalAmount ) )
  }

  Double getDiscountPercent( ) {
    return this.getDiscountPercent( false )
  }
  
  Double getDiscountPercent( boolean pPromotionOnly ) {
    double discount = 0
    if ( Math.abs( this.regularAmount ) > ZERO_TOLERANCE ) {
      discount = this.discountAmount / this.regularAmount
    }
    return discount   
  } 
   
  Double getFinalSubtotalAmount( ) {
    Double amount = 0
    for ( PromotionOrderDetail det : orderDetailSet.values() ) {
      amount += det.finalAmount
    }  
    return amount
  }
  
  Double getFinalTotalAmount( ) {
    return finalSubtotalAmount
  }
  
  // Entity methods
  int compareTo( PromotionOrder pOrder ) {
    return this.orderNbr.compareTo( pOrder.orderNbr )
  }
  
  boolean equals( Object pObj ) {
    boolean result = false
    if ( pObj instanceof PromotionOrder ) {
      result = this.orderNbr.equals( ( pObj as PromotionOrder ).orderNbr )
    } else if ( pObj instanceof NotaVenta ) {
      result = this.orderNbr.equalsIgnoreCase( StringUtils.trimToEmpty( ( pObj as NotaVenta ).id ) )
    } else if ( pObj instanceof String ) {
      result = this.orderNbr.equalsIgnoreCase( StringUtils.trimToEmpty( pObj as String ) )
    }
    return result
  }
  
  public int hashCode( ) {
    return this.orderNbr.hashCode( )
  }
  
  String toString( ) {
    StringBuffer sb = new StringBuffer( )
    sb.append( String.format( "Order:%s  RegularPrice:%,.2f  Subtotal:%,.2f(%,.1f%%)  Total:%,.2f(%,.1f%%) ", 
        this.orderNbr, this.regularAmount, this.finalSubtotalAmount, ( 100.0 * this.getDiscountPercent( true ) ), 
        this.finalTotalAmount, ( 100.0 * this.discountPercent ) ) )
    if ( orderDetailSet.size( ) > 0 ) {
      for ( PromotionOrderDetail det : orderDetailSet.values( ) ) {
        sb.append( String.format( "\n    %s", det.toString( ) ) )   
      }
    }
    return sb.toString( )
  }
}
