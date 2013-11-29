package mx.lux.pos.ui.view.dialog

import java.awt.event.ActionEvent
import java.util.List;

import groovy.swing.SwingBuilder
import javax.swing.JDialog
import javax.swing.JTextField;
import javax.swing.border.TitledBorder
import net.miginfocom.swing.MigLayout

class ContactLensesDialog extends JDialog {

	private def sb = new SwingBuilder()
	
	private JTextField txtObservaciones
	private JTextField txtNotas
	private JTextField txtODCB
	private JTextField txtODEsfera
	private JTextField txtODCil
	private JTextField txtODEje
	private JTextField txtODAdc
	private JTextField txtODDiam
	private JTextField txtODModelo
	
	private JTextField txtOICB
	private JTextField txtOIEsfera
	private JTextField txtOICil
	private JTextField txtOIEje
	private JTextField txtOIAdc
	private JTextField txtOIDiam
	private JTextField txtOIModelo
	
	List data = [[first:'test0', last:'test1']]
	
	ContactLensesDialog(){
		buildUI()
	}
	
	
	// UI Layout Definition
	void buildUI( ) {
	  sb.dialog( this,
		  title: "Lentes de Contacto",
		  resizable: true,
		  pack: true,
		  modal: true,
		  preferredSize: [ 800, 600 ],
		  //location: [ 200, 250 ],
	  ){
	  panel( layout: new MigLayout("fill, wrap 2") ){
		  
		  scrollPane (
		  ) {
		  table {
			  sb.tableModel( list : data ) {
				  propertyColumn(header:'Fecha.', propertyName:'first', maxWidth: 50, editable: false)
				  propertyColumn(header:'Num.', propertyName:'last', maxWidth: 40, editable: false)
				  propertyColumn(header:'Factura', propertyName:'first', maxWidth: 100, editable: false)
				  propertyColumn(header:'Saldo', propertyName:'last', maxWidth: 50, editable: false)
				  propertyColumn(header:'Visual_Tex', propertyName:'first', maxWidth: 300, editable: false)
			  }
		  }
		}
		  
		  panel( layout: new MigLayout("fill, wrap") ){
			  label( text:"Notas:" )
			  txtNotas = textField( editable:false, minimumSize: [280,110], enabled:false )
			  button( text:"Poner Nota", actionPerformed: onClickObs )
		  }
		  
		  scrollPane (
		  ) {
		  table {
			  sb.tableModel( list : data ) {
				  propertyColumn(header:'Fecha.', propertyName:'first', maxWidth: 50, editable: false)
				  propertyColumn(header:'Num.', propertyName:'last', maxWidth: 40, editable: false)
				  propertyColumn(header:'Tipo', propertyName:'first', maxWidth: 100, editable: false)
				  propertyColumn(header:'Ojo', propertyName:'last', maxWidth: 50, editable: false)
				  propertyColumn(header:'Factura', propertyName:'first', maxWidth: 300, editable: false)
				  propertyColumn(header:'Visual_Tex', propertyName:'first', maxWidth: 300, editable: false)
			  }
		  }
		}
		  
		  panel( layout: new MigLayout("fill,wrap") ){
			  label( text:"Observaciones" )
			  txtNotas = textField( editable:false, minimumSize: [280,110] )
		  }
		  
		  panel( constraints:"span 2", layout: new MigLayout( 'left', '[fill,110!]' )  ) {
			  button( text:"Nuevo", actionPerformed: onNew )
			  button( text:"Factura", actionPerformed: onBill )
			  button( text:"Cancelar" )
			  button( text:"Pedir", actionPerformed: onRequest )
			  button( text:"Lente Prueba", actionPerformed: onTrial )
			}
		  
		  panel( border: new TitledBorder("Rx"), layout: new MigLayout("fill,wrap 8",
			  "[][grow,fill][grow,fill][center,grow,fill][grow,fill][grow,fill][grow,fill][grow,fill]") ){
			  label( text:"")
			  label( text:"C.B.", border: new TitledBorder("") )
			  label( text:"Esfera", border: new TitledBorder("") )
			  label( text:"Cil.", border: new TitledBorder("") )
			  label( text:"Eje", border: new TitledBorder("") )
			  label( text:"Adc.", border: new TitledBorder("") )
			  label( text:"Diam.", border: new TitledBorder("") )
			  label( text:"Modelo", border: new TitledBorder("") )
			  label( text:"O.D." )
			  txtODCB = textField( )
			  txtODEsfera = textField( )
			  txtODCil = textField( )
			  txtODEje = textField( )
			  txtODAdc = textField( )
			  txtODDiam = textField( )
			  txtODModelo = textField( )
			  label( text:"O.I." )
			  txtOICB = textField( )
			  txtOIEsfera = textField( )
			  txtOICil = textField( )
			  txtOIEje = textField( )
			  txtOIAdc = textField( )
			  txtOIDiam = textField( )
			  txtOIModelo = textField( )
		  }
		  
		  panel( layout: new MigLayout("fill") ){
			  label( text:"" )

		  }
		  
		  
		  panel( constraints:"span 2", layout: new MigLayout( 'right', '[fill,150!]' )  ) {
			  button( text:"Ver" )
			  button( text:"Corrección" )
			  button( text:"Reposición" )
			  button( text:"Imprimir" )
			  button( text:"Cerrar", actionPerformed: {dispose()} )
			}
		  
		  
		  
	  }
	  
	  }
	}
	
	private def onClickObs = { ActionEvent ev ->
		
		new ObservationsDialog( ).show()
	}
	
	private def onNew = { ActionEvent ev ->
		
		new ContactLensesRegisterDialog().show()
		
	}
	
	private def onBill = { ActionEvent ev ->
		new BillDialog().show()
		
	}
	
	private def onRequest = { ActionEvent ev ->
		new OrderRequestDialog().show()
		
	}
	 
	private def onTrial = { ActionEvent ev ->
		new TrialGlassesDialog().show()
		
	}
}
