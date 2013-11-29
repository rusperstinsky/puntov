package mx.lux.pos.ui.view.dialog

import java.awt.event.ActionEvent
import java.util.List;

import groovy.swing.SwingBuilder
import javax.swing.JDialog
import javax.swing.JTextField
import net.miginfocom.swing.MigLayout

class ReviewDialog extends JDialog{
	
	private def sb = new SwingBuilder()
	
	private JTextField txtProblema
	private JTextField txtDiagnostico
	
	List data = [[first:'test0', last:'test1']]
	
	ReviewDialog(){
		buildUI()
	}
	
	void buildUI(){
	  sb.dialog( this,
		  title: "SELECCIONA REVISIONES",
		  resizable: true,
		  pack: true,
		  modal: true,
		  preferredSize: [ 600, 400 ],
		  //location: [ 200, 250 ],
	  ){
	  panel( layout: new MigLayout("fill, wrap 2","[center]") ){
		  
		  panel( constraints:"span 2", layout: new MigLayout("fill,wrap 1","[center]") ){
			  label( text:"REVISIONES" )
		  }
		  
		  scrollPane ( constraints:"span 2"
		  ) {
		  table {
			  sb.tableModel( list : data ) {
				  propertyColumn(header:'Fecha.', propertyName:'first', maxWidth: 50, editable: false)
				  propertyColumn(header:'Hora.', propertyName:'last', maxWidth: 40, editable: false)
				  propertyColumn(header:'Elaboro', propertyName:'first', maxWidth: 350, editable: false)
			  }
		  }
		}
		  
		  panel( layout: new MigLayout("fill,wrap") ){
			  label( text:"Problema:" )
			  txtProblema = textField( minimumSize: [250,100] )
		  }
		  
		  panel( layout: new MigLayout("fill,wrap") ){
			  label( text:"Diagnóstico" )
			  txtDiagnostico = textField( minimumSize:[250,100] )
		  }
		  
		  panel( constraints:"span 2", layout: new MigLayout("right","[right,fill,150!]") ){
			  button( text:"Nuevo", actionPerformed: onNewReviewDialog )
			  button( text:"Cerrar" )
		  }
		  
		  
	  }
	  
	  }
	}
	
	private def onNewReviewDialog = { ActionEvent ev ->
		
		new NewReviewDialog( ).show()
	}

}
