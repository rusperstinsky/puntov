package mx.lux.pos.service

import mx.lux.pos.model.Cliente
import mx.lux.pos.model.ClientePais
import mx.lux.pos.model.Dominio
import mx.lux.pos.model.Titulo
import mx.lux.pos.model.InstitucionIc

interface ClienteService {

  Cliente obtenerCliente( Integer id )

  List<Cliente> buscarCliente( String nombre, String apellidoPaterno, String apellidoMaterno )

  Cliente agregarCliente( Cliente cliente, boolean editar )

  ClientePais agregarClientePais( ClientePais clientePais )

  Cliente obtenerClientePorDefecto( )

  List<Titulo> listarTitulosClientes( )

  List<Dominio> listarDominiosClientes( )

  //List<InstitucionIc> obtenerConvenios( String convenio )

}
