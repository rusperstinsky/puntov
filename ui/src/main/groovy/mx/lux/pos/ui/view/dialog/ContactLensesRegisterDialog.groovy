package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog
import javax.swing.JTextField;
import javax.swing.border.TitledBorder
import net.miginfocom.swing.MigLayout
import mx.lux.pos.ui.resources.ImageLibrary

class ContactLensesRegisterDialog extends JDialog {

	private SwingBuilder sb = new SwingBuilder()
	
	private JTextField txtRefODEsfera
	private JTextField txtRefODCilindro
	private JTextField txtRefODEje
	private JTextField txtRefODAdc
	private JTextField txtRefOIEsfera
	private JTextField txtRefOICilindro
	private JTextField txtRefOIEje
	private JTextField txtRefOIAdc
	
	private JTextField txtQueODmCurv
	private JTextField txtQueODMCurv
	private JTextField txtQueODEje
	private JTextField txtQueODMm
	private JTextField txtQueODmm
	private JTextField txtQueOImCurv
	private JTextField txtQueOIMCurv
	private JTextField txtQueOIEje
	private JTextField txtQueOIMm
	private JTextField txtQueOImm
	
	private JComboBox cbODModLente
	private JComboBox cbOIModLente
	private JComboBox cbODDiam
	private JComboBox cbOIDiam
	
	private JTextField txtLenPruODCB
	private JTextField txtLenPruODEsfera
	private JTextField txtLenPruODCil
	private JTextField txtLenPruODEje
	private JTextField txtLenPruODAdc
	private JTextField txtLenPruODDiam
	private JTextField txtLenPruOICB
	private JTextField txtLenPruOIEsfera
	private JTextField txtLenPruOICil
	private JTextField txtLenPruOIEje
	private JTextField txtLenPruOIAdc
	private JTextField txtLenPruOIDiam
	
	private JTextField txtLenPruRxCB1
	private JTextField txtLenPruRxEsfera1
	private JTextField txtLenPruRxCilindro1
	private JTextField txtLenPruRxEje1
	private JTextField txtLenPruRxAdc1
	private JTextField txtLenPruRxDiam1
	private JTextField txtLenPruRxCB2
	private JTextField txtLenPruRxEsfera2
	private JTextField txtLenPruRxCilindro2
	private JTextField txtLenPruRxEje2
	private JTextField txtLenPruRxAdc2
	private JTextField txtLenPruRxDiam2
	
	private JTextField txtSobEsfera1
	private JTextField txtSobAV1
	private JTextField txtSobEsfera2
	private JTextField txtSobAV2
	
	private JCheckBox cbxOD
	private JTextField txtRxODCB
	private JTextField txtRxODEsfera
	private JTextField txtRxODCilindro
	private JTextField txtRxODEje
	private JTextField txtRxODAdc
	private JTextField txtRxODDiam
	private JComboBox cbRxODColor
	private JCheckBox cbxOI
	private JTextField txtRxOICB
	private JTextField txtRxOIEsfera
	private JTextField txtRxOICilindro
	private JTextField txtRxOIEje
	private JTextField txtRxOIAdc
	private JTextField txtRxOIDiam
	private JComboBox cbRxOIColor
	private JTextField txtObservaciones
	
	private JComboBox cbEjeTor
	private JComboBox cbOD
	private JTextField txtTorEjeCorr
	private JTextField txtODEjeCorr
	
	private JTextField txtIdEmpleado
	private JTextField txtNomEmpleado
	
	ContactLensesRegisterDialog(){
		buildUI()
	}

	
	// UI Layout Definition
	void buildUI( ) {
	  sb.dialog( this,
		  title: "Lentes de Contacto",
		  resizable: true,
		  pack: true,
		  modal: true,
		  preferredSize: [ 1100, 700 ],
		  //location: [ 200, 250 ],
	  ){
	  panel( layout: new MigLayout("fill,wrap 3","[fill][fill][fill]","1") ){
		  
		  panel( border: new TitledBorder("Refracción"), layout: new MigLayout("fill,wrap 5",
			  "[fill][fill][fill][fill][fill]") ){
			  label( text:"" )
			  label( text:"Esfera", border: new TitledBorder("") )
			  label( text:"Cilindro", border: new TitledBorder("") )
			  label( text:"Eje", border: new TitledBorder("") )
			  label( text:"Adc.", border: new TitledBorder("") )
			  label( text:"O.D.", border: new TitledBorder("") )
			  txtRefODEsfera = textField( )
			  txtRefODCilindro = textField( )
			  txtRefODEje = textField( )
			  txtRefODAdc = textField( )
			  label( text:"O.I.", border: new TitledBorder("") )
			  txtRefOIEsfera = textField( )
			  txtRefOICilindro = textField( )
			  txtRefOIEje = textField( )
			  txtRefOIAdc = textField( )
		  }
			  
		  panel( border: new TitledBorder("Queratometría"), layout: new MigLayout("fill,wrap 6",
			  "[fill][fill][fill][fill][fill][fill]") ){
			  label( text:"" )
			  label( text:"-Curvo", border: new TitledBorder("") )
			  label( text:"+Curvo", border: new TitledBorder("") )
			  label( text:"Eje", border: new TitledBorder("")  )
			  label( text:"Mm", border: new TitledBorder("") )
			  label( text:"mm", border: new TitledBorder("") )
			  label( text:"O.D.", border: new TitledBorder("") )
			  txtQueODmCurv = textField( )
			  txtQueODMCurv = textField( )
			  txtQueODEje = textField( )
			  txtQueODMm = textField( )
			  txtQueODmm = textField( )
			  label( text:"O.I.", border: new TitledBorder("") )
			  txtQueOImCurv = textField( )
			  txtQueOIMCurv = textField( )
			  txtQueOIEje = textField( )
			  txtQueOIMm = textField( )
			  txtQueOImm = textField( )
		  }
			  
		  panel( layout: new MigLayout("fill,wrap 2",
			  "[grow,fill][grow,fill]" ) ){
			  label( text:"Modelo lente:" )
			  label( text:"Diam.:" )
			  cbODModLente = comboBox( )
			  cbODDiam = comboBox( )
			  cbOIModLente = comboBox( )
			  cbOIDiam = comboBox( )
		  }
				  
		  panel( border: new TitledBorder("Lente Prueba teórico"), layout: new MigLayout("fill,wrap 7","[fill]") ){
			  label( text:"" )
			  label( text:"C.B." )
			  label( text:"Esfera" )
			  label( text:"Cil." )
			  label( text:"Eje" )
			  label( text:"Adc." )
			  label( text:"Diam." )
			  label(text:"O.D." )
			  txtLenPruODCB = textField( )
			  txtLenPruODEsfera = textField( )
			  txtLenPruODCil = textField( )
			  txtLenPruODEje = textField( )
			  txtLenPruODAdc = textField( )
			  txtLenPruODDiam = textField( )
			  label( text:"O.I." )
			  txtLenPruOICB = textField( )
			  txtLenPruOIEsfera = textField( )
			  txtLenPruOICil = textField( )
			  txtLenPruOIEje = textField( )
			  txtLenPruOIAdc = textField( )
			  txtLenPruOIDiam = textField( )
		  }
		  
		  panel( border: new TitledBorder("Lente Prueba Rx"), layout: new MigLayout("fill,wrap 6","[fill]") ){
			  label( text:"C.B.", border: new TitledBorder("") )
			  label( text:"Esfera", border: new TitledBorder("") )
			  label( text:"Cilindro", border: new TitledBorder("") )
			  label( text:"Eje", border: new TitledBorder("") )
			  label( text:"Adc.", border: new TitledBorder("") )
			  label( text:"Diam.", border: new TitledBorder("") )
			  txtLenPruRxCB1 = textField( )
			  txtLenPruRxEsfera1 = textField( )
			  txtLenPruRxCilindro1 = textField( )
			  txtLenPruRxEje1 = textField( )
			  txtLenPruRxAdc1 = textField( )
			  txtLenPruRxDiam1 = textField( )
			  txtLenPruRxCB2 = textField( )
			  txtLenPruRxEsfera2 = textField( )
			  txtLenPruRxCilindro2 = textField( )
			  txtLenPruRxEje2 = textField( )
			  txtLenPruRxAdc2 = textField( )
			  txtLenPruRxDiam2 = textField( )
		  }
		  
		  panel( border: new TitledBorder("Sobrerefracción"), layout: new MigLayout("fill,wrap 3","[center,grow,fill]1[fill]1[center,grow,fill]") ){
			  label( text:"Esfera", border: new TitledBorder("") )
			  label( text:"A.V.", constraints:"span 2", border: new TitledBorder("") )
			  txtSobEsfera1 = textField( )
			  label( text:"20/" )
			  txtSobAV1 = textField( )
			  txtSobEsfera2 = textField( )
			  label( text:"20/" )
			  txtSobAV2 = textField(  )
			  
		  }
		  
		  
		  panel( constraints:"span 2", layout: new MigLayout("fill,wrap 2","[fill][]") ){
			  label( text:"Orden" )
			  button( text:"D/N" )
		  }
	  
		  panel( layout: new MigLayout("fill") ){
			  
			  label( text:"" )
		  }
	  
		  
		  panel( border: new TitledBorder("Rx, tipo"), layout: new MigLayout("fill,wrap 8",
			  "[]1[fill]1[fill]1[fill]1[fill]1[fill]1[fill]1[grow,fill]") ){
			  label( text:"" )
			  label( text:"C.B.", border: new TitledBorder("") )
			  label( text:"Esfera", border: new TitledBorder("") )
			  label( text:"Cilindro", border: new TitledBorder("") )
			  label( text:"Eje", border: new TitledBorder("") )
			  label( text:"Adc.", border: new TitledBorder("") )
			  label( text:"Diam.", border: new TitledBorder("") )
			  label( text:"Color", border: new TitledBorder("") )
			  cbxOD = checkBox( text:"O.D." )
			  txtRxODCB = textField( )
			  txtRxODEsfera = textField( )
			  txtRxODCilindro = textField( )
			  txtRxODEje = textField( )
			  txtRxODAdc = textField( )
			  txtRxODDiam = textField( )
			  cbRxODColor = comboBox()
			  cbxOI = checkBox( text:"O.I." )
			  txtRxOICB = textField( )
			  txtRxOIEsfera = textField( )
			  txtRxOICilindro = textField( )
			  txtRxOIEje = textField( )
			  txtRxOIAdc = textField( )
			  txtRxOIDiam = textField( )
			  cbRxOIColor = comboBox( )
			  label( text:"Observaciones:" )
			  txtObservaciones = textField( minimumSize:[200,50], constraints:"span 7" )
		  }
			
		  panel( layout: new MigLayout("fill") ){
			  label( text:"" )
		  }
		  
		  panel( border: new TitledBorder("Eje-Tóricos     O.D."), layout: new MigLayout("fill,wrap 4",
			  "[fill][grow,fill][fill][grow,fill]") ){
			
			    cbEjeTor = comboBox(  )
				label( text:"" )
				cbOD = comboBox(  )
				label( text:"" )
				label( text:"", constraints:"span 2" )
				label( text:"", constraints:"span 2" )
				//label( icon: imageIcon( url: ImageLibrary.getURL( ImageLibrary.IMG_CIRCLE_MID ) ), constraints:"span 2" )
				//label( icon: imageIcon( url: ImageLibrary.getURL( ImageLibrary.IMG_CIRCLE_MID ) ), constraints:"span 2" )
				label( text:"Eje corr:" )
				txtTorEjeCorr = textField( )
				label( text:"Eje corr:" )
				txtODEjeCorr = textField( )
		  }
		  
		  panel(  layout: new MigLayout("fill,wrap 3","[][fill][grow,fill]") ){
			  label( text:"Examino:" )
			  txtIdEmpleado = textField( minimumSize:[45,25] )
			  txtNomEmpleado = textField( )
		  }
		  
		  panel( constraints:"span 2", layout: new MigLayout("fill,wrap") ){
			  label( text:"" )
		  }
		  
		  panel( constraints:"span 3", layout: new MigLayout( 'right', '[fill,150!]' )  ) {
			  button( text:"Guardar" )
			  button( text:"Limpiar" )
			  button( text:"Cerrar", actionPerformed: {dispose()} )
			}
		  
		  
		  
	  }
	  
	  }
	}
}
