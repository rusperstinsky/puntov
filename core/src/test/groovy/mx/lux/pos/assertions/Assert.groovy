package mx.lux.pos.assertions

import mx.lux.pos.model.*

class Assert {

  private static final String DATE_FORMAT = 'dd-MM-yyyy'
  private static final String TIME_FORMAT = 'HH:mm'
  private static final String DATE_TIME_FORMAT = 'dd-MM-yyyy HH:mm'

  static void assertNotaVentaEquals( NotaVenta expected, NotaVenta actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.idEmpleado == actual.idEmpleado
      assert expected.idCliente == actual.idCliente
      assert expected.idConvenio == actual.idConvenio
      assert expected.idRepVenta == actual.idRepVenta
      assert expected.tipoNotaVenta == actual.tipoNotaVenta
      assert expected.fechaRecOrd?.format( DATE_TIME_FORMAT ) == actual.fechaRecOrd?.format( DATE_TIME_FORMAT )
      assert expected.tipoCli == actual.tipoCli
      assert expected.fExpideFactura == actual.fExpideFactura
      assert expected.ventaTotal == actual.ventaTotal
      assert expected.ventaNeta == actual.ventaNeta
      assert expected.sumaPagos == actual.sumaPagos
      assert expected.fechaHoraFactura?.format( DATE_TIME_FORMAT ) == actual.fechaHoraFactura?.format( DATE_TIME_FORMAT )
      assert expected.fechaPrometida?.format( DATE_FORMAT ) == actual.fechaPrometida?.format( DATE_FORMAT )
      assert expected.fechaEntrega?.format( DATE_FORMAT ) == actual.fechaEntrega?.format( DATE_FORMAT )
      assert expected.fArmazonCli == actual.fArmazonCli
      assert expected.por100Descuento == actual.por100Descuento
      assert expected.montoDescuento == actual.montoDescuento
      assert expected.tipoDescuento == actual.tipoDescuento
      assert expected.idEmpleadoDescto == actual.idEmpleadoDescto
      assert expected.fResumenNotasMo == actual.fResumenNotasMo
      assert expected.sFactura == actual.sFactura
      assert expected.numeroOrden == actual.numeroOrden
      assert expected.tipoEntrega == actual.tipoEntrega
      assert expected.observacionesNv == actual.observacionesNv
      assert expected.idSync == actual.idSync
      assert expected.fechaMod?.format( DATE_TIME_FORMAT ) == actual.fechaMod?.format( DATE_TIME_FORMAT )
      assert expected.idMod == actual.idMod
      assert expected.idSucursal == actual.idSucursal
      assert expected.factura == actual.factura
      assert expected.cantLente == actual.cantLente
      assert expected.udf2 == actual.udf2
      assert expected.udf3 == actual.udf3
      assert expected.udf4 == actual.udf4
      assert expected.udf5 == actual.udf5
      assert expected.sucDest == actual.sucDest
      assert expected.tDeduc == actual.tDeduc
      assert expected.receta == actual.receta
      assert expected.empEntrego == actual.empEntrego
      assert expected.lc == actual.lc
      assert expected.horaEntrega?.format( TIME_FORMAT ) == actual.horaEntrega?.format( TIME_FORMAT )
      assert expected.descuento == actual.descuento
      assert expected.polEnt == actual.polEnt
      assert expected.tipoVenta == actual.tipoVenta
      assert expected.poliza == actual.poliza
    } else {
      assert expected == actual
    }
  }

  static void assertDetalleNotaVentaEquals( DetalleNotaVenta expected, DetalleNotaVenta actual ) {
    if ( expected != null && actual != null ) {
      assert expected.idFactura == actual.idFactura
      assert expected.idArticulo == actual.idArticulo
      assert expected.precioUnitFinal == actual.precioUnitFinal
      assert expected.cantidadFac == actual.cantidadFac
    } else {
      assert expected == actual
    }
  }

  static void assertPagoEquals( Pago expected, Pago actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.idFactura == actual.idFactura
      assert expected.idBanco == actual.idBanco
      assert expected.idFormaPago == actual.idFormaPago
      assert expected.tipoPago == actual.tipoPago
      assert expected.referenciaPago == actual.referenciaPago
      assert expected.monto == actual.monto
      assert expected.fecha?.format( DATE_TIME_FORMAT ) == actual.fecha?.format( DATE_TIME_FORMAT )
      assert expected.idEmpleado == actual.idEmpleado
      assert expected.idSync == actual.idSync
      assert expected.fechaModificacion?.format( DATE_TIME_FORMAT ) == actual.fechaModificacion?.format( DATE_TIME_FORMAT )
      assert expected.idMod == actual.idMod
      assert expected.idSucursal == actual.idSucursal
      assert expected.idRecibo == actual.idRecibo
      assert expected.parcialidad == actual.parcialidad
      assert expected.idFPago == actual.idFPago
      assert expected.clave == actual.clave
      assert expected.referenciaClave == actual.referenciaClave
      assert expected.idBancoEmisor == actual.idBancoEmisor
      assert expected.idTerminal == actual.idTerminal
      assert expected.idPlan == actual.idPlan
      assert expected.confirmado == actual.confirmado
      assert expected.porDevolver == actual.porDevolver
    } else {
      assert expected == actual
    }
  }

  static void assertArticuloEquals( Articulo expected, Articulo actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.articulo == actual.articulo
      assert expected.codigoColor == actual.codigoColor
      assert expected.descripcion == actual.descripcion
      assert expected.idGenerico == actual.idGenerico
      assert expected.idGenTipo == actual.idGenTipo
      assert expected.idGenSubtipo == actual.idGenSubtipo
      assert expected.precio == actual.precio
      assert expected.precioO == actual.precioO
      assert expected.sArticulo == actual.sArticulo
      assert expected.idSync == actual.idSync
      assert expected.fechaMod?.format( DATE_TIME_FORMAT ) == actual.fechaMod?.format( DATE_TIME_FORMAT )
      assert expected.idMod == actual.idMod
      assert expected.idSucursal == actual.idSucursal
      assert expected.descripcionColor == actual.descripcionColor
      assert expected.idCb == actual.idCb
      assert expected.idDisenoLente == actual.idDisenoLente
      assert expected.cantExistencia == actual.cantExistencia
    } else {
      assert expected == actual
    }
  }

  static void assertCausaCancelacionEquals( CausaCancelacion expected, CausaCancelacion actual ) {
    if ( expected && actual ) {
      assert expected.id == actual.id
      assert expected.descripcion == actual.descripcion
      assert expected.idSync == actual.idSync
      assert expected.fechaModificacion?.format( DATE_TIME_FORMAT ) == actual.fechaModificacion?.format( DATE_TIME_FORMAT )
      assert expected.idModificacion == actual.idModificacion
      assert expected.idSucursal == actual.idSucursal
    } else {
      assert actual == expected
    }
  }

  static void assertModificacionEquals( Modificacion expected, Modificacion actual ) {
    if ( expected && actual ) {
      assert expected.id == actual.id
      assert expected.idFactura == actual.idFactura
      assert expected.tipo == actual.tipo
      assert expected.fecha?.format( DATE_TIME_FORMAT ) == actual.fecha?.format( DATE_TIME_FORMAT )
      assert expected.idEmpleado == actual.idEmpleado
      assert expected.causa == actual.causa
      assert expected.observaciones == actual.observaciones
    } else {
      assert actual == expected
    }
  }

  static void assertDevolucionEquals( Devolucion expected, Devolucion actual ) {
    if ( expected && actual ) {
      assert expected.id == actual.id
      assert expected.idMod == actual.idMod
      assert expected.idPago == actual.idPago
      assert expected.idFormaPago == actual.idFormaPago
      assert expected.idBanco == actual.idBanco
      assert expected.referencia == actual.referencia
      assert expected.monto == actual.monto
      assert expected.fecha?.format( DATE_TIME_FORMAT ) == actual.fecha?.format( DATE_TIME_FORMAT )
      assert expected.transf == actual.transf
      assert expected.tipo == actual.tipo
    } else {
      assert actual == expected
    }
  }

  static void assertListaPreciosEquals( ListaPrecios expected, ListaPrecios actual ) {
    if ( expected && actual ) {
      assert expected.id == actual.id
      assert expected.filename == actual.filename
      assert expected.tipoCarga == actual.tipoCarga
      assert expected.fechaAct?.format( DATE_TIME_FORMAT ) == actual.fechaAct?.format( DATE_TIME_FORMAT )
      assert expected.fechaActAuto?.format( DATE_TIME_FORMAT ) == actual.fechaActAuto?.format( DATE_TIME_FORMAT )
      assert expected.fechaMod?.format( DATE_TIME_FORMAT ) == actual.fechaMod?.format( DATE_TIME_FORMAT )
      assert expected.fechaCarga?.format( DATE_TIME_FORMAT ) == actual.fechaCarga?.format( DATE_TIME_FORMAT )
    } else {
      assert actual == expected
    }
  }

  static void assertClienteEquals( Cliente expected, Cliente actual ) {
    if ( expected && actual ) {
      assert expected.id == actual.id
      assert expected.idConvenio == actual.idConvenio
      assert expected.titulo == actual.titulo
      assert expected.idOftalmologo == actual.idOftalmologo
      assert expected.idLocalidad == actual.idLocalidad
      assert expected.idEstado == actual.idEstado
      assert expected.fechaAlta?.dateString == actual.fechaAlta?.dateString
      assert expected.tipo == actual.tipo
      assert expected.sexo == actual.sexo
      assert expected.apellidoPaterno == actual.apellidoPaterno
      assert expected.apellidoMaterno == actual.apellidoMaterno
      assert expected.fCasada == actual.fCasada
      assert expected.nombre == actual.nombre
      assert expected.direccion == actual.direccion
      assert expected.colonia == actual.colonia
      assert expected.codigo == actual.codigo
      assert expected.telefonoCasa == actual.telefonoCasa
      assert expected.telefonoTrabajo == actual.telefonoTrabajo
      assert expected.extTrabajo == actual.extTrabajo
      assert expected.telefonoAdicional == actual.telefonoAdicional
      assert expected.extAdicional == actual.extAdicional
      assert expected.email == actual.email
      assert expected.sUsaAnteojos == actual.sUsaAnteojos
      assert expected.avisar == actual.avisar
      assert expected.idAtendio == actual.idAtendio
      assert expected.idSync == actual.idSync
      assert expected.fechaModificado?.dateString == actual.fechaModificado?.dateString
      assert expected.idModificado == actual.idModificado
      assert expected.idSucursal == actual.idSucursal
      assert expected.udf1 == actual.udf1
      assert expected.udf2 == actual.udf2
      assert expected.ori == actual.ori
      assert expected.udf4 == actual.udf4
      assert expected.udf5 == actual.udf5
      assert expected.udf6 == actual.udf6
      assert expected.receta == actual.receta
      assert expected.obs == actual.obs
      assert expected.fechaNacimiento?.dateString == actual.fechaNacimiento?.dateString
      assert expected.cuc == actual.cuc
      assert expected.horaAlta?.timeString == actual.horaAlta?.timeString
      assert expected.finado == actual.finado
      assert expected.fechaImp == actual.fechaImp
      assert expected.modImp == actual.modImp
      assert expected.calif == actual.calif
      assert expected.fechaImpUpdate?.dateString == actual.fechaImpUpdate?.dateString
    } else {
      assert actual == expected
    }
  }

  static void assertComprobanteEquals( Comprobante expected, Comprobante actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.idFiscal == actual.idFiscal
      assert expected.rfc == actual.rfc
      assert expected.ticket == actual.ticket
      assert expected.idEmpleado == actual.idEmpleado
      assert expected.idFactura == actual.idFactura
      assert expected.factura == actual.factura
      assert expected.tipo == actual.tipo
      assert expected.idOrigen == actual.idOrigen
      assert expected.idCliente == actual.idCliente
      assert expected.razon == actual.razon
      assert expected.estatus == actual.estatus
      assert expected.tipoFactura == actual.tipoFactura
      assert expected.nombre == actual.nombre
      assert expected.calle == actual.calle
      assert expected.exterior == actual.exterior
      assert expected.interior == actual.interior
      assert expected.colonia == actual.colonia
      assert expected.localidad == actual.localidad
      assert expected.referencia == actual.referencia
      assert expected.municipio == actual.municipio
      assert expected.estado == actual.estado
      assert expected.pais == actual.pais
      assert expected.codigoPostal == actual.codigoPostal
      assert expected.fechaImpresion?.format( DATE_TIME_FORMAT ) == actual.fechaImpresion?.format( DATE_TIME_FORMAT )
      assert expected.fechaModificacion?.format( DATE_TIME_FORMAT ) == actual.fechaModificacion?.format( DATE_TIME_FORMAT )
      assert expected.importe == actual.importe
      assert expected.email == actual.email
      assert expected.url == actual.url
      assert expected.xml == actual.xml
      assert expected.rx == actual.rx
      assert expected.paciente == actual.paciente
      assert expected.formaPago == actual.formaPago
      assert expected.metodoPago == actual.metodoPago
      assert expected.observaciones == actual.observaciones
    } else {
      assert expected == actual
    }
  }

  static void assertDetalleComprobanteEquals( DetalleComprobante expected, DetalleComprobante actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.idFiscal == actual.idFiscal
      assert expected.idArticulo == actual.idArticulo
      assert expected.articulo == actual.articulo
      assert expected.color == actual.color
      assert expected.idGenerico == actual.idGenerico
      assert expected.descripcion == actual.descripcion
      assert expected.cantidad == actual.cantidad
      assert expected.precioUnitario == actual.precioUnitario
      assert expected.importe == actual.importe
      assert expected.fechaModificacion?.format( DATE_TIME_FORMAT ) == actual.fechaModificacion?.format( DATE_TIME_FORMAT )
    } else {
      assert expected == actual
    }
  }

  static void assertContribuyenteEquals( Contribuyente expected, Contribuyente actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.idCliente == actual.idCliente
      assert expected.rfc == actual.rfc
      assert expected.nombre == actual.nombre
      assert expected.domicilio == actual.domicilio
      assert expected.colonia == actual.colonia
      assert expected.ciudad == actual.ciudad
      assert expected.idEstado == actual.idEstado
      assert expected.codigoPostal == actual.codigoPostal
      assert expected.telefono == actual.telefono
      assert expected.idSync == actual.idSync
      assert expected.fechaModificacion?.format( DATE_TIME_FORMAT ) == actual.fechaModificacion?.format( DATE_TIME_FORMAT )
      assert expected.idSucursal == actual.idSucursal
      assert expected.fechaRegistro?.format( DATE_TIME_FORMAT ) == actual.fechaRegistro?.format( DATE_TIME_FORMAT )
      assert expected.impresion == actual.impresion
      assert expected.email == actual.email
    } else {
      assert expected == actual
    }
  }
}
