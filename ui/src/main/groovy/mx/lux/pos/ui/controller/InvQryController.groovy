package mx.lux.pos.ui.controller

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.InvOhSummary
import mx.lux.pos.ui.model.InvOhData
import mx.lux.pos.ui.resources.ServiceManager
import mx.lux.pos.ui.view.dialog.InvOhTicketDialog
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class InvQryController {
  
  private Logger logger = LoggerFactory.getLogger( InvQryController.class )

  // Constructor: Implemented as singleton
  protected static InvQryController instance
  protected InvQryController() { }
  static InvQryController getInstance() {
    if ( instance == null ) {
      instance = new InvQryController()
    }
    return instance
  }
  
  // Requests
  void requestInvOhTicket() {
    this.requestInvOhTicket( new InvOhTicketDialog() )
  }
  
  void requestInvOhTicket( InvOhTicketDialog pDialog ) {
    logger.debug( "Request InvOHTicket" )
    InvOhData invData = new InvOhData()
    dispatchInventoryRequest( invData )
    pDialog.setOhData( invData )  
    pDialog.activate()
    if ( pDialog.printRequested ) {
      logger.debug( String.format( "Request InvOH Ticket print (%s, %s)", pDialog.genreSelected, pDialog.brandSelected ) )
      InvOhSummary ticketRequest = invData.getSummary( pDialog.genreSelected, pDialog.brandSelected )
      ServiceManager.ticketService.imprimeResumenExistencias( ticketRequest )      
    }
  }
  
  // Dispatchers
  protected void dispatchInventoryRequest( InvOhData pInvOhData ) {
    logger.debug( "Dispatch Inventory Request")
    Collection<Articulo> partsOH = ServiceManager.inventoryService.listarArticulosConExistencia( )
    pInvOhData.input = partsOH
  }
}
