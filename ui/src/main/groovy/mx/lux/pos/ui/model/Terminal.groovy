package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Bindable
@ToString
@EqualsAndHashCode
class Terminal {
  String id
  String description
  String number
  Branch branch
  Bank bank

  static Terminal toTerminal( mx.lux.pos.model.Terminal terminal ) {
    if ( terminal?.id ) {
      Terminal tmpTerminal = new Terminal(
          id: terminal.id.trim(),
          description: terminal.descripcion,
          number: terminal.numero,
          branch: Branch.toBranch( terminal.sucursal ),
          bank: Bank.toBank( terminal.bancoDeposito )
      )
      return tmpTerminal
    }
    return null
  }
}
