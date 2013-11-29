package mx.lux.pos.service.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import mx.lux.pos.model.NotaVenta
import mx.lux.pos.model.DetalleNotaVenta
import mx.lux.pos.model.Pago
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional( readOnly = true )
class BulkLoaderServiceImpl {

    Logger logger = LoggerFactory.getLogger( this.getClass() )

    // Private Methods

    // Public Methods
    void purge( List<NotaVenta> pOrders ) {
        if ( pOrders.size() ) {
            List<Pago> payments = selectPayments( pOrders )
            if (payments.size() > 0) {
                this.remove( payments )
            }
            List<DetalleNotaVenta> orderLines = selectOrderLines( pOrders )
            if (orderLines.size() > 0) {
                this.remove( orderLines )
            }
            this.remove( pOrders )
        }
    }

    void save( List<NotaVenta> pOrders, List<DetalleNotaVenta> pOrderLines, List<Pago> pPayments ) {
        this.write( pOrders )
        this.write( pOrderLines )
        this.write( pPayments )
    }
}
