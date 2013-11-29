package mx.lux.pos.ui.assertions

import mx.lux.pos.ui.model.*

class Assert {

  private static final String DATE_TIME_FORMAT = 'dd-MM-yyyy HH:mm'

  static void assertOrderEquals( Order expected, Order actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.bill == actual.bill
      assert expected.comments == actual.comments
      assert expected.status == actual.status
      assert expected.date?.format( DATE_TIME_FORMAT ) == actual.date?.format( DATE_TIME_FORMAT )
      assert expected.branch == actual.branch
      assert expected.customer == actual.customer
      assert expected.total == actual.total
      assert expected.paid == actual.paid
      assert expected.due == actual.due
      expected.items?.each { OrderItem orderItem ->
        assert actual.items?.contains( orderItem )
      }
      expected.payments?.each { Payment payment ->
        assert actual.payments?.contains( payment )
      }
      expected.deals?.each { Deal deal ->
        assert actual.deals?.contains( deal )
      }
    } else {
      assert expected == actual
    }
  }

  static void assertUserEquals( User expected, User actual ) {
    if ( expected != null && actual != null ) {
      assert expected.name == actual.name
      assert expected.fathersName == actual.fathersName
      assert expected.mothersName == actual.mothersName
      assert expected.username == actual.username
      assert expected.password == actual.password
    } else {
      assert expected == actual
    }
  }

  static void assertPaymentEquals( Payment expected, Payment actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.order == actual.order
      assert expected.paymentReference == actual.paymentReference
      assert expected.codeReference == actual.codeReference
      assert expected.username == actual.username
      assert expected.paymentType == actual.paymentType
      assert expected.paymentTypeId == actual.paymentTypeId
      assert expected.terminal == actual.terminal
      assert expected.terminalId == actual.terminalId
      assert expected.plan == actual.plan
      assert expected.planId == actual.planId
      assert expected.issuerBank == actual.issuerBank
      assert expected.issuerBankId == actual.issuerBankId
      assert expected.refundMethod == actual.refundMethod
      assert expected.amount == actual.amount
      assert expected.refund == actual.refund
      assert expected.refundable == actual.refundable
      assert expected.date?.format( DATE_TIME_FORMAT ) == actual.date?.format( DATE_TIME_FORMAT )
    } else {
      assert expected == actual
    }
  }

  static void assertRefundEquals( Refund expected, Refund actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.modificationId == actual.modificationId
      assert expected.paymentId == actual.paymentId
      assert expected.bankId == actual.bankId
      assert expected.paymentTypeId == actual.paymentTypeId
      assert expected.reference == actual.reference
      assert expected.transfer == actual.transfer
      assert expected.type == actual.type
      assert expected.amount == actual.amount
      assert expected.date?.format( DATE_TIME_FORMAT ) == actual.date?.format( DATE_TIME_FORMAT )
    } else {
      assert expected == actual
    }
  }

  static void assertBranchEquals( Branch expected, Branch actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.name == actual.name
      assert expected.address == actual.address
      assert expected.colony == actual.colony
      assert expected.postalCode == actual.postalCode
      assert expected.city == actual.city
      assert expected.telephoneNumbers == actual.telephoneNumbers
    } else {
      assert expected == actual
    }
  }

  static void assertPriceListEquals( PriceList expected, PriceList actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.filename == actual.filename
      assert expected.branch == actual.branch
      assert expected.activated?.format( DATE_TIME_FORMAT ) == actual.activated?.format( DATE_TIME_FORMAT )
      assert expected.autoActivated?.format( DATE_TIME_FORMAT ) == actual.autoActivated?.format( DATE_TIME_FORMAT )
      assert expected.loaded?.format( DATE_TIME_FORMAT ) == actual.loaded?.format( DATE_TIME_FORMAT )
      assert expected.loadType == actual.loadType
    } else {
      assert expected == actual
    }
  }

  static void assertInvoiceEquals( Invoice expected, Invoice actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.invoiceId == actual.invoiceId
      assert expected.rfc == actual.rfc
      assert expected.ticket == actual.ticket
      assert expected.orderId == actual.orderId
      assert expected.bill == actual.bill
      assert expected.type == actual.type
      assert expected.sourceId == actual.sourceId
      assert expected.customerId == actual.customerId
      assert expected.bizName == actual.bizName
      assert expected.status == actual.status
      assert expected.invoiceType == actual.invoiceType
      assert expected.primary == actual.primary
      assert expected.location == actual.location
      assert expected.city == actual.city
      assert expected.state == actual.state
      assert expected.country == actual.country
      assert expected.zipcode == actual.zipcode
      assert expected.amount == actual.amount
      assert expected.email == actual.email
      assert expected.url == actual.url
      assert expected.xml == actual.xml
      assert expected.rx == actual.rx
      assert expected.patient == actual.patient
      assert expected.paymentForm == actual.paymentForm
      assert expected.paymentMethod == actual.paymentMethod
      assert expected.comments == actual.comments
      assert expected.issueDate?.format( DATE_TIME_FORMAT ) == actual.issueDate?.format( DATE_TIME_FORMAT )
    } else {
      assert expected == actual
    }
  }

  static void assertCustomerEquals( Customer expected, Customer actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.name == actual.name
      assert expected.fathersName == actual.fathersName
      assert expected.mothersName == actual.mothersName
      assert expected.title == actual.title
      assert expected.legalEntity == actual.legalEntity
      assert expected.type == actual.type
      assert expected.rfc == actual.rfc
      assert expected.dob?.dateTimeString == actual.dob?.dateTimeString
      assert expected.gender == actual.gender
      assert expected.address == actual.address
      assert expected.contacts == actual.contacts
    } else {
      assert expected == actual
    }
  }

  static void assertTaxpayerEquals( Taxpayer expected, Taxpayer actual ) {
    if ( expected != null && actual != null ) {
      assert expected.id == actual.id
      assert expected.customerId == actual.customerId
      assert expected.rfc == actual.rfc
      assert expected.name == actual.name
      assert expected.primary == actual.primary
      assert expected.location == actual.location
      assert expected.city == actual.city
      assert expected.stateId == actual.stateId
      assert expected.zipcode == actual.zipcode
      assert expected.phone == actual.phone
      assert expected.email == actual.email
      assert expected.date?.format( DATE_TIME_FORMAT ) == actual.date?.format( DATE_TIME_FORMAT )
    } else {
      assert expected == actual
    }
  }
}
