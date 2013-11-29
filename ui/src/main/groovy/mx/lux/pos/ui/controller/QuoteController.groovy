package mx.lux.pos.ui.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import mx.lux.pos.ui.view.dialog.InvOhTicketDialog
import mx.lux.pos.ui.view.dialog.*
import mx.lux.pos.model.InvOhSummary
import mx.lux.pos.ui.resources.ServiceManager
import mx.lux.pos.ui.model.Invoice
import org.apache.commons.lang3.StringUtils
import mx.lux.pos.ui.model.User
import mx.lux.pos.ui.model.Session
import mx.lux.pos.ui.model.SessionItem
import mx.lux.pos.model.Comprobante
import mx.lux.pos.ui.model.*
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component
import mx.lux.pos.model.Cotizacion
import mx.lux.pos.service.CotizacionService
import mx.lux.pos.service.ConvenioService
import mx.lux.pos.model.*
import mx.lux.pos.ui.model.adapter.AgreementAdapter

@Slf4j
@Component
class QuoteController {


    private static QuoteDialog quoteDialog
    private static CotizacionService cotizacionService

// Constructor: Implemented as singleton
    protected static QuoteController instance
    protected QuoteController() { }
    static QuoteController getInstance() {
        if ( instance == null ) {
            instance = new QuoteController()
        }
        return instance
    }


  // Requests
  void requestQuote() {
    this.requestQuoteTicket( new QuoteDialog() )
  }

    void requestQuoteTicket( QuoteDialog dialog ){
        if (quoteDialog == null) {
            quoteDialog = new QuoteDialog()
            List<InstitucionIc> convenios = ServiceManager.agreementService.obtenerConvenios(  )
            //quoteDialog.contractList = convenios
        }
        quoteDialog.show()

        if (quoteDialog.printRequested && quoteDialog.codigo1 != null && quoteDialog.precio1 != null) {
            Quote quote = quoteDialog.quote
            //if ( StringUtils.isNotBlank( quote?.articulo ) ) {
            User user = Session.get(SessionItem.USER) as User
            Cotizacion cotizaciones = new Cotizacion()
            cotizaciones.setEmp(user.username)
            cotizaciones.setTitulo(quoteDialog.titulo)
            cotizaciones.setNombre(quoteDialog.cliente)
            cotizaciones.setTel(quoteDialog.telefono)
            cotizaciones.setObservaciones(quoteDialog.observaciones)

            cotizaciones = ServiceManager.registrarCotizacion.registrarCotizacion(cotizaciones)
            CotizaDet cotizaDet = new CotizaDet()

            if ( !quoteDialog.codigo1.empty && !quoteDialog.precio1.empty &&
                    quoteDialog.codigo1 != null && quoteDialog.precio1 != null ) {
                String price = quoteDialog.precio1
                price = price.replace('$',' ');
                price = price.replaceAll(",","");
                //price = price.replace(".", "");
                price = price.trim();
                BigDecimal precio = new BigDecimal(price);

                cotizaDet = new CotizaDet()
                cotizaDet.setId_cotiza(cotizaciones.id)
                cotizaDet.setArticulo(quoteDialog.codigo1)
                //cotizaDet.setIdArticulo(quoteDialog.idArticulo1)
                cotizaDet.setColor(quoteDialog.color1)
                cotizaDet.setPrecioUnit(new BigDecimal( precio ))

                cotizaDet = ServiceManager.registrarCotizacion.registrarCotizaDet(cotizaDet)
                cotizaDet = null
            }

            if ( !quoteDialog.codigo2.empty && !quoteDialog.precio2.empty &&
                    quoteDialog.codigo2 != null && quoteDialog.precio2 != null ) {
                String price2 = quoteDialog.precio2
                price2 = price2.replace('$',' ');
                price2 = price2.replaceAll(",","");
                //price = price.replace(".", "");
                price2 = price2.trim();
                BigDecimal precio2 = new BigDecimal(price2);

                CotizaDet cotizaDet2 = new CotizaDet()
                cotizaDet2.setId_cotiza(cotizaciones.id)
                cotizaDet2.setArticulo(quoteDialog.codigo2)
                //cotizaDet2.setIdArticulo(quoteDialog.idArticulo2)
                cotizaDet2.setColor(quoteDialog.color2)
                cotizaDet2.setPrecioUnit( new BigDecimal(precio2) )

                cotizaDet2 = ServiceManager.registrarCotizacion.registrarCotizaDet(cotizaDet2)
                cotizaDet = null
            }

            if ( !quoteDialog.codigo3.empty && !quoteDialog.precio3.empty &&
                    quoteDialog.codigo3 != null && quoteDialog.precio3 != null ) {
                String price3 = quoteDialog.precio3
                price3 = price3.replace('$',' ');
                price3 = price3.replaceAll(",","");
                //price = price.replace(".", "");
                price3 = price3.trim();
                BigDecimal precio3 = new BigDecimal(price3);
                CotizaDet cotizaDet3 = new CotizaDet()
                cotizaDet3.setId_cotiza(cotizaciones.id)
                cotizaDet3.setArticulo(quoteDialog.codigo3)
                //cotizaDet3.setIdArticulo(quoteDialog.idArticulo3)
                cotizaDet3.setColor(quoteDialog.color3)
                cotizaDet3.setPrecioUnit( new BigDecimal(precio3) )
                cotizaDet3 = ServiceManager.registrarCotizacion.registrarCotizaDet(cotizaDet3)
                cotizaDet = null
            }

            if ( !quoteDialog.codigo4.empty && !quoteDialog.precio4.empty &&
                    quoteDialog.codigo4 != null && quoteDialog.precio4 != null ) {
                String price4 = quoteDialog.precio4
                price4 = price4.replace('$',' ');
                price4 = price4.replaceAll(",","");
                //price = price.replace(".", "");
                price4 = price4.trim();
                BigDecimal precio4 = new BigDecimal(price4);
                CotizaDet cotizaDet4 = new CotizaDet()
                cotizaDet4.setId_cotiza(cotizaciones.id)
                cotizaDet4.setArticulo(quoteDialog.codigo4)
                //cotizaDet4.setIdArticulo(quoteDialog.idArticulo4)
                cotizaDet4.setColor(quoteDialog.color4)
                cotizaDet4.setPrecioUnit( new BigDecimal(precio4) )
                cotizaDet4 = ServiceManager.registrarCotizacion.registrarCotizaDet(cotizaDet4)
                cotizaDet = null
            }

            if ( !quoteDialog.codigo5.empty && !quoteDialog.precio5.empty &&
                    quoteDialog.codigo5 != null && quoteDialog.precio5 != null ) {
                String price5 = quoteDialog.precio5
                price5 = price5.replace('$',' ');
                price5 = price5.replaceAll(",","");
                //price = price.replace(".", "");
                price5 = price5.trim();
                BigDecimal precio5 = new BigDecimal(price5);
                CotizaDet cotizaDet5 = new CotizaDet()
                cotizaDet5.setId_cotiza(cotizaciones.id)
                cotizaDet5.setArticulo(quoteDialog.codigo5)
                //cotizaDet4.setIdArticulo(quoteDialog.idArticulo4)
                cotizaDet5.setColor(quoteDialog.color5)
                cotizaDet5.setPrecioUnit( new BigDecimal(precio5) )
                cotizaDet5 = ServiceManager.registrarCotizacion.registrarCotizaDet(cotizaDet5)
                cotizaDet = null
            }
            boolean totalizar = quoteDialog.getCbxTotalizar()
            boolean convenio
            String convenioDesc = quoteDialog.getConvenio()
          if( convenioDesc == null  ){
            convenio = false
          } else {
            convenio = true
          }
            ServiceManager.ticketService.imprimeCotizacion( cotizaciones, cotizaDet, totalizar, convenio, convenioDesc )
        }
        quoteDialog = null
    }

    static List<InstitucionIc> ObtenerConvenio( String clave ){

        //List<String> lstConvenios = new ArrayList<String>()
        //List<InstitucionIc> lstCon = ServiceManager.convenioService.obtenerConvenios( clave )
        List<InstitucionIc> lstConvenios = findAgreements( clave )
        //String lstConvenios = lstCon.first().inicialesIc
        /*for( InstitucionIc institucion : lstCon ){
                lstConvenios.add( institucion.inicialesIc )
        }*/
        return lstConvenios
        //return lstCon.collect( AgreementItem.toAgreementItem() )
    }

    static List<InstitucionIc> findAgreements( final String clave ){
        return findAllAgreements( clave )
    }

    static List<InstitucionIc> findAllAgreements( final String clave ){
        List<InstitucionIc> items = [ ]

        items = ServiceManager.convenioService.obtenerConvenios( clave ) ?: [ ]
        return items
    }

    static BigDecimal obtenerPrecio( Integer idArticulo, String convenio, BigDecimal precio ){
        BigDecimal precioFinal = BigDecimal.ZERO

        precioFinal = ServiceManager.cotizacionService.obtenerPrecio( idArticulo, convenio, precio )

        return precioFinal
    }

}
