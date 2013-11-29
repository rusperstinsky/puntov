package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import javax.swing.JDialog
import javax.swing.JTextField
import net.miginfocom.swing.MigLayout

class BillDialog extends JDialog {

	private SwingBuilder sb = new SwingBuilder()
	
	private JTextField txtFactura
	private JTextField txtVisualTex
	private JTextField txtRxDia
	private JTextField txtRxMes
	private JTextField txtRxAnio
	private JTextField txtEntDia
	private JTextField txtEntMes
	private JTextField txtEntAnio
	
	BillDialog(){
		buildUI()
	}
	
	// UI Layout Definition
	void buildUI( ) {
	  sb.dialog( this,
		  title: "Factura",
		  resizable: true,
		  pack: true,
		  modal: true,
		  preferredSize: [ 500, 300 ],
		  //location: [ 200, 250 ],
	  ){
	  
	  panel( layout: new MigLayout("fill, wrap 2","5[grow,fill]15[grow,fill]5") ){
		  
		  panel( constraints:"span 2", layout: new MigLayout("fill,wrap 4",
			  "[][grow,fill]100[][grow,fill]") ){
			  label( text:"Número de Rx:" )
			  label( text:"12345" )
			  label( text:"Tipo Rx:" )
			  label( text:"Nuevo" )
			  label( text:"Factura:" )
			  txtFactura = textField( )
			  label( text:"Visual Tex:" )
			  txtVisualTex = textField( )
		  }
		  
		panel( layout: new MigLayout("fill,wrap 5",
			  "[grow,fill][][grow,fill][][grow,fill]") ){
			  
		  	  label( text:"Fecha Rx:", constraints:"span 5" )
			  
			  txtRxDia = textField( )
			  label( text:"/" )
			  txtRxMes = textField( )
			  label( text:"/" )
			  txtRxAnio = textField( )
			  
		  }
			  
		  panel( layout: new MigLayout("fill,wrap 5",
			  "[grow,fill][][grow,fill][][grow,fill]") ){
			  label( text:"Fecha Entrega:", constraints:"span 5" )
			  
			  txtEntDia = textField( )
			  label( text:"/" )
			  txtEntMes = textField( )
			  label( text:"/" )
			  txtEntAnio = textField( )
		  }
		  
		  panel( constraints:"span 2", layout: new MigLayout( 'right', '[fill,150!]' )  ) {
			  button( text:"Actualizar" )
			  button( text:"Cerrar", actionPerformed: {dispose()} )
			}
		  
		  
		  
	  }
	  
	  }
	}
}
