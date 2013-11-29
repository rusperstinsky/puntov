package mx.lux.pos.ui.controller

import groovy.util.logging.Slf4j
import mx.lux.pos.model.Contribuyente
import mx.lux.pos.service.ContribuyenteService
import mx.lux.pos.ui.model.Taxpayer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Slf4j
@Component
class TaxpayerController {

  private static ContribuyenteService contribuyenteService

  @Autowired
  TaxpayerController( ContribuyenteService contribuyenteService ) {
    this.contribuyenteService = contribuyenteService
  }

  static Taxpayer findTaxpayerByRfc( String rfc ) {
    log.info( "obteniendo taxpayer con rfc: ${rfc}" )
    Contribuyente result = contribuyenteService.obtenerContribuyentePorRfc( rfc )
    return Taxpayer.toTaxpayer( result )
  }

  static List<Taxpayer> findTaxpayersLike( String input ) {
    log.info( "buscando taxpayers con rfc similar a: ${input}" )
    List<Contribuyente> results = contribuyenteService.listarContribuyentesPorRfcSimilar( input )
    List<Taxpayer> taxpayers = results?.collect { Contribuyente tmp ->
      Taxpayer.toTaxpayer( tmp )
    }
    return taxpayers?.any() ? taxpayers : [ ]
  }
}
