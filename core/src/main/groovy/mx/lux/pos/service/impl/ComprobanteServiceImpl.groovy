package mx.lux.pos.service.impl

import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import groovy.util.logging.Slf4j
import mx.lux.pos.service.ComprobanteService
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.math.MathContext
import javax.annotation.Resource

import mx.lux.pos.model.*
import mx.lux.pos.repository.*
import mx.lux.pos.service.business.Registry

@Slf4j
@Service( 'comprobanteService' )
@Transactional( readOnly = true )
class ComprobanteServiceImpl implements ComprobanteService {

  private static final String DATE_TIME_FORMAT = 'dd-MM-yyyy HH:mm:ss'
  private static final String TAG_GENERICO_A = 'A'
  private static final String TAG_GENERICO_E = 'E'

  @Resource
  private ComprobanteRepository comprobanteRepository

  @Resource
  private DetalleComprobanteRepository detalleComprobanteRepository

  @Resource
  private NotaVentaRepository notaVentaRepository

  @Resource
  private DetalleNotaVentaRepository detalleNotaVentaRepository

  @Resource
  private ArticuloRepository articuloRepository

  @Resource
  private PagoRepository pagoRepository

  @Resource
  private SucursalRepository sucursalRepository

  @Resource
  private ParametroRepository parametroRepository

  @Resource
  private ReimpresionRepository reimpresionRepository

  private static Integer maxLength

  @Override
  Comprobante obtenerComprobante( String idFiscal ) {
    log.info( "obteniendo comprobante idFiscal: ${idFiscal}" )
    if ( StringUtils.isNotBlank( idFiscal ) ) {
      Comprobante comprobante = comprobanteRepository.findByIdFiscal( idFiscal )
      log.debug( "obtiene comprobante idFiscal: ${comprobante?.idFiscal}," )
      log.debug( "fechaImpresion: ${comprobante?.fechaImpresion?.format( DATE_TIME_FORMAT )}" )
      return comprobante
    } else {
      log.warn( 'no se obtiene comprobante, parametro invalido' )
    }
    return null
  }

  private Integer getMaxLength( ) {
    if (maxLength == null) {
      maxLength = Registry.maxLengthDescription
    }
    return maxLength
  }

  private boolean esTicketValido( String ticket ) {
    log.info( "validando estructura ticket: ${ticket}" )
    List<String> tokens = StringUtils.splitPreserveAllTokens( ticket, '-' )
    if ( StringUtils.isNotBlank( ticket ) && tokens?.size() == 2 ) {
      String centroCostos = tokens.get( 0 )
      String factura = tokens.get( 1 )
      if ( StringUtils.isNotBlank( factura ) && StringUtils.isNotBlank( centroCostos ) ) {
        log.debug( 'estructura ticket valida' )
        return true
      } else {
        log.warn( 'elementos ticket invalidos, ticket invalido' )
      }
    } else {
      log.warn( 'no se valida ticket, parametro invalido' )
    }
    return false
  }

  @Override
  List<Comprobante> listarComprobantesPorTicket( String ticket ) {
    log.info( "listando comprobantes por ticket: ${ticket}" )
    if ( esTicketValido( ticket ) ) {
      List<Comprobante> comprobantes = comprobanteRepository.findByTicketOrderByFechaImpresionDesc( ticket )
      log.debug( "obtiene comprobantes idFiscal: ${comprobantes*.idFiscal}" )
      return comprobantes?.any() ? comprobantes : [ ]
    } else {
      log.warn( 'no se listan comprobantes, parametro invalido' )
    }
    return [ ]
  }

  private String generarParametrosServicioWeb( Comprobante comprobante, List<DetalleComprobante> detalles ) {
    log.info( "generando parametros servicio web facturacion, ticket: ${comprobante?.ticket}" )
    if ( esTicketValido( comprobante?.ticket ) ) {
      if ( detalles?.any() ) {
        Integer idSucursal = sucursalRepository.getCurrentSucursalId()
        Parametro parametroEmpresa = parametroRepository.findOne( TipoParametro.EMP_ELECTRONICO.value )
        Parametro parametroTasa = parametroRepository.findOne( TipoParametro.IVA_VIGENTE.value )
        String plantilla = '$id_sucursal|$ticket|$tipo_comprobante|$forma_pago|$subtotal|$total|' +
            '$metodo_pago|$impuestos|$tasa|$rfc|$nombre|$calle|$colonia|$municipio|$estado|$pais|' +
            '$codigo_postal|$email|$observaciones|$empresa|$tipo|<% listaDetalles.each { print it } %>'
        String decimalFormat = '%,3.2f'
        List<String> listaDetalles = [ ]
        detalles.eachWithIndex { DetalleComprobante tmp, int idx ->
          String cantidad = tmp.cantidad ?: ''
          String articulo = tmp.articulo ?: ''
          String desc = StringUtils.trimToEmpty( tmp.descripcion )
          String descripcion = ''

          if ( desc.length() > 0 ) {
            if ( desc.length() >= this.getMaxLength() ) {
              descripcion = desc.substring( 0, this.getMaxLength() )
            } else {
              descripcion = desc
            }
          }

          String precio = sprintf( decimalFormat, tmp?.precioUnitario ?: BigDecimal.ZERO )
          String importe = sprintf( decimalFormat, tmp?.importe ?: BigDecimal.ZERO )
          listaDetalles.add( "${idx == 0 ? '>' : ''}${cantidad}|${articulo}|${descripcion}|${precio}|${importe}|>" )
        }
        Map<String, String> valores = [
            id_sucursal: idSucursal ?: '',
            ticket: comprobante.ticket,
            tipo_comprobante: 'ingreso',
            forma_pago: comprobante.formaPago ?: '',
            subtotal: sprintf( decimalFormat, comprobante.subtotal ?: BigDecimal.ZERO ),
            total: comprobante.importe?.replace( '$', '' ) ?: '0.00',
            metodo_pago: comprobante.metodoPago ?: '',
            impuestos: sprintf( decimalFormat, comprobante.impuestos ?: BigDecimal.ZERO ),
            tasa: parametroTasa?.valor ?: '',
            rfc: comprobante.rfc ?: '',
            nombre: comprobante.razon ?: '',
            calle: comprobante.calle ?: '',
            colonia: comprobante.colonia ?: '',
            municipio: comprobante.municipio ?: '',
            estado: comprobante.estado ?: '',
            pais: comprobante.pais ?: '',
            codigo_postal: comprobante.codigoPostal ?: '',
            email: comprobante.email ?: '',
            observaciones: comprobante.observaciones ?: '',
            empresa: parametroEmpresa?.valor ?: '',
            tipo: comprobante.idOrigen ?: 'N',
            listaDetalles: listaDetalles
        ] as Map
        Template template = new SimpleTemplateEngine().createTemplate( plantilla )
        String parametros = template.make( valores ).toString()
        log.debug( "parametros generados: ${parametros}" )
        return parametros
      } else {
        log.warn( 'no se generan parametros servicio web facturacion, no se obtienen detalles' )
      }
    } else {
      log.warn( 'no se generan parametros servicio web facturacion, parametro invalido' )
    }
    return null
  }

  private Comprobante procesarRespuestaServicioWeb( Comprobante comprobante, String respuesta ) {
    log.info( 'procesando respuesta servicio web facturacion' )
    if ( StringUtils.isNotBlank( respuesta ) ) {
      String resultado = respuesta?.find( /<XX>\s*(.*)\s*<\/XX>/ ) {m, r -> return r}
      log.debug( "resultado solicitud: ${resultado}" )
      List<String> elementos = resultado?.tokenize( '|' )
      List<String> datos = resultado?.tokenize( '>' )
      if ( datos?.any() ) {
          String codigo = elementos.first()
          log.debug( "codigo resultado: ${codigo}" )
          if ( codigo.matches( /.+PROCESADA.+/ ) ) {
              comprobante.sello = datos.get( 1 )
              comprobante.cadenaOriginal = datos.get( 2 )
          } else {
              log.warn( 'error al procesar la solicitud' )
          }
      }
      if ( elementos?.any() ) {
        String codigo = elementos.first()
        log.debug( "codigo resultado: ${codigo}" )
        if ( codigo.matches( /.+PROCESADA.+/ ) ) {
          comprobante.idFiscal = elementos.get( 1 )
          comprobante.url = elementos.get( 2 )
          comprobante.xml = elementos.get( 3 )
          return comprobante
        } else {
          log.warn( 'error al procesar la solicitud' )
        }
      } else {
        log.warn( 'no se recibe respuesta de servicio web facturacion' )
      }
    } else {
      log.warn( 'no se solicita servicio web facturacion, parametro invalido' )
    }
    return null
  }

  private Comprobante solicitarComprobanteServicioWeb( Comprobante comprobante, List<DetalleComprobante> detalles ) {
    log.info( "solicitando comprobante fiscal a servicio web facturacion, ticket: ${comprobante?.ticket}" )
    if ( StringUtils.isNotBlank( comprobante?.ticket ) && detalles?.any() ) {
      Parametro parametroURL = parametroRepository.findOne( TipoParametro.PIDE_FACTURA.value )
      log.debug( "url obtenida de servicio web: ${parametroURL?.valor}" )
      if ( StringUtils.isNotBlank( parametroURL?.valor ) ) {
        String parametros = generarParametrosServicioWeb( comprobante, detalles )
        if ( StringUtils.isNotBlank( parametros ) ) {
          URL url = "${parametroURL.valor}?arg=${URLEncoder.encode( parametros ?: '' )}".toURL()
          log.debug( "url a solicitar: ${url}" )
          Comprobante resultado = procesarRespuestaServicioWeb( comprobante, url?.text )
          log.debug( "comprobante procesado, idFiscal: ${resultado?.idFiscal}" )
          return resultado
        } else {
          log.warn( 'no se solicita servicio web facturacion, parametros no generados' )
        }
      } else {
        log.warn( 'no se solicita servicio web facturacion, url servicio invalida' )
      }
    } else {
      log.warn( 'no se solicita servicio web facturacion, parametros invalidos' )
    }
    return null
  }

  @Override
  @Transactional
  Comprobante registrarComprobante( Comprobante comprobante ) {
    log.info( "registrando comprobante fiscal, ticket: ${comprobante?.ticket}, idFactura: ${comprobante?.idFactura}" )
    if ( esTicketValido( comprobante?.ticket ) && StringUtils.isNotBlank( comprobante?.idFactura ) ) {
      NotaVenta venta = notaVentaRepository.findOne( comprobante.idFactura )
      log.debug( "obtiene notaVenta id: ${venta?.id}" )
      if ( StringUtils.isNotBlank( venta?.id ) ) {
        try {
          Parametro parametroTasa = parametroRepository.findOne( TipoParametro.IVA_VIGENTE.value )
          String tasa = parametroTasa?.valor
          log.debug( "obtiene tasa vigente: ${tasa}" )
          Double referencia = ( ( tasa?.isDouble() ? tasa.toDouble() : 0 ) / 100 ) + 1
          MathContext mathContext = new MathContext( 5 )
          BigDecimal total = venta.ventaNeta ?: BigDecimal.ZERO
          BigDecimal subTotal = total.divide( referencia, mathContext ) ?: BigDecimal.ZERO
          BigDecimal impuestos = total.subtract( subTotal )

          Comprobante ultimo = null
          List<Comprobante> anteriores = comprobanteRepository.findByTicketOrderByFechaImpresionDesc( comprobante.ticket )
          log.debug( "obtiene comprobantes anteriores idFiscal: ${anteriores*.idFiscal}" )
          if ( anteriores?.any() ) {
            ultimo = anteriores.first()
            log.debug( "obtiene ultimo comprobante: ${ultimo?.idFiscal}" )
            ultimo.estatus = 'R'
            comprobante.idOrigen = ultimo.idFiscal
            comprobante.tipo = 'R'
          } else {
            comprobante.tipo = 'O'
          }

          List<Pago> pagosVenta = pagoRepository.findByIdFacturaOrderByFechaAsc( venta.id )
          log.debug( "obtiene pagos notaVenta: ${pagosVenta*.id}" )
          pagosVenta?.removeAll { Pago pmt ->
            String idFormaPago = pmt?.idFormaPago ?: ''
            'VA'.equalsIgnoreCase( idFormaPago ) || 'TR'.equalsIgnoreCase( idFormaPago )
          }
          Pago pago = pagosVenta?.any() ? pagosVenta.first() : null

          List<DetalleComprobante> detalles = [ ]
          List<DetalleNotaVenta> detallesVenta = detalleNotaVentaRepository.findByIdFacturaOrderByIdArticuloAsc( venta.id )
          log.debug( "obtiene detalles notaVenta: ${detallesVenta*.id}" )
          detallesVenta?.each { DetalleNotaVenta det ->
            Articulo articulo = articuloRepository.findOne( det?.idArticulo ?: 0 )
            if ( articulo?.id ) {
              Integer cantidad = det?.cantidadFac ?: 0
              BigDecimal precioVenta = det.precioUnitFinal ?: BigDecimal.ZERO
              BigDecimal precioUnitario = precioVenta.divide( referencia, mathContext ) ?: BigDecimal.ZERO
              BigDecimal importe = precioUnitario.multiply( cantidad )
              DetalleComprobante detalle = new DetalleComprobante(
                  idArticulo: articulo.id,
                  //articulo: articulo.articulo.replace('Ñ','N'),
                  color: articulo.codigoColor,
                  idGenerico: articulo.idGenerico,
                  descripcion: articulo.idGenerico.trim().equalsIgnoreCase(TAG_GENERICO_A) ? "[${articulo.id}]Anteojo Solar" : "[${articulo.id}]Accesorio",
                  cantidad: cantidad,
                  precioUnitario: precioUnitario,
                  importe: importe
              )
              log.debug( "genera detalle comprobante: ${detalle.dump()}" )
              detalles.add( detalle )
            }
          }

          comprobante.factura = venta.factura
          comprobante.idCliente = venta.idCliente
          comprobante.importe = sprintf( '$%,3.2f', total ?: BigDecimal.ZERO )
          comprobante.subtotal = subTotal
          comprobante.impuestos = impuestos
          comprobante.estatus = 'N'
          comprobante.formaPago = 'UNA SOLA EXHIBICION'
          comprobante.metodoPago = "${pago?.eTipoPago?.descripcion ?: ''} ${pago?.referenciaPago ?: ''}"

          log.debug( "genera comprobante ${comprobante.dump()}" )

          comprobante = solicitarComprobanteServicioWeb( comprobante, detalles )
          if ( StringUtils.isNotBlank( comprobante?.idFiscal ) ) {
            comprobante = comprobanteRepository.save( comprobante )
            if ( comprobante?.id ) {
              detalles.each { DetalleComprobante detalle ->
                detalle.idFiscal = comprobante.idFiscal
              }
              detalleComprobanteRepository.save( detalles )
              if ( ultimo?.id ) {
                comprobanteRepository.save( ultimo )
                Reimpresion reimpresion = new Reimpresion(
                    nota: 'Fa',
                    idNota: venta.id,
                    idEmpleado: comprobante.idEmpleado,
                    factura: comprobante.idFiscal
                )
                reimpresionRepository.save( reimpresion )
              }
              log.debug( "comprobante registrado con idFiscal: ${comprobante?.idFiscal}" )
              return comprobante
            } else {
              throw new Exception( 'error al guardar comprobante' )
            }
          } else {
            log.error( '[ComprobanteServiceImpl]registrarComprobante: no se obtiene idFiscal valido' )
          }
        } catch ( ex ) {
          log.error( 'no se registra comprobante fiscal, ocurrio un error', ex )
        }
      } else {
        log.warn( 'no se registra comprobante fiscal, no existe notaVenta' )
      }
    } else {
      log.warn( 'no se registra comprobante fiscal, parametros invalidos' )
    }
    return null
  }

  private File descargarArchivo( URL url, File archivo ) {
    log.info( "descargando archivo de: ${url} a: ${archivo?.path}" )
    if ( url && StringUtils.isNotBlank( archivo?.path ) ) {
      if ( archivo?.parentFile?.canWrite() ) {
        try {
          archivo.exists() ? archivo.delete() : null
          url.withInputStream { BufferedInputStream is ->
            archivo.withOutputStream { BufferedOutputStream os ->
              os << is
            }
          }
          log.debug( "escribe archivo ${archivo.path} de ${archivo.size()} bytes" )
          return archivo
        } catch ( ex ) {
          log.error( 'error al escribir archivo', ex )
        }
      } else {
        log.warn( 'no se puede descargar archivo, no se puede escribir en ruta' )
      }
    } else {
      log.warn( 'no se puede descargar archivo, parametros invalidos' )
    }
    return null
  }

  @Override
  List<File> descargarArchivosComprobante( String idFiscal ) {
    log.info( "descargando archivos comprobante con idFiscal: ${idFiscal}" )
    if ( StringUtils.isNotBlank( idFiscal ) ) {
      Comprobante comprobante = obtenerComprobante( idFiscal )
      if ( comprobante?.id ) {
        Parametro parametroRuta = parametroRepository.findOne( TipoParametro.RUTA_COMPROBANTES.value )
        try {
          File dirFacturas = new File( parametroRuta?.valor )
          dirFacturas.exists() ?: dirFacturas.mkdir()
          File dirReceptor = new File( dirFacturas, comprobante.rfc )
          dirReceptor.exists() ?: dirReceptor.mkdir()
          String nombreDirComprobante = "${comprobante.idFiscal}-${comprobante.ticket}"
          File dirComprobante = new File( dirReceptor, nombreDirComprobante )
          dirComprobante.exists() ?: dirComprobante.mkdir()
          File xmlComprobante = new File( dirComprobante, "${comprobante.ticket}.xml" )
          xmlComprobante.exists() ?: descargarArchivo( comprobante.xml?.toURL(), xmlComprobante )
          File pdfComprobante = new File( dirComprobante, "${comprobante.ticket}.pdf" )
          pdfComprobante.exists() ?: descargarArchivo( comprobante.url?.toURL(), pdfComprobante )
          return [ xmlComprobante, pdfComprobante ]
        } catch ( ex ) {
          log.error( 'error al descargar archivos', ex )
        }
      } else {
        log.warn( 'no se descargan archivos comprobante, comprobante no existe' )
      }
    } else {
      log.warn( 'no se descargan archivos comprobante, parametro invalido' )
    }
    return [ ]
  }
}
