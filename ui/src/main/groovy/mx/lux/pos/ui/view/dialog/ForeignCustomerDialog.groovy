package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.CustomerController
import mx.lux.pos.ui.view.verifier.NotEmptyVerifier
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.StringUtils

import java.awt.Component
import java.awt.event.ActionEvent

import mx.lux.pos.ui.model.*

import javax.swing.*

class ForeignCustomerDialog extends JDialog {


  private static final Integer EDAD_DEFAULT = 25
  private static final Integer EDAD_MINIMA = 10
  private static final Integer EDAD_MAXIMA = 100

  private SwingBuilder sb
  private Customer customer
  private JTextField firstName
  private JTextField fathersName
  private JTextField mothersName
  private JTextField city
  private JComboBox country
  private JTextField emailUser
  private JSpinner age
  private JComboBox gender
  private JComboBox emailDomain
  private JComboBox stateField
  private Contact tmpEmailContact
  private List<String> domains
  private List<String> states
  private List<String> lstPaises = new ArrayList<String>()
  private boolean domestic
  private boolean edit
  Boolean canceled
  private JButton btnCancel

  ForeignCustomerDialog( Component parent, final Customer customer, boolean editar ) {
    edit = editar
    this.customer = new Customer()
    lstPaises = CustomerController.countries()
    sb = new SwingBuilder()
    domains = CustomerController.findAllCustomersDomains()
    tmpEmailContact = new Contact( type: ContactType.EMAIL )
    states = CustomerController.findAllStates()
    initialize( customer )
    buildUI( parent )
    doBindings()
  }

  Customer getCustomer( ) {
    return customer
  }

  private void initialize( Customer customer ) {
    domestic = CustomerType.DOMESTIC.equals( customer?.type )
    this.customer.type = customer?.type
    this.customer.rfc = customer?.type?.rfc
    this.customer.gender = GenderType.MALE
    this.customer.address = new Address()
    if ( customer?.id ) {
      this.customer.id = customer.id
      this.customer.name = customer.name
      this.customer.fathersName = customer.fathersName
      this.customer.mothersName = customer.mothersName
      this.customer.gender = customer.gender ?: this.customer.gender
      this.customer.age = customer.age
      if ( customer.address ) {
        this.customer.address = new Address(
            city: customer.address.city,
            country: customer.address.country
        )
      }
      if ( customer.contacts?.any() ) {
        customer.contacts.each { Contact tmp ->
          this.customer.contacts.add( tmp )
          switch ( tmp.type ) {
            case ContactType.EMAIL:
              tmpEmailContact = tmp
              List<String> tokens = StringUtils.splitPreserveAllTokens( tmp.primary, '@' )
              if ( tokens?.size() == 2 ) {
                tmpEmailContact.primary = tokens.get( 0 )
                tmpEmailContact.extra = tokens.get( 1 )
              }
              break
          }
        }
      }
    }
  }

  private void buildUI( Component parent ) {
    sb.dialog( this,
        title: "${customer?.id ? 'Editar' : 'Agregar'} Cliente ${domestic ? 'Estadist\u00edca' : 'Extranjero' }",
        location: parent.location,
        resizable: false,
        modal: true,
        pack: true,
        layout: new MigLayout( 'fill,wrap', '[fill]' )
    ) {
      panel( border: titledBorder( 'Datos Generales' ), layout: new MigLayout( 'wrap 4', '[][fill,100!][][fill]' ) ) {
        label( 'Nombre' )
        firstName = textField( document: new UpperCaseDocument(), inputVerifier: new NotEmptyVerifier(), constraints: 'span' )

        label( 'Apellido Paterno' )
        fathersName = textField( document: new UpperCaseDocument(), inputVerifier: new NotEmptyVerifier(), constraints: 'span' )

        label( 'Apellido Materno' )
        mothersName = textField( document: new UpperCaseDocument(), constraints: 'span' )

        label( 'Edad' )
        age = spinner( model: spinnerNumberModel( value: EDAD_DEFAULT, minimum: EDAD_MINIMA, maximum: EDAD_MAXIMA ) )

        label( 'Sexo' )
        gender = comboBox( items: GenderType.values() )

        label( 'E-Mail' )
        emailUser = textField()
        label( '@' )
        emailDomain = comboBox( items: domains, editable: true )
      }

      panel( border: titledBorder( 'Dirección' ), layout: new MigLayout( 'wrap 2', '[][fill,200!]' ) ) {
        label( 'Ciudad' )
        city = textField( document: new UpperCaseDocument() )

        label( 'Estado', visible: domestic, constraints: 'hidemode 3' )
        stateField = comboBox( items: states, visible: domestic, constraints: 'hidemode 3' )

        label( 'Pais', visible: !domestic, constraints: 'hidemode 3' )
        country = comboBox( selectedItem: "${domestic ? 'MEXICO' : lstPaises.get(0)}",
            items: lstPaises,
            visible: !domestic,
            constraints: 'hidemode 3', editable: !domestic
        )
      }

      panel( layout: new MigLayout( 'right', '[fill,100!]' ) ) {
        button( 'Borrar', visible: customer?.id ? true : false, actionPerformed: doDelete )
        button( 'Aplicar', actionPerformed: doSubmit )
        button( 'Limpiar', visible: customer?.id ? false : true, actionPerformed: doClear )
        btnCancel = button( 'Cancelar', defaultButton: true, actionPerformed: doCancel )
      }
    }

    age.editor = new JSpinner.NumberEditor( age as JSpinner )
  }

  private void doBindings( ) {
    sb.build {
      bean( firstName, text: bind( source: customer, sourceProperty: 'name', mutual: true ) )
      bean( fathersName, text: bind( source: customer, sourceProperty: 'fathersName', mutual: true ) )
      bean( mothersName, text: bind( source: customer, sourceProperty: 'mothersName', mutual: true ) )
      bean( age, value: bind( source: customer, sourceProperty: 'age', mutual: true ) )
      bean( gender, selectedItem: bind( source: customer, sourceProperty: 'gender', mutual: true ) )
      bean( emailUser, text: bind( source: tmpEmailContact, sourceProperty: 'primary', mutual: true ) )
      bean( emailDomain, selectedItem: bind( source: tmpEmailContact, sourceProperty: 'extra', mutual: true ) )
      bean( city, text: bind( source: customer.address, sourceProperty: 'city', mutual: true ) )
      bean( stateField, selectedItem: bind( source: customer.address, sourceProperty: 'state', mutual: true ) )
      bean( country, selectedItem: bind( source: customer.address, sourceProperty: 'country', mutual: true ) )
    }
  }

  private def doDelete = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    dispose()
  }

  private boolean isValidInput( ) {
    if ( StringUtils.isNotBlank( firstName.text ) ) {
      return true
    } else {
      sb.optionPane(
          message: 'Se debe registrar el nombre y pa\u00eds',
          messageType: JOptionPane.ERROR_MESSAGE
      ).createDialog( this, 'No se puede registrar la venta' )
          .show()
    }
    return false
  }

  private def doSubmit = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    if ( StringUtils.isNotBlank( tmpEmailContact?.primary ) ) {
      String mail = "${tmpEmailContact.primary}@${tmpEmailContact.extra}"
      tmpEmailContact.primary = mail
      customer.contacts.add( tmpEmailContact )
    }
    if ( isValidInput() ) {
        if( country.selectedItem != null ){
            CustomerController.saveCountries( country.selectedItem.toString().trim().toUpperCase() )
        }
      Customer tmpCustomer = CustomerController.addCustomer( customer, edit )
      if ( tmpCustomer?.id ) {
        customer = tmpCustomer
        dispose()
      }
    }
    source.enabled = true
  }

  private def doCancel = {
    canceled = true
    dispose()
  }

  private def doClear = {
    firstName.text = null
    fathersName.text = null
    mothersName.text = null
    age.value = 1
    gender.selectedItem = GenderType.MALE
    emailUser.text = null
    emailDomain.selectedItem = null
    city.text = null
    stateField.selectedItem = null
    country.selectedItem = null
  }
}
