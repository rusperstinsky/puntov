package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.model.Generico
import mx.lux.pos.ui.model.InvOhData
import mx.lux.pos.ui.model.InvOhListener
import mx.lux.pos.ui.model.adapter.BrandListModelAdapter
import mx.lux.pos.ui.model.adapter.GenreListModelAdapter
import mx.lux.pos.ui.resources.UI_Standards
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import javax.swing.*

class InvOhTicketDialog extends JDialog implements InvOhListener {

  private static final String TXT_DIALOG_TITLE = "Imprimir ticket con resumen de existencias"
  private static final String TXT_INSTRUCTIONS = "Seleccione la información a incluir en el ticket."
  private static final String TXT_BRAND_LABEL = "Marca"
  private static final String TXT_GENRE_LABEL = "Familia"
  private static final String TXT_PART_LABEL = "Articulo"
  private static final String TXT_COUNT_SUMMARY = "%d %ss"
  private static final String TXT_TOTAL_SUMMARY = "%d unidades"
  private static final String TXT_CANCEL_CAPTION = "Cancelar"
  private static final String TXT_PRINT_CAPTION = "Imprimir"
  private static final String ALL_SELECTED = "Todos"

  private Logger logger = LoggerFactory.getLogger( this.getClass() )

  InvOhData ohData
  GenreListModelAdapter genreModel
  BrandListModelAdapter brandModel
  JComboBox genreSelector, brandSelector
  Boolean printRequested
  JLabel lblCount, lblTotal

  private SwingBuilder sb = new SwingBuilder()

  private Font bigText

  InvOhTicketDialog( ) {
    init()
    buildUI()
    setupTriggers()
  }

  // Internal Methods
  private String asId( String pId ) {
    return StringUtils.trimToEmpty( pId ).toUpperCase()
  }

  protected void init( ) {
    bigText = new Font( '', Font.BOLD, 14 )
    genreModel = new GenreListModelAdapter()
    brandModel = new BrandListModelAdapter()
  }

  protected void setPrintRequested( Boolean pRequested ) {
    this.printRequested = pRequested
  }

  // Dialog Layout 
  protected void buildUI( ) {
    sb.dialog( this,
        title: TXT_DIALOG_TITLE,
        location: [ 250, 75 ],
        preferredSize: [ 250, 250 ],
        resizable: false,
        modal: true,
        pack: true,
    ) {
      borderLayout()
      panel( constraints: BorderLayout.PAGE_START,
          border: BorderFactory.createEmptyBorder( 10, 10, 5, 10 )
      ) {
        borderLayout()
        label( text: TXT_INSTRUCTIONS,
            constraints: BorderLayout.LINE_START
        )
      }
      composeSelectionPane()
      //composeTicketPane()
      composeButtonPane()
    }
  }

  protected JComponent composeSelectionPane( ) {
    sb.panel( constraints: BorderLayout.CENTER,
        layout: new MigLayout( "wrap 2", "10[][fill,grow]10", "[]5[]20[]5[]" )
    ) {
      label( text: TXT_GENRE_LABEL )
      genreSelector = comboBox( model: genreModel,
          actionPerformed: { onGenreValueChanged() }
      )
      label( text: TXT_BRAND_LABEL )
      brandSelector = comboBox( model: brandModel,
          actionPerformed: { onBrandValueChanged() }
      )
      lblCount = label( text: TXT_COUNT_SUMMARY, constraints: "span 2, grow",
          font: bigText, horizontalAlignment: SwingConstants.CENTER
      )
      lblTotal = label( text: TXT_TOTAL_SUMMARY, constraints: "span 2, grow",
          font: bigText, horizontalAlignment: SwingConstants.CENTER
      )
    }
  }

  protected JComponent composeTicketPane( ) {
    sb.panel( constraints: BorderLayout.LINE_END,
        border: BorderFactory.createEmptyBorder( 0, 0, 20, 10 ),
        layout: new MigLayout( "", "[fill,grow]", "[fill,grow]" ),
        preferredSize: [ 300, 100 ]
    ) {
      textField( editable: false, background: Color.WHITE )
    }
  }

  protected JComponent composeButtonPane( ) {
    sb.panel( constraints: BorderLayout.PAGE_END ) {
      borderLayout()
      panel( constraints: BorderLayout.LINE_END ) {
        button( text: TXT_PRINT_CAPTION,
            preferredSize: UI_Standards.BUTTON_SIZE,
            actionPerformed: { onButtonPrint() }
        )
        button( text: TXT_CANCEL_CAPTION,
            preferredSize: UI_Standards.BUTTON_SIZE,
            actionPerformed: { onButtonCancel() }
        )
      }
    }
  }

  protected void setBrandList( String pIdGenre ) {
    logger.debug( "Set Brand List " )
    brandModel.setupBrandList( ohData.getBrandList( asId( pIdGenre ) ) )
  }

  protected void setupTriggers( ) {

  }

  // Public methods
  void activate( ) {
    this.setPrintRequested( false )
    this.setVisible( true )
  }

  String getBrandSelected( ) {
    logger.debug( "Get Brand Selected" )
    String brand = null
    if ( this.printRequested ) {
      brand = ( brandModel.isAllSelected() ? "" : asId( brandModel.getSelectedBrand() ) )
    }
    return brand
  }

  String getGenreSelected( ) {
    logger.debug( "Get Genre Selected" )
    String genre = null
    if ( this.printRequested ) {
      genre = ( genreModel.isAllSelected() ? "" : asId( genreModel.getSelectedGenre().id ) )
    }
    return genre
  }

  void notifyDataChanged( ) {
    logger.debug( "Notify Data Changed" )
    Collection<Generico> genreList = ohData.genreList
    genreModel.setupGenreList( genreList )
    if ( ( genreList.size() > 1 ) || ( genreList.size() == 0 ) ) {
      genreSelector.setSelectedIndex( 0 )
    } else {
      genreSelector.setSelectedIndex( 1 )
    }
    onGenreValueChanged()
  }

  void setOhData( InvOhData pData ) {
    logger.debug( String.format( "Set OhData: %s", pData.getClass().getSimpleName() ) )
    ohData = pData
    notifyDataChanged()
  }

  // UI Performance  
  protected void onButtonCancel( ) {
    logger.debug( "On Button Cancel" )
    this.setVisible( false )
  }

  protected void onButtonPrint( ) {
    logger.debug( "On Button Print" )
    this.setPrintRequested( true )
    this.setVisible( false )
  }

  protected void onBrandValueChanged( ) {
    logger.debug( String.format( "BrandValueChanged Trigger: %s", brandModel.getSelectedBrand() ) )
    if ( brandModel.isAllSelected() ) {
      StringBuffer sb = new StringBuffer()
      String idGenre = asId( genreModel.getSelectedGenre().id )
      Integer qty = 0
      Integer nBrand = 0
      for ( String brand : ohData.getBrandList( idGenre ) ) {
        Integer qtyOh = ohData.getQtyOh( idGenre, asId( brand ) )
        qty += qtyOh
        nBrand++
        sb.append( String.format( "\n(%s:%s) Exist:%d", idGenre, brand, qtyOh ) )
      }
      logger.debug( sb.toString() )
      this.lblCount.text = String.format( TXT_COUNT_SUMMARY, nBrand, TXT_BRAND_LABEL )
      this.lblTotal.text = String.format( TXT_TOTAL_SUMMARY, qty )
    } else {
      StringBuffer sb = new StringBuffer()
      String idGenre = asId( genreModel.getSelectedGenre().id )
      String brand = asId( brandModel.getSelectedBrand() )
      Integer qty = 0
      Integer nParts = 0
      for ( String partNbr : ohData.getPartList( idGenre, brand ) ) {
        Integer qtyOh = ohData.getQtyOh( idGenre, brand, asId( partNbr ) )
        qty += qtyOh
        nParts++
        sb.append( String.format( "\n(%s:%s) PartNbr:%s, Exist:%d", idGenre, brand, partNbr, qtyOh ) )
      }
      logger.debug( sb.toString() )
      this.lblCount.text = String.format( TXT_COUNT_SUMMARY, nParts, TXT_PART_LABEL )
      this.lblTotal.text = String.format( TXT_TOTAL_SUMMARY, qty )
    }
  }

  protected void onGenreValueChanged( ) {
    logger.debug( String.format( "GenreValueChanged Trigger: %s", genreModel.getSelectedGenre() ) )
    if ( genreModel.isAllSelected() ) {
      StringBuffer sb = new StringBuffer()
      Integer qty = 0
      Integer nGenre = 0
      for ( Generico g : ohData.genreList ) {
        Integer qtyOh = ohData.getQtyOh( asId( g.id ) )
        qty += qtyOh
        nGenre++
        sb.append( String.format( "\n(%s - %s) Exist:%d", g.id, g.descripcion, qtyOh ) )
      }
      logger.debug( sb.toString() )
      this.lblCount.text = String.format( TXT_COUNT_SUMMARY, nGenre, TXT_GENRE_LABEL )
      this.lblTotal.text = String.format( TXT_TOTAL_SUMMARY, qty )
    } else {
      setBrandList( genreModel.getSelectedGenre().id )
      brandSelector.setSelectedIndex( 0 )
      onBrandValueChanged()
    }
  }

}
