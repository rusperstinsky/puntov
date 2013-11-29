package mx.lux.pos.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.DateFormat
import java.text.SimpleDateFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import mx.lux.pos.repository.AperturaRepository
import mx.lux.pos.service.OpenSalesDayService
import mx.lux.pos.model.Apertura
import org.apache.commons.lang3.time.DateUtils

@Service
@Transactional( readOnly = true )
class OpenSalesDayServiceImpl implements OpenSalesDayService {

    private static DateFormat df = new SimpleDateFormat("dd/MM/yyyy")
    private static OpenSalesDayService instance

    private Logger logger = LoggerFactory.getLogger( this.getClass() )

    @Autowired
    AperturaRepository aperturaLog

    OpenSalesDayServiceImpl( ) {
        instance = this
    }

    static OpenSalesDayService getInstance() {
        return instance
    }

    Apertura findSalesDay( Date pDate ) {
        Date day = DateUtils.truncate( pDate, Calendar.DATE )
        Apertura salesDay = aperturaLog.findOne( day )
        return salesDay
    }

    Apertura register( Date pOpeningForDate, Double pCurrentAmt, Double pCurrentAmtUsd, String pRemarks ) {
        Date effDate = DateUtils.truncate( pOpeningForDate, Calendar.DATE )
        Apertura day = this.findSalesDay( effDate )
        if ( day != null ) {
            if ( effDate.equals( day.fechaApertura ) ) {
                this.remove( day.fechaApertura )
            }
        }
        day = new Apertura()
        day.fechaApertura = effDate
        day.efvoPesos = pCurrentAmt
        day.efvoDolares = pCurrentAmtUsd
        day.observaciones = pRemarks
        this.write( day )

        return day
    }

    @Transactional
    void remove( Date pOpeningForDate ) {
        if ( pOpeningForDate != null ) {
            aperturaLog.delete( pOpeningForDate )
            aperturaLog.flush()
        }
    }

    @Transactional
    Apertura write( Apertura pApertura ) {
        if ( pApertura != null ) {
            try {
                aperturaLog.saveAndFlush( pApertura )
            } catch (Exception e) {
                this.logger.error( String.format( "Error registering Opening for: %s", pApertura.toString() ), e)
            }
        }
        return pApertura
    }

}