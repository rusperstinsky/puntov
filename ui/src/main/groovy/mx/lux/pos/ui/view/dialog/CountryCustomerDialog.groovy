package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.CustomerController
import mx.lux.pos.ui.model.UpperCaseDocument
import mx.lux.pos.ui.resources.UI_Standards
import net.miginfocom.swing.MigLayout
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.*
import java.awt.Component

class CountryCustomerDialog extends JDialog implements ActionListener{

  private def sb = new SwingBuilder()

  private String selectedCountry
  private String selectedState
  private JComboBox cbPaises
  private JComboBox cbEstado
  private JTextField txtPais
  private List<String> lstPaises = new ArrayList<String>()
  private List<String> lstEstados = new ArrayList<String>()
  private JLabel lblWarning
  private JLabel lblStates
  public boolean button = false
  private static final String TAG_MEXICO = 'MEXICO'
  private static final String TAG_OTROS = 'OTROS'

    CountryCustomerDialog( Component parent ) {
        lstPaises = CustomerController.countries()
        lstEstados = CustomerController.states()
        buildUI( parent )
  }

  // UI Layout Definition
  void buildUI( Component parent  ) {
    sb.dialog( this,
        title: "Seleccionar Pa\u00eds",
        resizable: true,
        pack: true,
        location: parent.locationOnScreen,
        modal: true,
        preferredSize: [ 300, 220 ],
    ) {
      panel() {
        borderLayout()
        panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 2", "20[][grow,fill]60", "20[]10[]" ) ) {
          label( text: "Seleccione el pa\u00eds de origen del cliente", constraints: "span 2" )
          label( text: " ", constraints: "span 2" )
          label( text: "Pa\u00eds:" )
          cbPaises = comboBox( items: lstPaises, editable: false )
          cbPaises.addActionListener(this)
          lblStates = label( text: "Estado:", constraints: 'hidemode 3' )
          cbEstado = comboBox( items: lstEstados, constraints: 'hidemode 3', editable: false )
          label()
          txtPais = textField( constraints: 'hidemode 3', visible: false, document: new UpperCaseDocument() )
          lblWarning = label( constraints: "span 2, hidemode 3", visible: false, foreground: UI_Standards.WARNING_FOREGROUND, )
        }

        panel( constraints: BorderLayout.PAGE_END ) {
          borderLayout()
          panel( constraints: BorderLayout.LINE_END ) {
            button( text: "Aceptar", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonOk() }
            )
            button( text: "Cerrar", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonCancel() }
            )
          }
        }
      }

    }
  }

    String getPais(){
        return selectedCountry
    }
  // UI Management
  protected void refreshUI( ) {

  }

  // Public Methods
  void activate( ) {
    refreshUI()
    setVisible( true )
  }

  // UI Response
  protected void onButtonCancel( ) {
    button = false
    dispose()
  }

  protected void onButtonOk( ) {
    selectedState = (cbEstado.visible == true && !cbEstado.selectedItem.toString().trim().isEmpty()) ? ','+cbEstado.selectedItem.toString().trim() : ''
    selectedCountry = cbPaises.selectedItem.toString().trim()+selectedState
      if( selectedCountry.length() > 0 ){
        if(cbEstado.visible == true && selectedState != null && selectedState.trim() != ''){
          button = true
          dispose()
        } else if(cbEstado.visible == false){
          if(txtPais.visible == true && !txtPais.text.trim() != ''){
            selectedCountry = txtPais.text.trim()
            button = true
            dispose()
          } else if(txtPais.visible == true && txtPais.text.trim() != ''){
            lblWarning.text = 'Es necesario seleccionar un pais'
            lblWarning.visible = true
          } else {
            button = true
            dispose()
          }
        } else {
            lblWarning.text = 'Es necesario seleccionar un estado'
            lblWarning.visible = true
        }
      } else {
          lblWarning.text = 'Es necesario seleccionar un pais'
          lblWarning.visible = true
      }
  }

    @Override
   void actionPerformed(ActionEvent e) {
        if( cbPaises.selectedItem.toString().trim().equalsIgnoreCase(TAG_MEXICO) ){
          cbEstado.visible = true
          lblStates.visible = true
          lblWarning.visible = false
        } else if( cbPaises.selectedItem.toString().trim().equalsIgnoreCase(TAG_OTROS) ){
          txtPais.setVisible( true )
          cbEstado.visible = false
          lblStates.visible = false
          cbEstado.selectedItem = ''
          lblWarning.visible = false
        }else {
          cbEstado.visible = false
          lblStates.visible = false
          cbEstado.selectedItem = ''
          lblWarning.visible = false
        }
    }
}
