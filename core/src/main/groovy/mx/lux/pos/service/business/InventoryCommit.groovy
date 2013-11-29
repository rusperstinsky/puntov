package mx.lux.pos.service.business

import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.impl.ServiceFactory
import mx.lux.pos.service.io.InvTrFile
import mx.lux.pos.service.io.ShippingNoticeFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import mx.lux.pos.model.*
import org.apache.commons.lang3.StringUtils

class InventoryCommit {

  private static final String TXT_TR_TYPE_ADJUST_DESC = 'Ajuste Inventario'
  private static final String TXT_TR_TYPE_ISSUE_DESC = 'Transferencia de Producto'
  private static final String TXT_TR_TYPE_RECEIPT_DESC = 'Recibe Remision'
  private static final String TXT_TR_TYPE_RETURN_DESC = 'Devolucion de Venta'
  private static final String TXT_TR_TYPE_RETURNXO_DESC = 'Devolucion ajena a Tienda'
  private static final String TXT_TR_TYPE_SALE_DESC = 'Salida por Venta'

  private static Logger log = LoggerFactory.getLogger( InventoryCommit.class )

  static void exportarTransaccion( TransInv pTransInv ) {
    if ( Registry.isExportEnabledForInventory( pTransInv.idTipoTrans ) ) {
      if( !pTransInv.idTipoTrans.trim().equalsIgnoreCase('SALIDA_TIENDA') ){
          InvTrFile file = ResourceManager.getInvTrFile()
          file.write( pTransInv )
      }
    }
    if ( InvTrType.ISSUE.equals( pTransInv ) && Registry.isExchangeDataFileRequired() ) {
      ShippingNoticeFile file = ResourceManager.getShippingNoticeFile()
      file.write( pTransInv )
    }
  }

  static Integer registrarTransaccion( TransInv pTrMstr ) {
    Integer trnbr = null
    log.debug( "[Service] Registrar Trans Inventario" )
    log.debug( "Antes de registrar ${ pTrMstr.toString() }" )
    if ( pTrMstr.trDet.size() > 0 )
      try {
        // Fill auto data
        pTrMstr.folio = ServiceFactory.inventory.obtenerSiguienteFolio( pTrMstr.idTipoTrans )

        // Update Existencia
        List<Articulo> list = new ArrayList<Articulo>()
        for ( TransInvDetalle trDet in pTrMstr.trDet ) {
          TipoMov mov = TipoMov.parse( trDet.tipoMov )
          Articulo part = ServiceFactory.partMaster.obtenerArticulo( trDet.sku, false )
          part.cantExistencia += mov.factor * trDet.cantidad
        }
        ServiceFactory.partMaster.registrarListaArticulos( list )

        // Register Transactions
        RepositoryFactory.inventoryMaster.save( pTrMstr )
        for ( TransInvDetalle det in pTrMstr.trDet ) {
          RepositoryFactory.inventoryDetail.save( det )
        }
        RepositoryFactory.inventoryDetail.flush()
        RepositoryFactory.inventoryMaster.flush()

        trnbr = pTrMstr.folio
        TransInv tr = InventorySearch.obtenerTransaccion( pTrMstr.idTipoTrans, trnbr )
        if ( tr != null ) {
          pTrMstr = tr
        }
        log.debug( "Después de registrar: ${ pTrMstr.toString() }" )
      } catch ( Exception pException ) {
        log.error( "Error al registrar transaccion", pException )
      }
    return trnbr
  }

  static TipoTransInv createTrType( TipoParametro pTrTypeId, String pType ) {
    TipoTransInv trType = InventorySearch.findTrType( pType )
    if (trType == null) {
      trType = new TipoTransInv( )
      trType.idTipoTrans = pType
      if ( TipoParametro.TRANS_INV_TIPO_AJUSTE.equals( pTrTypeId )) {
      trType.descripcion = TXT_TR_TYPE_ADJUST_DESC
      trType.tipoMov = TipoMov.RECEIPT.codigo
      } else if ( TipoParametro.TRANS_INV_TIPO_RECIBE_REMISION.equals( pTrTypeId )) {
        trType.descripcion = TXT_TR_TYPE_RECEIPT_DESC
        trType.tipoMov = TipoMov.RECEIPT.codigo
      } else if ( TipoParametro.TRANS_INV_TIPO_SALIDA.equals( pTrTypeId )) {
        trType.descripcion = TXT_TR_TYPE_ISSUE_DESC
        trType.tipoMov = TipoMov.ISSUE.codigo
      } else if ( TipoParametro.TRANS_INV_TIPO_VENTA.equals( pTrTypeId )) {
        trType.descripcion = TXT_TR_TYPE_SALE_DESC
        trType.tipoMov = TipoMov.ISSUE.codigo
      } else if ( TipoParametro.TRANS_INV_TIPO_CANCELACION.equals( pTrTypeId )) {
        trType.descripcion = TXT_TR_TYPE_RETURN_DESC
        trType.tipoMov = TipoMov.RECEIPT.codigo
      } else if ( TipoParametro.TRANS_INV_TIPO_CANCELACION_EXTRA.equals( pTrTypeId )) {
        trType.descripcion = TXT_TR_TYPE_RETURNXO_DESC
        trType.tipoMov = TipoMov.RECEIPT.codigo
      }
      trType.descripcion = StringUtils.trimToEmpty(trType.descripcion).toUpperCase()
      trType.ultimoFolio = 0
      trType = RepositoryFactory.trTypes.saveAndFlush(trType)
    }
    return trType
  }

}
