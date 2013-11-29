package mx.lux.pos.service

import mx.lux.pos.model.Contribuyente

interface ContribuyenteService {

  Contribuyente obtenerContribuyentePorRfc( String rfc )

  List<Contribuyente> listarContribuyentesPorRfcSimilar( String rfc )

  boolean esRfcValido( String rfc )

  Contribuyente registrarContribuyente( Contribuyente contribuyente )

}
