package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.Banco
import mx.lux.pos.model.BancoDeposito
import mx.lux.pos.model.BancoEmisor

@Bindable
@ToString
@EqualsAndHashCode
class Bank {
  Integer id
  String name

  static Bank toBank( Banco banco ) {
    if ( banco?.id ) {
      Bank bank = new Bank()
      bank.id = banco.id
      bank.name = banco.nombre?.toUpperCase()
      return bank
    }
    return null
  }

  static Bank toBank( BancoEmisor banco ) {
    if ( banco?.id ) {
      Bank bank = new Bank()
      bank.id = banco.id
      bank.name = banco.descripcion?.toUpperCase()
      return bank
    }
    return null
  }

  static Bank toBank( BancoDeposito banco ) {
    if ( banco?.id ) {
      Bank bank = new Bank()
      bank.id = banco.id
      bank.name = banco.nombre?.toUpperCase()
      return bank
    }
    return null
  }

  static Banco toBanco( Bank bank ) {
    Banco banco = new Banco()
    if ( bank != null ) {
      banco.id = bank.id
      banco.nombre = bank.name?.toUpperCase()
    }
    banco
  }
}
