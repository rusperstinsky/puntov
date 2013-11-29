package mx.lux.pos.service

import mx.lux.pos.model.Acuse
import mx.lux.pos.model.TransInv

public interface IOService {

  String getPartFilename( )

  void loadPartFile( )

  String loadPartFile( File pFile )

  String getPartClassFilename( )

  Map<String, Object> loadPartClassFile( File pFile )

  File getIncomingLocation( )

  FilenameFilter getPartMasterFilter( )

  File getArchiveLocation( )

  FilenameFilter getAllFilesFilter( )

  String getProductsFilePattern( )

  String getClasificationsFilePattern( )

  String getEmployeeFilePattern( )

  String getFxRatesFilePattern( )

  Map<String, Object> loadEmployeeFile( File pInputFile )

  Map<String, Object> loadFxRatesFile( File pInputFile )

  void logSalesNotification(String pIdFactura)

  void logAdjustmentNotification( Integer pIdMod )

  void startAsyncNotifyDispatcher()

  void saveAcknowledgement(Acuse pAcknowledgement)

  void saveAcknowledgementTrans(TransInv pAcknowledgement)

}