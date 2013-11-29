package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import javax.swing.ButtonGroup
import javax.swing.JCheckBox
import javax.swing.JDialog
import javax.swing.JRadioButton
import javax.swing.JTextField
import net.miginfocom.swing.MigLayout

class OrderRequestDialog extends JDialog {
	
	SwingBuilder sb = new SwingBuilder()
	
	private JTextField txtCantidadOD
	private JTextField txtCantidadOI
	private JCheckBox cbxLenticularRx
	private JCheckBox cbxLenticularFact
	private ButtonGroup rx
	private JRadioButton rbRxSi
	private JRadioButton rbRxNo 
	private ButtonGroup factura
	private JRadioButton rbFacSi
	private JRadioButton rbFacNo
	private JTextField txtFactura
	private JTextField txtNoVisTex
	
	OrderRequestDialog(){
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
		  
		  panel( layout: new MigLayout("fill,wrap 7",
			  "[center][grow,fill,center]120[center][grow,fill,center][center][center][center]" ) ){
			  label( text:"PEDIDO DE L.C. A VISUAL TEX", constraints:"span 7" )
			  label( text:"", constraints:"span 7" )
			  label( text:"Número de Rx:" )
			  label( text:"12345" )
			  label( text:"Cant.", constraints:"span 2" )
			  label( text:"Lenticular" )
			  label( text:"Marcado", constraints:"span 2" )
			  label( text:"Tipo Rx:" )
			  label( text:"Nuevo" )
			  label( text:"OD" )
			  txtCantidadOD = textField( )
			  cbxLenticularRx = checkBox( )
			  rx = buttonGroup()
			  rbRxSi = radioButton( buttonGroup: rx )
			  rbRxNo = radioButton( buttonGroup: rx )
			  label( text:"Factura:" )
			  txtFactura = textField( )
			  label( text:"OI" )
			  txtCantidadOI = textField( )
			  cbxLenticularFact = checkBox( )
			  factura = buttonGroup()
			  rbFacSi = radioButton( buttonGroup: factura )
			  rbFacNo = radioButton( buttonGroup: factura )
		  }
		  
		  panel( layout: new MigLayout("fill,wrap 2","100[center,fill][center,grow,fill]100") ){
			  label( text:"No. VISUAL TEX:" )
			  txtNoVisTex = textField( )
		  }
		  
		  panel( constraints:"span 2", layout: new MigLayout( 'right', '[fill,150!]' )  ) {
			  button( text:"Enviar" )
			  button( text:"Cerrar", actionPerformed: {dispose()} )
			}
		  
		  
	  }
	  
	  }
	}

}
