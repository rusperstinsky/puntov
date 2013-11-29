package mx.lux.pos.ui.view.panel

import groovy.model.DefaultTableModel
import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.DailyCloseController
import mx.lux.pos.ui.model.DailyClose
import mx.lux.pos.ui.view.dialog.dailyclose.DailyCloseDepositsDialog
import mx.lux.pos.ui.view.dialog.dailyclose.TerminalCloseDialog
import mx.lux.pos.ui.view.dialog.dailyclose.TerminalFixDialog
import mx.lux.pos.ui.view.renderer.DateCellRenderer
import mx.lux.pos.ui.view.renderer.MoneyCellRenderer
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.time.DateUtils

import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import javax.swing.*

class DailyClosePanel extends JPanel {

  private SwingBuilder sb
  private List<DailyClose> days
  private JSpinner dateStart
  private JSpinner dateEnd
  private DefaultTableModel daysModel
  private JCheckBox cbSoloAbierto
  private JButton btnBuscar
  private Date today = new Date()

  DailyClosePanel( ) {
    sb = new SwingBuilder()
    days = [ ] as ObservableList
    days.addAll( DailyCloseController.findWithStatusOpened() )
    buildUI()
  }

  private void buildUI( ) {
    sb.panel( this, layout: new MigLayout( 'fill,wrap', '[fill]', '[fill]' ) ) {
      panel( border: loweredEtchedBorder(), layout: new MigLayout( 'wrap 6', '[]200[][][][][]' ) ) {
        cbSoloAbierto = checkBox( text: 'Solo Abierto', selected: true, actionPerformed: onSelect )
        label( 'Desde' )
        dateStart = spinner( model: spinnerDateModel(), enabled: false )
        dateStart.editor = new JSpinner.DateEditor( dateStart as JSpinner, 'dd-MM-yyyy' )
        dateStart.value = DateUtils.addDays( today, -10 )

        label( 'Hasta' )
        dateEnd = spinner( model: spinnerDateModel(), enabled: false )
        dateEnd.editor = new JSpinner.DateEditor( dateEnd as JSpinner, 'dd-MM-yyyy' )
        dateEnd.value = today
        dateEnd.addChangeListener( new ChangeListener() {
          @Override
          void stateChanged( ChangeEvent e ) {
            if ( dateEnd.value < dateStart.value ) {
              dateStart.value = DateUtils.addDays( dateEnd.value as Date, -10 )
            }
          }
        } )

        btnBuscar = button( 'Buscar', actionPerformed: doSearch, enabled: false )
      }

      scrollPane {
        table( selectionMode: ListSelectionModel.SINGLE_SELECTION, mouseClicked: doClick, mousePressed: doPress ) {
          daysModel = tableModel( list: days ) {
            closureColumn( header: 'Estado', read: {DailyClose tmp -> tmp?.status}, maxWidth: 50 )
            closureColumn( header: 'Fecha ingreso', read: {DailyClose tmp -> tmp?.date}, cellRenderer: new DateCellRenderer() )
            closureColumn( header: 'Fecha cierre', read: {DailyClose tmp -> tmp?.dateClosed ?: ''}, cellRenderer: new DateCellRenderer() )
            closureColumn( header: 'Venta', read: {DailyClose tmp -> tmp?.netSell}, cellRenderer: new MoneyCellRenderer() )
            closureColumn( header: 'Observaciones', read: {DailyClose tmp -> tmp?.observations} )
          } as DefaultTableModel
        }
      }
    }
  }

  private def doSearch = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    Date dayStart = dateStart.value as Date
    Date dayEnd = dateEnd.value as Date
    if ( dayStart != null && dayEnd != null ) {
      List<DailyClose> lstDailyClose = DailyCloseController.findByDatesBetween( dayStart, dayEnd )
      if ( lstDailyClose.size() > 0 ) {
        days.clear()
        days.addAll( lstDailyClose )
        daysModel.fireTableDataChanged()
      } else {
        sb.optionPane().showMessageDialog( this, 'La fecha de b\u00fasqueda no corresponde a un d\u00eda existente', 'D\u00eda erroneo', JOptionPane.ERROR_MESSAGE )
      }
    } else {
      sb.optionPane().showMessageDialog( this, 'La fecha de b\u00fasqueda no es correcta', 'Error', JOptionPane.ERROR_MESSAGE )
    }
    source.enabled = true
  }

  private def doClean = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    dateStart.value = new Date()
    days.clear()
    days.addAll( DailyCloseController.findWithStatusOpened() )
    daysModel.fireTableDataChanged()
    source.enabled = true
  }

  private def onSelect = { ActionEvent ev ->
    JCheckBox source = ev.source as JCheckBox
    if ( source.selected ) {
      dateStart.enabled = false
      dateEnd.enabled = false
      btnBuscar.enabled = false
      days.clear()
      days.addAll( DailyCloseController.findWithStatusOpened() )
      daysModel.fireTableDataChanged()
    } else {
      dateStart.enabled = true
      dateEnd.enabled = true
      btnBuscar.enabled = true
    }
  }

  private def doClick = { MouseEvent ev ->
    if ( SwingUtilities.isLeftMouseButton( ev ) ) {
      DailyClose selection = ev.source.selectedElement as DailyClose
      if ( ev.clickCount == 2 && selection?.date ) {
        boolean dataLoaded = DailyCloseController.loadDayData( selection )
        if ( dataLoaded ) {
          DailyClose updated = DailyCloseController.findByDate( selection?.date )
          new DailyCloseDepositsDialog( this, updated ).show()
        } else {
          sb.optionPane().showMessageDialog( this, 'Se ha producido un error al cargar los datos', 'Error', JOptionPane.ERROR_MESSAGE )
        }
      }
    }
  }

  private def doPress = { MouseEvent ev ->
    DailyClose selection = ev.source.selectedElement as DailyClose
    if ( SwingUtilities.isRightMouseButton( ev ) ) {
      if ( selection?.date ) {
        sb.popupMenu {
          menuItem( text: 'Corregir Terminales',
              actionPerformed: {
                new TerminalFixDialog( selection.date ).show()
              }
          )
          menuItem( text: 'Cierre de Terminal',
              actionPerformed: {
                new TerminalCloseDialog( selection.date ).show()
              }
          )
        }.show( ev.component, ev.x, ev.y )
      } else {
        sb.optionPane().showMessageDialog( null, 'La fecha de b\u00fasqueda no es correcta', 'Error', JOptionPane.ERROR_MESSAGE )
      }
    }
  }
}