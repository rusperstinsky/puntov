package mx.lux.pos.service.business

import mx.lux.pos.repository.AcuseRepository
import mx.lux.pos.repository.ParametroRepository
import mx.lux.pos.repository.RetornoDetRepository
import mx.lux.pos.repository.RetornoRepository
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.service.InventarioService
import mx.lux.pos.service.SucursalService
import mx.lux.pos.util.CustomDateUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mx.lux.pos.model.*

import javax.annotation.Resource
import java.text.NumberFormat

@Component
class PrepareInvTrBusiness {
  private static final String TR_TYPE_ISSUE_SALES = 'VENTA'
  private static final String TR_TYPE_RECEIPT_RETURN = 'DEVOLUCION'

  private static ArticuloService parts
  private static InventarioService inventory
  private static SucursalService sites
  private static ParametroRepository parameters
  private static AcuseRepository acuseRepository
  private static RetornoRepository retornoRepository
  private static RetornoDetRepository retornoDetRepository

  static PrepareInvTrBusiness instance

  private Logger log = LoggerFactory.getLogger( this.class )

  @Autowired
  PrepareInvTrBusiness( ArticuloService pArticuloService, InventarioService pInventarioService, SucursalService pSucursalService,
                        ParametroRepository pParametroRepository, AcuseRepository pAcuseRepository, RetornoRepository pRetornoRepository,
                        RetornoDetRepository pRetornoDetRepository) {
    parts = pArticuloService
    inventory = pInventarioService
    sites = pSucursalService
    parameters = pParametroRepository
    acuseRepository = pAcuseRepository
    retornoRepository = pRetornoRepository
    retornoDetRepository = pRetornoDetRepository
    instance = this
  }

  private TransInv prepareTransaction( InvTrRequest pRequest ) {
    TipoTransInv trType = inventory.obtenerTipoTransaccion( pRequest.trType )
    Sucursal site = sites.obtenSucursalActual()
    TransInv trMstr = null

    if ( ( trType != null ) && ( site != null ) ) {
      trMstr = new TransInv()
      trMstr.fecha = DateUtils.truncate( pRequest.effDate, Calendar.DATE )
        if( trType.idTipoTrans.equalsIgnoreCase("ENTRADA_TIENDA")){
            trMstr.sucursalDestino = pRequest.siteFrom
            trMstr.sucursal = pRequest.siteTo
        } else {
            trMstr.sucursalDestino = pRequest.siteTo
        }
      trMstr.observaciones = pRequest.remarks
      trMstr.sucursal = site.id
      trMstr.idEmpleado = pRequest.idUser

        String aleatoria = claveAleatoria(trMstr.sucursal, trType.ultimoFolio+1)
        Parametro p = Registry.find( TipoParametro.TRANS_INV_TIPO_SALIDA_ALMACEN )
        if (trType.idTipoTrans.equalsIgnoreCase(p.valor)) {
            Integer ultimoFolio = trType.ultimoFolio+1
            String url = Registry.getURL( trType.idTipoTrans);
            String variable = trMstr.sucursal + '>' + trMstr.sucursalDestino + '>' +
                     ultimoFolio+ '>' +
                    aleatoria + '>' +
                    trMstr.idEmpleado.trim() + '>'

            for (int i = 0; i < pRequest.skuList.size(); i++) {
                variable += pRequest.skuList[i].sku + ',' + pRequest.skuList[i].qty +'|'
            }
            log.debug( "cadena a enviar: ${variable}" )
            url += String.format( '?arg=%s', URLEncoder.encode( String.format( '%s', variable ), 'UTF-8' ) )
            String response = ''
            try{
                response = url.toURL().text
                response = response?.find( /<XX>\s*(.*)\s*<\/XX>/ ) {m, r -> return r}
            } catch ( Exception e ){
                println e
            }
            log.debug( "respuesta: ${response}" )
            trMstr.referencia = aleatoria
            insertAcuseTransaction( trMstr, variable, response, trType.idTipoTrans )
            } else {
                trMstr.referencia = pRequest.reference
            }



      Integer iDet = 0
      for ( InvTrDetRequest detReq in pRequest.skuList ) {
        if ( parts.esInventariable( detReq.sku ) ) {
          TransInvDetalle det = new TransInvDetalle()
          det.linea = ++iDet
          det.sku = detReq.sku
          det.cantidad = detReq.qty
          det.tipoMov = trType.tipoMovObj.codigo
          trMstr.add( det )
        }
      }
      trMstr.idTipoTrans = trType.idTipoTrans
      Parametro param = Registry.find( TipoParametro.TRANS_INV_TIPO_CANCELACION_EXTRA )
      if (trType.idTipoTrans.equalsIgnoreCase(param.valor)) {
          insertaRetorno( trType.ultimoFolio+1, pRequest, trMstr )
      }
    }
    return trMstr
  }

  private verifyRequest( InvTrRequest pRequest ) {
    boolean valid = true

    // SiteTo
    if ( valid && pRequest.siteTo )
      valid = sites.validarSucursal( pRequest.siteTo )

    // Part
    if ( valid ) {
      for ( part in pRequest.skuList ) {
        if ( valid )
          valid = parts.validarArticulo( part.sku )
      }
    }

    return valid
  }

  // Public methods
  TransInv prepareRequest( InvTrRequest pRequest ) {
    TransInv tr = null
    if ( verifyRequest( pRequest ) ) {
      tr = prepareTransaction( pRequest )
    }
    return tr
  }

  InvTrRequest requestSalesIssue( NotaVenta pNotaVenta ) {
    InvTrRequest request = new InvTrRequest()

    request.trType = TR_TYPE_ISSUE_SALES
    String trType = parameters.findOne( TipoParametro.TRANS_INV_TIPO_VENTA.value )?.valor
    if ( StringUtils.trimToNull( trType ) != null ) {
      request.trType = trType
    }

    request.effDate = pNotaVenta.fechaMod
    request.idUser = pNotaVenta.idEmpleado
    request.reference = pNotaVenta.id

    for ( DetalleNotaVenta det in pNotaVenta.detalles ) {
      if ( parts.validarArticulo( det.idArticulo ) ) {
        request.skuList.add( new InvTrDetRequest( det.idArticulo, det.cantidadFac.intValue() ) )
      }
    }
    return request
  }

  InvTrRequest requestReturnReceipt( NotaVenta pNotaVenta ) {
    InvTrRequest request = new InvTrRequest()

    request.trType = TR_TYPE_RECEIPT_RETURN
    String trType = parameters.findOne( TipoParametro.TRANS_INV_TIPO_CANCELACION.value )?.valor
    if ( StringUtils.trimToNull( trType ) != null ) {
      request.trType = trType
    }

    request.effDate = pNotaVenta.fechaMod
    request.idUser = pNotaVenta.idEmpleado
    request.reference = pNotaVenta.id

    for ( DetalleNotaVenta det in pNotaVenta.detalles ) {
      if ( parts.validarArticulo( det.idArticulo ) ) {
        request.skuList.add( new InvTrDetRequest( det.idArticulo, det.cantidadFac.intValue() ) )
      }
    }
    return request
  }


    protected  String claveAleatoria(Integer sucursal, Integer folio) {
        String folioAux = "" + folio.intValue();
        String sucursalAux = "" + sucursal.intValue()
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        if (folioAux.size() < 4) {
            folioAux = folioAux?.padLeft( 4, '0' )
        }
        else {
            folioAux = folioAux.substring(0,4);
        }
        String resultado = sucursalAux?.padLeft( 3, '0' ) + folioAux


        for (int i = 0; i < resultado.size(); i++) {
            int numAleatorio = (int) (Math.random() * abc.size());
            if (resultado.charAt(i) == '0') {
                resultado = replaceCharAt(resultado, i, abc.charAt(numAleatorio))
            }
            else {
                int numero = Integer.parseInt ("" + resultado.charAt(i));
                numero = 10 - numero
                char diff = Character.forDigit(numero, 10);
                resultado = replaceCharAt(resultado, i, diff)
            }


        }
        return resultado;
    }

    protected static String replaceCharAt(String s, int pos, char c) {
        StringBuffer buf = new StringBuffer( s );
        buf.setCharAt( pos, c );
        return buf.toString( );
    }


    private void insertAcuseTransaction( TransInv transInv, String contenido, String folio, String tipoTransaccion ){
        log.debug( "Insertando Acuse" )
        Acuse acuse = new Acuse()
        acuse.idTipo = tipoTransaccion
        acuse.folio = StringUtils.trimToEmpty(folio)
        if( StringUtils.trimToEmpty(folio) != '' ){
            acuse.fechaAcuso = transInv.fecha
        }
        acuse.intentos = 0
        acuse.contenido = contenido
        acuse.fechaCarga = new Date()
        acuseRepository.save( acuse )
    }


    private void insertaRetorno( Integer folio, InvTrRequest invTrRequest, TransInv transInv ){
        log.debug( "Insertando Retorno" )
        String[] articulos = invTrRequest.reference.split("--")
        String ref = articulos[0]
        String refDet = articulos[1]
        String[] dataRef = ref.split(/\|/)
        String[] dataRefDet = refDet.split(/\|/)
        String ticketOrigen = dataRef[0]
        String price = dataRef[1].trim().replace( ',','' )
        BigDecimal montoTotal = new BigDecimal(price)

        Retorno retorno = new Retorno()
        retorno.id = folio
        retorno.ticketOrigen = ticketOrigen
        retorno.monto = montoTotal
        retornoRepository.save( retorno )

        Integer flag = 0;
        List<RetornoDet> lstRetornoDet = new ArrayList<RetornoDet>()
        for( InvTrDetRequest item : invTrRequest.skuList ){
            String importe = dataRefDet[flag].trim()
            RetornoDet retornoDet = new RetornoDet()
            retornoDet.idTransaccion = retorno.id
            retornoDet.sku = item.sku
            retornoDet.cantidad = item.qty
            retornoDet.importe = new BigDecimal( importe )
            flag = flag+1
            lstRetornoDet.add( retornoDet )
        }

        for(RetornoDet retDet : lstRetornoDet){
            retornoDetRepository.save( retDet )
        }
        retornoRepository.flush()
        retornoDetRepository.flush()
    }

    void saveAcuse( Acuse acuse ){
        acuseRepository.save( acuse )
    }
}