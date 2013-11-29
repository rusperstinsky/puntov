package mx.lux.pos.util

import java.sql.Date as SQLdate

import mx.lux.pos.model.TransInv
import mx.lux.pos.model.TransInvDetalle
import spock.lang.Shared
import spock.lang.Specification

import java.text.SimpleDateFormat

class TransInvFilterTest extends Specification {
  
  @Shared List<TransInv> trList = []

  def setupSpec() {
    def TransInv tr
    for ( List<String> data in setupData() ) {
      if ( data[0] == "TrMstr" ) {
        tr = buildTrMstr( data )
        trList.add(tr)
      } else {
        tr.add( buildTrDet( data ) )
      }
    }
  }

  // Internal Methods
  private TransInv buildTrMstr( List<String> pData ) {
    final parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    TransInv tr = new TransInv()
    tr.setIdTipoTrans( pData[1] )
    tr.setFolio( Integer.valueOf( pData[2] ) )
    tr.setFecha( SQLdate.valueOf( pData[3] ) )
    tr.setSucursal( Integer.valueOf( pData[4] ) )
    tr.setSucursalDestino( Integer.valueOf( pData[5] ) )
    tr.setReferencia( pData[6] )
    tr.setObservaciones( pData[7] )
    tr.setIdEmpleado( pData[8] )
    tr.setFechaMod( parser.parse( pData[9] ) )
    return tr
  }
  
  private TransInvDetalle buildTrDet( List<String> pData ) {
    TransInvDetalle trDet = new TransInvDetalle()
    trDet.setIdTipoTrans( pData[1] )
    trDet.setFolio( Integer.valueOf( pData[2] ) )
    trDet.setLinea( Integer.valueOf( pData[3] ) )
    trDet.setSku( Integer.valueOf( pData[4] ) )
    trDet.setTipoMov( pData[5] )
    trDet.setCantidad( Integer.valueOf( pData[6] ) )
    return trDet
  }

  private printList ( String pMessage, List<TransInv> pList ) {
    println String.format( "%s (Size: %,d)", pMessage, pList.size() )
    if (pList.size() > 0) {
      println pList[0]
      pList[0].getTrDet().each() { println it }
    }
  } 
   
  private setupData() {
    def dataset = []
    dataset.add( [ "TrMstr", "AJUSTE", "1", "2012-07-25", "9999", "0", "", "Auditoria Inventario", "1769", "2012-07-25 11:00:00" ] )
    dataset.add( [ "TrDet", "AJUSTE", "1", "1", "161717", "E", "-1" ])
    dataset.add( [ "TrDet", "AJUSTE", "1", "2", "155509", "E", "1" ])
    dataset.add( [ "TrDet", "AJUSTE", "1", "3", "143661", "E", "-1" ])
    dataset.add( [ "TrDet", "AJUSTE", "1", "4", "118963", "E", "1" ])
    dataset.add( [ "TrMstr", "AJUSTE", "2", "2012-07-26", "9999", "0", "", "Auditoria Inventario", "1769", "2012-07-26 11:50:00" ] )
    dataset.add( [ "TrMstr", "DEVOLUCION", "1", "2012-07-23", "9999", "0", "B29212", "Rotos en garantia", "1567", "2012-07-23 09:40:00" ] )
    dataset.add( [ "TrDet", "DEVOLUCION", "1", "1", "112511", "E", "1" ])
    dataset.add( [ "TrMstr", "ENTRADA", "1", "2012-07-23", "9999", "0", "R00331", "Inventario Inicial", "1567", "2012-07-23 08:20:00" ] )
    dataset.add( [ "TrDet", "ENTRADA", "1", "1", "7433", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "2", "8166", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "3", "8195", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "4", "112625", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "5", "123743", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "6", "141743", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "7", "143057", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "8", "148564", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "9", "154964", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "10", "155509", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "11", "251", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "12", "540", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "13", "584", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "14", "943", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "15", "2229", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "16", "3088", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "17", "4978", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "18", "5722", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "19", "6196", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "20", "6831", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "21", "7962", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "22", "8206", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "23", "8428", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "24", "8622", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "25", "8635", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "26", "9188", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "27", "9554", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "28", "11342", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "29", "12449", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "1", "30", "13217", "E", "1" ])
    dataset.add( [ "TrMstr", "ENTRADA", "2", "2012-07-23", "9999", "0", "R00338", "Inventario Inicial", "2017", "2012-07-23 08:30:00" ] )
    dataset.add( [ "TrDet", "ENTRADA", "2", "1", "13521", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "2", "14007", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "3", "14828", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "4", "15430", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "5", "100840", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "6", "101327", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "7", "102509", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "8", "103700", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "9", "104742", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "10", "104754", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "11", "104961", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "12", "105017", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "13", "105580", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "14", "106245", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "15", "106381", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "16", "106502", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "17", "106633", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "18", "107113", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "19", "109143", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "20", "109403", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "21", "110395", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "22", "110521", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "23", "110587", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "24", "111036", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "25", "111097", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "26", "111618", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "27", "111767", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "28", "112269", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "29", "112511", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "2", "30", "113166", "E", "1" ])
    dataset.add( [ "TrMstr", "ENTRADA", "3", "2012-07-23", "9999", "0", "R00345", "Inventario Inicial", "1769", "2012-07-23 08:40:00" ] )
    dataset.add( [ "TrDet", "ENTRADA", "3", "1", "113280", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "2", "115314", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "3", "115319", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "4", "115660", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "5", "115967", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "6", "116527", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "7", "117384", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "8", "117422", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "9", "117631", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "10", "117860", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "11", "118175", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "12", "118963", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "13", "119114", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "14", "120457", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "15", "120576", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "16", "120742", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "17", "121361", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "18", "121518", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "19", "121799", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "20", "122139", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "21", "122502", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "22", "123252", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "23", "125533", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "24", "125635", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "25", "126124", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "26", "126165", "E", "3" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "27", "126175", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "28", "126367", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "29", "128743", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "3", "30", "128897", "E", "1" ])
    dataset.add( [ "TrMstr", "ENTRADA", "4", "2012-07-23", "9999", "0", "R00351", "Inventario Inicial", "2017", "2012-07-23 08:50:00" ] )
    dataset.add( [ "TrDet", "ENTRADA", "4", "1", "130682", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "2", "135225", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "3", "135943", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "4", "136092", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "5", "136657", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "6", "136760", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "7", "137166", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "8", "138481", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "9", "139601", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "10", "140258", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "11", "140308", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "12", "140319", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "13", "140481", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "14", "140648", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "15", "140713", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "16", "141059", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "17", "143355", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "18", "143661", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "19", "144830", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "20", "145127", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "21", "145964", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "22", "146339", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "23", "146587", "E", "3" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "24", "146877", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "25", "147101", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "26", "147522", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "27", "150367", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "28", "150391", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "29", "150742", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "4", "30", "150876", "E", "1" ])
    dataset.add( [ "TrMstr", "ENTRADA", "5", "2012-07-23", "9999", "0", "R00352", "Inventario Inicial", "1769", "2012-07-23 09:00:00" ] )
    dataset.add( [ "TrDet", "ENTRADA", "5", "1", "151429", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "2", "153260", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "3", "153869", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "4", "154909", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "5", "159578", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "6", "159836", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "7", "159931", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "8", "160396", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "9", "161717", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "10", "161731", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "11", "163505", "E", "2" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "12", "163744", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "13", "164602", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "14", "164819", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "15", "166901", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "16", "167014", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "17", "168820", "E", "1" ])
    dataset.add( [ "TrDet", "ENTRADA", "5", "18", "170253", "E", "1" ])
    dataset.add( [ "TrMstr", "SALIDA", "1", "2012-07-24", "9999", "1", "E00028", "Prestamo de Armazones", "2017", "2012-07-24 10:20:00" ] )
    dataset.add( [ "TrDet", "SALIDA", "1", "1", "117422", "S", "1" ])
    dataset.add( [ "TrDet", "SALIDA", "1", "2", "143661", "S", "1" ])
    dataset.add( [ "TrDet", "SALIDA", "1", "3", "105580", "S", "1" ])
    dataset.add( [ "TrDet", "SALIDA", "1", "4", "166901", "S", "1" ])
    dataset.add( [ "TrMstr", "SALIDA", "2", "2012-07-25", "9999", "1", "E00028", "Prestamo de Armazones", "2017", "2012-07-25 10:50:00" ] )
    dataset.add( [ "TrDet", "SALIDA", "2", "1", "111036", "S", "1" ])
    dataset.add( [ "TrDet", "SALIDA", "2", "2", "154964", "S", "1" ])
    dataset.add( [ "TrMstr", "SALIDA", "3", "2012-07-26", "9999", "1", "E00028", "Prestamo de Armazones", "2017", "2012-07-26 11:30:00" ] )
    dataset.add( [ "TrDet", "SALIDA", "3", "1", "155509", "S", "1" ])
    dataset.add( [ "TrMstr", "SALIDA", "4", "2012-07-26", "9999", "1", "E00028", "Prestamo de Armazones", "2017", "2012-07-26 11:40:00" ] )
    dataset.add( [ "TrDet", "SALIDA", "4", "1", "155509", "S", "-1" ])
    dataset.add( [ "TrDet", "SALIDA", "4", "2", "123743", "S", "1" ])
    dataset.add( [ "TrMstr", "VENTA", "1", "2012-07-23", "9999", "0", "B29210", "", "2017", "2012-07-23 09:10:00" ] )
    dataset.add( [ "TrDet", "VENTA", "1", "1", "119114", "S", "1" ])
    dataset.add( [ "TrDet", "VENTA", "1", "2", "153869", "S", "1" ])
    dataset.add( [ "TrMstr", "VENTA", "2", "2012-07-23", "9999", "0", "B29211", "", "1567", "2012-07-23 09:20:00" ] )
    dataset.add( [ "TrDet", "VENTA", "2", "1", "109143", "S", "1" ])
    dataset.add( [ "TrDet", "VENTA", "2", "2", "147101", "S", "1" ])
    dataset.add( [ "TrMstr", "VENTA", "3", "2012-07-23", "9999", "0", "B29212", "", "1769", "2012-07-23 09:30:00" ] )
    dataset.add( [ "TrDet", "VENTA", "3", "1", "128743", "S", "1" ])
    dataset.add( [ "TrDet", "VENTA", "3", "2", "112511", "S", "1" ])
    dataset.add( [ "TrMstr", "VENTA", "4", "2012-07-24", "9999", "0", "B29215", "", "1769", "2012-07-24 09:50:00" ] )
    dataset.add( [ "TrDet", "VENTA", "4", "1", "135943", "S", "1" ])
    dataset.add( [ "TrDet", "VENTA", "4", "2", "144830", "S", "1" ])
    dataset.add( [ "TrMstr", "VENTA", "5", "2012-07-24", "9999", "0", "B29216", "", "1769", "2012-07-24 10:00:00" ] )
    dataset.add( [ "TrDet", "VENTA", "5", "1", "125533", "S", "1" ])
    dataset.add( [ "TrDet", "VENTA", "5", "2", "159931", "S", "1" ])
    dataset.add( [ "TrDet", "VENTA", "5", "3", "151429", "S", "1" ])
    dataset.add( [ "TrMstr", "VENTA", "6", "2012-07-24", "9999", "0", "B29217", "", "2017", "2012-07-24 10:10:00" ] )
    dataset.add( [ "TrDet", "VENTA", "6", "1", "115967", "S", "1" ])
    dataset.add( [ "TrMstr", "VENTA", "7", "2012-07-25", "9999", "0", "B29220", "", "1567", "2012-07-25 10:30:00" ] )
    dataset.add( [ "TrDet", "VENTA", "7", "1", "159931", "S", "1" ])
    dataset.add( [ "TrDet", "VENTA", "7", "2", "101327", "S", "1" ])
    dataset.add( [ "TrMstr", "VENTA", "8", "2012-07-25", "9999", "0", "B29221", "", "2017", "2012-07-25 10:40:00" ] )
    dataset.add( [ "TrDet", "VENTA", "8", "1", "111036", "S", "1" ])
    dataset.add( [ "TrDet", "VENTA", "8", "2", "150367", "S", "1" ])
    dataset.add( [ "TrMstr", "VENTA", "9", "2012-07-26", "9999", "0", "B29225", "", "1567", "2012-07-26 11:10:00" ] )
    dataset.add( [ "TrDet", "VENTA", "9", "1", "145127", "S", "1" ])
    dataset.add( [ "TrDet", "VENTA", "9", "2", "118175", "S", "1" ])
    dataset.add( [ "TrDet", "VENTA", "9", "3", "140713", "S", "1" ])
    dataset.add( [ "TrDet", "VENTA", "9", "4", "146587", "S", "1" ])
    dataset.add( [ "TrMstr", "VENTA", "10", "2012-07-26", "9999", "0", "B29226", "", "1769", "2012-07-26 11:20:00" ] )
    dataset.add( [ "TrDet", "VENTA", "10", "1", "104742", "S", "1" ])
    return dataset
  }
  
  // Test Methods
  def "Test Dataset"() {
    when: 
      printList( "Dataset", trList )
      
    then:
      trList.size() == 22
  }

  def "Test Filter by Date" ()  {
    when: 
      TransInvFilter filter = new TransInvFilter()
      Date dateFrom = (Date) java.sql.Date.valueOf( "2012-07-24" )
      filter.setDateRange(dateFrom, dateFrom + 1)
      println filter
      def list = filter.filter( trList )
      printList( "Filtered by Date", list )
      
    then:
      list.size() == 8
  } 

  def "Test Filter by TrType" ()  {
    when:
      TransInvFilter filter = new TransInvFilter()
      filter.setDateRange(null)
      filter.setIdTrType("VENTA")
      println filter
      def list = filter.filter( trList )
      printList( "Filtered by Date", list )
      
    then:
      list.size() == 10
  }

  def "Test Filter by SiteTo" ()  {
    when:
      TransInvFilter filter = new TransInvFilter()
      filter.setDateRange(null)
      filter.setSiteTo(1)
      println filter
      def list = filter.filter( trList )
      printList( "Filtered by Date", list )
      
    then:
      list.size() == 4
  }

  def "Test Filter by SKU" ()  {
    when:
      TransInvFilter filter = new TransInvFilter()
      filter.setDateRange(null)
      filter.setSku(159931)
      println filter
      def list = filter.filter( trList )
      printList( "Filtered by Date", list )
      
    then:
      list.size() == 3
  }

}
