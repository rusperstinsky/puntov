package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.model.Cotizacion
import mx.lux.pos.model.InstitucionIc
import mx.lux.pos.ui.controller.CustomerController
import mx.lux.pos.ui.controller.ItemController
import mx.lux.pos.ui.controller.QuoteController
import mx.lux.pos.ui.model.Item
import mx.lux.pos.ui.model.Quote
import mx.lux.pos.ui.model.UpperCaseDocument
import mx.lux.pos.ui.model.adapter.AgreementAdapter
import mx.lux.pos.ui.view.component.ComboBoxHintSelector
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.StringUtils

import java.awt.Dimension
import java.text.NumberFormat
import javax.swing.border.TitledBorder
import java.awt.event.*
import javax.swing.*

class QuoteDialog extends JDialog implements ItemListener, FocusListener {

  //implements ItemListener

  SwingBuilder sb = new SwingBuilder()

  private JComboBox cbTitulo
  private JTextField txtCliente
  private JTextField txtTelefono
  private JCheckBox cbxTotalizar
  private JCheckBox cbxConvenio
  private JComboBox cbConvenio
  private JTextField txtObservaciones

  private JTextField txtCant1
  private JTextField txtCodigo1
  private JTextField txtColor1
  private JTextField txtPrecio1
  private JTextField txtCant2
  private JTextField txtCodigo2
  private JTextField txtColor2
  private JTextField txtPrecio2
  private JTextField txtCant3
  private JTextField txtCodigo3
  private JTextField txtColor3
  private JTextField txtPrecio3
  private JTextField txtCant4
  private JTextField txtCodigo4
  private JTextField txtColor4
  private JTextField txtPrecio4
  private JTextField txtCant5
  private JTextField txtCodigo5
  private JTextField txtColor5
  private JTextField txtPrecio5

  private Cotizacion cotizacion
  private static final String ARTICULO_1 = '1'
  private static final String ARTICULO_2 = '2'
  private static final String ARTICULO_3 = '3'
  private static final String ARTICULO_4 = '4'
  private static final String ARTICULO_5 = '5'
  private static final String COLOR_DE_ARTICULO_1 = 'color1'
  private static final String COLOR_DE_ARTICULO_2 = 'color2'
  private static final String COLOR_DE_ARTICULO_3 = 'color3'
  private static final String COLOR_DE_ARTICULO_4 = 'color4'
  private static final String COLOR_DE_ARTICULO_5 = 'color5'

  private static final String MSG_BUSQUEDA_TITULO = 'Es necesario ingresar una b\u00fasqeda v\u00e1lida'
  private static final String DLG_BUSQUEDA_ERROR = "B\u00fasqueda inv\u00e1lida"

  String titulo
  String cliente
  String telefono
  String observaciones
  String convenio
  String codigo1
  String color1
  String precio1
  String cant2
  String codigo2
  String color2
  String precio2
  String cant3
  String codigo3
  String color3
  String precio3
  String cant4
  String codigo4
  String color4
  String precio4
  String cant5
  String codigo5
  String color5
  String precio5
  //Integer idArticulo1
  //Integer idArticulo2
  //Integer idArticulo3
  //Integer idArticulo4
  FocusListener colorFocus1
  FocusListener colorFocus2
  FocusListener colorFocus3
  FocusListener colorFocus4
  String idConvenio
  boolean respondingToCombo = false
  String action
  Item item1 = new Item()
  Item item2 = new Item()
  Item item3 = new Item()
  Item item4 = new Item()
  Item item5 = new Item()
  Quote quote
  AgreementAdapter agreementAdapter = new AgreementAdapter()
  List<InstitucionIc> lstConvenios = new ArrayList<InstitucionIc>()

  List<LinkedHashMap<String, Object>> titulos
  ComboBoxHintSelector<InstitucionIc> comboAgreement = new ComboBoxHintSelector<InstitucionIc>( new AgreementAdapter() )

  Boolean printRequested

  QuoteDialog( ) {
    quote = new Quote()
    titulos = CustomerController.findAllCustomersTitles()
    buildUI()

  }

  protected void setPrintRequested( Boolean pRequested ) {
    this.printRequested = pRequested
  }

  // UI Layout Definition
  void buildUI( ) {
    sb.dialog( this,
        title: "Cotización",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 700, 400 ] as Dimension
    ) {
      panel( layout: new MigLayout( "center,fill, wrap", "[center,grow,fill]" ) ) {

        panel( layout: new MigLayout( "fill,wrap 5",
            "[][grow,fill][][grow,fill][grow,fill]" ) ) {
          label( text: "Título:" )
          cbTitulo = comboBox( items: titulos*.title, editable: true )
          label( text: "Cliente:" )
          txtCliente = textField( constraints: "span 3", document: new UpperCaseDocument() )
          label( text: "Teléfono:" )
          txtTelefono = textField( document: new UpperCaseDocument() )
          cbxTotalizar = checkBox( text: "Totalizar" )
          cbxConvenio = checkBox( text: "Convenio", selected: false, actionPerformed: doConvenio )
          cbConvenio = comboBox( editable: true, enabled: false, actionPerformed: doConvenioSearch )
          cbConvenio.addItemListener( this )
          label( text: "Observaciones:" )
          txtObservaciones = textField( constraints: "span 4", document: new UpperCaseDocument() )
        }

        panel( border: new TitledBorder( "" ), autoscrolls: true, layout: new MigLayout( "center,fill,wrap 4",
            "50[center,fill]1[center,fill]1[center,fill]1[right,fill]50", "[]1[]" ) ) {
          label( text: "Cant.", border: new TitledBorder( "" ) )
          label( text: "Código", border: new TitledBorder( "" ) )
          label( text: "Color", border: new TitledBorder( "" ) )
          label( text: "Precio", border: new TitledBorder( "" ) )

          txtCant1 = textField( editable: false )
          txtCodigo1 = textField( document: new UpperCaseDocument(), actionPerformed: doItemSearch, name: ARTICULO_1 )
          txtColor1 = textField( document: new UpperCaseDocument(), name: COLOR_DE_ARTICULO_1 )
          txtColor1.addFocusListener( this )
          txtPrecio1 = textField( editable: false )

          txtCant2 = textField( editable: false )
          txtCodigo2 = textField( document: new UpperCaseDocument(), actionPerformed: doItemSearch, name: ARTICULO_2 )
          txtColor2 = textField( document: new UpperCaseDocument(), name: COLOR_DE_ARTICULO_2 )
          txtColor2.addFocusListener( this )
          txtPrecio2 = textField( editable: false )


          txtCant3 = textField( editable: false )
          txtCodigo3 = textField( document: new UpperCaseDocument(), actionPerformed: doItemSearch, name: ARTICULO_3 )
          txtColor3 = textField( document: new UpperCaseDocument(), name: COLOR_DE_ARTICULO_3 )
          txtColor3.addFocusListener( this )
          txtPrecio3 = textField( editable: false )

          txtCant4 = textField( editable: false )
          txtCodigo4 = textField( document: new UpperCaseDocument(), actionPerformed: doItemSearch, name: ARTICULO_4 )
          txtColor4 = textField( document: new UpperCaseDocument(), name: COLOR_DE_ARTICULO_4 )
          txtColor4.addFocusListener( this )
          txtPrecio4 = textField( editable: false )

          txtCant5 = textField( editable: false )
          txtCodigo5 = textField( document: new UpperCaseDocument(), actionPerformed: doItemSearch, name: ARTICULO_5 )
          txtColor5 = textField( document: new UpperCaseDocument(), name: COLOR_DE_ARTICULO_5 )
          txtColor5.addFocusListener( this )
          txtPrecio5 = textField( editable: false )

        }

        panel( layout: new MigLayout( "right", "[fill,110!]" ) ) {
          button( text: "Imprimir", actionPerformed: {onPrint()} )
          //button(text: "Facturar")
          button( text: "Cerrar", actionPerformed: { dispose() } )
        }

      }
    }
  }

  // Public methods
  void activate( ) {
    this.setPrintRequested( false )
    this.setVisible( true )
  }

  private def doItemSearch = { ActionEvent ev ->
    JTextField source = ev.source as JTextField
    String input = source.text
    String nombre = source.name
    String color = ""
    if ( nombre.equalsIgnoreCase( ARTICULO_1 ) )
      color = txtColor1.text.trim()
    if ( nombre.equalsIgnoreCase( ARTICULO_2 ) )
      color = txtColor2.text.trim()
    if ( nombre.equalsIgnoreCase( ARTICULO_3 ) )
      color = txtColor3.text.trim()
    if ( nombre.equalsIgnoreCase( ARTICULO_4 ) )
      color = txtColor4.text.trim()
    if ( nombre.equalsIgnoreCase( ARTICULO_5 ) )
      color = txtColor5.text.trim()

    if ( StringUtils.isNotBlank( input ) ) {
      sb.doOutside {
        limpiarCampos( nombre )
        List<Item> results = ItemController.findItemByArticleAndColor( input, color )

        if ( results?.any() ) {
          println "RESULTADO:: ${results.dump()}"
          Item item = results.first()

          llenarArticulo( item, nombre )
        } else {
          optionPane( message: "No se encontraron resultados para: ${input}", optionType: JOptionPane.DEFAULT_OPTION )
              .createDialog( source, "B\u00fasqueda: ${input}" )
              .show()
        }
      }
    } else {
      sb.optionPane( message: MSG_BUSQUEDA_TITULO, optionType: JOptionPane.DEFAULT_OPTION )
          .createDialog( source, DLG_BUSQUEDA_ERROR )
          .show()
    }
  }


  private void doColor( String color ) {
    //JTextField source = ev.source as JTextField
    //String input = source.text
    //String nombre = source.name
    String articulo = ""
    String coulor = ""
    boolean condicion = false

    if ( color.equalsIgnoreCase( COLOR_DE_ARTICULO_1 ) && StringUtils.isNotBlank( txtCodigo1.text ) ) {
      articulo = txtCodigo1.text.trim()
      coulor = txtColor1.text.trim()
      condicion = true
    }
    if ( color.equalsIgnoreCase( COLOR_DE_ARTICULO_2 ) && StringUtils.isNotBlank( txtCodigo2.text ) ) {
      articulo = txtCodigo2.text.trim()
      coulor = txtColor2.text.trim()
      condicion = true
    }
    if ( color.equalsIgnoreCase( COLOR_DE_ARTICULO_3 ) && StringUtils.isNotBlank( txtCodigo3.text ) ) {
      articulo = txtCodigo3.text.trim()
      coulor = txtColor3.text.trim()
      condicion = true
    }
    if ( color.equalsIgnoreCase( COLOR_DE_ARTICULO_4 ) && StringUtils.isNotBlank( txtCodigo4.text ) ) {
      articulo = txtCodigo4.text.trim()
      coulor = txtColor4.text.trim()
      condicion = true
    }
    if ( color.equalsIgnoreCase( COLOR_DE_ARTICULO_5 ) && StringUtils.isNotBlank( txtCodigo5.text ) ) {
      articulo = txtCodigo5.text.trim()
      coulor = txtColor5.text.trim()
      condicion = true
    }

    if ( condicion ) {
      sb.doOutside {
        limpiarCampos( color )
        List<Item> results = ItemController.findItemByArticleAndColor( articulo, coulor )

        if ( results?.any() && results.size() > 0 ) {
          println "RESULTADO:: ${results.dump()}"
          Item item = results.first()
          llenarArticulo( item, color )
        } else {
          limpiarCampos( color )
          optionPane( message: "No se encontraron resultados para: ${coulor}", optionType: JOptionPane.DEFAULT_OPTION )
              .createDialog( "B\u00fasqueda: ${coulor}" )
              .show()
        }
      }
    } else {
      limpiarCampos( color )
      sb.optionPane( message: MSG_BUSQUEDA_TITULO, optionType: JOptionPane.DEFAULT_OPTION )
          .createDialog( new JTextField(), DLG_BUSQUEDA_ERROR )
          .show()
    }
  }

  public void focusGained( FocusEvent e ) {
    println e.getComponent().name
  }

  public void focusLost( FocusEvent e ) {

    if ( e.component.name.equalsIgnoreCase( COLOR_DE_ARTICULO_1 ) ) {
      doColor( COLOR_DE_ARTICULO_1 )
    }
    if ( e.component.name.equalsIgnoreCase( COLOR_DE_ARTICULO_2 ) ) {
      doColor( COLOR_DE_ARTICULO_2 )
    }
    if ( e.component.name.equalsIgnoreCase( COLOR_DE_ARTICULO_3 ) ) {
      doColor( COLOR_DE_ARTICULO_3 )
    }
    if ( e.component.name.equalsIgnoreCase( COLOR_DE_ARTICULO_4 ) ) {
      doColor( COLOR_DE_ARTICULO_4 )
    }
    if ( e.component.name.equalsIgnoreCase( COLOR_DE_ARTICULO_5 ) ) {
      doColor( COLOR_DE_ARTICULO_5 )
    }
  }

  private def doConvenioSearch = { ActionEvent ev ->
    JComboBox source = ev.source as JComboBox
    String input = source.getSelectedItem().toString().toUpperCase()

    action = ev.actionCommand
    println "ActionCommand:: $action "
    if ( StringUtils.isNotBlank( input ) ) {
      if ( ev.actionCommand.equalsIgnoreCase( "comboBoxEdited" ) || !ev.actionCommand.equalsIgnoreCase( "comboBoxChanged" ) ) {
        cbConvenio.removeAllItems()
        respondingToCombo = true
        sb.doLater {
          lstConvenios = QuoteController.ObtenerConvenio( input )
          if ( lstConvenios?.any() ) {
            println "RESULTADO:: ${lstConvenios.size()}"
            for ( InstitucionIc convenio : lstConvenios ) {
              println "Convenios:: ${convenio.dump()}"
              cbConvenio.addItem( agreementAdapter.getText( convenio ) )
            }

          } else {
            optionPane( message: "No se encontraron resultados para: ${input}", optionType: JOptionPane.DEFAULT_OPTION )
                .createDialog( source, "B\u00fasqueda: ${input}" )
                .show()
          }
          respondingToCombo = false
        }
      }
    } else {
      sb.optionPane( message: MSG_BUSQUEDA_TITULO, optionType: JOptionPane.DEFAULT_OPTION )
          .createDialog( source, DLG_BUSQUEDA_ERROR )
          .show()
    }
  }



  private def doConvenio = {
    if ( getCbxConvenio() ) {
      cbConvenio.enabled = true
    } else {
      cbConvenio.enabled = false
      cbConvenio.removeAllItems()
      if ( txtCodigo1.text.length() > 0 ) {
        llenarArticulo( item1, ARTICULO_1 )
      }
      if ( txtCodigo2.text.length() > 0 ) {
        llenarArticulo( item2, ARTICULO_2 )
      }
      if ( txtCodigo3.text.length() > 0 ) {
        llenarArticulo( item3, ARTICULO_3 )
      }
      if ( txtCodigo4.text.length() > 0 ) {
        llenarArticulo( item4, ARTICULO_4 )
      }
      if ( txtCodigo5.text.length() > 0 ) {
        llenarArticulo( item5, ARTICULO_5 )
      }
    }
  }


  private void llenarArticulo( Item item, String nombre ) {
    NumberFormat formatter = NumberFormat.getCurrencyInstance( Locale.US )
    if ( nombre.equalsIgnoreCase( COLOR_DE_ARTICULO_1 ) || nombre.equalsIgnoreCase( ARTICULO_1 ) ) {
      txtCant1.setText( ARTICULO_1 )
      txtCodigo1.setText( item.name )
      txtColor1.setText( item.color )
      //idArticulo1 = item.id
      item1 = item
      if ( cbxConvenio.selected && cbConvenio.itemCount > 0 ) {
        InstitucionIc convenio = agreementAdapter.findObject( cbConvenio.selectedItem as String, lstConvenios )
        if ( convenio != null ) {
          BigDecimal precio = QuoteController.obtenerPrecio( item.id, convenio.id, item.price )
          String formatPrice = formatter.format( precio )
          txtPrecio1.setText( formatPrice )
        }
      } else {
        txtPrecio1.text = formatter.format( item.price )
      }

    }
    if ( nombre.equalsIgnoreCase( COLOR_DE_ARTICULO_2 ) || nombre.equalsIgnoreCase( ARTICULO_2 ) ) {
      txtCant2.setText( ARTICULO_2 )
      txtCodigo2.setText( item.name )
      txtColor2.setText( item.color )
      //idArticulo2 = item.id
      item2 = item
      if ( cbxConvenio.selected && cbConvenio.itemCount > 0 ) {
        InstitucionIc convenio = agreementAdapter.findObject( cbConvenio.selectedItem as String, lstConvenios )
        if ( convenio != null ) {
          BigDecimal precio = QuoteController.obtenerPrecio( item.id, convenio.id, item.price )
          String formatPrice = formatter.format( precio )
          txtPrecio2.setText( formatPrice )
        }
      } else {
        txtPrecio2.text = formatter.format( item.price )
      }

    }
    if ( nombre.equalsIgnoreCase( COLOR_DE_ARTICULO_3 ) || nombre.equalsIgnoreCase( ARTICULO_3 ) ) {
      txtCant3.setText( ARTICULO_3 )
      txtCodigo3.setText( item.name )
      txtColor3.setText( item.color )
      //idArticulo3 = item.id
      item3 = item
      if ( cbxConvenio.selected && cbConvenio.size() > 0 ) {
        InstitucionIc convenio = agreementAdapter.findObject( cbConvenio.selectedItem as String, lstConvenios )
        if ( convenio != null ) {
          BigDecimal precio = QuoteController.obtenerPrecio( item.id, convenio.id, item.price )
          String formatPrice = formatter.format( precio )
          txtPrecio3.setText( formatPrice )
        }
      } else {
        txtPrecio3.text = formatter.format( item.price )
      }

    }
    if ( nombre.equalsIgnoreCase( COLOR_DE_ARTICULO_4 ) || nombre.equalsIgnoreCase( ARTICULO_4 ) ) {
      txtCant4.setText( ARTICULO_4 )
      txtCodigo4.setText( item.name )
      txtColor4.setText( item.color )
      //idArticulo4 = item.id
      item4 = item
      if ( cbxConvenio.selected && cbConvenio.size() > 0 ) {
        InstitucionIc convenio = agreementAdapter.findObject( cbConvenio.selectedItem as String, lstConvenios )
        if ( convenio != null ) {
          BigDecimal precio = QuoteController.obtenerPrecio( item.id, convenio.id, item.price )
          String formatPrice = formatter.format( precio )
          txtPrecio4.setText( formatPrice )
        }
      } else {
        txtPrecio4.text = formatter.format( item.price )
      }

    }
    if ( nombre.equalsIgnoreCase( COLOR_DE_ARTICULO_5 ) || nombre.equalsIgnoreCase( ARTICULO_5 ) ) {
      txtCant5.setText( ARTICULO_5 )
      txtCodigo5.setText( item.name )
      txtColor5.setText( item.color )
      item5 = item
      if ( cbxConvenio.selected && cbConvenio.size() > 0 ) {
        InstitucionIc convenio = agreementAdapter.findObject( cbConvenio.selectedItem as String, lstConvenios )
        if ( convenio != null ) {
          BigDecimal precio = QuoteController.obtenerPrecio( item.id, convenio.id, item.price )
          String formatPrice = formatter.format( precio )
          txtPrecio5.setText( formatPrice )
        }
      } else {
        txtPrecio5.text = formatter.format( item.price )
      }

    }
  }

  private void limpiarCampos( String nombre ) {
    if ( nombre.equalsIgnoreCase( ARTICULO_1 ) ) {
      txtCant1.setText( null )
      txtColor1.setText( null )
      txtPrecio1.setText( null )
    }
    if ( nombre.equalsIgnoreCase( ARTICULO_2 ) ) {
      txtCant2.setText( null )
      txtColor2.setText( null )
      txtPrecio2.setText( null )
    }
    if ( nombre.equalsIgnoreCase( ARTICULO_3 ) ) {
      txtCant3.setText( null )
      txtColor3.setText( null )
      txtPrecio3.setText( null )
    }
    if ( nombre.equalsIgnoreCase( ARTICULO_4 ) ) {
      txtCant4.setText( null )
      txtColor4.setText( null )
      txtPrecio4.setText( null )
    }
    if ( nombre.equalsIgnoreCase( ARTICULO_5 ) ) {
      txtCant5.setText( null )
      txtColor5.setText( null )
      txtPrecio5.setText( null )
    }
  }


  void itemStateChanged( ItemEvent e ) {
    if ( action != null && getCbxConvenio() && cbConvenio.itemCount > 0 && e.getStateChange() == ItemEvent.SELECTED ) {

      //cbConvenio.enabled = false
      if ( txtCodigo1.text.length() > 0 ) {
        llenarArticulo( item1, ARTICULO_1 )
      }
      if ( txtCodigo2.text.length() > 0 ) {
        llenarArticulo( item2, ARTICULO_2 )
      }
      if ( txtCodigo3.text.length() > 0 ) {
        llenarArticulo( item3, ARTICULO_3 )
      }
      if ( txtCodigo4.text.length() > 0 ) {
        llenarArticulo( item4, ARTICULO_4 )
      }
      if ( txtCodigo5.text.length() > 0 ) {
        llenarArticulo( item5, ARTICULO_5 )
      }
    }

  }

  private def onCerrar = {
    this.setVisible( false )
    this.setPrintRequested( false )

  }

  String getCliente( ) {
    return cliente
  }

  String getTelefono( ) {
    return telefono
  }

  String getConvenioDesc( ) {
    return convenio
  }

  String getCodigo1( ) {
    return codigo1
  }

  String getColor1( ) {
    return color1
  }

  String getPrecio1( ) {
    return precio1
  }

  String getCant2( ) {
    return cant2
  }

  String getCodigo2( ) {
    return codigo2
  }

  String getColor2( ) {
    return color2
  }

  String getPrecio2( ) {
    return precio2
  }

  String getCant3( ) {
    return cant3
  }

  String getCodigo3( ) {
    return codigo3
  }

  String getColor3( ) {
    return color3
  }

  String getPrecio3( ) {
    return precio3
  }

  String getCant4( ) {
    return cant4
  }

  String getCodigo4( ) {
    return codigo4
  }

  String getColor4( ) {
    return color4
  }

  String getPrecio4( ) {
    return precio4
  }

  String getCant5( ) {
    return cant5
  }

  String getCodigo5( ) {
    return codigo5
  }

  String getColor5( ) {
    return color5
  }

  String getPrecio5( ) {
    return precio5
  }

  String getTitulo( ) {
    return titulo
  }

  boolean getCbxTotalizar( ) {
    return cbxTotalizar.selected
  }

  boolean getCbxConvenio( ) {
    return cbxConvenio.selected
  }

  String getConvenio( ) {
    if ( cbConvenio.itemCount > 0 && !cbConvenio.getSelectedItem().toString().isEmpty() ) {
      return cbConvenio.getSelectedItem().toString()
    }
    return null
  }

  protected void onPrint( ) {
    this.setPrintRequested( true )
    this.dispose()
    cliente = txtCliente.getText().trim()
    telefono = txtTelefono.getText().trim()
    observaciones = txtObservaciones.getText().trim()
    convenio = cbConvenio.getSelectedItem().toString()
    codigo1 = txtCodigo1.getText().trim()
    color1 = txtColor1.getText().trim()
    precio1 = txtPrecio1.getText().trim()
    cant2 = txtCant2.getText().trim()
    codigo2 = txtCodigo2.getText().trim()
    color2 = txtColor2.getText().trim()
    precio2 = txtPrecio2.getText().trim()
    cant3 = txtCant3.getText().trim()
    codigo3 = txtCodigo3.getText().trim()
    color3 = txtColor3.getText().trim()
    precio3 = txtPrecio3.getText().trim()
    cant4 = txtCant4.getText().trim()
    codigo4 = txtCodigo4.getText().trim()
    color4 = txtColor4.getText().trim()
    precio4 = txtPrecio4.getText().trim()
    cant5 = txtCant5.getText().trim()
    codigo5 = txtCodigo5.getText().trim()
    color5 = txtColor5.getText().trim()
    precio5 = txtPrecio5.getText().trim()
    titulo = cbTitulo.getSelectedItem().toString()

  }

}
