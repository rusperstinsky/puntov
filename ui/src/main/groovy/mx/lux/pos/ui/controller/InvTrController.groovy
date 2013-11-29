package mx.lux.pos.ui.controller

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.InvTrRequest
import mx.lux.pos.model.Shipment
import mx.lux.pos.model.ShipmentLine
import mx.lux.pos.model.Sucursal
import mx.lux.pos.model.TransInv
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.service.InventarioService
import mx.lux.pos.service.SucursalService
import mx.lux.pos.ui.MainWindow
import mx.lux.pos.ui.model.InvTrViewMode
import mx.lux.pos.ui.model.Session
import mx.lux.pos.ui.model.SessionItem
import mx.lux.pos.ui.model.User
import mx.lux.pos.ui.model.adapter.InvTrFilter
import mx.lux.pos.ui.model.adapter.RequestAdapter
import mx.lux.pos.ui.resources.ServiceManager
import mx.lux.pos.ui.view.component.NavigationBar.Command
import mx.lux.pos.ui.view.dialog.InboundDialog
import mx.lux.pos.ui.view.dialog.InvTrSelectorDialog
import mx.lux.pos.ui.view.dialog.PartSelectionDialog
import mx.lux.pos.ui.view.panel.InvTrView
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.Resource
import javax.swing.JDialog
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.JTextField
import javax.swing.SwingUtilities
import mx.lux.pos.model.InvAdjustSheet
import mx.lux.pos.service.business.Registry

class InvTrController {

  private static final Logger log = LoggerFactory.getLogger( InvTrController.class )

  private static InvTrController instance
  private PartSelectionDialog dlgPartSelection
  private InvTrSelectorDialog dlgSelector
  private JFileChooser dlgFile

 // private static SucursalService sucursalService

  private InvTrController( ) { }

  static InvTrController getInstance( ) {
    if ( instance == null ) {
      instance = new InvTrController()
    }
    return instance
  }

  // Initialize
  private JFileChooser getDialogFile( ) {
    if ( dlgFile == null ) {
      dlgFile = new JFileChooser()
    }
    return dlgFile
  }

  // Dispatch Actions
  protected void dispatchDocument( InvTrView pView, Shipment pDocument ) {
    log.debug( "[Controller] Dispatch receiving document" )
    pView.notifyDocument( pDocument )
  }

  protected void dispatchDocument( InvTrView pView, InvAdjustSheet pDocument ) {
    log.debug( "[Controller] Dispatch receiving document" )
    pView.notifyDocument( pDocument )
  }

  protected void dispatchDocumentEmpty( InvTrView pView, Boolean fileAlreadyProccessed, String articleNotFound ) {
    log.debug( "[Controller] Dispatch document unavailable" )
    InvTrController controller = this
    SwingUtilities.invokeLater( new Runnable() {
      public void run( ) {
        controller.dispatchViewModeQuery( pView )
        if( fileAlreadyProccessed ){
          pView.data.txtStatus = pView.panel.MSG_DOCUMENT_ALREADY_PROCCESED
        } else if( articleNotFound != '' ){
          pView.data.txtStatus = String.format( pView.panel.MSG_ARTICLE_NOT_FOUND, articleNotFound )
        } else {
          pView.data.txtStatus = pView.panel.MSG_NO_DOCUMENT_AVAILABLE
        }
        pView.fireRefreshUI()
      }
    } )
  }

  protected void dispatchPartMasterUpdate( Shipment pDocument ) {
    log.debug( "[Controller] Update Part Master with Parts in Receiving Document" )
    if ( ( pDocument != null ) && ( pDocument.partShadows.size() > 0 ) ) {
      ArticuloService partMaster = ServiceManager.partService

      partMaster.actualizarArticulosConSombra( pDocument.partShadows )
    }
  }

  protected void dispatchPartsSelected( InvTrView pView, List<Articulo> pPartList ) {
    for ( Articulo part in pPartList ) {
      pView.data.addPart( part )
    }
    pView.fireConsumePartSeed()
    pView.fireRefreshUI()
  }

  protected void dispatchPrintTransaction( String pTrType, Integer pTrNbr ) {
    TransInv tr = ServiceManager.inventoryService.obtenerTransaccion( pTrType, pTrNbr )
    if ( tr != null ) {
      ServiceManager.ticketService.imprimeTransInv( tr )
    }
  }
   /*
    protected static String replaceCharAt(String s, int pos, char c) {
        StringBuffer buf = new StringBuffer( s );
        buf.setCharAt( pos, c );
        return buf.toString( );
    }

    protected  String claveAleatoria(Integer sucursal, Integer folio) {
    String folioAux = "" + folio.intValue();
    String sucursalAux = "" + sucursal.intValue()
    String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    if (folioAux.size() < 4) {
        folioAux = folioAux?.padLeft( 4, '0' )
    }
    else {
        folioAux = folioAux.substring(0,4);
    }
    String resultado = sucursalAux?.padLeft( 3, '0' ) + folioAux


    for (int i = 0; i < resultado.size(); i++) {
        int numAleatorio = (int) (Math.random() * abc.size());
        if (resultado.charAt(i) == '0') {
            resultado = replaceCharAt(resultado, i, abc.charAt(numAleatorio))
        }
        else {
            int numero = Integer.parseInt ("" + resultado.charAt(i));
            numero = 10 - numero
            char diff = Character.forDigit(numero, 10);
            resultado = replaceCharAt(resultado, i, diff)
        }


    }
    return resultado;
  }

  protected String generaSalida( InvTrViewMode viewMode, InvTrView pView ) {
      String url = Registry.getURL( viewMode.trType.idTipoTrans );
      if ( StringUtils.trimToNull( url ) != null ) {
          User user = Session.get( SessionItem.USER ) as User

         String variable = pView.data.qryDataset.dataset[0].sucursal + '>' + pView.data.postSiteTo.id + '>' +
                          pView.data.postTrType.ultimoFolio + '>' +
                          claveAleatoria(pView.data.qryDataset.dataset[0].sucursal, pView.data.postTrType.ultimoFolio)  +
                          '>' + user.username + '>'

         for (int i = 0; i < pView.data.skuList.size(); i++) {
             variable += pView.data.skuList[i].part.id + '|'
         }
         url += String.format( '?arg=%s', URLEncoder.encode( String.format( '%s', variable ), 'UTF-8' ) )
         String response = url.toURL().text
         response = response?.find( /<XX>\s*(.*)\s*<\/XX>/ ) {m, r -> return r}
         log.debug( "resultado solicitud: ${response}" )
         return response
      }
  }
       */

  protected String confirmaEntrada(InvTrViewMode viewMode, InvTrView pView){
      String url = Registry.getURLConfirmacion( viewMode.trType.idTipoTrans );
      if ( StringUtils.trimToNull( url ) != null ) {
        String variable = pView.data.claveCodificada + ">" + pView.data.postTrType.ultimoFolio
        url += String.format( '?arg=%s', URLEncoder.encode( String.format( '%s', variable ), 'UTF-8' ) )
        String response = url.toURL().text
        response = response?.find( /<XX>\s*(.*)\s*<\/XX>/ ) {m, r -> return r}
        log.debug( "resultado solicitud: ${response}" )
        return response
      }
  }

  protected void dispatchViewModeAdjust( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.ADJUST )
    pView.fireDisplay()
  }

  protected void dispatchViewModeFileAdjust( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.FILE_ADJUST )
    pView.fireDisplay()
    this.requestAdjustFile( pView )
  }

  protected void dispatchViewModeIssue( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.ISSUE )
    pView.data.postSiteTo = null
    pView.fireDisplay()
  }

  List<Sucursal> listaAlmacenes(){
      List<Sucursal> lstAlmacenes = ServiceManager.getInventoryService().listarAlmacenes()
      return lstAlmacenes
  }

    List<Sucursal> listaSoloSucursales(){
        List<Sucursal> lstAlmacenes = ServiceManager.getInventoryService().listarSoloSucursales()
        return lstAlmacenes
    }

  protected void dispatchViewModeQuery( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.QUERY )
    if ( pView.data.qryDataset.size > 0 ) {
      pView.data.qryInvTr = pView.data.qryDataset.first
    }
    pView.fireDisplay()
  }

  protected void dispatchViewModeReceipt( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.RECEIPT )
    pView.fireDisplay()
    requestReceipt( pView )
  }

  protected void dispatchViewModeReturn( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.RETURN )
    pView.fireDisplay()
  }


  protected void dispatchViewModeOutBound( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.OUTBOUND )
    pView.data.postSiteTo = null
    pView.fireDisplay()
  }

  protected void dispatchViewModeInBound( InvTrView pView ) {
    pView.data.clear()
    pView.fireResetUI()
    pView.notifyViewMode( InvTrViewMode.INBOUND )
    pView.fireDisplay()
    requestInBound( pView )
  }
    // Actions started
  protected void fireChangeViewMode( InvTrView pView, InvTrViewMode pNewMode ) {
    Boolean confirmed = true
    if ( !pNewMode.equals( pView.data.viewMode ) ) {
      if ( pView.data.dirty ) {
        String msg = String.format( pView.panel.MSG_CONFIRM_TO_PROCEED, pNewMode.toString() )
        Integer confirm = JOptionPane.showConfirmDialog( pView.panel, msg, pView.panel.TXT_CONFIRM_TITLE,
            JOptionPane.YES_NO_OPTION )
        confirmed = ( confirm == JOptionPane.YES_OPTION )
      }
      if ( confirmed ) {
        if ( pNewMode.equals( InvTrViewMode.ISSUE ) ) {
          dispatchViewModeIssue( pView )
        } else if ( pNewMode.equals( InvTrViewMode.RECEIPT ) ) {
          dispatchViewModeReceipt( pView )
        } else if ( pNewMode.equals( InvTrViewMode.QUERY ) ) {
          dispatchViewModeQuery( pView )
        } else if ( pNewMode.equals( InvTrViewMode.ADJUST ) ) {
          dispatchViewModeAdjust( pView )
        } else if ( pNewMode.equals( InvTrViewMode.RETURN ) ) {
          dispatchViewModeReturn( pView )
        } else if ( pNewMode.equals( InvTrViewMode.FILE_ADJUST ) ) {
            dispatchViewModeFileAdjust( pView )
        } else if ( pNewMode.equals( InvTrViewMode.OUTBOUND) ) {
            dispatchViewModeOutBound( pView )
        } else if ( pNewMode.equals( InvTrViewMode.INBOUND) ) {
            dispatchViewModeInBound( pView )
        }
      } else {
        pView.notifyViewModeChangeCancelled()
      }
    }
  }

  // Requests
  void requestAdjustFile( InvTrView pView ) {
    log.debug( "[Controller] Request Adjust File" )
    InvAdjustSheet document = null
    JFileChooser dialog = this.getDialogFile()
    dialog.currentDirectory = new File(SettingsController.instance.incomingPath)
    dialog.fileSelectionMode = JFileChooser.FILES_ONLY
    String filename = null
    int fileAction = getDialogFile().showOpenDialog( pView.panel )
    if ( fileAction == JFileChooser.APPROVE_OPTION ) {
      filename = dlgFile.getSelectedFile().absolutePath
      log.debug( String.format( "[Controller] File chosen: %s", filename ) )
      document = ServiceManager.getInventoryService().leerArchivoAjuste( filename )
    }
    if ( document != null ) {
      dispatchDocument( pView, document )
      pView.data.inFile = new File( filename )
      log.debug ( String.format('Adjust File: %s', document.headerToString()) )
    } else {
      dispatchDocumentEmpty( pView, false, '' )
      log.debug ( 'No document' )
    }

  }

  void requestItem( InvTrView pView, Command pCommand ) {
    log.debug( String.format( "[Controller] Navigate to <%s>", pCommand.toString() ) )
    pView.data.qryInvTr = pView.data.qryDataset.get( pCommand )
    pView.fireRefreshUI()
  }

  void requestNewSearch( InvTrView pView ) {
    log.debug( "[Controller] New Search" )
    if ( dlgSelector == null ) {
      dlgSelector = new InvTrSelectorDialog( pView.data.qryDataset )
    }
    dlgSelector.activate()
    if ( pView.data.qryDataset.currentIndex == null ) {
      pView.data.qryInvTr = pView.data.qryDataset.first
    } else {
      pView.data.qryInvTr = pView.data.qryDataset.getCurrent()
    }
    pView.fireRefreshUI()
  }

  void requestPart( InvTrView pView ) {
      String[] part = pView.data.partSeed.split(',')
      log.debug( String.format( "[Controller] Request Part with seed <%s>", part[0] ) )
    String seed = part[0]
    List<Articulo> partList = ItemController.findPartsByQuery( seed, false )
    if ( ( partList.size() == 0 ) && ( seed.length() > 6 ) ) {
      seed = seed.substring( 0, 6 )
      partList = ItemController.findPartsByQuery( seed, false )
    }
    if ( partList?.any() ) {
      if ( partList.size() == 1 )  {
        if( partList.first().cantExistencia <= 0 && pView.data.viewMode.trType.tipoMov.trim().equalsIgnoreCase('S') &&
        ItemController.isInventoried(partList.first().id)){
          Integer question =JOptionPane.showConfirmDialog( new JDialog(), pView.panel.MSG_NO_STOCK, pView.panel.TXT_NO_STOCK,
                  JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE )
          if( question == 0){
            dispatchPartsSelected( pView, partList )
          } else {
            pView.panel.stock = false
          }
        } else {
          dispatchPartsSelected( pView, partList )
        }

      } else {
        if ( dlgPartSelection == null ) {
          dlgPartSelection = new PartSelectionDialog( pView.panel )
        }
        dlgPartSelection.setItems( partList )
        dlgPartSelection.setSeed( seed )
        if ( InvTrViewMode.ADJUST.equals( pView.data.viewMode ) ) {
          dlgPartSelection.multiSelection = false
        }
        dlgPartSelection.activate()
        List<Articulo> selection = dlgPartSelection.getSelection()
        if ( selection != null ) {
          if( selection.first().cantExistencia <= 0 && pView.data.viewMode.trType.tipoMov.trim().equalsIgnoreCase('S') &&
                  ItemController.isInventoried(partList.first().id)){
              Integer question =JOptionPane.showConfirmDialog( new JDialog(), pView.panel.MSG_NO_STOCK, pView.panel.TXT_NO_STOCK,
                      JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE )
              if( question == 0){
                  dispatchPartsSelected( pView, selection )
              } else {
                pView.panel.stock = false
              }
          } else {
            log.debug( String.format( "[Controller] %d Selected, (%d) %s", selection.size(), selection[ 0 ].id, selection[ 0 ].descripcion ) )
            dispatchPartsSelected( pView, selection )
          }
        }
      }
    } else {
      JOptionPane.showMessageDialog( pView.panel, String.format( pView.panel.MSG_NO_RESULTS_FOUND, seed ),
          String.format( pView.panel.TXT_QUERY_TITLE, seed ), JOptionPane.INFORMATION_MESSAGE )
    }
  }

  void requestReceipt( InvTrView pView ) {
    log.debug( "[Controller] Request Receipt" )
    Shipment document = null
    int fileAction = getDialogFile().showOpenDialog( pView.panel )
    if ( fileAction == JFileChooser.APPROVE_OPTION ) {
      log.debug( String.format( "[Controller] File chosen: %s", dlgFile.getSelectedFile().absolutePath ) )
      document = ServiceManager.getInventoryService().leerArchivoRemesa( dlgFile.getSelectedFile().absolutePath )
    }
    if ( document != null ) {
      InventarioService service = ServiceManager.inventoryService
      List<TransInv> trList = service.listarTransaccionesPorTipoAndReferencia( InvTrViewMode.RECEIPT.trType.idTipoTrans,
              document?.ref.trim() )
      if( trList.size() <= 0 ){
          dispatchPartMasterUpdate( document )
          dispatchDocument( pView, document )
      } else {
          dispatchDocumentEmpty( pView, true, '' )
      }
    } else {
      dispatchDocumentEmpty( pView, false, '' )
    }

  }


  void requestInBound( InvTrView pView ) {
    log.debug( "[Controller] Request Inbound" )
    Shipment document = null
      InboundDialog inboundDialog = new InboundDialog()
      inboundDialog.activate()
      log.debug(inboundDialog.getTxtClave())
      if (inboundDialog.button) {
          Boolean claveNoCargada = ServiceManager.inventoryService.transaccionCargada( inboundDialog.getTxtClave() )
              pView.data.claveCodificada = inboundDialog.getTxtClave()
              Sucursal sucursal = ServiceManager.inventoryService.sucursalActual()
              log.debug("" + sucursal.id)
              document = ServiceManager.getInventoryService().obtieneArticuloEntrada(inboundDialog.getTxtClave(),sucursal.id, pView.data.viewMode.trType.idTipoTrans)
              Boolean articleExist = true
              String articles = ''
                if( document != null ){
                    for(ShipmentLine line : document.lines ){
                        if(line.partCode == null || line.partCode == ''){
                            articleExist = false
                            articles = articles+"["+line.sku.toString()+"]"+" "
                        }
                    }
                }
               if ( document != null && !claveNoCargada && articleExist ) {
                  dispatchPartMasterUpdate( document )
                  dispatchDocument( pView, document )
              } else {
                   dispatchDocumentEmpty( pView, false, articles )
              }
      }
    }

  void requestSaveAndPrint( InvTrView pView ) {
    log.debug( "[Controller] Save and Print" )
    InvTrRequest request = RequestAdapter.getRequest( pView.data )
    if ( request != null ) {
        request.remarks = request.remarks.replaceAll("[^a-zA-Z0-9]+"," ");
      Integer trNbr = ServiceManager.getInventoryService().solicitarTransaccion( request )
      if ( trNbr != null ) {
        if ( pView.data.inFile != null ) {
          try {
            File moved = new File( SettingsController.instance.processedPath, pView.data.inFile.name )
            if (InvTrViewMode.OUTBOUND.equals( pView.data.viewMode ) || InvTrViewMode.FILE_ADJUST.equals( pView.data.viewMode )) {
                pView.data.inFile.delete();
            }
            else {
                pView.data.inFile.renameTo( moved )
            }
          } catch (Exception e) {
            this.log.debug( e.getMessage() )
          }
        }
        InvTrViewMode viewMode = pView.data.viewMode
        if ( InvTrViewMode.ISSUE.equals( viewMode ) || InvTrViewMode.ADJUST.equals( viewMode )
            || InvTrViewMode.RETURN.equals( viewMode ) || InvTrViewMode.FILE_ADJUST.equals( viewMode )
            || InvTrViewMode.RECEIPT.equals( viewMode ) || InvTrViewMode.OUTBOUND.equals( viewMode )
            || InvTrViewMode.INBOUND.equals( viewMode )) {
          dispatchPrintTransaction( viewMode.trType.idTipoTrans, trNbr )
          if (InvTrViewMode.INBOUND.equals( viewMode )) {
               String resultado = confirmaEntrada(viewMode, pView)
          }
          if( ServiceManager.getInventoryService().isReceiptDuplicate() ){
            dispatchPrintTransaction( viewMode.trType.idTipoTrans, trNbr )
          }
        }
        pView.fireResetUI()
        pView.data.clear()
        if ( InvTrViewMode.RECEIPT.equals( viewMode ) || InvTrViewMode.FILE_ADJUST.equals( viewMode ) ) {
          InvTrController controller = this
          SwingUtilities.invokeLater( new Runnable() {
            void run( ) {
              pView.uiDisabled = true
              controller.dispatchViewModeQuery( pView )
              InvTrFilter filter = pView.data.qryDataset.filter
              filter.reset()
              filter.setDateRange( new Date() )
              pView.data.qryDataset.requestTransactions()
              pView.data.txtStatus = pView.panel.MSG_TRANSACTION_POSTED
              pView.fireRefreshUI()
              pView.uiDisabled = false
            }
          } )
        } else {
          pView.data.txtStatus = pView.panel.MSG_TRANSACTION_POSTED
          pView.fireRefreshUI()
        }
      } else {
        JOptionPane.showMessageDialog( pView.panel, pView.panel.MSG_POST_FAILED, pView.panel.TXT_POST_TITLE, JOptionPane.ERROR_MESSAGE )
      }
    } else {
      log.debug( "[Controller] Request not available" )
    }
  }

  void requestPrint( String pIdTipoTrans, Integer pTrNbr ) {
    log.debug( "[Controller] Print Transaction" )
    dispatchPrintTransaction( pIdTipoTrans, pTrNbr )
  }

  void requestViewModeChange( InvTrView pView ) {
    log.debug( String.format( "[Controller] View Mode change: <%s>", pView.panel.comboViewMode.selection ) )
    fireChangeViewMode( pView, pView.panel.comboViewMode.selection )
  }

  void requestPrintTransactions( Date fechaTicket ){
    log.debug( "requestPrintTransactions" )
    ServiceManager.ticketService.imprimeTransaccionesInventario( fechaTicket )
  }

}

