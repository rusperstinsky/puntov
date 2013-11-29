package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.verifier.DateVerifier
import net.miginfocom.swing.MigLayout

import java.awt.BorderLayout
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.swing.ButtonGroup
import javax.swing.JDialog
import javax.swing.JRadioButton

class FilterDialog extends JDialog {

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  private DateVerifier dv = DateVerifier.instance
  private def sb = new SwingBuilder()
  
  private ButtonGroup orden
  private JRadioButton factura
  private JRadioButton fecha
  
  private ButtonGroup estado
  private JRadioButton retenido
  private JRadioButton porEnviar
  private JRadioButton pino
  private JRadioButton sucursal
  private JRadioButton todo
  
  public boolean button = false
  
  FilterDialog( ) {
    buildUI()
  }  
  
  // UI Layout Definition
  void buildUI() {
    sb.dialog( this,
        title: "Seleccionar fechas",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [450, 250],
        location: [200, 250],
    ) {
      panel() {
        borderLayout( )
		    panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 4", "20[][grow,fill]5", "10[]5[]" ) ) {
	          label( text: "Seleccione el campo por el que desea filtrar", constraints: "span 4" )
			  label( text: " ", constraints:"span 4" )
			  label( text:"Estado del Trabajo:")
			  filtro = buttonGroup()
			  retenido = radioButton( text:"Retenidos", buttonGroup:filtro)
			  porEnviar = radioButton( text:"Por Enviar", buttonGroup:filtro)
			  pino = radioButton( text:"Pino", buttonGroup:filtro)
			  label( text: " " )
			  sucursal = radioButton( text:"Sucursal", buttonGroup:filtro)
			  todo = radioButton( text:"Todos", buttonGroup:filtro, selected:true)
			  label( text: " " )
			  
			  label( text: " ", constraints:"span 4" )
			  label( text:"Ordenado:")
			  orden = buttonGroup()
			  factura = radioButton( text:"Factura", , buttonGroup:orden, selected:true)
			  fecha = radioButton( text:"Fecha", , buttonGroup:orden)
			  label( text: " " )
			  
		  }
		
        panel( constraints: BorderLayout.PAGE_END ) {
          borderLayout() 
          panel( constraints: BorderLayout.LINE_END ) {
            button( text: "Generar", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonOk( ) }
            )
            button( text: "Cerrar", preferredSize: UI_Standards.BUTTON_SIZE, 
                actionPerformed: { onButtonCancel( ) }
            )
          }
        }
      } 
	     
    }
  }
  
  // UI Management
  protected void refreshUI() {
    /*if ( selectedDateStart == null || selectedDateEnd == null ) {
      selectedDateStart = DateUtils.truncate( new Date(), Calendar.DATE )
	  selectedDateEnd = DateUtils.truncate( new Date(), Calendar.DATE )
	}
    txtDateStart.setText( df.format( selectedDateStart ) )
	txtDateEnd.setText( df.format( selectedDateEnd ) )*/
	
  }
  
  // Public Methods
  void activate() {
    refreshUI()
    setVisible(true)
  }
  
  boolean getRetenido(){
	  return retenido.selected
  }
  
  boolean getPorEnviar(){
	  return porEnviar.selected
  }
  
  boolean getPino(){
	  return pino.selected
  }
  
  boolean getSucursal(){
	  return sucursal.selected
  }
  
  boolean getTodo(){
	  return todo.selected
  }
  
  boolean getFactura(){
	  return factura.selected
  }
  
  boolean getFecha(){
	  return fecha.selected
  }
  
  void setDefaultDates( Date pDateStart, Date pDateEnd ) {
   
  }
  
  // UI Response
  protected void onButtonCancel() {
    button = false
    setVisible(false)
  }

  protected void onButtonOk() {
    button = true
    setVisible(false)
  }
}
