package mx.lux.pos.service.business

import mx.lux.pos.model.FxRateAdapter
import mx.lux.pos.model.Moneda
import mx.lux.pos.model.MonedaDetalle
import mx.lux.pos.repository.MonedaDetalleRepository
import mx.lux.pos.repository.MonedaRepository
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.io.FxRateReader
import org.apache.commons.lang3.time.DateUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FxRatesImportTask {

  private Logger logger = LoggerFactory.getLogger( this.getClass() )
  private Integer nUpdated, nRead
  private File inFile
  private FxRateReader reader
  private Integer site

  protected Moneda findOrCreateCurrency( String pIdCurrency ) {
    MonedaRepository catalog = RepositoryFactory.currencyCatalog
    Moneda curr = catalog.findOne( pIdCurrency )
    if ( curr == null ) {
      curr = new Moneda()
      curr.idMoneda = pIdCurrency
      curr.descripcion = String.format( 'Moneda %s', curr.idMoneda )
      catalog.save( curr )
    }
    return curr
  }

  protected MonedaDetalle findOrCreateRate( String pIdCurrency, Date pStartEff ) {
    MonedaDetalleRepository catalog = RepositoryFactory.fxRates
    List<MonedaDetalle> fxrates = catalog.findByIdMonedaAndFechaActiva( pIdCurrency,
        DateUtils.truncate( pStartEff, Calendar.DATE ) )
    MonedaDetalle fxrate
    if ( fxrates.size() == 0 ) {
      fxrate = new MonedaDetalle()
      fxrate.idMoneda = pIdCurrency
      fxrate.fechaActiva = DateUtils.truncate( pStartEff, Calendar.DATE )
      catalog.save( fxrate )
    } else {
      fxrate = fxrates.get( 0 )
    }
    return fxrate
  }

  protected FxRateReader getReader( ) {
    if ( this.reader == null ) {
      this.reader = new FxRateReader( inFile )
    }
    return this.reader
  }

  // Public methods
  Integer getReadCount( ) {
    return this.nRead;
  }

  Integer getUpdatedCount( ) {
    return this.nUpdated;
  }

  void run( ) {
    this.logger.debug( String.format( "[STARTED] Import FxRates (%s)", this.inFile.getName() ) )
    this.nUpdated = 0
    this.nRead = 0
    if ( ( this.inFile != null ) && ( this.inFile.exists() ) ) {
      FxRateAdapter fxRate = this.getReader().read()
      while ( fxRate != null ) {
        this.nRead++
        if ( fxRate.isValid() ) {
          Moneda curr = this.findOrCreateCurrency( fxRate.idCurrency )
          MonedaDetalle currDet = this.findOrCreateRate( fxRate.idCurrency, fxRate.startEff )
          currDet.tipoCambio = fxRate.rate
          RepositoryFactory.fxRates.save( currDet )
          nUpdated++
        }
        fxRate = this.getReader().read()
      }
      this.getReader().close()
      if ( this.nUpdated > 0 ) {
        RepositoryFactory.fxRates.flush()
        RepositoryFactory.currencyCatalog.flush()
      }
    }
    this.logger.debug( String.format( '[FINISHED] Import FxRates  Updated:%,d/%,d', this.nUpdated, this.nRead ) )
  }

  void setInputFile( File pFile ) {
    this.inFile = pFile
  }

}
