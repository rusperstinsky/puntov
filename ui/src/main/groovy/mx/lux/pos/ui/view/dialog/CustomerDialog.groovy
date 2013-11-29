package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.CustomerController
import mx.lux.pos.ui.view.verifier.NotEmptyVerifier
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.StringUtils

import java.awt.Component
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent

import mx.lux.pos.ui.model.*

import javax.swing.*

class CustomerDialog extends JDialog {

  private SwingBuilder sb
  private Customer customer
  private String defaultState
  private List<String> states
  private List<String> domains
  private List<LinkedHashMap<String, String>> locations
  private List<LinkedHashMap<String, Object>> titles
  private Contact tmpHomeContact
  private Contact tmpEmailContact
  private JTextField firstName
  private JTextField fathersName
  private JTextField mothersName
  private JTextField primary
  private JTextField homePhone
  private JTextField email
  private JComboBox salutation
  private JComboBox stateField
  private JComboBox locationField
  private JComboBox gender
  private JComboBox city
  private JComboBox zipcode
  private JComboBox domain
  private JSpinner dob
    private boolean edit

  CustomerDialog( Component parent, final Customer customer, boolean editar ) {
    edit = editar
    sb = new SwingBuilder()
    this.customer = new Customer()
    defaultState = CustomerController.findDefaultState()
    states = CustomerController.findAllStates()
    titles = CustomerController.findAllCustomersTitles()
    domains = CustomerController.findAllCustomersDomains()
    locations = [ ]
    tmpHomeContact = new Contact( type: ContactType.HOME_PHONE )
    tmpEmailContact = new Contact( type: ContactType.EMAIL )
    initialize( customer )
    buildUI( parent )
    doBindings()
  }

  Customer getCustomer( ) {
    return customer
  }

  private void initialize( Customer customer ) {
    this.customer.type = CustomerType.DOMESTIC
    this.customer.rfc = CustomerType.DOMESTIC.rfc
    this.customer.gender = GenderType.MALE
    this.customer.address = new Address( state: defaultState )
    this.customer.contacts = [ ]
    if ( customer?.id ) {
      this.customer.id = customer.id
      this.customer.name = customer.name
      this.customer.fathersName = customer.fathersName
      this.customer.mothersName = customer.mothersName
      this.customer.title = customer.title
      this.customer.legalEntity = customer.legalEntity
      this.customer.rfc = customer.rfc
      this.customer.dob = customer.dob
      this.customer.gender = customer.gender
      if ( customer.address ) {
        this.customer.address = new Address(
            primary: customer.address.primary,
            zipcode: customer.address.zipcode,
            location: customer.address.location,
            city: customer.address.city,
            state: customer.address.state
        )
      }
      if ( customer.contacts?.any() ) {
        customer.contacts.each { Contact tmp ->
          this.customer.contacts.add( tmp )
          switch ( tmp.type ) {
            case ContactType.HOME_PHONE:
              tmpHomeContact = tmp
              break
            case ContactType.EMAIL:
              tmpEmailContact = tmp
              List<String> emailTokens = tmp.primary?.tokenize( '@' )
              tmpEmailContact.primary = emailTokens?.first()
              tmpEmailContact.extra = emailTokens?.last()
              break
          }
        }
      }
    }
  }

  private void buildUI( Component parent ) {
    sb.dialog( this,
        title: "${customer?.id ? 'Editar' : 'Agregar'} Cliente Nacional",
        location: parent.location,
        resizable: false,
        modal: true,
        pack: true,
        layout: new MigLayout( 'fill,wrap', '[fill]' )
    ) {
      panel( border: titledBorder( 'Datos Generales' ), layout: new MigLayout( 'wrap 4', '[][fill,180!][][fill]' ) ) {
        label( 'Saludo' )
        salutation = comboBox( items: titles*.title, itemStateChanged: titleChanged )

        label( 'Sexo' )
        gender = comboBox( items: GenderType.values() )

        label( 'Nombre' )
        firstName = textField( document: new UpperCaseDocument(), inputVerifier: new NotEmptyVerifier() )

        label( 'F. Nacimiento' )
        dob = spinner( model: spinnerDateModel() )

        label( 'Apellido Paterno' )
        fathersName = textField( document: new UpperCaseDocument(), inputVerifier: new NotEmptyVerifier(), constraints: 'wrap' )

        label( 'Apellido Materno' )
        mothersName = textField( document: new UpperCaseDocument() )
      }

      panel( border: titledBorder( 'Dirección' ), layout: new MigLayout( 'wrap 2', '[][fill,330!]' ) ) {
        label( 'Calle y Número' )
        primary = textField( document: new UpperCaseDocument(), inputVerifier: new NotEmptyVerifier() )

        label( 'Estado' )
        stateField = comboBox( items: states, itemStateChanged: stateChanged )

        label( 'Delegación/Mnpo' )
        city = comboBox( itemStateChanged: cityChanged, constraints: 'w 330!' )

        label( 'Colonia' )
        locationField = comboBox( itemStateChanged: locationChanged, constraints: 'w 330!' )

        label( 'C.P.' )
        zipcode = comboBox( editable: true )

        button( 'Buscar', actionPerformed: doSearch )
      }

      panel( border: titledBorder( 'Contacto' ), layout: new MigLayout( '', '[][fill,180!][center,25!][fill,180!]' ) ) {
        label( text: ContactType.HOME_PHONE )
        homePhone = textField( constraints: 'wrap' )

        label( text: ContactType.EMAIL )
        email = textField()
        label( '@' )
        domain = comboBox( editable: true, items: domains )
      }

      panel( layout: new MigLayout( 'right', '[fill,100!]' ) ) {
        button( 'Borrar', visible: customer?.id ? true : false, actionPerformed: doDelete )
        button( 'Aplicar', actionPerformed: doSubmit )
        button( 'Limpiar', visible: customer?.id ? false : true, actionPerformed: doClear )
        button( 'Cancelar', defaultButton: true, actionPerformed: {dispose()} )
      }
    }

    dob.editor = new JSpinner.DateEditor( dob as JSpinner, 'dd-MM-yyyy' )
  }

  private void doBindings( ) {
    sb.build {
      bean( firstName, text: bind( source: customer, sourceProperty: 'name', mutual: true ) )
      bean( fathersName, text: bind( source: customer, sourceProperty: 'fathersName', mutual: true ) )
      bean( mothersName, text: bind( source: customer, sourceProperty: 'mothersName', mutual: true ) )
      bean( salutation, selectedItem: bind( source: customer, sourceProperty: 'title', mutual: true ) )
      bean( dob, value: bind( source: customer, sourceProperty: 'dob', mutual: true ) )
      bean( gender, selectedItem: bind( source: customer, sourceProperty: 'gender', mutual: true ) )
      bean( primary, text: bind( source: customer.address, sourceProperty: 'primary', mutual: true ) )
      bean( stateField, selectedItem: bind( source: customer.address, sourceProperty: 'state', mutual: true ) )
      bean( city, selectedItem: bind( source: customer.address, sourceProperty: 'city', mutual: true ) )
      bean( locationField, selectedItem: bind( source: customer.address, sourceProperty: 'location', mutual: true ) )
      bean( zipcode, selectedItem: bind( source: customer.address, sourceProperty: 'zipcode', mutual: true ) )
      bean( homePhone, text: bind( source: tmpHomeContact, sourceProperty: 'primary', mutual: true ) )
      bean( email, text: bind( source: tmpEmailContact, sourceProperty: 'primary', mutual: true ) )
      bean( domain, selectedItem: bind( source: tmpEmailContact, sourceProperty: 'extra', mutual: true ) )
    }
  }

  private def doSearch = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    List<Address> results = CustomerController.findAddresesByZipcode( zipcode.selectedItem as String ) ?: [ ]
    if ( results.any() ) {
      JOptionPane inputPane = sb.optionPane( message: 'Selecciona una colonia',
          selectionValues: results*.location,
          optionType: JOptionPane.OK_CANCEL_OPTION
      )
      inputPane.createDialog( zipcode, 'Resultados de búsqueda por C.P.' ).show()
      String selection = inputPane?.inputValue as String
      Address tmpAddress = results.find { Address tmp ->
        tmp?.location?.equalsIgnoreCase( selection )
      }
      if ( tmpAddress != null ) {
        sb.doOutside {
          stateField.selectedItem = tmpAddress.state
          city.selectedItem = tmpAddress.city
          locationField.selectedItem = tmpAddress.location
        }
      }
    } else {
      sb.optionPane( message: 'No se encontraron resultados' )
          .createDialog( zipcode, 'No se encontraron resultados' )
          .show()
    }
    source.enabled = true
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
          message: 'Se debe registrar el nombre',
          messageType: JOptionPane.ERROR_MESSAGE
      ).createDialog( this, 'No se puede registrar la venta' )
          .show()
    }
    return false
  }

  private def doSubmit = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    customer?.contacts?.clear()
    if ( StringUtils.isNotBlank( tmpHomeContact?.primary ) ) {
      customer.contacts.add( tmpHomeContact )
    }
    if ( StringUtils.isNotBlank( tmpEmailContact?.primary ) ) {
      String mail = "${tmpEmailContact.primary}@${tmpEmailContact.extra}"
      tmpEmailContact.primary = mail
      customer.contacts.add( tmpEmailContact )
    }
    if ( isValidInput() ) {
      Customer tmpCustomer = CustomerController.addCustomer( getCustomer(), edit )
      if ( tmpCustomer?.id ) {
        customer = tmpCustomer
        dispose()
      }
    }
    source.enabled = true
  }

  private def doClear = {
    firstName.text = null
    fathersName.text = null
    mothersName.text = null
    salutation.selectedItem = null
    dob.value = new Date()
    gender.selectedItem = GenderType.MALE
    primary.text = null
    stateField.selectedItem = defaultState
    homePhone.text = null
    email.text = null
    domain.selectedItem = null
  }

  private def titleChanged = { ItemEvent ev ->
    if ( ev.stateChange == ItemEvent.SELECTED ) {
      String title = ev.item
      def tmpTitle = titles.find {
        it?.title?.equalsIgnoreCase( title )
      }
      switch ( tmpTitle?.gender ) {
        case 'f':
          gender.selectedItem = GenderType.FEMALE
          break
        case 'm':
          gender.selectedItem = GenderType.MALE
          break
      }
    }
  }

  private def stateChanged = { ItemEvent ev ->
    if ( ev.stateChange == ItemEvent.SELECTED ) {
      String stateName = ev.item
      List<String> results = CustomerController.findCitiesByStateName( stateName ) ?: [ ]
      results.each {
        city.addItem( it )
      }
      city.selectedIndex = -1
    } else {
      city.removeAllItems()
      locationField.removeAllItems()
      zipcode.removeAllItems()
      locations.clear()
    }
  }

  private def cityChanged = { ItemEvent ev ->
    if ( ev.stateChange == ItemEvent.SELECTED ) {
      String stateName = stateField.selectedItem
      String cityName = ev.item
      Set<String> zipcodes = [ ]
      locations = CustomerController.findLocationsByStateNameAndCityName( stateName, cityName ) ?: [ ]
      locations.each {
        locationField.addItem( it?.location )
        zipcodes.add( it?.zipcode )
      }
      locationField.selectedIndex = -1
      zipcodes.sort().each {
        zipcode.addItem( it )
      }
    } else {
      locationField.removeAllItems()
      zipcode.removeAllItems()
      locations.clear()
    }
  }

  private def locationChanged = { ItemEvent ev ->
    if ( ev.stateChange == ItemEvent.SELECTED ) {
      String locationName = ev.item
      def result = locations.find {
        it?.location?.equalsIgnoreCase( locationName )
      }
      zipcode.selectedItem = result?.zipcode
    }
  }
}
