package mx.lux.pos.model

import mx.lux.pos.service.impl.ServiceFactory

class InvTrType {

  static InvTrType ADJUST = new InvTrType( ServiceFactory.inventory.obtenerTipoTransaccionAjuste() )
  static InvTrType ISSUE = new InvTrType( ServiceFactory.inventory.obtenerTipoTransaccionSalida() )
  static InvTrType RECEIPT = new InvTrType( ServiceFactory.inventory.obtenerTipoTransaccionEntrada() )
  static InvTrType SALES = new InvTrType( ServiceFactory.inventory.obtenerTipoTransaccionVenta() )
  static InvTrType RETURN = new InvTrType( ServiceFactory.inventory.obtenerTipoTransaccionDevolucion() )
  static InvTrType RETURN_XO = new InvTrType( ServiceFactory.inventory.obtenerTipoTransaccionDevolucionExtraordinaria() )
  static InvTrType OUTBOUND = new InvTrType( ServiceFactory.inventory.obtenerTipoTransaccionSalidaAlmacen() )
  static InvTrType INBOUND = new InvTrType( ServiceFactory.inventory.obtenerTipoTransaccionEntradaAlmacen() )

  private TipoTransInv trType

  private InvTrType( TipoTransInv pTrType ) {
    this.trType = pTrType
  }

  // Public methods
  boolean equals( Object pObj ) {
    boolean result = false
    if ( pObj instanceof InvTrType ) {
      result = this.trType.idTipoTrans.equals( ( pObj as InvTrType ).trType.idTipoTrans )
    } else if ( pObj instanceof TransInv ) {
      result = this.trType.idTipoTrans.equalsIgnoreCase( ( pObj as TransInv ).idTipoTrans )
    } else if ( pObj instanceof String ) {
      result = this.trType.idTipoTrans.equalsIgnoreCase( ( pObj as String ).trim() )
    }
    return result
  }

  String getTrType( ) {
    return trType.idTipoTrans
  }

}
