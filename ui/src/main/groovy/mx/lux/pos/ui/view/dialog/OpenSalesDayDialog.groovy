package mx.lux.pos.ui.view.dialog

import javax.swing.JDialog
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import mx.lux.pos.ui.model.InvOhData
import mx.lux.pos.ui.model.adapter.GenreListModelAdapter
import mx.lux.pos.ui.model.adapter.BrandListModelAdapter
import javax.swing.JComboBox
import javax.swing.JLabel
import groovy.swing.SwingBuilder
import java.awt.Font
import javax.swing.JComponent
import java.awt.BorderLayout
import javax.swing.BorderFactory
import net.miginfocom.swing.MigLayout
import javax.swing.SwingConstants
import java.awt.Color
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.controller.OpenSalesController
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import javax.swing.JTextField

class OpenSalesDayDialog extends JDialog {

    private static final String TXT_DIALOG_TITLE = "Apertura de Caja"
    private static final String TXT_INSTRUCTIONS = ""
    private static final String TXT_USD_RATE_LABEL = "Dólar Americano"
    private static final String TXT_USD_CASH_LABEL = "Efectivo (Dólares)"
    private static final String TXT_MXN_CASH_LABEL = "Efectivo (Pesos)"
    private static final String TXT_REMARKS_LABEL = "Observaciones"
    private static final String TXT_RATES_CAPTION = "T.C. USD"
    private static final String TXT_PRINT_CAPTION = "Imprimir"

    private Logger logger = LoggerFactory.getLogger( this.getClass() )

    private SwingBuilder sb = new SwingBuilder()

    private JTextField txtCurrentAmount
    private JTextField txtCurrentAmountUsd
    private JTextField txtUsdRate
    private JTextField txtRemarks
    Boolean printRequested

    OpenSalesDayDialog( ) {
        buildUI()
        setupTriggers()
    }

    protected void setPrintRequested(Boolean pRequested) {
        this.printRequested = pRequested
    }

    // Internal Methods
    protected Double getAmount( String pAmountString ) {
        Double amount = 0
        if (StringUtils.trimToNull( pAmountString ) != null ) {
            pAmountString = pAmountString.replace( ",", "" )
            try {
                amount = NumberUtils.createDouble( pAmountString )
            } catch (NumberFormatException e) {
                this.logger.debug( String.format( "Unable to parse: %s", StringUtils.trimToEmpty( pAmountString )))
            }
        }
        return amount
    }

    protected void init( ) {
        updateUsdRate()
        txtCurrentAmount.text = String.format( "%,.2f", OpenSalesController.instance.requestCurrentAmount( ) )
        txtCurrentAmountUsd.text = String.format( "%,.0f", OpenSalesController.instance.requestCurrentAmountUsd( ) )
    }

    protected void updateUsdRate( ) {
        txtUsdRate.text = String.format( "%,.4f", OpenSalesController.instance.requestActiveRate( "USD" ) )
    }

    // Dialog Layout
    protected void buildUI( ) {
        sb.dialog( this,
                title: TXT_DIALOG_TITLE,
                location: [ 250, 75 ],
                preferredSize: [ 400, 200 ],
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
            composeButtonPane()
        }
    }

    protected JComponent composeSelectionPane( ) {
        sb.panel( constraints: BorderLayout.CENTER,
                layout: new MigLayout( "wrap 2", "10[][fill,grow]10", "[]5[]5[]5[]15" )
        ) {
            label( text: TXT_USD_RATE_LABEL )
            txtUsdRate = textField( text: "13.1000", editable: false, background: UI_Standards.LOCKED_BACKGROUND )
            label( text: TXT_MXN_CASH_LABEL )
            txtCurrentAmount = textField( text: "13,500.00" )
            label( text: TXT_USD_CASH_LABEL )
            txtCurrentAmountUsd = textField( text: "1,500.00" )
            label( text: TXT_REMARKS_LABEL )
            txtRemarks = textField( text: "" )
        }
    }

    protected JComponent composeButtonPane( ) {
        sb.panel( constraints: BorderLayout.PAGE_END ) {
            borderLayout()
            panel( constraints: BorderLayout.LINE_START ) {
                button( text: TXT_RATES_CAPTION,
                        preferredSize: UI_Standards.BUTTON_SIZE,
                        actionPerformed: { onButtonRates() }
                )
            }
            label( text:  "", constraints: BorderLayout.CENTER )
            panel( constraints: BorderLayout.LINE_END ) {
                button( text: TXT_PRINT_CAPTION,
                        preferredSize: UI_Standards.BUTTON_SIZE,
                        actionPerformed: { onButtonPrint() }
                )
            }
        }
    }

    protected void setupTriggers( ) {

    }

    // Public Methods
    void activate( ) {
        this.init( )
        this.setVisible( true )
    }

    Double getCurrentAmount( ) {
      return getAmount( StringUtils.trimToEmpty( txtCurrentAmount.text ) )
    }

    Double getCurrentAmountUsd( ) {
        return getAmount( StringUtils.trimToEmpty( txtCurrentAmountUsd.text ) )
    }

    String getRemarks( ) {
        return txtRemarks.text
    }

    // UI Response
    protected void onButtonPrint() {
        this.setVisible( false )
        this.setPrintRequested( true )
    }

    protected void onButtonRates() {
        OpenSalesController.instance.requestExchangeRateSetup()
        this.updateUsdRate()
    }

}
