package mx.lux.pos.service.impl

import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.service.EmpleadoService
import mx.lux.pos.service.InventarioService
import mx.lux.pos.service.SucursalService
import mx.lux.pos.service.io.ShippingNoticeFile
import mx.lux.pos.service.io.ShippingNoticeFileSunglass
import org.apache.commons.lang3.time.DateUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.apache.commons.lang.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

import mx.lux.pos.model.*
import mx.lux.pos.repository.*
import mx.lux.pos.service.business.*
import mx.lux.pos.service.io.InventoryAdjustFile

@Service( 'inventarioService' )
@Transactional( readOnly = true )
class InventarioServiceImpl implements InventarioService {
  private static final String TR_TYPE_ISSUE = "SALIDA"
  private static final String TR_TYPE_RECEIPT = "ENTRADA"
  private static final String TR_TYPE_ADJUST = "AJUSTE"
  private static final String TR_TYPE_SALE = "VENTA"
  private static final String TR_TYPE_RETURN = "DEVOLUCION"

  private Logger log = LoggerFactory.getLogger( this.class )

  // TipoTransInv data object  
  @Resource
  private TipoTransInvRepository tipoTransInvRepository

  @Resource
  private TransInvRepository transInvRepository

  @Resource
  private TransInvDetalleRepository transInvDetalleRepository

  @Resource
  private ArticuloRepository articuloRepository

  @Resource
  private ParametroRepository parametroRepository

  @Resource
  private ArticuloService articuloService

  @Resource
  private EmpleadoService empleadoService

  // Services
  List<TipoTransInv> listarTiposTransaccion( ) {
    return this.tipoTransInvRepository.findAll()
  }

  @Transactional
  Integer obtenerSiguienteFolio( String pIdTipoTransInv ) {
    TipoTransInv tipo = tipoTransInvRepository.findOne( pIdTipoTransInv )
    Integer folio = tipo?.getUltimoFolio() + 1
    tipo?.setUltimoFolio( folio )
    if ( tipo != null )
      tipoTransInvRepository.save( tipo )
    return folio
  }

  TipoTransInv obtenerTipoTransaccion( String pIdTipoTransInv ) {
    this.tipoTransInvRepository.findOne( pIdTipoTransInv?.toUpperCase() )
  }

  // TransInv data object
  @Resource
  private InventarioServiceUtil utilities

  Date obtenerUltimaFechaTransaccion( ) {
    Date lastDate = DateUtils.truncate( new Date(), Calendar.DATE )
    def fechaSort = new Sort( Sort.Direction.DESC, [ "fecha" ] )
    Page<TransInv> list = transInvRepository.findAll( new PageRequest( 0, 1, fechaSort ) )
    if ( list.numberOfElements > 0 ) {
      lastDate = list.content[ 0 ].fecha
    }
    return lastDate
  }

  @Transactional
  Integer registrarTransaccion( TransInv pTrMstr ) {
    Integer trnbr = InventoryCommit.registrarTransaccion( pTrMstr )
    if ( trnbr != null ) {
      InventoryCommit.exportarTransaccion( pTrMstr )
    }
    return trnbr
  }

  Integer solicitarTransaccion( InvTrRequest pRequest ) {
    Integer registrado = null
    PrepareInvTrBusiness task = PrepareInvTrBusiness.instance
    TransInv tr = task.prepareRequest( pRequest )
    if ( tr != null ) {
      registrado = registrarTransaccion( tr )
    }
    return registrado
  }

  Boolean solicitarTransaccionVenta( NotaVenta pNotaVenta ) {
    Boolean registrado = false
    PrepareInvTrBusiness task = PrepareInvTrBusiness.instance
    InvTrRequest request = task.requestSalesIssue( pNotaVenta )
    if ( request != null ) {
      registrado = ( solicitarTransaccion( request ) != null )
    }
    return registrado
  }

  Boolean solicitarTransaccionDevolucion( NotaVenta pNotaVenta ) {
    Boolean registrado = false
    PrepareInvTrBusiness task = PrepareInvTrBusiness.instance
    InvTrRequest request = task.requestReturnReceipt( pNotaVenta )
    if ( request != null ) {
      registrado = ( solicitarTransaccion( request ) != null )
    }
    return registrado
  }

  TransInv obtenerTransaccion( String pIdTipoTrans, Integer pFolio ) {
    InventorySearch.obtenerTransaccion( pIdTipoTrans, pFolio )
  }

  // TransInvDetalle data object

  // Articulo Services
  Integer obtenerExistenciaPorArticulo( Integer id ) {
    return articuloService.obtenerExistencia( id )
  }

  Collection<Articulo> listarArticulosConExistencia( ) {
    Collection<Articulo> lista = new ArrayList<Articulo>()
    lista.addAll( RepositoryFactory.partMaster.findByCantExistenciaGreaterThan( 0 ) )
    lista.addAll( RepositoryFactory.partMaster.findByCantExistenciaLessThan( 0 ) )
    return lista
  }

  // Sucursal Services
  @Resource
  private SucursalService sucursalService

  List<Sucursal> listarSucursales( ) {
    return sucursalService.listarSucursales()
  }

  Sucursal obtenerSucursal( Integer pSucursal ) {
    return sucursalService.obtenerSucursal( pSucursal )
  }

  Sucursal sucursalActual() {
    return sucursalService.obtenSucursalActual()
  }
  // Services added for Inventory View
  TipoTransInv obtenerTipoTransaccionAjuste( ) {
    return Registry.invTrTypeAdjust
  }

  TipoTransInv obtenerTipoTransaccionDevolucion( ) {
    return Registry.invTrTypeReturn
  }

  TipoTransInv obtenerTipoTransaccionDevolucionExtraordinaria( ) {
    return Registry.invTrTypeReturnXO
  }

  TipoTransInv obtenerTipoTransaccionEntrada( ) {
    return Registry.invTrTypeReceipt
  }

  TipoTransInv obtenerTipoTransaccionSalida( ) {
    return Registry.invTrTypeIssue
  }

  TipoTransInv obtenerTipoTransaccionVenta( ) {
    return Registry.invTrTypeSale
  }

  TipoTransInv obtenerTipoTransaccionSalidaAlmacen( ) {
        return Registry.invTrTypeSalidaAlmacen
  }

  TipoTransInv obtenerTipoTransaccionEntradaAlmacen( ) {
        return Registry.invTrTypeEntradaAlmacen
  }

    Empleado obtenerEmpleado( String pEmpId ) {
    return empleadoService.obtenerEmpleado( pEmpId )
  }

  List<TransInv> listarTransaccionesPorRangoFecha( Date pRangeStart, Date pRangeEnd ) {
    return InventorySearch.listarTransaccionesPorRangoFecha( pRangeStart, pRangeEnd )
  }

  List<TransInv> listarTransaccionesPorTipo( String pIdTipoTrans ) {
    return InventorySearch.listarTransaccionesPorTipo( pIdTipoTrans )
  }

  List<TransInv> listarTransaccionesPorSucursalDestino( Integer pSiteTo ) {
    return InventorySearch.listarTransaccionesPorSucursalDestino( pSiteTo )
  }

  List<TransInv> listarTransaccionesPorSku( Integer pSku ) {
    return InventorySearch.listarTransaccionesPorSku( pSku )
  }

  List<TransInv> listarTransaccionesPorArticulo( String pPartCodeSeed ) {
    return InventorySearch.listarTransaccionesPorArticulo( pPartCodeSeed )
  }

  List<TransInv> listarTransaccionesPorTipoAndReferencia( String pTrType, String pReference ) {
    return InventorySearch.listarTransaccionesPorTipoAndReferencia( pTrType, pReference )
  }

  Shipment leerArchivoRemesa( String pFilename ) {
    Shipment shipment = new ShippingNoticeFileSunglass().read( pFilename )
    if ( shipment == null ) {
      shipment = new ShippingNoticeFile().read( pFilename )
    }
    return shipment
  }

  Shipment obtieneArticuloEntrada(String clave, Integer sucursal, String pIdTipoTrans) {
      String url = Registry.getURL( pIdTipoTrans );
      if ( StringUtils.trimToNull( url ) != null ) {
          String variable = clave + ">" + sucursal
          //String variable = clave + ">" + "1"
          url += String.format( '?arg=%s', URLEncoder.encode( String.format( '%s', variable ), 'UTF-8' ) )
          log.debug(url)
          String response = url.toURL().text
          response = response?.find( /<XX>\s*(.*)\s*<\/XX>/ ) {m, r -> return r}
          log.debug( "resultado solicitud: ${response}" )
          if (response.contains(">")) {
            Shipment shipment = new ShippingNoticeFileSunglass().parseSolicitud(response);
            return shipment
          }
      }
  }

  InvAdjustSheet leerArchivoAjuste( String pFilename ) {
    InvAdjustSheet sheet = new InventoryAdjustFile().read( pFilename )
    return sheet
  }

  Boolean isReceiptDuplicate( ) {
    return Registry.isReceiptDuplicate()
  }

    List<Sucursal> listarAlmacenes( ) {
        return sucursalService.listarAlmacenes()
    }

    List<Sucursal> listarSoloSucursales( ) {
        return sucursalService.listarSoloSucursales()
    }

    Boolean transaccionCargada( String clave ){
        clave = clave.trim().toUpperCase()
        Boolean transCargada = false
        QTransInv tranInv = QTransInv.transInv
        TransInv transaccion = transInvRepository.findOne( tranInv.referencia.eq( clave.trim() ).and(tranInv.idTipoTrans.eq('ENTRADA_TIENDA')) )
        if( transaccion != null ){
            transCargada = true
        }
        return transCargada
    }


    void generaArchivoAcuseAjuste( String folio ){
      String fichero = "${Registry.archivePath}/${Registry.currentSite}_${folio}.reg"
      log.debug( "Generando Fichero: ${ fichero }" )
      File file = new File( fichero )
      log.debug( 'Creando Writer' )
      PrintStream strOut = new PrintStream( file )
      StringBuffer sb = new StringBuffer()
      sb.append("${Registry.currentSite}|${folio}")
      strOut.println sb.toString()
      strOut.close()
    }
}
