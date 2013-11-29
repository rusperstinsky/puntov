package mx.lux.pos.service.impl

import java.sql.Date as SQLdate

import mx.lux.pos.model.InvTrDetRequest
import mx.lux.pos.model.InvTrRequest
import mx.lux.pos.model.TransInv
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.service.InventarioService
import mx.lux.pos.service.NotaVentaService
import mx.lux.pos.util.TransInvFilter
import org.apache.commons.lang3.time.DateUtils
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.text.SimpleDateFormat
import javax.annotation.Resource
import mx.lux.pos.service.io.InventoryAdjustFile
import mx.lux.pos.model.InvAdjustSheet

@ContextConfiguration('classpath:spring-config.xml')
class InventarioServiceIntegration extends Specification {

  @Resource
  private InventarioService service
  @Resource
  private ArticuloService parts
  @Resource
  private NotaVentaService orders 
  
  def "test ListarTiposTransaccion"(  ) {
    when:
      def list = service.listarTiposTransaccion()
      println "\nTipos de Transaccion"
      list.each() { println it }
      
    then:
      list.size() == 5
  }

  def "test ObtenerUltimaFecha Transaccion"(  ) {
    when:
      Date expected = DateUtils.truncate(SQLdate.valueOf("2012-07-26"), Calendar.DATE)
      def lastDate = service.obtenerUltimaFechaTransaccion()
      println "\nUltima Fecha Transaccion: " + new SimpleDateFormat("dd/MMM/yyyy").format(lastDate)
      
    then:
      lastDate == expected
  }  

  def "test Listar Transaccion By Filtro"(  ) {
    def filter = new TransInvFilter()
    def Date dateFrom = (Date) java.sql.Date.valueOf( "2012-07-24" )
    def Date dateTo = dateFrom + 1
    def trType = "VENTA"
    def siteTo = 1
    def sku = 159931
    def partCode = "EA"
    
    when: 
      filter.setDateRange( dateFrom, dateTo )
      List<TransInv> trList = service.listarTransacciones( filter )
      println "\nTransacciones por Rango de fechas ${ dateFrom } - ${ dateTo }"
      trList.each() { println it }
      
    then:
      trList.size() == 8
      
    when:
      filter.reset(  )
      filter.setIdTrType( trType )
      trList = service.listarTransacciones( filter )
      println "\nTransacciones por Tipo ${ trType }"
      trList.each() { println it }

    then:
      trList.size() == 10

    when:
      filter.reset(  )
      filter.setSiteTo( siteTo )
      trList = service.listarTransacciones( filter )
      println "\nTransacciones por Sucursal Destino ${ siteTo }"
      trList.each() { println it }

    then:
      trList.size() == 4
      
    when:
      filter.reset(  )
      filter.setDateRange( dateFrom, dateTo )
      filter.setIdTrType( trType )
      trList = service.listarTransacciones( filter )
      println "\nTransacciones por Tipo(${ trType }) y Fecha(${ dateFrom } - ${ dateTo })"
      trList.each() { println it }

    then:
      trList.size() == 5

    when:
      filter.reset(  )
      filter.setSku( sku )
      trList = service.listarTransacciones( filter )
      println "\nTransacciones por SKU ${ sku }"
      trList.each() { println it }

    then:
      trList.size() == 3

    when:
      filter.reset(  )
      filter.setIdPartCode( partCode )
      trList = service.listarTransacciones( filter )
      println "\nTransacciones por PartCode ${ partCode }"
      trList.each() { println it }

    then:
      trList.size() == 7

    when:
      filter.reset(  )
      filter.setDateRange( dateFrom - 1 )
      filter.setIdPartCode( partCode )
      trList = service.listarTransacciones( filter )
      println "\nTransacciones por PartCode ${ partCode } y Fecha(${ dateFrom } - ${ dateTo })"
      trList.each() { println it }

    then:
      trList.size() == 6

  }  

  def "test Obtener existencias por Articulo" ( ) {
    expect:
      service.obtenerExistenciaPorArticulo( idArticulo ) == existenciaExpected
     
    where:
      idArticulo << [111618, 126165, 128743, 144830, 160396]
      existenciaExpected << [1, 1, 0, 0, 1]
  }

  def "test Solicitar Transaction Request"  (  ) {
    when:
      InvTrRequest request = [ TrType: "VENTA", effDate: new Date(), reference: "TEST", remarks: "Generada en Prueba", idUser: "9999" ]
      request.skuList = [ new InvTrDetRequest(169800, 1), new InvTrDetRequest(169383, 2) ]
      println "\nTransaction Request:"
      println request
      
    then:
      service.solicitarTransaccion( request ) == true

    when:
      request = [ TrType: "VENTA", effDate: new Date(), reference: "TEST", remarks: "Generada en Prueba", idUser: "9999" ]
      request.skuList = [ new InvTrDetRequest(169810, 1), new InvTrDetRequest(169383, 2) ]
      println "\nTransaction Request:"
      println request

    then:
      service.solicitarTransaccion( request ) == false

    when:
      request = [ TrType: "VENTA", effDate: new Date(), siteTo: 120,  reference: "TEST", idUser: "9999" ]
      request.skuList = [ new InvTrDetRequest(169800, 1), new InvTrDetRequest(169383, 2) ]
      println "\nTransaction Request:"
      println request
      
    then:
      service.solicitarTransaccion( request ) == false
  }
 
  def "test Solicitar Transaccion Venta" ( ) {
    setup:
      def order = orders.obtenNotaVenta( "B28235" )
      Boolean expected = true
      
    when:
      Boolean result = service.solicitarTransaccionVenta(order)
      
    then:
      result == expected
  }

  def "test Inventory Adjust File"() {
    when:
    InventoryAdjustFile file = new InventoryAdjustFile()
    InvAdjustSheet document = file.read( '/home/datos/por_recibir/Ajuste.04-04-2013.inv' )
    println document.toString()

    then:
    document.lines.size() > 0

  }
}
