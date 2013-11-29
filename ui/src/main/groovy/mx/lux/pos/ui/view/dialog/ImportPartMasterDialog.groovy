package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.MainWindow
import mx.lux.pos.ui.model.FileList
import mx.lux.pos.ui.resources.UI_Standards
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JOptionPane
import javax.swing.JTextArea
import java.awt.*
import mx.lux.pos.ui.controller.IOController
import mx.lux.pos.ui.model.file.FileFilteredList
import mx.lux.pos.ui.resources.ServiceManager

class ImportPartMasterDialog extends JDialog {

  private static final String TXT_DIALOG_TITLE = MainWindow.TEXT_LOAD_PARTS_TITLE
  private static final String TXT_BUTTON_CANCEL = 'Cancelar'
  private static final String TXT_BUTTON_IMPORT = 'Importar'
  private static final String MSG_LONG_RUN_WARNING = 'Este proceso puede durar varios minutos.'

  private Logger logger = LoggerFactory.getLogger( this.getClass() )
  private SwingBuilder sb = new SwingBuilder()

  private FileFilteredList filesToLoad
  private JTextArea txtFiles
  private JButton btnCancel
  private JButton btnImport

  Component parent

  ImportPartMasterDialog( ) {
    this.parent = MainWindow.instance
    buildUI()
    setupTriggers()
  }

  // Compose UI
  protected void buildUI( ) {
    sb.dialog( this,
        title: TXT_DIALOG_TITLE,
        location: [ 200, 75 ] as Point,
        preferredSize: [ 400, 300 ] as Dimension,
        resizable: false,
        modal: true,
        pack: true
    ) {
      borderLayout()
      panel( constraints: BorderLayout.PAGE_START,
          border: BorderFactory.createEmptyBorder( 10, 10, 5, 10 )
      ) {
        borderLayout()
        label( text: MSG_LONG_RUN_WARNING,
            constraints: BorderLayout.CENTER
        )
      }
      this.composeContentsPane()
      sb.panel( constraints: BorderLayout.PAGE_END ) {
        borderLayout()
        panel( constraints: BorderLayout.LINE_END ) {
          this.btnCancel = button( text: TXT_BUTTON_CANCEL,
              preferredSize: UI_Standards.BUTTON_SIZE,
              actionPerformed: { onButtonCancel() }
          )
          this.btnImport = button( text: TXT_BUTTON_IMPORT,
              preferredSize: UI_Standards.BUTTON_SIZE,
              actionPerformed: { onButtonImport() }
          )
        }
      }
    }
  }

  protected void composeContentsPane( ) {
    sb.panel( constraints: BorderLayout.CENTER,
        border: BorderFactory.createEmptyBorder( 5, 10, 5, 10 )
    ) {
      borderLayout()
      label( text: 'Archivos a importar',
          constraints: BorderLayout.PAGE_START
      )
      this.txtFiles = textArea(
          constraints: BorderLayout.CENTER,
          editable: false,
          border: BorderFactory.createLineBorder( Color.BLACK )
      )
    }
  }

  protected void setupTriggers( ) {

  }

  // UI Management
  protected void startImport( ) {
    while( !this.txtFiles.text.isEmpty() ) {
      File f = this.filesToLoad.pop()
      String errors = IOController.getInstance().dispatchImportPartMaster(f)
      sb.edt( { this.txtFiles.setText( this.filesToLoad.toString() ) } )
      if( errors.length() > 0 ){
        JOptionPane.showMessageDialog( new JDialog(), "No se cargaron los siguientes articulos: ${errors}", "Error al cargar",
                JOptionPane.INFORMATION_MESSAGE )
      }
    }
    this.visible = false
  }

  protected void refreshUI( ) {
    this.txtFiles.text = ''
    if ( !this.filesToLoad.isEmpty() ) {
      this.txtFiles.text = this.filesToLoad.toString()
    }
  }

  // Public methods
  void activate( ) {
    File incomingPath = ServiceManager.ioServices.getIncomingLocation()
    for ( File f : incomingPath.listFiles() ) {
      this.filesToLoad.add( f )
    }
    this.refreshUI()
    this.show()
  }

  void setFilenamePattern( String pPattern ) {
    this.filesToLoad = new FileFilteredList( pPattern )
  }

  // UI Response
  protected void onButtonCancel( ) {
    this.visible = false
  }

  protected void onButtonImport( ) {
    logger.debug( 'ImportPartMaster confirmed' )
    this.btnCancel.enabled = false
    this.btnImport.enabled = false
    sb.doOutside { this.startImport() }
  }
}
