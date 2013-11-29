package mx.lux.pos.service.impl

import mx.lux.pos.model.Moneda
import mx.lux.pos.model.MonedaDetalle
import mx.lux.pos.repository.MonedaDetalleRepository
import mx.lux.pos.repository.MonedaRepository
import mx.lux.pos.service.MonedaExtranjeraService
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.apache.commons.lang3.math.NumberUtils
import mx.lux.pos.repository.impl.RepositoryFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.text.DateFormat
import mx.lux.pos.service.business.Registry

@Service
@Transactional( readOnly = true )
class MonedaExtranjeraServiceImpl implements MonedaExtranjeraService {

  private static DateFormat df = new SimpleDateFormat("dd/MM/yyyy")
  private static MonedaExtranjeraService instance

  private Logger logger = LoggerFactory.getLogger( this.getClass() )

  @Autowired
  MonedaRepository currMaster

  @Autowired
  MonedaDetalleRepository currRateDetail

  MonedaExtranjeraServiceImpl( ) {
    instance = this
  }

  static MonedaExtranjeraService getInstance() {
    return instance
  }

  Moneda findCurrency( String pIdMoneda ) {
    Moneda curr
    String idMoneda = StringUtils.trimToEmpty( pIdMoneda ).toUpperCase()
    curr = currMaster.findOne( idMoneda )
    return curr
  }

  MonedaDetalle findActiveRate( String pIdMoneda ) {
    return findActiveRate( pIdMoneda, new Date() )
  }

  MonedaDetalle findActiveRate( String pIdMoneda, Date pDate ) {
    MonedaDetalle rate = null
    Date date = DateUtils.addDays( DateUtils.truncate( pDate, Calendar.DATE ), 1 )
    String idMoneda = StringUtils.trimToEmpty( pIdMoneda ).toUpperCase()
    List<MonedaDetalle> rates = currRateDetail.findByIdMonedaAndFechaActivaBefore( idMoneda, date )
    if ( rates.size() > 0 ) {
      Collections.sort( rates )
      rate = rates[ 0 ]
    }
    return rate
  }

    void register(String pIdMoneda, Double pRate) {
        this.register( new Date(), pIdMoneda, pRate )
    }

    void register(Date pEffectiveFrom, String pIdMoneda, Double pRate) {
        MonedaDetalle fxrate = findActiveRate( pIdMoneda, pEffectiveFrom )
        Date effDate = DateUtils.truncate( pEffectiveFrom, Calendar.DATE )
        if ( fxrate != null ) {
          this.logger.debug( String.format( "%s.equals(%s):%b", df.format(effDate), df.format(fxrate.fechaActiva),
                  effDate.equals(fxrate.fechaActiva)))
          if ( effDate.equals( fxrate.fechaActiva ) ) {
              this.remove( fxrate )
          }
        }
        fxrate = new MonedaDetalle()
        fxrate.fechaActiva = DateUtils.truncate( pEffectiveFrom, Calendar.DATE )
        fxrate.idMoneda = StringUtils.trimToEmpty(pIdMoneda).toUpperCase()
        fxrate.tipoCambio = NumberUtils.createBigDecimal( String.format( "%.5f", pRate ))
        this.write( fxrate )
    }

    @Transactional
    void remove( MonedaDetalle pRate ) {
        if ( pRate != null ) {
            RepositoryFactory.fxRates.delete( pRate.numSerial )
            RepositoryFactory.fxRates.flush( )
        }
    }

    @Transactional
    void write( MonedaDetalle pRate ) {
        if ( pRate != null ) {
            try {
                RepositoryFactory.fxRates.saveAndFlush( pRate )
            } catch (Exception e) {
                this.logger.error( "Error al guardar tipo de cambio", e )
            }
        }
    }

    Boolean requestUsdDisplayed( ) {
        return Registry.isUsdDisplayEnabled()
    }
}
