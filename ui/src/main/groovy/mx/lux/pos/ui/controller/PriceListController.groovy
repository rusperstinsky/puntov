package mx.lux.pos.ui.controller

import groovy.util.logging.Slf4j
import mx.lux.pos.model.Articulo
import mx.lux.pos.model.ListaPrecios
import mx.lux.pos.service.ListaPreciosService
import mx.lux.pos.service.SucursalService
import mx.lux.pos.service.TicketService
import mx.lux.pos.ui.model.Item
import mx.lux.pos.ui.model.PriceList
import mx.lux.pos.ui.model.PriceListLoadType
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mx.lux.pos.model.Parametro
import mx.lux.pos.model.TipoParametro
import mx.lux.pos.model.Sucursal

@Slf4j
@Component
class PriceListController {

  private static ListaPreciosService listaPreciosService
  private static SucursalService sucursalService
  private static TicketService ticketService
  private static String receivePath
  private static String priceListPath
  private static String receivedPath

  @Autowired
  PriceListController( ListaPreciosService listaPreciosService, SucursalService sucursalService, TicketService ticketService ) {
    this.listaPreciosService = listaPreciosService
    this.sucursalService = sucursalService
    this.ticketService = ticketService
    receivePath = listaPreciosService.obtenRutaPorRecibir()
    priceListPath = listaPreciosService.obtenRutaListaPrecios()
    receivedPath = listaPreciosService.obtenRutaRecibidos()
  }

  static void processPendingPriceLists( ) {
    log.info( "procesando listas de precios pendientes de cargar en: ${receivePath}" )
    File folder = new File( receivePath )
    File newFolder = new File( priceListPath )
    if ( folder?.canRead() && newFolder?.canWrite() ) {
      folder.eachFileMatch( ~/4_.+_.+\.LP/ ) { File file ->
        log.debug( "leyendo archivo: ${file.name}" )
        def header = file.readLines()?.first() as String
        def priceList = initializePriceList( header, file.name )
        def branch = '0'
        def sucursal = sucursalService.obtenSucursalActual()
        def branchId = sucursal?.id?.toString()?.padLeft( 4, '0' )
        log.debug( "sucursal: ${branchId}" )
        try{
          if ( branch?.equalsIgnoreCase( '0' ) || branch?.equalsIgnoreCase( branchId ) ) {
            println branch
          def listaPrecios = listaPreciosService.registrarListaPrecios( toListaPrecios( priceList ) )
          if ( listaPrecios?.id ) {
            def newFile = new File( priceListPath, file.name )
            def moved = file.renameTo( newFile )
            log.debug( "renombrando archivo a: ${newFile?.path} - ${moved}" )
          } else {
            log.debug( "no se cargo la lista de precios ${priceList.id}, archivo: ${file.name}" )
          }
        }
      }catch( Exception e ){
         System.out.println( e )
        }
      }
    } else {
      log.warn( "no se procesan listas de precios pendientes de cargar, no existe ruta por recibir" )
    }
  }

  static List<PriceList> getPendingPriceLists( ) {
    log.info( "obteniendo listas de precios en: ${priceListPath}" )
    def folder = new File( priceListPath )
    if ( folder?.exists() ) {
      def list = [ ]
      //folder.eachFileMatch( ~/3_.+_.+_.+\.LP/ ) { file ->
        folder.eachFileMatch( ~/4_.+_.+\.LP/ ) { file ->
        log.debug( "leyendo archivo: ${file.name}" )
        def lines = file.readLines()
        def header = lines?.first() as String
        def priceList = initializePriceList( header, file.name )
        lines.tail().each {
         String line = it.replace( "||", "| |")
         line = line.replace( "||", "| |")
         def elements = line?.split( /\|/ )
            log.debug( "Tamaño de la cadena:: ${elements.size()}" )
          if ( elements?.size() >= 13 ) {
              log.debug( "leyendo linea ${it}" )
            def pli = new Item(
                operation: elements[ 0 ] ?: null,
                name: elements[ 1 ] ?: null,
                type: elements[ 2 ] ?: null,
                genericType: elements[ 3 ] ?: null,
                reference: elements[ 4 ] ?: null,
                priceType: elements[ 5 ] ?: null,
                price: ( elements[ 6 ] ?: null ) as BigDecimal,
                lensDesign: elements[ 7 ] ?: null,
                id: ( elements[ 8 ] ?: null ) as Integer,
                genericSubType: elements[ 9 ] ?: null,
                brand: elements[ 10 ] ?: null,
                typ: elements[ 11 ] ?: null,
                subtype: elements[ 12 ] ?: null
            )
            priceList?.items?.add( pli )
          }
        }
        list.add( priceList )
      }
      return list
    } else {
      log.warn( "no se procesan listas de precios, no existe ruta lista de precios" )
    }
    return [ ]
  }

  private static PriceList initializePriceList( String header, String filename ) {
    log.info( "inicializando lista de precios con header: ${header}, filename: ${filename}" )
    def elements = header?.tokenize( '|' )
    if ( elements?.size() >= 3 && StringUtils.isNotBlank( filename ) ) {
      def format = 'dd/MM/yyyy'
      def priceList = new PriceList(
          id: elements.get( 0 ),
          filename: filename,
          activated: Date.parse( format, elements.get( 1 ) ),
          autoActivated: Date.parse( format, elements.get( 2 ) ),
          branch: '0'
      )
      log.debug( "lista de precios generada: ${priceList}" )
      return priceList
    }
    return null
  }

  static List<Item> validatePriceList( final PriceList priceList ) {
    log.info( "validando lista de precios: ${priceList}" )
    if ( priceList?.id && priceList?.items?.any() ) {
      def listaPrecios = toListaPrecios( priceList )
      def articulos = toListArticulos( priceList.items )
      def results = listaPreciosService.validarListaPrecios( listaPrecios, articulos )
      return results?.collect { Item.toItem( it ) }
    }
    return [ ]
  }

  static void printPriceListLocation( PriceList priceList ) {
    log.info( "imprimiendo ubicacion de lista de precios: ${priceList}" )
    if ( priceList?.id && priceList?.items?.any() ) {
      def listaPrecios = toListaPrecios( priceList )
      def articulos = toListArticulos( priceList.items )
      ticketService.imprimeUbicacionListaPrecios( listaPrecios, articulos )
    }
  }

  static PriceList loadPriceList( PriceList priceList, PriceListLoadType loadType ) {
    log.info( "cargando lista de precios: ${priceList?.id}, tipo de carga: ${loadType}" )
    if ( priceList?.id && priceList?.items?.any() && loadType?.name() ) {
      priceList.loadType = loadType
      def listaPrecios = toListaPrecios( priceList )
      def articulos = toListArticulos( priceList.items )
      listaPrecios = listaPreciosService.cargarListaPrecios( listaPrecios, articulos )
      priceList.loaded = listaPrecios?.fechaCarga
      log.debug( "fecha de carga: ${priceList?.loaded}" )
      if ( listaPrecios?.fechaCarga ) {
        def filename = priceList.filename
        def file = new File( priceListPath, filename )
        log.debug( "archivo de carga: ${filename} en: ${priceListPath} - ${file?.exists()}" )
        def newFile = new File( receivedPath, filename )
        def moved = file.renameTo( newFile )
        log.debug( "renombrando archivo a: ${newFile.path} - ${moved}" )
      }
      return priceList
    }
    return null
  }

  static void loadExpiredPriceList( ) {
    processPendingPriceLists()
    def pending = getPendingPriceLists()
    def expired = pending?.findAll {
      it?.autoActivated?.before( new Date() )
    }
    expired?.each {
      def pl = loadPriceList( it, PriceListLoadType.AUTO )
      if ( pl?.loaded ) {
        ticketService.imprimeCargaListaPrecios( toListaPrecios( pl ) )
      }
    }
  }

  private static ListaPrecios toListaPrecios( PriceList priceList ) {
    new ListaPrecios(
        id: priceList.id,
        filename: priceList.filename,
        tipoCarga: priceList.loadType,
        fechaAct: priceList.activated,
        fechaActAuto: priceList.autoActivated
    )
  }

  private static List<Articulo> toListArticulos( List<Item> items ) {
    items.collect {
      new Articulo(
          id: it?.id,
          articulo: it?.name,
          codigoColor: it?.color,
          descripcionColor: it?.colorDesc,
          descripcion: it?.reference,
          idGenerico: it?.type,
          idGenTipo: it?.genericType,
          idGenSubtipo: it?.genericSubType,
          precio: it.price,
          idDisenoLente: it?.lensDesign,
          operacion: it?.operation,
          tipoPrecio: it?.priceType,
          ubicacion: it?.location,
          marca: it?.brand,
          tipo: it?.typ,
          subtipo: it?.subtype
      )
    }
  }
}
