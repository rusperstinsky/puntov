package mx.lux.pos.service.business

import mx.lux.pos.repository.TipoTransInvRepository
import mx.lux.pos.repository.TransInvDetalleRepository
import mx.lux.pos.repository.TransInvRepository
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.service.CierreDiarioService
import mx.lux.pos.service.io.ZInFile
import mx.lux.pos.util.CustomDateUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mx.lux.pos.model.*

@Component
class InventorySearch {

  private static TransInvRepository trInvMaster
  private static TransInvDetalleRepository trInvDetail
  private static TipoTransInvRepository trTypeCatalog
  private static ArticuloService partMaster
  private static CierreDiarioService cierreDiarioService

  @Autowired InventorySearch( TransInvRepository pTrMstr, TransInvDetalleRepository pTrDet, ArticuloService pParts,
                              TipoTransInvRepository pTypeCatalog, CierreDiarioService pCierreDiarioService
  ) {
    trInvMaster = pTrMstr
    trInvDetail = pTrDet
    partMaster = pParts
    trTypeCatalog = pTypeCatalog
    cierreDiarioService = pCierreDiarioService
  }

  static List<Integer> buildSkuList( List<Articulo> pPartList ) {
    List<Integer> list = new ArrayList<Integer>( pPartList.size() )
    for ( Articulo part : pPartList ) {
      list.add( part.getId() )
    }
    return list
  }

  static void loadDetails( List<TransInv> pMaster ) {
    for ( TransInv tr in pMaster ) {
      tr.setTrDet( trInvDetail.findByIdTipoTransAndFolio( tr.idTipoTrans, tr.folio ) )
    }
  }

  static List<TransInv> detailToMaster( List<TransInvDetalle> pDetails ) {
    List<TransInv> mstrList = new ArrayList<TransInv>()
    for ( TransInvDetalle det in pDetails ) {
      List<TransInv> one = trInvMaster.findByIdTipoTransAndFolio( det.idTipoTrans, det.folio )
      TransInv mstr = ( one.size() > 0 ? one.get( 0 ) : null )
      if ( mstr != null ) {
        if ( !mstrList.contains( mstr ) ) {
          mstrList.add( mstr )
        }
      }
    }
    Collections.sort( mstrList )
    return mstrList
  }

  static List<TransInv> listarTransaccionesPorRangoFecha( Date pRangeStart, Date pRangeEnd ) {
    List<TransInv> selected = trInvMaster.findByFechaBetween( pRangeStart, pRangeEnd )
    loadDetails( selected )
    return selected
  }

  static List<TransInv> listarTransaccionesPorTipo( String pIdTipoTrans ) {
    List<TransInv> selected = trInvMaster.findByIdTipoTrans( pIdTipoTrans )
    loadDetails( selected )
    return selected
  }

  static List<TransInv> listarTransaccionesPorSucursalDestino( Integer pSiteTo ) {
    List<TransInv> selected = trInvMaster.findBySucursalDestino( pSiteTo )
    loadDetails( selected )
    return selected
  }

  static List<TransInv> listarTransaccionesPorSku( Integer pSku ) {
    List<TransInvDetalle> details = trInvDetail.findBySku( pSku )
    List<TransInv> selected = detailToMaster( details )
    loadDetails( selected )
    return selected
  }

  static List<TransInv> listarTransaccionesPorArticulo( String pPartCodeSeed ) {
    List<Articulo> partList = partMaster.listarArticulosPorCodigoSimilar( pPartCodeSeed, false )
    List<TransInvDetalle> details = new ArrayList<TransInvDetalle>()
    if ( partList.size() > 0 ) {
      List<Integer> skuList = buildSkuList( partList )
      details = trInvDetail.findBySkuIn( skuList )
    }
    List<TransInv> selected = detailToMaster( details )
    loadDetails( selected )
    return selected
  }

  static List<TransInv> listarTransaccionesPorTipoAndReferencia( String pTrType, String pReference ) {
    List<TransInv> selected = trInvMaster.findByIdTipoTransAndReferencia( pTrType, pReference )
    loadDetails( selected )
    return selected
  }

  static List<TransInv> listarTransaccionesPorFecha( Date pDateFrom, Date pDateTo, Boolean pCierre ) {
    Date dtFrom = DateUtils.truncate( pDateFrom, Calendar.DATE )
    Date dtTo = DateUtils.truncate( pDateTo, Calendar.DATE )
    List<TransInv> selected = trInvMaster.findByFechaBetween( dtFrom, dtTo )
    if( pCierre ){
    List<CierreDiario> lstOpenDays = cierreDiarioService.buscarConEstadoAbierto()
    for(CierreDiario cierre : lstOpenDays){
      cierreDiarioService.cargarDatosCierreDiario( cierre.fecha )
      CierreDiario closeDay = cierreDiarioService.buscarPorFecha( cierre.fecha )

      if( closeDay.ventaNeta.compareTo(BigDecimal.ZERO) <= 0 && !cierre.fecha.format('dd/MM/yyyy').equalsIgnoreCase(new Date().format('dd/MM/yyyy')) ){
      cierreDiarioService.cambiarEstatuCerrado( cierre.fecha )
      Date fechaInicio = DateUtils.truncate( cierre.fecha, Calendar.DAY_OF_MONTH );
      Date fechaFin = new Date( DateUtils.ceiling( cierre.fecha, Calendar.DAY_OF_MONTH ).getTime() - 1 );
      List<TransInv> lstTransacciones = trInvMaster.findByFechaBetween( fechaInicio, fechaFin )
      if(lstTransacciones.size() > 0){
        selected.addAll( lstTransacciones )
      }
      }
    }
    }
    loadDetails( selected )
    return selected
  }


  static List<TransInv> listarTransaccionesPorMes( Date pDateFrom, Date pDateTo ) {
    Date dtFrom = DateUtils.truncate( pDateFrom, Calendar.DATE )
    Date dtTo = DateUtils.truncate( pDateTo, Calendar.DATE )
    List<TransInv> selected = trInvMaster.findByFechaBetween( dtFrom, dtTo )
    loadDetails( selected )
    return selected
  }

  static TransInv obtenerTransaccion( String pIdTipoTrans, Integer pFolio ) {
    TransInv tr = null
    List<TransInv> trList = trInvMaster.findByIdTipoTransAndFolio( pIdTipoTrans, pFolio )
    if ( trList.size() > 0 ) {
      loadDetails( trList )
      tr = trList.get( 0 )
    }
    return tr
  }

  static Boolean esTipoTransaccionAjuste( String pIdTipoTrans ) {
    Parametro p = Registry.find( TipoParametro.TRANS_INV_TIPO_AJUSTE )
    return p.valor.equalsIgnoreCase( StringUtils.trimToEmpty( pIdTipoTrans ) )
  }

  static Boolean esTipoTransaccionDevolucion( String pIdTipoTrans ) {
    Parametro p = Registry.find( TipoParametro.TRANS_INV_TIPO_CANCELACION_EXTRA )
    return p.valor.equalsIgnoreCase( StringUtils.trimToEmpty( pIdTipoTrans ) )
  }

  static Boolean esTipoTransaccionSalida( String pIdTipoTrans ) {
    Parametro p = Registry.find( TipoParametro.TRANS_INV_TIPO_SALIDA )
    return p.valor.equalsIgnoreCase( StringUtils.trimToEmpty( pIdTipoTrans ) )
  }

  static Boolean esTipoTransaccionSalidaSucursal( String pIdTipoTrans ) {
     Parametro p = Registry.find( TipoParametro.TRANS_INV_TIPO_SALIDA_ALMACEN )
     return p.valor.equalsIgnoreCase( StringUtils.trimToEmpty( pIdTipoTrans ) )
  }

    static Boolean esTipoTransaccionEntrada( String pIdTipoTrans ) {
        Parametro p = Registry.find( TipoParametro.TRANS_INV_TIPO_ENTRADA_ALMACEN )
        Parametro p1 = Registry.find( TipoParametro.TRANS_INV_TIPO_RECIBE_REMISION )
        Boolean tipoEntrada = false
        if(p.valor.equalsIgnoreCase( StringUtils.trimToEmpty( pIdTipoTrans ) ) || p1.valor.equalsIgnoreCase( StringUtils.trimToEmpty( pIdTipoTrans ) )){
            tipoEntrada = true
        }
        return tipoEntrada
    }

    static void generateInFile( Date pDateFrom, Date pDateTo, Boolean cierre ) {
      ZInFile file = new ZInFile( DateUtils.truncate( pDateFrom, Calendar.DATE ), false )
      file.setInvTrList( listarTransaccionesPorFecha( pDateFrom, pDateTo, cierre ) )
      file.write()
  }

    static void generateInFile2( Date pDateFrom, Date pDateTo, Date dateFile ) {
      ZInFile file = new ZInFile( DateUtils.truncate( dateFile, Calendar.DATE ), true )
      file.setInvTrList( listarTransaccionesPorFecha2( pDateFrom, pDateTo, dateFile ) )
      file.writeMonth()
    }

    static void generateInFile2Unique( Date pDateFrom, Date pDateTo, Date dateFile ) {
        ZInFile file = new ZInFile( DateUtils.truncate( dateFile, Calendar.DATE ), true )
        file.setInvTrList( listarTransaccionesPorFecha2( pDateFrom, pDateTo, dateFile ) )
        file.writeMonthUnique()
    }

  static TipoTransInv findTrType( String pTipoTransInv ) {
    return trTypeCatalog.findOne( pTipoTransInv )
  }


    static List<TransInv> listarTransaccionesPorFecha2( Date pDateFrom, Date pDateTo, Date fechaCierre ) {
        Date dtFrom = DateUtils.truncate( pDateFrom, Calendar.DATE )
        Date dtTo = DateUtils.truncate( pDateTo, Calendar.DATE )
        List<TransInv> selected = trInvMaster.findByFechaBetween( dtFrom, dtTo )

        List<CierreDiario> lstOpen = cierreDiarioService.buscarConEstadoAbierto()
        List<CierreDiario> lstOpenDays = new ArrayList<CierreDiario>()
        for(CierreDiario close : lstOpen){
          String datelist = CustomDateUtils.format( close.fecha, 'dd-MM-yyyy' )
          String dateClose = CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' )
          if(!datelist.trim().equalsIgnoreCase(dateClose.trim())){
            lstOpenDays.add( close )
          }
        }
        for(CierreDiario cierre : lstOpenDays){
            cierreDiarioService.cargarDatosCierreDiario( cierre.fecha )
            cierreDiarioService.cambiarEstatuCerrado( cierre.fecha )
            Date fechaInicio = DateUtils.truncate( cierre.fecha, Calendar.DAY_OF_MONTH );
            Date fechaFin = new Date( DateUtils.ceiling( cierre.fecha, Calendar.DAY_OF_MONTH ).getTime() - 1 );
            List<TransInv> lstTransacciones = trInvMaster.findByFechaBetween( fechaInicio, fechaFin )
            if(lstTransacciones.size() > 0){
                selected.addAll( lstTransacciones )
            }
        }
        loadDetails( selected )
        return selected
    }
}
