package mx.lux.pos.service.impl

import com.mysema.query.BooleanBuilder
import com.mysema.query.types.Predicate
import groovy.util.logging.Slf4j
import mx.lux.pos.repository.ArticuloRepository
import mx.lux.pos.repository.DiferenciaRepository
import mx.lux.pos.repository.PrecioRepository
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.service.business.Registry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

import mx.lux.pos.model.*

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.regex.Pattern
import mx.lux.pos.util.CustomDateUtils
import org.springframework.ui.velocity.VelocityEngineUtils
import org.apache.velocity.app.VelocityEngine
import java.text.NumberFormat

@Slf4j
@Service( 'articuloService' )
@Transactional( readOnly = true )
class ArticuloServiceImpl implements ArticuloService {

  @Resource
  private ArticuloRepository articuloRepository

  @Resource
  private PrecioRepository precioRepository

  @Resource
  private DiferenciaRepository diferenciaRepository

  @Resource
  private VelocityEngine velocityEngine

  private static final Integer CANT_CARACTEREZ_SKU = 6
  private static final Integer CANT_CARACTEREZ_COD_BAR = 15
  private static final Integer CANT_ARTICULOS_ENVIAR = 500

  private Articulo establecerPrecio( Articulo articulo ) {
    // log.debug( "estableciendo precio para el articulo id: ${articulo?.id} articulo: ${articulo?.articulo}" )
    if ( articulo?.id ) {
      // log.debug( "obteniendo lista de precios" )
      List<Precio> precios = precioRepository.findByArticulo( articulo.articulo )
      if ( precios?.any() ) {
        Precio precioLista = precios.find { Precio tmp ->
          'L'.equalsIgnoreCase( tmp?.lista )
        }
        BigDecimal lista = precioLista?.precio ?: 0
        Precio precioOferta = precios.find { Precio tmp ->
          'O'.equalsIgnoreCase( tmp?.lista )
        }
        BigDecimal oferta = precioOferta?.precio ?: 0
        // log.debug( "precio lista valor: ${precioLista?.precio} id: ${precioLista?.id} lista: ${precioLista?.lista}" )
        // log.debug( "precio oferta valor: ${precioOferta?.precio} id: ${precioOferta?.id} lista: ${precioOferta?.lista}" )
        articulo.precio = oferta && ( oferta < lista ) ? oferta : lista
        articulo.precioO = oferta && ( oferta < lista ) ? oferta : 0
        // log.debug( "se establece precio ${articulo?.precio} para articulo id: ${articulo?.id}" )
      }
    }
    // log.debug( "Return articulo:: ${articulo.descripcion} " )
    return articulo
  }

  @Override
  Articulo obtenerArticulo( Integer id ) {
    return obtenerArticulo( id, true )
  }

  @Override
  Articulo obtenerArticulo( Integer id, boolean incluyePrecio ) {
    log.info( "obteniendo articulo con id: ${id} incluye precio: ${incluyePrecio}" )
    Articulo articulo = articuloRepository.findOne( id ?: 0 )
    if ( articulo?.id && incluyePrecio ) {
      return establecerPrecio( articulo )
    }
    return articulo
  }

  @Override
  List<Articulo> listarArticulosPorCodigo( String articulo ) {
    return listarArticulosPorCodigo( articulo, true )
  }

  @Override
  List<Articulo> listarArticulosPorCodigo( String articulo, boolean incluyePrecio ) {
    log.info( "listando articulos con articulo: ${articulo} incluye precio: ${incluyePrecio}" )
    Predicate predicate = QArticulo.articulo1.articulo.equalsIgnoreCase( articulo )
    List<Articulo> resultados = articuloRepository.findAll( predicate, QArticulo.articulo1.codigoColor.asc() ) as List<Articulo>
    if ( incluyePrecio ) {
      return resultados?.collect { Articulo tmp ->
        establecerPrecio( tmp )
      }
    }
    return resultados
  }

  @Override
  List<Articulo> listarArticulosPorCodigoSimilar( String articulo ) {
    return listarArticulosPorCodigoSimilar( articulo, true )
  }

  @Override
  List<Articulo> listarArticulosPorCodigoSimilar( String articulo, boolean incluyePrecio ) {
    log.info( "listando articulos con articulo similar: ${articulo}" )
    Predicate predicate = QArticulo.articulo1.articulo.startsWithIgnoreCase( articulo )
    List<Articulo> resultados = articuloRepository.findAll( predicate, QArticulo.articulo1.articulo.asc() ) as List<Articulo>
    if ( incluyePrecio ) {
      return resultados?.collect { Articulo tmp ->
        establecerPrecio( tmp )
      }
    }
    return resultados
  }

  @Override
  Integer obtenerExistencia( Integer id ) {
    Articulo articulo = obtenerArticulo( id, false )
    return articulo?.cantExistencia ?: 0
  }

  @Override
  Boolean validarArticulo( Integer id ) {
    return articuloRepository.exists( id )
  }

  @Override
  @Transactional
  Boolean registrarArticulo( Articulo pArticulo ) {
    if ( pArticulo != null ) {
      pArticulo = articuloRepository.save( pArticulo )
      return pArticulo?.id > 0
    }
    return false
  }

  @Override
  @Transactional
  Boolean registrarListaArticulos( List<Articulo> pListaArticulo ) {
    boolean registrado = false
    if ( ( pListaArticulo != null ) && ( pListaArticulo.size() > 0 ) ) {
      articuloRepository.save( pListaArticulo )
      registrado = true
    }
    articuloRepository.flush()
    return registrado
  }

  @Override
  Boolean esInventariable( Integer id ) {
    boolean inventariable = false
    Articulo articulo = obtenerArticulo( id, false )
    if ( articulo != null ) {
      Generico genre = RepositoryFactory.genres.findOne( articulo.idGenerico )
      inventariable = genre?.inventariable
    }
    return inventariable
  }

  @Override
  List<Articulo> obtenerListaArticulosPorId( List<Integer> pListaId ) {
    return articuloRepository.findByIdIn( pListaId )
  }

  @Override
  Boolean actualizarArticulosConSombra( Collection<ArticuloSombra> pShadowSet ) {
    log.debug( String.format( "[Service] Actualizar articulos: %,d en lista", pShadowSet.size() ) )
    Boolean actualizado = false
    try {
      List<Articulo> updatedList = new ArrayList<Articulo>()
      for ( ArticuloSombra shadow in pShadowSet ) {
        Articulo part = articuloRepository.findOne( shadow.id_articulo )
        if ( part != null ) {
          shadow.updateArticulo( part )
        } else {
          if ( shadow.isValidForNew() ) {
            part = shadow.createArticulo()
          }
        }
        if ( part != null ) {
          updatedList.add( part )
        }
      }
      actualizado = registrarListaArticulos( updatedList )
    } catch ( Exception e ) {
      log.error( "[Service] ERROR! Actualizando articulos", e )
    }
    return actualizado
  }

  Collection<Generico> listarGenericos( Collection<String> pIdGenericoSet ) {
    log.debug( "Listar Genericos(%d Ids)", pIdGenericoSet.size() )
    Collection<Generico> lista = new ArrayList<Generico>()
    if ( pIdGenericoSet.size() > 0 ) {
      lista = RepositoryFactory.genres.findByIdIn( pIdGenericoSet )
    }
    return lista
  }

  List<Articulo> findArticuloyColor( String articulo, String color ) {
    log.debug( "findArticuloyColor()" )

    List<Articulo> lstArticulos = new ArrayList<Articulo>()
    List<Articulo> lstArticulos2 = new ArrayList<Articulo>()
    Integer idArticulo = 0

     if ( !articulo.contains( "-" ) && !articulo.contains( "/" ) && !articulo.contains( "+" ) && !articulo.contains( "." ) && articulo.isNumber() ) {
      try{
        if( articulo.length() > CANT_CARACTEREZ_COD_BAR ){
          articulo = articulo.substring( 1 )
        }
          if( articulo.length() > CANT_CARACTEREZ_SKU ){
            idArticulo = Integer.parseInt( articulo.substring( 0, CANT_CARACTEREZ_SKU ) )
          } else {
            idArticulo = Integer.parseInt( articulo )
          }
      }catch ( Exception e ){
        log.error( "No se introdujo el SKU del articulo", e  )
      }
    }

    QArticulo art = QArticulo.articulo1
    lstArticulos2 = articuloRepository.findAll( art.id.eq( idArticulo ).or( art.articulo.eq( articulo ) ) ) as List
    if ( lstArticulos2.size() == 0 || lstArticulos2.size() > 1 ) {
      log.debug( "if de Articulos" )
      BooleanBuilder colour = new BooleanBuilder()
      if ( color.length() == 0 ) {
        colour.and( art.codigoColor.isNull() )
      } else {
        colour.and( art.codigoColor.eq( color ) )
      }
      lstArticulos2 = articuloRepository.findAll( art.id.eq( idArticulo ).or( art.articulo.eq( articulo ) ).and( colour ) ) as List
    }
    if ( lstArticulos2.size() > 0 ) {
      lstArticulos = lstArticulos2
    }

    return lstArticulos
  }


  String obtenerListaGenericosPrecioVariable( ) {
    return Registry.getManualPriceTypeList()
  }

  Boolean useShortItemDescription( ) {
    return Registry.isShortDescription()
  }


  Boolean generarArchivoInventario( ){
    log.debug( "generarArchivoInventarioFisico( )" )

    Parametro ubicacion = Registry.find( TipoParametro.RUTA_POR_ENVIAR )
    Parametro sucursal = Registry.find( TipoParametro.ID_SUCURSAL )
    String nombreFichero = "${ String.format("%02d", NumberFormat.getInstance().parse(sucursal.valor)) }.${ CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ) }.${ CustomDateUtils.format( new Date(), 'HHmm' ) }.inv"
    log.info( "Generando archivo ${ nombreFichero }" )
    QArticulo articulo = QArticulo.articulo1
    List<Articulo> lstArticulos = articuloRepository.findAll( articulo.cantExistencia.ne( 0 ).and(articulo.cantExistencia.isNotNull()), articulo.id.asc() )
    def datos = [
        articulos:lstArticulos
    ]
    Boolean generado = true
    try{
      String fichero = "${ ubicacion.valor }/${ nombreFichero }"
      log.debug( "Generando Fichero: ${ fichero }" )
      log.debug( "Plantilla: fichero-inv.vm" )
      File file = new File( fichero )
      if ( file.exists() ) { file.delete() } // Borramos el fichero si ya existe para crearlo de nuevo
      log.debug( 'Creando Writer' )
      FileWriter writer = new FileWriter( file )
      datos.writer = writer
      log.debug( 'Merge template' )
      VelocityEngineUtils.mergeTemplate( velocityEngine, "template/fichero-inv.vm", "ASCII", datos, writer )
      log.debug( 'Writer close' )
      writer.close()

    }catch(Exception e){
      log.error( "Error al generar archivo de inventario", e )
      generado = false
    }
    return generado
  }



  Boolean enviarInventario( ){
    log.debug("enviarInventario( )")
    DateFormat df = new SimpleDateFormat( "dd-MM-yyyy" )
    Integer idSuc = Registry.currentSite
    //String urlEnviaInv = Registry.URLSendInventory
    QArticulo articulo = QArticulo.articulo1
    List<Articulo> lstArticulos = articuloRepository.findAll( articulo.cantExistencia.ne( 0 ).and(articulo.cantExistencia.isNotNull()), articulo.id.asc() )
    String response = ''
    Integer noVecesEnviar = 0
    Integer cantArticulosEnviar = 0
    Integer num = lstArticulos.size()
    Integer sum = 0
    List<Integer> lstMultiplos = new ArrayList<>()
    for(int a=1; a <= num; a++){
        if(num%a == 0){
            lstMultiplos.add(a)
            sum = sum + a;
        }
    }
    noVecesEnviar = lstMultiplos.get(lstMultiplos.size()-2)
    cantArticulosEnviar = num/noVecesEnviar
    Integer contador = 0
    for(int i = 0; i <= noVecesEnviar; i++){
      String urlEnviaInv = Registry.URLSendInventory
      String valor = idSuc.toString().trim()+"|"+df.format(new Date())+"|"+i.toString()+'|'
      for(int j = 0; j < cantArticulosEnviar; j++){
        if(contador < num){
          valor = valor+lstArticulos.get(contador).id.toString().trim()+'>'+lstArticulos.get(contador).cantExistencia.toString().trim()+'|'
        }
        contador++
      }
      urlEnviaInv += String.format( '?arg=%s', URLEncoder.encode( String.format( '%s', valor ), 'UTF-8' ) )
      try{
          if(i <= noVecesEnviar){
          response = urlEnviaInv.toURL().text
          response = response?.find( /<XX>\s*(.*)\s*<\/XX>/ ) {m, r -> return r}
          }
      } catch ( Exception e ){
          println e
      }
    }
    log.debug(response)
    return response != null ? response.contains('FOLIO:') : false

  }



  Boolean recibeDiferencias( ){

    Integer idSuc = Registry.currentSite
    String urlRecibeDif = Registry.URLReceivedDifferences
    String response = ''
    try{
      urlRecibeDif += String.format( '?arg=%s', URLEncoder.encode( String.format( '%s', idSuc.toString().trim() ), 'UTF-8' ) )
      log.debug(urlRecibeDif)
      response = urlRecibeDif.toURL().text
      response = response?.find( /<XX>\s*(.*)\s*<\/XX>/ ) {m, r -> return r}
      log.debug( "resultado solicitud: ${response}" )
    } catch ( Exception e ){
        println e
    }
    String[] cadena = response != null ? response.split(/\|/) : ''
    if(cadena.length >= 1){
      diferenciaRepository.deleteAll()
      diferenciaRepository.flush()
      for(int i = 0;i < cadena.length;i++){
        String articulo = cadena[i]
        String[] descArticulo = articulo.split('>')
        Integer idArticulo = NumberFormat.getInstance().parse(descArticulo[0])
        Integer cantFisi = NumberFormat.getInstance().parse(descArticulo[1])
        Integer cantSoi = NumberFormat.getInstance().parse(descArticulo[2])
        Integer dif = NumberFormat.getInstance().parse(descArticulo[3])
        Diferencia diferencia = new Diferencia()
        diferencia.id = idArticulo
        diferencia.cantidadFisico = cantFisi
        diferencia.cantidadSoi = cantSoi
        diferencia.diferencias = dif
        diferenciaRepository.save( diferencia )
        diferenciaRepository.flush()
      }
    }

    return cadena.length > 0
  }


  @Override
  List<Diferencia> obtenerDiferencias(  ) {
      return diferenciaRepository.findAll()
  }


    Boolean generarArchivoInventarioFisico( ){
        log.debug( "generarArchivoInventarioFisico( )" )
        Parametro ubicacion = Registry.find( TipoParametro.RUTA_POR_ENVIAR )
        Parametro sucursal = Registry.find( TipoParametro.ID_SUCURSAL )
        String nombreFichero = "${ String.format("%02d", NumberFormat.getInstance().parse(sucursal.valor)) }.${ CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ) }.${ CustomDateUtils.format( new Date(), 'HHmm' ) }.invf"
        log.info( "Generando archivo ${ nombreFichero }" )
        QArticulo articulo = QArticulo.articulo1
        List<Articulo> lstArticulos = articuloRepository.findAll( articulo.cantExistencia.ne( 0 ).and(articulo.cantExistencia.isNotNull()), articulo.id.asc() )
        def datos = [
                articulos:lstArticulos
        ]
        Boolean generado = true
        try{
            String fichero = "${ ubicacion.valor }/${ nombreFichero }"
            log.debug( "Generando Fichero: ${ fichero }" )
            log.debug( "Plantilla: fichero-inv.vm" )
            File file = new File( fichero )
            if ( file.exists() ) { file.delete() } // Borramos el fichero si ya existe para crearlo de nuevo
            log.debug( 'Creando Writer' )
            FileWriter writer = new FileWriter( file )
            datos.writer = writer
            log.debug( 'Merge template' )
            VelocityEngineUtils.mergeTemplate( velocityEngine, "template/fichero-inv.vm", "ASCII", datos, writer )
            log.debug( 'Writer close' )
            writer.close()

        }catch(Exception e){
            log.error( "Error al generar archivo de inventario", e )
            generado = false
        }
        return generado
    }
}
