package mx.lux.pos.ui.view.panel

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.CustomerController
import mx.lux.pos.ui.controller.InvoiceController
import mx.lux.pos.ui.controller.OrderController
import mx.lux.pos.ui.controller.TaxpayerController
import mx.lux.pos.ui.view.dialog.SuggestedTaxpayersDialog
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.StringUtils

import java.awt.Color
import java.awt.event.ActionEvent
import javax.swing.border.TitledBorder

import mx.lux.pos.ui.model.*

import javax.swing.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class InvoicePanel extends JPanel {

  private static final String DATE_TIME_FORMAT = 'dd-MM-yyyy HH:mm'

  private SwingBuilder sb
  private JLabel lblCliente
  private JLabel lblFolio
  private JLabel lblFecha
  private JLabel lblEstatus
  private JLabel lblCallNum
  private JLabel lblColonia
  private JLabel lblEstado
  private JLabel lblPais
  private JLabel lblCP
  private JLabel lblCiudad
  private JTextField txtTicket
  private JTextField rfcInput
  private JTextField txtRazonSocial
  private JTextField txtCallNum
  private JTextField txtCol
  private JTextField txtDelMun
  private JTextField txtPais
  private JTextField txtCP
  private JTextField txtCorreo
  private JCheckBox cbExtranjero
  private JComboBox cbEstado
  private JComboBox cbCorreo
  private JButton searchButton
  private JButton editButton
  private JButton printInvoiceButton
  private JButton printReferenceButton
  private JButton displayButton
  private JButton requestButton
  private boolean invoiced
  private boolean editable
  private Branch branch
  private String estadoDefault
  private List<String> estados
  private List<String> dominios
  private Order order
  private Invoice invoice

  private static final String TAG_CANCELADO = 'T'

  InvoicePanel( ) {
    sb = new SwingBuilder()
    invoiced = false
    editable = false
    branch = Session.get( SessionItem.BRANCH ) as Branch
    estadoDefault = CustomerController.findDefaultState()
    estados = CustomerController.findAllStates()
    dominios = CustomerController.findAllCustomersDomains()
    order = new Order()
    invoice = new Invoice( state: estadoDefault )
    buildUI()
    doBindings()
  }

  private void buildUI( ) {
    sb.panel( this,
        border: new TitledBorder( 'Facturaci\u00f3n Electr\u00f3nica' ),
        layout: new MigLayout( 'fill,wrap', '[fill]', '[top][fill][bottom]' )
    ) {
      panel( layout: new MigLayout( 'insets 0,center', '[fill,120!]' ) ) {
        label( 'Ticket' )
        txtTicket = textField( "${branch?.costCenter}-", actionPerformed: {searchButton.doClick()} )
        searchButton = button( 'Buscar', actionPerformed: doTicketSearch, constraints: 'wrap' )
      }

      panel( border: loweredEtchedBorder(), layout: new MigLayout( 'center,wrap 4', '[][fill,140!][center][fill]' ) ) {
        label( 'Cliente' )
        lblCliente = label( constraints: 'span' )

        label( 'Folio' )
        lblFolio = label()
        label( 'Fecha' )
        lblFecha = label()

        label( 'Status' )
        lblEstatus = label( foreground: Color.BLUE )
        editButton = button( 'Modificar', visible: false, actionPerformed: doEdit, constraints: 'skip' )

        label( 'RFC' )
        rfcInput = textField( document: new UpperCaseDocument(), actionPerformed: doRfcSearch )
        cbExtranjero = checkBox( 'Extranjero', itemStateChanged: doToggleForeign, constraints: 'wrap' )

        label( 'Nombre/Raz\u00f3n Social' )
        txtRazonSocial = textField( document: new UpperCaseDocument(), constraints: 'span' )

        lblCallNum = label( 'Calle y N\u00famero', constraints: 'hidemode 3' )
        txtCallNum = textField( document: new UpperCaseDocument(), constraints: 'span,hidemode 3' )

        lblColonia = label( 'Colonia', constraints: 'hidemode 3' )
        txtCol = textField( document: new UpperCaseDocument(), constraints: 'span,hidemode 3' )

        lblCiudad = label( 'Delegaci\u00f3n/Municipio' )
        txtDelMun = textField( document: new UpperCaseDocument(), constraints: 'span' )

        lblCP = label( 'CP', constraints: 'hidemode 3' )
        txtCP = textField( document: new UpperCaseDocument(), constraints: 'hidemode 3' )

        lblEstado = label( 'Estado', constraints: 'hidemode 3' )
        cbEstado = comboBox( items: estados, constraints: 'hidemode 3' )

        lblPais = label( 'Pa\u00eds', visible: false, constraints: 'hidemode 3' )
        txtPais = textField( 'MEXICO', visible: false, document: new UpperCaseDocument(), constraints: 'wrap,hidemode 3' )

        label( 'Correo Electr\u00f3nico' )
        txtCorreo = textField()
        label( '@' )
        cbCorreo = comboBox( items: dominios, editable: true )
      }

      panel( layout: new MigLayout( 'right', '[fill,100!]' ) ) {
        printInvoiceButton = button( 'Imprimir', visible: false, actionPerformed: doPrintInvoice, constraints: 'hidemode 3' )
        printReferenceButton = button( 'Ligas', visible: false, actionPerformed: doPrintReference, constraints: 'hidemode 3' )
        displayButton = button( 'Ver', visible: false, actionPerformed: doShowInvoice, constraints: 'hidemode 3' )
        requestButton = button( 'Solicitar', actionPerformed: doRequest, constraints: 'hidemode 3' )
        button( 'Limpiar', actionPerformed: doClear )
      }
    }
  }

  private doBindings( ) {
    sb.build {
      bean( lblCliente, text: bind {order?.customer?.fullName ?: ''} )
      bean( lblFolio, text: bind {order?.id ?: ''} )
      bean( lblFecha, text: bind {order?.date?.format( DATE_TIME_FORMAT ) ?: ''} )
      bean( rfcInput, text: bind( source: invoice, sourceProperty: 'rfc', mutual: true ) )
      bean( rfcInput, enabled: bind {editable && !cbExtranjero.selected} )
      bean( cbExtranjero,
          selected: bind( source: invoice, sourceProperty: 'invoiceType',
              converter: {'I'.equalsIgnoreCase( it )},
              reverseConverter: {it ? 'I' : 'N'},
              mutual: true
          )
      )
        DateFormat df = new SimpleDateFormat( "dd-MM-yyyy" )
        String today = df.format( new Date() )
        String dateInvoice = df.format( invoice.issueDate != null ? invoice.issueDate : new Date() )
        println dateInvoice.equalsIgnoreCase(today)
        Boolean validateDate = dateInvoice.equalsIgnoreCase(today)
      bean( cbExtranjero, enabled: bind {editable} )
      bean( txtRazonSocial, text: bind( source: invoice, sourceProperty: 'bizName', mutual: true ) )
      bean( txtRazonSocial, enabled: bind {editable} )
      bean( txtCallNum, text: bind( source: invoice, sourceProperty: 'primary', mutual: true ) )
      bean( txtCallNum, enabled: bind {editable} )
      bean( txtCol, text: bind( source: invoice, sourceProperty: 'location', mutual: true ) )
      bean( txtCol, enabled: bind {editable} )
      bean( txtDelMun, text: bind( source: invoice, sourceProperty: 'city', mutual: true ) )
      bean( txtDelMun, enabled: bind {editable} )
      bean( cbEstado, selectedItem: bind( source: invoice, sourceProperty: 'state', mutual: true ) )
      bean( cbEstado, enabled: bind {editable} )
      bean( txtPais, text: bind( source: invoice, sourceProperty: 'country', mutual: true ) )
      bean( txtPais, enabled: bind {editable} )
      bean( txtCP, text: bind( source: invoice, sourceProperty: 'zipcode', mutual: true ) )
      bean( txtCP, enabled: bind {editable} )
      bean( txtCorreo, enabled: bind {editable} )
      bean( cbCorreo, enabled: bind {editable} )
      bean( editButton, visible: bind {invoiced && validateDate} )
      bean( printInvoiceButton, visible: bind {invoiced && !editable} )
      bean( printReferenceButton, visible: bind {invoiced && !editable} )
      bean( displayButton, visible: bind {invoiced && !editable} )
      bean( requestButton, visible: bind {editable} )
      bean( txtTicket, text: bind( target: invoice, targetProperty: 'ticket' ) )
      bean( invoice, orderId: bind {order.id} )
      bean( invoice, email: bind {"${txtCorreo.text}@${cbCorreo.selectedItem}"} )
    }
  }

  private void fillEmailFields( String email ) {
    List<String> tokens = StringUtils.splitPreserveAllTokens( email, '@' )
    if ( tokens?.any() ) {
      txtCorreo.text = tokens.get( 0 )
      cbCorreo.selectedItem = tokens.get( 1 )
    }
  }

  private void fillTaxpayerFields( Taxpayer taxpayer ) {
    if ( taxpayer?.id ) {
      rfcInput.text = taxpayer.rfc
      txtRazonSocial.text = taxpayer.name
      txtCallNum.text = taxpayer.primary
      txtCol.text = taxpayer.location
      txtDelMun.text = taxpayer.city
      cbEstado.selectedItem = CustomerController.findStateById( taxpayer.stateId )
      txtCP.text = taxpayer.zipcode
      fillEmailFields( taxpayer.email )
    }
  }

  private void clearTaxpayerFields( ) {
    rfcInput.text = null
    txtRazonSocial.text = null
    txtCallNum.text = null
    txtCol.text = null
    txtDelMun.text = null
    cbEstado.selectedItem = null
    txtCP.text = null
    txtCorreo.text = null
    cbCorreo.selectedItem = null
  }

  private void fillInvoiceFields( Invoice invoceTmp ) {
    if ( invoceTmp?.id ) {
      invoice = invoceTmp
      invoiced = true
      editable = false
      lblEstatus.text = 'Facturado'
      fillEmailFields( invoceTmp.email )
    } else {
      invoiced = false
      editable = true
      lblEstatus.text = 'Sin Facturar'
    }
    doBindings()
  }

  private void clearFields( ) {
    editable = false
    invoiced = false
    lblEstatus.text = null
    clearTaxpayerFields()
    order = new Order()
    invoice = new Invoice( state: estadoDefault )
    doBindings()
  }

  private def doTicketSearch = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    clearFields()
    Order orderTmp = OrderController.findOrderByTicket( txtTicket.text )
    if ( orderTmp?.id ) {
      if ( !orderTmp.due ) {
        if( TAG_CANCELADO.compareToIgnoreCase(orderTmp.status) ){
        order = orderTmp
        txtTicket.enabled = false
        if ( CustomerType.FOREIGN.equals( orderTmp.customer?.type ) ) {
          cbExtranjero.selected = true
          txtDelMun.text = orderTmp.customer?.address?.city
          txtPais.text = orderTmp.customer?.address?.country
        }
        Invoice invoceTmp = InvoiceController.findInvoiceByTicket( txtTicket.text )
        fillInvoiceFields( invoceTmp )
        } else {
          sb.optionPane(
              message: "Ticket cancelado",
              messageType: JOptionPane.ERROR_MESSAGE
          ).createDialog( this, 'No se puede facturar ticket' )
              .show()
        }
      } else {
        sb.optionPane(
            message: "No se puede facturar ticket: ${txtTicket.text} debido a que tiene un saldo pendiente",
            messageType: JOptionPane.ERROR_MESSAGE
        ).createDialog( this, 'No se puede facturar ticket' )
            .show()
      }
    } else {
      sb.optionPane(
          message: "No se encontraron resultados para el ticket: ${txtTicket.text}",
          messageType: JOptionPane.ERROR_MESSAGE
      ).createDialog( this, 'Sin resultados' )
          .show()
    }
    source.enabled = true
  }

  private def doEdit = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    editable = true
    doBindings()
    source.enabled = true
  }

  private def doRfcSearch = { ActionEvent ev ->
    JTextField source = ev.source as JTextField
    String input = source.text
    if ( StringUtils.isNotBlank( input ) ) {
      clearTaxpayerFields()
      sb.doOutside {
        List<Taxpayer> results = TaxpayerController.findTaxpayersLike( input )
        if ( results?.any() ) {
          if ( results.size() == 1 ) {
            fillTaxpayerFields( results.first() )
          } else {
            SuggestedTaxpayersDialog dialog = new SuggestedTaxpayersDialog( source, input, results )
            dialog.show()
            fillTaxpayerFields( dialog.taxpayer )
          }
          doBindings()
        } else {
          sb.optionPane(
              message: "No se encontraron resultados para el RFC: ${input}",
              messageType: JOptionPane.ERROR_MESSAGE
          ).createDialog( this, 'Sin resultados' )
              .show()
        }
      }
    } else {
      sb.optionPane(
          message: 'Es necesario ingresar una b\u00fasqeda v\u00e1lida',
          messageType: JOptionPane.ERROR_MESSAGE
      ).createDialog( this, 'B\u00fasqueda inv\u00e1lida' )
          .show()
    }
  }

  private def doToggleForeign = {
    if ( cbExtranjero.selected ) {
      lblCallNum.visible = false
      txtCallNum.visible = false
      txtCallNum.text = null
      lblColonia.visible = false
      txtCol.visible = false
      txtCol.text = null
      lblEstado.visible = false
      cbEstado.visible = false
      cbEstado.selectedItem = estadoDefault
      lblPais.visible = true
      txtPais.visible = true
      txtPais.text = null
      lblCP.visible = false
      txtCP.visible = false
      txtCP.text = null
      lblCiudad.text = 'Ciudad'
      rfcInput.text = 'XEXX010101000'
      txtRazonSocial.text = order?.customer?.fullName ?: ''
    } else {
      lblCallNum.visible = true
      txtCallNum.visible = true
      lblColonia.visible = true
      txtCol.visible = true
      lblEstado.visible = true
      cbEstado.visible = true
      lblCP.visible = true
      txtCP.visible = true
      lblPais.visible = false
      txtPais.visible = false
      txtPais.text = 'MEXICO'
      lblCiudad.text = 'Delegaci\u00f3n/Municipio'
      rfcInput.text = null
      txtRazonSocial.text = ''
    }
  }

  private boolean isValidInput( ) {
    if ( InvoiceController.isValidRfc( rfcInput.text ) ) {
      if( ( txtCorreo.text.length() > 0 && ( cbCorreo.getSelectedItem() != null ) )
      || ( txtCorreo.text.length() <= 0 && cbCorreo.getSelectedItem() == null ) ){
        return true
      } else {
        sb.optionPane(
            message: "Direccion de correo inv\u00e1lida",
            messageType: JOptionPane.INFORMATION_MESSAGE
        ).createDialog( this, 'Verificar Datos' )
            .show()
        return false
      }
    } else {
      sb.optionPane(
          message: "RFC inv\u00e1lido",
          messageType: JOptionPane.INFORMATION_MESSAGE
      ).createDialog( this, 'Verificar Datos' )
          .show()
      return false
    }
  }

  private def doPrintInvoice = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    InvoiceController.printInvoice( invoice.invoiceId )
    source.enabled = true
  }

  private def doPrintReference = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    InvoiceController.printInvoiceReference( invoice.invoiceId )
    source.enabled = true
  }

  private def doShowInvoice = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    InvoiceController.showInvoice( invoice.invoiceId )
    source.enabled = true
  }

  private def doRequest = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    if ( isValidInput() ) {
        if( cbExtranjero.selected ){
            invoice.state = 'NA'
        }
      Invoice invoiceTmp = InvoiceController.requestInvoice( invoice )
      if ( invoiceTmp?.id ) {
        fillInvoiceFields( invoiceTmp )
        sb.optionPane(
            message: "Ticket facturado correctamente, folio fiscal: ${invoiceTmp.invoiceId}",
            messageType: JOptionPane.INFORMATION_MESSAGE
        ).createDialog( this, 'Ticket facturado correctamente' )
            .show()
        InvoiceController.printInvoiceReference( invoiceTmp.invoiceId )
      } else {
        sb.optionPane(
            message: "Ocurrio un error al facturar ticket, intente nuevamente",
            messageType: JOptionPane.ERROR_MESSAGE
        ).createDialog( this, 'No se puede facturar ticket' )
            .show()
      }
    }
    source.enabled = true
  }

  private def doClear = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    txtTicket.enabled = true
    txtTicket.text = "${branch?.costCenter}-"
    clearFields()
    source.enabled = true
  }
}
