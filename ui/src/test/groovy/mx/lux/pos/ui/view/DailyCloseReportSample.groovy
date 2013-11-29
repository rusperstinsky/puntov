package mx.lux.pos.ui.view

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.ReportController

import java.awt.CardLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

class DailyCloseReportSample extends JFrame {

  private SwingBuilder sb = new SwingBuilder()

  private JPanel mainPanel
  private ReportController reports

  DailyCloseReportSample( ) {
    buildUI()
  }

  // Internal methods
  private void buildUI( ) {
    sb.build() {
      lookAndFeel( 'system' )
      frame( this,
          title: 'Sample Frame to call DailyClose report',
          show: true,
          pack: true,
          resizable: true,
          preferredSize: [ 800, 600 ],
          defaultCloseOperation: EXIT_ON_CLOSE
      ) {
        menuBar {
          menu( text: "Archivo", mnemonic: "A" ) {
            menuItem( text: "Salir", visible: true, actionPerformed: { println "Salir" } )
          }
          menu( text: "Reportes", mnemonic: "R" ) {
            menuItem( text: "Cancelaciones", visible: true, actionPerformed: { onCancellations() } )
            menuItem( text: "Cierre Diario", visible: true, actionPerformed: { onDailyCloseReportSelected() } )
            menuItem( text: "Ingresos Por Sucursal", visible: true, actionPerformed: { onIncomePerBranchReportSelected() } )
            menuItem( text: "Ingresos Por Vendedor", visible: true, actionPerformed: { onSellerRevenue() } )
            menuItem( text: "Ventas", visible: true, actionPerformed: { onSales() } )
            menuItem( text: "Ventas Por Vendedor", visible: true, actionPerformed: { onSalesbySeller() } )
            menuItem( text: "Ventas Por Linea", visible: true, actionPerformed: { onSalesbyLine() } )
            menuItem( text: "Ventas Por Marca", visible: true, actionPerformed: { onSalesbyBrand() } )
            menuItem( text: "Ventas Por Vendedor Por Marca", visible: true, actionPerformed: { onSalesbySellerbyBrand() } )
            menuItem( text: "Existencias Por Marca", visible: true, actionPerformed: { onStockbyBrand() } )
            menuItem( text: "Existencias Por Articulo", visible: true, actionPerformed: { onStockbyBrandColor() } )
            menuItem( text: "Control de Trabajos", visible: true, actionPerformed: { onJobControl() } )
            menuItem( text: "Trabajos Entregados", visible: true, actionPerformed: { onWorkSubmitted() } )
            menuItem( text: "Facturas Fiscales", visible: true, actionPerformed: { onTaxBills() } )
            menuItem( text: "Descuentos", visible: true, actionPerformed: {onDiscounts()} )
            menuItem( text: "Promociones", visible: true, actionPerformed: { onPromotions() } )
            menuItem( text: "Pagos", visible: true, actionPerformed: { onPayments() } )
            menuItem( text: "Cotizaciones", visible: true, actionPerformed: { onQuote() } )
            menuItem( text: "Examenes", visible: true, actionPerformed: { onExams() } )
            menuItem( text: "Ventas Por Optometrista", visible: true, actionPerformed: { onOptometristSales() } )
          }
        }
        mainPanel = panel( layout: new CardLayout() )
      }
    }
  }

  // UI Response
  protected void onCancellations( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.Cancellations )
  }

  protected void onDailyCloseReportSelected( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.DailyClose )
  }

  protected void onIncomePerBranchReportSelected( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.IncomePerBranch )
  }

  protected void onSellerRevenue( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.SellerRevenue )
  }

  protected void onSales( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.Sales )
  }

  protected void onSalesbySeller( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.SalesbySeller )
  }

  protected void onSalesbyLine( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.SalesbyLine )
  }

  protected void onSalesbyBrand( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.SalesbyBrand )
  }

  protected void onSalesbySellerbyBrand( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.SalesbySellerbyBrand )
  }

  protected void onStockbyBrand( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.StockbyBrand )
  }

  protected void onStockbyBrandColor( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.StockbyBrandColor )
  }

  protected void onJobControl( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.JobControl )
  }

  protected void onWorkSubmitted( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.WorkSubmitted )
  }

  protected void onTaxBills( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.TaxBills )
  }

  protected void onDiscounts( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.Discounts )
  }

  protected void onPromotions( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.Promotions )
  }

  protected void onPayments( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.Payments )
  }

  protected void onQuote( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.Quote )
  }

  protected void onExams( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.Exams )
  }


  protected void onOptometristSales( ) {
    if ( reports == null ) {
      reports = new ReportController()
    }
    reports.fireReport( ReportController.Report.OptometristSales )
  }


  // Test Method
  static void main( String[] args ) {
    SwingUtilities.invokeLater(
        new Runnable() {
          void run( ) {
            //ApplicationContext ctx = new ClassPathXmlApplicationContext( "classpath:spring-config.xml" )
            //ctx.registerShutdownHook()
            DailyCloseReportSample sample = new DailyCloseReportSample()
          }
        }
    )
  }

}
