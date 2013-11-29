package mx.lux.pos.ui.view.dialog

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import groovy.swing.SwingBuilder
import java.awt.Font
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JComponent
import net.miginfocom.swing.MigLayout
import mx.lux.pos.ui.resources.UI_Standards
import javax.swing.JDialog
import javax.swing.JTextField
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import mx.lux.pos.ui.controller.OpenSalesController

class EffectiveRateDialog extends JDialog {

    private static final String TXT_DIALOG_TITLE = "Tipo de cambio efectivo"
    private static final String TXT_INSTRUCTIONS = ""
    private static final String TXT_USD_RATE_LABEL = "Dólar Americano"
    private static final String TXT_EUR_RATE_LABEL = "Euro"
    private static final String TXT_OK_CAPTION = "Aceptar"
    private static final String TXT_CANCEL_CAPTION = "Cancelar"

    private Logger logger = LoggerFactory.getLogger( this.getClass() )

    private SwingBuilder sb = new SwingBuilder()

    Boolean btnOk
    JTextField txtUsdRate
    JTextField txtEurRate

    EffectiveRateDialog( ) {
        buildUI()
        setupTriggers()
    }

    // Internal Methods
    protected Double getRate( String pRateString ) {
        Double rate = 0
        if (StringUtils.trimToNull( pRateString ) != null ) {
            pRateString = pRateString.replace( ",", "" )
            try {
                rate = NumberUtils.createDouble( pRateString )
            } catch (NumberFormatException e) {
                this.logger.debug( String.format( "Unable to parse: %s", StringUtils.trimToEmpty( pRateString )))
            }
        }
        return rate
    }
    protected void init( ) {
        this.btnOk = false
        txtUsdRate.text = String.format( "%,.4f", OpenSalesController.instance.requestActiveRate( "USD" ) )
        txtEurRate.text = String.format( "%,.4f", OpenSalesController.instance.requestActiveRate( "EUR" ) )
    }

    // Dialog Layout
    protected void buildUI( ) {
        sb.dialog( this,
                title: TXT_DIALOG_TITLE,
                location: [ 260, 85 ],
                preferredSize: [ 300, 150 ],
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
                layout: new MigLayout( "wrap 2", "10[][fill,grow]10", "[]5[]15" )
        ) {
            label( text: TXT_USD_RATE_LABEL )
            txtUsdRate = textField( text: "13.00" )
            label( text: TXT_EUR_RATE_LABEL )
            txtEurRate = textField( text: "18.00" )
        }
    }

    protected JComponent composeButtonPane( ) {
        sb.panel( constraints: BorderLayout.PAGE_END ) {
            borderLayout()
            panel( constraints: BorderLayout.LINE_END ) {
                button( text: TXT_OK_CAPTION,
                        preferredSize: UI_Standards.BUTTON_SIZE,
                        actionPerformed: { onButtonOk() }
                )
                button( text: TXT_CANCEL_CAPTION,
                        preferredSize: UI_Standards.BUTTON_SIZE,
                        actionPerformed: { onButtonCancel() }
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

    Double getUsdRate( ) {
        return getRate( StringUtils.trimToEmpty( txtUsdRate.text ) )
    }

    Double getEurRate( ) {
        return getRate( StringUtils.trimToEmpty( txtEurRate.text ) )
    }

    // UI Response
    protected void onButtonCancel() {
        println "Change Rates Cancelled"
        this.setVisible( false )
    }

    protected void onButtonOk() {
        println "Change Rates Accepted"
        this.btnOk = true
        this.setVisible( false )
    }

}
