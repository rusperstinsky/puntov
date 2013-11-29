package mx.lux.pos.ui.view.panel

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.dialog.SingleListDialog
import net.miginfocom.swing.MigLayout

import java.awt.Font
import java.awt.event.ActionEvent
import javax.swing.border.TitledBorder
import javax.swing.*

class BudgetPanel extends JPanel {

  private SwingBuilder sb
  private JTextField txtVendedor
  private JTextField txtCliente
  private JTextField txtDatosAdicionales
  private JTextField txtApePaterno
  private JTextField txtApeMaterno
  private JTextField txtNombre
  private JTextField txtNoPresupuesto
  private JTextField txtTelefono
  private JTextField txtVigencia
  private JTextField txtTopeArmazon
  private JTextField txtTopeLentes
  private JTextField txtTopeLContacto
  private JTextField txtObservaciones
  private JComboBox cbParentesco
  private JComboBox cbTitulo
  private JCheckBox cbConvenio
  private JComboBox cbxConvenio
  private JCheckBox cbTopeAnteojo
  private JCheckBox cbCalculoAutomatico
  private JCheckBox cbImrpimirCopia
  private JCheckBox cbTratamiento

  List data = [ [ first: 'test0', last: 'test1' ],
      [ first: 'test2', last: 'test3' ],
      [ first: 'test4', last: 'test5' ] ]

  JTable tBrowser

  BudgetPanel( ) {
    sb = new SwingBuilder()
    buildUI()
  }

  private void buildUI( ) {

    sb.panel( this, layout: new MigLayout( 'fill, wrap 3', '20[grow,fill][grow,fill][fill]20' ) ) {

      panel( constraints: "span 2", layout: new MigLayout( "fill, wrap 4", "[][grow,fill][][grow,fill]" ) ) {
        label( text: "No. Presupuesto:" )
        txtNoPresupuesto = textField( minimumSize: [ 100, 25 ] )
        label( text: "Vigencia:" )
        txtVigencia = textField()
        label( text: "Cliente:" )
        txtCliente = textField( constraints: "span3" )
        label( text: "Datos Adicionales:" )
        txtDatosAdicionales = textField( constraints: "span 3" )
        label( text: "Parentesco:" )
        cbParentesco = comboBox( maximumSize: [ 100, 25 ], constraints: "span 3" )
      }

      panel( layout: new MigLayout( "right,fill,wrap", "[right,grow,fill]" ) ) {
        def displayFont = new Font( '', Font.BOLD, 30 )
        def font = new Font( '', Font.BOLD, 20 )
        label( text: "       0.00", minimumSize: [ 50, 60 ], font: displayFont )
        label( text: "      Version XX", font: font )
      }

      panel( border: new TitledBorder( "Beneficiario" ), layout: new MigLayout( "center,fill,wrap 3", "[fill][fill][grow,fill]" ) ) {
        label( text: "Titulo:     " )
        label( text: "Ap. Pat.:" )
        txtApePaterno = textField()
        cbTitulo = comboBox()
        label( text: "Ap. Mat.:" )
        txtApeMaterno = textField()
        label( text: "" )
        label( text: "nombre:" )
        txtNombre = textField()
        label( text: "" )
        label( text: "Teléfono:" )
        txtTelefono = textField()
        label( text: "" )

      }

      panel( border: new TitledBorder( "Descuento" ), layout: new MigLayout( "fill,wrap", "25[grow,fill]25" ) ) {
        cbConvenio = checkBox( text: "Convenio", actionPerformed: doConvenio )
        cbxConvenio = comboBox( enabled: false )
        button( text: "Requisitos", preferredSize: UI_Standards.BUTTON_SIZE, actionPerformed: onClick )
      }

      panel( border: new TitledBorder( "Topes" ), layout: new MigLayout( "fill,wrap 2", "[][fill]" ) ) {
        cbTopeAnteojo = checkBox( text: "Anteojo", enabled: false, constraints: "span 2" )
        label( text: "Armazón" )
        txtTopeArmazon = textField()
        label( text: "Lentes" )
        txtTopeLentes = textField()
        label( text: "L. Contacto" )
        txtTopeLContacto = textField()
        cbCalculoAutomatico = checkBox( text: "Cálculo Automático", enabled: false, constraints: "span 2" )
      }

      panel( border: new TitledBorder( "" ), layout: new MigLayout( "left,fill,wrap", "[left]" ) ) {
        cbImrpimirCopia = checkBox( text: "Copia" )
        cbTratamiento = checkBox( text: "Tratamiento a cargo de la empresa", enabled: false )
      }

      panel( constraints: "span 2", layout: new MigLayout() ) {
        label( text: "" )
      }

      scrollPane( constraints: "span 3"
      ) {
        table {
          sb.tableModel( list: data ) {
            propertyColumn( header: 'Cant.', propertyName: 'first', maxWidth: 50, editable: false )
            propertyColumn( header: 'Tipo', propertyName: 'last', maxWidth: 40, editable: false )
            propertyColumn( header: 'Código', propertyName: 'first', maxWidth: 100, editable: false )
            propertyColumn( header: 'Color', propertyName: 'last', maxWidth: 50, editable: false )
            propertyColumn( header: 'Descripción', propertyName: 'first', maxWidth: 300, editable: false )
            propertyColumn( header: 'P.U', propertyName: 'last', maxWidth: 100, editable: false )
            propertyColumn( header: 'Importe', propertyName: 'first', maxWidth: 100, editable: false )
          }
        }
      }

      panel( constraints: "span 3", layout: new MigLayout( "fill, wrap 2", "[][grow,fill]" ) ) {
        label( text: "Obs:" )
        txtObservaciones = textField()
      }

      panel( constraints: "span 3", layout: new MigLayout( "right", "[fill,110!]" ) ) {
        button( text: "Guardar" )
        button( text: "Buscar" )
        button( text: "Reimprimir" )
        button( text: "Limpiar" )
        button( text: "Cerrar" )
      }

    }
  }


  private def onClick = { ActionEvent ev ->

    new SingleListDialog().show()

  }

  private def doConvenio = {

    if ( cbConvenio.selected == true ) {
      cbxConvenio.enabled = true
    } else {
      cbxConvenio.enabled = false
    }

  }

}
