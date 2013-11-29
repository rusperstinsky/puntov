package mx.lux.pos.ui.controller

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.Empleado
import mx.lux.pos.model.InvAdjustLine
import mx.lux.pos.model.InvAdjustSheet
import mx.lux.pos.model.InvTrRequest
import mx.lux.pos.model.Parametro
import mx.lux.pos.model.Sucursal
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.service.business.Registry
import mx.lux.pos.ui.model.Branch
import mx.lux.pos.ui.model.InvTr
import mx.lux.pos.ui.model.InvTrViewMode
import mx.lux.pos.ui.model.Session
import mx.lux.pos.ui.model.SessionItem
import mx.lux.pos.ui.model.User
import mx.lux.pos.ui.model.adapter.InvTrFilter
import mx.lux.pos.ui.model.adapter.RequestAdapter
import mx.lux.pos.ui.resources.ServiceManager
import mx.lux.pos.ui.view.dialog.ImportPartMasterDialog
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import mx.lux.pos.service.IOService
import mx.lux.pos.ui.model.file.FileFilteredList
import mx.lux.pos.ui.model.file.FileFiltered
import mx.lux.pos.ui.view.dialog.ImportClasificationArticleDialog
import mx.lux.pos.ui.model.file.DateFileFiltered

import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import java.text.SimpleDateFormat
import org.apache.commons.lang.StringUtils


class IOController {

    private Logger log = LoggerFactory.getLogger(this.getClass())
    private static IOController instance

    private static InvTr data = new InvTr()

    private IOController() { }

    static IOController getInstance() {
        if (instance == null) {
            instance = new IOController()
        }
        return instance
    }

    void requestImportPartMaster() {
        log.debug(String.format('Request ImportPartMaster'))
        ImportPartMasterDialog dialog = new ImportPartMasterDialog()
        dialog.setFilenamePattern(ServiceManager.ioServices.productsFilePattern)
        dialog.activate()
    }

    void requestImportClasificationArtMaster() {
        log.debug(String.format('Request ImportClasificationArtMaster'))
        ImportClasificationArticleDialog dialog = new ImportClasificationArticleDialog()
        dialog.setFilenamePattern( ServiceManager.ioServices.clasificationsFilePattern )
        dialog.activate()
    }


    String dispatchImportPartMaster(File pFile) {
      String errorSku = ServiceManager.ioServices.loadPartFile(pFile)
      return errorSku
    }

    void autoUpdateEmployeeFile() {
        this.log.debug('AutoUpdate EmployeeFile')
        String pattern = ServiceManager.ioServices.getEmployeeFilePattern()
        FileFilteredList list = new FileFilteredList(pattern)
        File incomingPath = ServiceManager.ioServices.getIncomingLocation()
        for (File f : incomingPath.listFiles()) {
            list.add(f)
        }
        File f = list.pop()
        while (f != null) {
            ServiceManager.ioServices.loadEmployeeFile(f)
            f = list.pop()
        }
    }

    void autoUpdateFxRates() {
        this.log.debug('AutoUpdate FxRates')
        String pattern = ServiceManager.ioServices.getFxRatesFilePattern()
        FileFilteredList list = new FileFilteredList(pattern)
        File incomingPath = ServiceManager.ioServices.getIncomingLocation()
        for (File f : incomingPath.listFiles()) {
            list.add(f)
        }
        File f = list.pop()
        while (f != null) {
            ServiceManager.ioServices.loadFxRatesFile(f)
            f = list.pop()
        }
    }

    void startAsyncNotifyDispatcher() {
        this.log.debug('Trigger Async Notification Dispatcher')
        ServiceManager.ioServices.startAsyncNotifyDispatcher()
    }


    void dispatchImportClasificationArt(File pFile) {
        this.log.debug( 'Importando Clasificacion de Articulos' )
        Map<String, Object> importSummary = ServiceManager.ioServices.loadPartClassFile( pFile )
    }


    Boolean validateCentroCostos( String centroCostos ){
        Boolean valid = false
        Sucursal sucActual = ServiceManager.storeService.obtenSucursalActual()
        if( !sucActual.centroCostos.trim().equalsIgnoreCase(centroCostos.trim()) ){
            valid = true
        }
        return valid
    }

    Boolean isManagerLogged( ){
        Boolean isManager = false
        User user = Session.get( SessionItem.USER ) as User
        String gerente = ServiceManager.employeeService.gerente( )
        log.debug( "usuario en sesion: ${user?.username}" )
        if ( org.apache.commons.lang3.StringUtils.isNotBlank( user?.username ) ) {
            Empleado empleado = ServiceManager.employeeService.obtenerEmpleado( user.username )
            if( gerente.trim().contains(empleado.id.trim()) ){
                isManager = true
            }
        }
        return isManager
    }


    void loadAdjustFile( ){
      log.debug( "loadAdjustFile( )" )
      String receivePath = Registry.inputFilePath
      File folder = new File( receivePath )
        if ( folder?.canRead() ){
          folder.eachFileMatch( ~/.+_.+\.reg/ ) { File file ->
              log.debug( "Archivo ajuste: ${file.absolutePath}" )
              InvAdjustSheet document = ServiceManager.inventoryService.leerArchivoAjuste( file.absolutePath )
              if ( document != null ) {
                  data = new InvTr()
                  dispatchDocument( document )
                  data.inFile = new File( file.absolutePath )
                  data.postTrType = ServiceManager.inventoryService.obtenerTipoTransaccionAjuste()
                  data.viewMode = InvTrViewMode.FILE_ADJUST
                  InvTrRequest request = RequestAdapter.getRequest( data )
                  log.debug ( String.format('Adjust File: %s', document.headerToString()) )
                  if ( request != null ) {
                      request.remarks = request.remarks.replaceAll("[^a-zA-Z0-9]+"," ");
                      Integer trNbr = ServiceManager.getInventoryService().solicitarTransaccion( request )
                      if ( trNbr != null ) {
                          if ( data.inFile != null ) {
                              try {
                                  String fileTmp = file.absolutePath.split( "/" ).last()
                                  String fileName = fileTmp.split( "_" ).last()
                                  String[] folio = fileName.split( /\./ )
                                  ServiceManager.getInventoryService().generaArchivoAcuseAjuste( folio[0] )
                                  println data.viewMode
                                  if (InvTrViewMode.FILE_ADJUST.equals( data.viewMode )) {
                                      println data.inFile.absolutePath
                                      data.inFile.delete();
                                  }
                              } catch (Exception e) {
                                  println e.getMessage()
                              }
                          }
                          InvTrViewMode viewMode = data.viewMode
                      }
                  } else {
                      log.debug( "[Controller] Request not available" )
                  }
              }
          }
        }
    }

    void dispatchDocument( InvAdjustSheet pDocument ){
        data.receiptDocument = null
        data.adjustDocument = pDocument
        if ( pDocument.site == Registry.currentSite ) {
            ArticuloService partMaster = ServiceManager.partService
            data.postReference = org.apache.commons.lang3.StringUtils.trimToEmpty(data.adjustDocument.ref )
            data.postRemarks = org.apache.commons.lang3.StringUtils.trimToEmpty( data.adjustDocument.trReason )
            for ( InvAdjustLine ln in data.adjustDocument.lines ) {
                Articulo part = partMaster.obtenerArticulo( ln.sku, false )
                if ( part != null ) {
                    data.addPart( part, ln.qty )
                }
            }
        }
    }

}

