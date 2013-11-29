package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import javax.swing.JComboBox
import javax.swing.JDialog
import javax.swing.JTextField
import net.miginfocom.swing.MigLayout

class ReceptionDialog extends JDialog {

	SwingBuilder sb = new SwingBuilder()
	
	private JTextField txtDejo
	private JComboBox cbServicio
	private JTextField txtInstruccion
	private JTextField txtCondiciones
	
	ReceptionDialog(){
		buildUI()
	}
	
	// UI Layout Definition
	void buildUI(){
		sb.dialog(this,
			title:"Recepcion de Material",
			resizable: true,
			pack: true,
			modal: true,
			preferredSize: [ 500,300 ]
			){  panel( layout: new MigLayout("center,fill, wrap","[center,grow,fill]") ){

		 panel( layout: new MigLayout("center,fill,wrap","[fill][grow,fill]") ){
			 label( text:"Recepcion de Material de Cliente", constraints:"span 2" )
			 label( text:" ", constraints:"span 2" )
			 label( text:"Dejó:" )
			 txtDejo = textField( )
			 label( text:"Servicio:" )
			 cbServicio = comboBox( )
			 label( text:"Instrucción:" )
			 txtInstruccion = textField( )
			 label( text:"CondicionesGenerales:" )
			 txtCondiciones = textField( )
		 }
		 
		 panel( layout: new MigLayout("right","fill,110!") ){
            button( text:"Guardar")
            button( text:"Cancelar", actionPerformed:{dispose()} )
         }
	  }
	 }
	}
}
