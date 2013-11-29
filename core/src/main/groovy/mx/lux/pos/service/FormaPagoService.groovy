package mx.lux.pos.service

import mx.lux.pos.model.FormaPago

interface FormaPagoService {

  List<FormaPago> listarFormasPago( )

  List<FormaPago> listarFormasPagoActivas( )

}
