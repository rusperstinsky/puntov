package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import javax.swing.ButtonGroup
import javax.swing.JDialog
import javax.swing.JRadioButton
import javax.swing.JTextField
import net.miginfocom.swing.MigLayout

class TrialGlassesDialog extends JDialog {

	SwingBuilder sb = new SwingBuilder()
	
	private ButtonGroup lentPrueba
	private JRadioButton rbLentesPrueba
	private JRadioButton rbPrimCambio
	private JRadioButton rbSegunCambio
	private JTextField txtObservaciones
	
	TrialGlassesDialog(){
		buildUI()
	}
	
	// UI Layout Definition
	void buildUI( ) {
	  sb.dialog( this,
		  title: "Lentes de Contacto",
		  resizable: true,
		  pack: true,
		  modal: true,
		  preferredSize: [ 550, 350 ],
		  //location: [ 200, 250 ],
	  ){
	  
	  panel( layout: new MigLayout("fill, wrap ","[grow,fill,center]") ){
	  		
		  panel( layout: new MigLayout("fill,wrap 2","[center][grow,fill]") ){
			  label( text:"FORMA PARA SOLICITAR LENTE(S) DE PRUEBA", constraints:"span 2" )
			  label( text:"", constraints:"span 2" )
			  lentPrueba = buttonGroup()
			  rbLentesPrueba = radioButton( text:"Lentes de Prueba", constraints:"span 2", buttonGroup: lentPrueba )
			  rbPrimCambio = radioButton( text:"     Primer Cambio", constraints:"span 2", buttonGroup: lentPrueba )
			  rbSegunCambio = radioButton( text:" Segundo Cambio", constraints:"span 2", buttonGroup: lentPrueba )
			  label( text:"", constraints:"span 2" )
			  label( text:"Observaciones:" )
			  txtObservaciones = textField()
		  }
		  
		  panel( layout: new MigLayout("right","[fill,110!]") ){
			  button( text:"Aceptar" )
			  button( text:"Imprimir" )
			  button( text:"Cerrar", actionPerformed: {dispose()} )
		  }
		  
	  }
	  }
	}
	
	
}
