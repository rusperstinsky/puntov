package mx.lux.pos.ui.view

import groovy.swing.SwingBuilder
import javax.swing.JPanel
import javax.swing.JFrame

import java.awt.BorderLayout
import mx.lux.pos.ui.resources.UI_Standards
import javax.swing.SwingUtilities
import mx.lux.pos.ui.controller.OpenSalesController
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

class OpenDayDialogSample extends JFrame {

    private SwingBuilder sb = new SwingBuilder()
    private JPanel mainPanel

    OpenDayDialogSample() {
        buildUI()
    }

    // Internal Methods
    private void buildUI() {
        sb.build() {
            lookAndFeel('system')
            frame(this,
                    title: 'Sample Frame to trigger a dialog',
                    show: true,
                    pack: true,
                    resizable: true,
                    location: [100, 0],
                    preferredSize: [800, 600],
                    defaultCloseOperation: EXIT_ON_CLOSE
            ) {
                menuBar {
                    menu(text: "Archivo", mnemonic: "A") {
                        menuItem(text: "Salir", visible: true, actionPerformed: { println "Salir" })
                    }
                    menu(text: "Herramientas", mnemonic: "H") {
                        menuItem(text: "Apertura de Caja", visible: true,
                                actionPerformed: { OpenSalesController.instance.requestNewDay() }
                        )
                    }
                }
                panel() {
                    borderLayout()
                    panel(constraints: BorderLayout.PAGE_END) {
                        borderLayout()
                        panel(constraints: BorderLayout.LINE_END) {
                            button(text: "Launch",
                                    preferredSize: UI_Standards.BUTTON_SIZE,
                                    actionPerformed: { OpenSalesController.instance.requestNewDay() }
                            )
                        }
                    }
                }
            }
        }
    }

    // UI Response
    static void main(String[] args) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    void run() {
                        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring-config.xml")
                        ctx.registerShutdownHook()
                        OpenDayDialogSample sample = new OpenDayDialogSample()
                    }
                }
        )
    }


}
