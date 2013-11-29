package mx.lux.pos.repository.custom

import mx.lux.pos.model.Pago

interface PagoRepositoryCustom {

  List<Pago> findBy_Fecha( Date fecha )

  List<Pago> findBy_Fecha_IdFactura_DescripcionTerminal( Date fecha, String descripcionTerminal, String plan )

  Pago findBy_Id( Integer id )

}
