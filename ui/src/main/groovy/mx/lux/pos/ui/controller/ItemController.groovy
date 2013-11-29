package mx.lux.pos.ui.controller

import groovy.util.logging.Slf4j
import mx.lux.pos.model.Articulo
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.service.TicketService
import mx.lux.pos.ui.model.Differences
import mx.lux.pos.ui.model.Item
import mx.lux.pos.ui.view.dialog.DifferencesDialog
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mx.lux.pos.model.QArticulo
import mx.lux.pos.ui.view.dialog.ImportPartMasterDialog
import javax.swing.JOptionPane
import javax.swing.JDialog
import mx.lux.pos.service.business.Registry

@Slf4j
@Component
class ItemController {

  private static final String MSJ_ARCHIVO_GENERADO = 'El archivo de inventario fue generado correctamente en %s'
  private static final String TXT_ARCHIVO_GENERADO = 'Archivo de Inventario'
  private static final String MSJ_ARCHIVO_NO_GENERADO = 'No se genero correctamente el archivo de inventario'
  private static final String TXT_DIFERENCIAS = 'Diferencias'
  private static final String MSJ_DIFERENCIAS_NO_RECIBIDAS = 'No se recibieron correctamente las diferencias'

    private static final String MSJ_INV_FISICO_NO_ENVIADO = 'No se han podido enviar los datos del inventario'
    private static final String MSJ_INV_FISICO_ENVIADO = 'Se han enviado correctamente los datos del inventario'

  private static ArticuloService articuloService
  private static TicketService ticketService

  @Autowired
  public ItemController( ArticuloService articuloService, TicketService ticketService ) {
    this.articuloService = articuloService
    this.ticketService = ticketService
  }

  static Item findItem( Integer id ) {
    log.debug( "obteniendo articulo con id: ${id}" )
    Item.toItem( articuloService.obtenerArticulo( id ) )
  }

  static List<Item> findItems( String code ) {
    log.debug( "buscando articulos con articulo: ${code}" )
    def results = articuloService.listarArticulosPorCodigo( code )
    results.collect {
      Item.toItem( it )
    }
  }

  static List<Item> findItemsLike( String input ) {
    log.debug( "buscando articulos con articulo similar a: $input" )
    def results = articuloService.listarArticulosPorCodigoSimilar( input )
    results.collect {
      Item.toItem( it )
    }
  }

  static List<Item> findItemsByQuery( final String query ) {
    log.debug( "buscando de articulos con query: $query" )
    if ( StringUtils.isNotBlank( query ) ) {
      List<Articulo> items = findPartsByQuery( query )
      if (items.size() > 0) {
        log.debug( "Items:: ${items.first()?.dump()} " )
        return items?.collect { Item.toItem( it ) }
      }
    }
    return [ ]
  }

  static List<Articulo> findPartsByQuery( final String query ) {
    return findPartsByQuery( query, true )
  }

  static List<Articulo> findPartsByQuery( final String query, Boolean incluyePrecio ) {
    List<Articulo> items = [ ]
    if ( StringUtils.isNotBlank( query ) ) {
      if ( query.integer ) {
        log.debug( "busqueda por id exacto ${query}" )
        Articulo articulo = articuloService.obtenerArticulo( query.toInteger(), incluyePrecio )
        if( articulo != null ){
          items.add( articulo )
        }
      } else {
        def anyMatch = '*'
        def colorMatch = ','
        def typeMatch = '+'
        if ( query.contains( anyMatch ) ) {
          def tokens = query.tokenize( anyMatch )
          def code = tokens?.first() ?: null
          log.debug( "busqueda con codigo similar: ${code}" )
          items = articuloService.listarArticulosPorCodigoSimilar( code, incluyePrecio ) ?: [ ]
        } else {
          def tokens = query.replaceAll( /[+|,]/, '|' ).tokenize( '|' )
          def code = tokens?.first() ?: null
          log.debug( "busqueda con codigo exacto: ${code}" )
          items = articuloService.listarArticulosPorCodigo( code, incluyePrecio ) ?: [ ]
        }
        if ( query.contains( colorMatch ) ) {
          String color = query.find( /\,(\w+)/ ) { m, c -> return c }
          log.debug( "busqueda con color: ${color}" )
          items = items.findAll { it?.codigoColor?.equalsIgnoreCase( color ) }
        }
        if ( query.contains( typeMatch ) ) {
          String type = query.find( /\+(\w+)/ ) { m, t -> return t }
          log.debug( "busqueda con tipo: ${type}" )
          items = items.findAll { it?.idGenerico?.equalsIgnoreCase( type ) }
        }
      }
    }
    return items
  }

    static List<Item> findItemByArticleAndColor( String query, String color  ) {
        log.debug( "buscando de un articulo con query: $query" )
        if ( StringUtils.isNotBlank( query ) ) {

            List<Articulo> items = new ArrayList<Articulo>()
            try{
            items = articuloService.findArticuloyColor( query, color )
            } catch( Exception e ){
                System.out.println( e )
            }
            return items?.collect { Item.toItem( it ) }
        }
        return [ ]
    }

  static String getManualPriceTypeList( ) {
    String list = articuloService.obtenerListaGenericosPrecioVariable()
    log.debug( "Determina la lista de Genericos precio variable: ${ list } " )
    return list
  }


  static void generateInventoryFile( ){
    log.debug( "generateInventoryFile( )" )
    Boolean archGenerado = articuloService.generarArchivoInventario()
    if( archGenerado ){
      JOptionPane.showMessageDialog( new JDialog(), String.format(MSJ_ARCHIVO_GENERADO, Registry.archivePath), TXT_ARCHIVO_GENERADO, JOptionPane.INFORMATION_MESSAGE )
    } else {
      JOptionPane.showMessageDialog( new JDialog(), MSJ_ARCHIVO_NO_GENERADO, TXT_ARCHIVO_GENERADO, JOptionPane.INFORMATION_MESSAGE )
    }
  }


  static void sendInventoryFile( ){
      log.debug( "sendInventoryFile( )" )
      Boolean archGenerado = articuloService.enviarInventario()
      if( archGenerado ){
          JOptionPane.showMessageDialog( new JDialog(), MSJ_INV_FISICO_ENVIADO, TXT_DIFERENCIAS, JOptionPane.INFORMATION_MESSAGE )
      } else {
          JOptionPane.showMessageDialog( new JDialog(), MSJ_INV_FISICO_NO_ENVIADO, TXT_DIFERENCIAS, JOptionPane.INFORMATION_MESSAGE )
      }
  }


  static void receivedDifferencesFile(  ){
      log.debug( "receivedDifferencesFile( )" )
      Boolean recibidos = articuloService.recibeDiferencias( )
      if( recibidos ){
          DifferencesDialog dialog = new DifferencesDialog( )
          dialog.show()
      } else {
          JOptionPane.showMessageDialog( new JDialog(), MSJ_DIFERENCIAS_NO_RECIBIDAS, TXT_DIFERENCIAS, JOptionPane.INFORMATION_MESSAGE )
      }
  }

  static List<Differences> findAllDifferences( ) {
    log.debug('findAllDifferences( )')
    List<Differences> lstDifferences = articuloService.obtenerDiferencias()
    lstDifferences.collect {
        Differences.toDifferences( it )
    }
  }

  static void printDifferences( ){
    ticketService.imprimeDiferencias()
  }


  static void generatePhysicalInventoryFile( ){
      log.debug( "generatePhysicalInventoryFile( )" )
      Boolean archGenerado = articuloService.generarArchivoInventarioFisico()
      if( archGenerado ){
          JOptionPane.showMessageDialog( new JDialog(), String.format(MSJ_ARCHIVO_GENERADO, Registry.archivePath), TXT_ARCHIVO_GENERADO, JOptionPane.INFORMATION_MESSAGE )
      } else {
          JOptionPane.showMessageDialog( new JDialog(), MSJ_ARCHIVO_NO_GENERADO, TXT_ARCHIVO_GENERADO, JOptionPane.INFORMATION_MESSAGE )
      }
  }



  static Boolean isInventoried( Integer idArticulo ){
    log.debug( "isInventoried( )" )
    Boolean esInventariable = true
    Articulo articulo = articuloService.obtenerArticulo( idArticulo )
    if(articulo != null){
      if(!articulo.generico.inventariable){
        esInventariable = false
      }
    }
    return esInventariable
  }



}
