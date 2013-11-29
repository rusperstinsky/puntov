package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.Deposito

@Bindable
@ToString
@EqualsAndHashCode
class Deposit {
  Integer id
  Integer number
  Date closeDate
  Date depositDate
  Date enterDate
  String depositType
  String bank
  String bankId
  String reference
  BigDecimal ammount

  static Deposit toDeposit( Deposito deposito ) {
    if ( deposito?.id ) {
      Deposit deposit = new Deposit(
          id: deposito.id,
          closeDate: deposito.fechaCierre,
          enterDate: deposito.fechaIngreso,
          depositDate: deposito.fechaDeposito,
          number: deposito.numeroDeposito,
          depositType: deposito.tipoDeposito,
          bankId: deposito.idBanco,
          reference: deposito.referencia,
          ammount: deposito.monto
      )
      return deposit
    }
    return null
  }
}
