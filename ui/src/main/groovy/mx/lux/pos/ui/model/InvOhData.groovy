package mx.lux.pos.ui.model

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.Generico
import mx.lux.pos.model.InvOhDet
import mx.lux.pos.model.InvOhSummary
import mx.lux.pos.ui.resources.ServiceManager
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.NumberFormat

class InvOhData {

  private Logger logger = LoggerFactory.getLogger( this.getClass() )
  
  private List<Articulo> dataset
  private Map<String, Generico> genres
  private InvOhListener listener
  
  InvOhData() {
    logger.debug( "Instantiated" )
    dataset = new ArrayList<Articulo>()
    genres = new TreeMap<String, Generico>( )
  }
  
  // Internal Methods
  protected static String asId( String pId ) {
    return StringUtils.trimToEmpty( pId ).toUpperCase( )
  }
  protected static String asId( String pPartNbr, String pColorId ) {
    return String.format( "%s %s", StringUtils.trimToEmpty( pPartNbr ).toUpperCase( ),
      StringUtils.trimToEmpty( pColorId ).toUpperCase( )).trim( )
  }
  protected Collection<String> getIdGenreList( ) {
    logger.debug( "Get IdGenre List" )
    List<String> idGenreList = new ArrayList<String>( )
    for ( Articulo part : dataset ) {
      String idGenre = StringUtils.trimToEmpty( part.idGenerico ).toUpperCase( ) 
      if ( !idGenreList.contains( idGenre ) ) {
        idGenreList.add( idGenre )
      }
    }
    return idGenreList 
  } 
  
  protected void loadPartList( Collection<Articulo> pParts ) {
    logger.debug( "load Part list" )
    dataset.clear( )
    dataset.addAll( pParts )
  }

  protected void setGenres( Collection<Generico> pGenreList ) {
    logger.debug( String.format( "set Genres: %,d elements", pGenreList.size( ) ) )
    genres.clear( ) 
    for ( Generico genre : pGenreList ) {
      genres.put( StringUtils.trimToEmpty( genre.id ).toUpperCase( ), genre )
    }
    dispatchDataChanged( )
  } 
   
  // Public methods
  Collection<String> getBrandList( String pIdGenre ) {
    logger.debug( String.format( "Get Brand List(%s)", pIdGenre ) )
    String idGenre = asId( pIdGenre )
    List<String> brandList = new ArrayList<String>( )
    for ( Articulo part : dataset ) {
      if ( idGenre.equals( asId( part.idGenerico) ) &&  !brandList.contains( asId( part.marca ) ) ) {
        brandList.add( asId( part.marca ) )
      }
    }
    Collections.sort( brandList )
    return brandList
  }
  
  Collection<Generico> getGenreList( ) {
    logger.debug( "Get Genre List" )
    return genres.values()  
  }
  
  Collection<String> getPartList( String pIdGenre, String pBrand ) {
    logger.debug( "Get Part List" )
    String idGenre = asId( pIdGenre )
    String brand = asId( pBrand )
    List<String> partList = new ArrayList<String>( )
    for ( Articulo part : dataset ) {
      String partNbr = asId( part.articulo, part.codigoColor )
      if ( idGenre.equals( asId( part.idGenerico) ) &&  brand.equals( asId( part.marca) ) 
          && !partList.contains( partNbr ) ) {
        partList.add( partNbr )
      }
    }
    Collections.sort( partList )
    return partList
  }
  
  Integer getQtyOh( String pIdGenre ) {
    logger.debug( String.format( "Get QtyOh( Genre:%s )", pIdGenre ) )
    String idGenre = asId( pIdGenre )
    Integer qty = 0
    for ( Articulo part : dataset ) {
      if ( idGenre.equals( asId( part.idGenerico ) ) ) {
        qty += part.cantExistencia
      }
    }
    return qty
  }
  
  Integer getQtyOh( String pIdGenre, String pBrand ) {
    logger.debug( String.format( "Get QtyOh( Genre:%s, Brand:%s )", pIdGenre, pBrand ) )
    String idGenre = asId( pIdGenre )
    String brand = asId( pBrand ) 
    Integer qty = 0
    for ( Articulo part : dataset ) {
      if ( idGenre.equals( asId( part.idGenerico ) ) && brand.equals( asId( part.marca ) ) ) {
        qty += part.cantExistencia
      }
    }
    return qty
  }
  
  Integer getQtyOh( String pIdGenre, String pBrand, String pPartNbr ) {
    logger.debug( String.format( "Get QtyOh( Genre:%s, Brand:%s, PartNbr:%s )", pIdGenre, pBrand, pPartNbr ) )
    String idGenre = asId( pIdGenre )
    String brand = asId( pBrand ) 
    String partNbr = asId( pPartNbr )
    Integer qty = 0
    for ( Articulo part : dataset ) {
      if ( idGenre.equals( asId( part.idGenerico ) ) && brand.equals( asId( part.marca ) ) 
          && partNbr.equals( asId( part.articulo, part.codigoColor ) ) ) {
        qty += part.cantExistencia
      }
    }
    return qty
  }


  InvOhSummary getSummary( String pIdGenre, String pBrand ) {
    String genre = asId( pIdGenre )
    String brand = asId( pBrand )

    InvOhSummary summary = new InvOhSummary( )
    summary.genre = genre
    summary.brand = brand
    
    if ( genre.length() > 0 ) {
      if ( brand.length() > 0 ) {
        for ( Articulo part : dataset ) {
          if ( genre.equals( asId( part.idGenerico ) ) && brand.equals( asId( part.marca ) ) ) {
            InvOhDet det = new InvOhDet( )
            det.sku = part.id
            det.id = part.articulo
            det.qty = part.cantExistencia
            det.price = part.precio
            summary.lines.add( det )
          }
        }
      } else {
        for ( String b : this.getBrandList( genre ) ) {
          InvOhDet det = new InvOhDet( )
          det.id = b
          det.qty = getQtyOh( genre, b )
          summary.lines.add( det )
        }
      }
    } else {
      for ( String idGenre : this.getIdGenreList( ) ) {
        InvOhDet det = new InvOhDet( )
        Generico g = this.genres.get( idGenre )
        det.id = String.format( "%s:%s", g.id, g.descripcion )
        det.qty = getQtyOh( idGenre )
        summary.lines.add( det )
      }
    }
    
    return summary    
  }
  
  void setInput( Collection<Articulo> pParts ) {
    logger.debug( String.format( "Input: %,d parts", pParts.size( ) ) )
    this.loadPartList( pParts )
    this.setGenres( ServiceManager.partService.listarGenericos( this.idGenreList ) )
  }
  
  // Observable methods
  protected void dispatchDataChanged( ) {
    logger.debug( String.format( "Dispatch genres" ) )
    if ( this.listener != null ) {
      this.listener.notifyDataChanged( )
    }
  } 
  
  void setListener( InvOhListener pListener ) {
    logger.debug( String.format( "Listener: [%s]", pListener.toString( ) ) )
    this.listener = pListener
  }
  
}
