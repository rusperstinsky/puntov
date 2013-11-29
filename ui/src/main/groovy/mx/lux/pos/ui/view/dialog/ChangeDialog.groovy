package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.resources.UI_Standards
import net.miginfocom.swing.MigLayout

import java.awt.BorderLayout
import javax.swing.ButtonGroup
import javax.swing.JDialog
import javax.swing.JList
import javax.swing.JScrollPane
import javax.swing.border.TitledBorder
import javax.swing.JLabel
import java.text.NumberFormat
import mx.lux.pos.ui.controller.PaymentController
import mx.lux.pos.model.MensajesPorParametro

class ChangeDialog extends JDialog {

  private def sb = new SwingBuilder()

  private static final String title = PaymentController.obtenerMensaje( MensajesPorParametro.CAMBIO )

  private BigDecimal cambio
  private BigDecimal monto
  private BigDecimal importe

    ChangeDialog( BigDecimal change, BigDecimal pago, BigDecimal venta ) {
    cambio = change
    monto = pago
    importe = venta
    buildUI()
  }

  void buildUI( ) {
    sb.dialog( this,
        title: title,
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 250, 200 ],
        location: [ 300, 350 ],
    ) {
      panel() {
        borderLayout()
        panel( layout: new MigLayout( "fill,wrap 2", "[right,grow][left,grow]" ) ) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance( Locale.US )
            label( text: 'Venta:' )
            label( text: formatter.format(importe) )
            label( text: 'Pago: ')
            label( text: formatter.format(monto) )
            label( text: 'Cambio:' )
            label( text: formatter.format(cambio) )
        }
      }

      panel( constraints: BorderLayout.PAGE_END ) {
        borderLayout()
        panel( constraints: BorderLayout.LINE_END ) {
          button( text: "Aceptar", preferredSize: UI_Standards.BUTTON_SIZE, actionPerformed: {dispose()} )
        }
      }

    }
  }
}
