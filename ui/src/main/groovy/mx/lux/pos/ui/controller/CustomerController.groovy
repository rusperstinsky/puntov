package mx.lux.pos.ui.controller

import groovy.util.logging.Slf4j
import mx.lux.pos.model.Cliente
import mx.lux.pos.model.ClientePais
import mx.lux.pos.model.Estado
import mx.lux.pos.model.Paises
import mx.lux.pos.model.Rep
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mx.lux.pos.service.*
import mx.lux.pos.ui.model.*
import mx.lux.pos.ui.model.adapter.AgreementAdapter
import mx.lux.pos.ui.view.component.ComboBoxHintSelector
import mx.lux.pos.model.InstitucionIc
import mx.lux.pos.model.Titulo

@Slf4j
@Component
class CustomerController {

    private static ClienteService clienteService
    private static EstadoService estadoService
    private static MunicipioService municipioService
    private static LocalidadService localidadService
    private static ConvenioService convenioService
    private static PaisesService paisesService

    @Autowired
    public CustomerController(
            ClienteService clienteService,
            EstadoService estadoService,
            MunicipioService municipioService,
            LocalidadService localidadService,
            ConvenioService convenioService,
            PaisesService paisesService
    ) {
        this.clienteService = clienteService
        this.estadoService = estadoService
        this.municipioService = municipioService
        this.localidadService = localidadService
        this.convenioService = convenioService
        this.paisesService = paisesService
    }

    static private final String TAG_OTROS_PAISES = 'OTROS'


    static List<String> findAllStates() {
        log.debug("obteniendo lista de nombres de los estados")
        def results = estadoService.listaEstados()
        results.collect {
            it.nombre
        }
    }

    static String findStateById(String id) {
        log.info("obteniendo nombre de estado con id: ${id}")
        Estado result = estadoService.obtenerEstado(id)
        return result?.nombre ?: null
    }

    static String findDefaultState() {
        log.debug("obteniendo nombre estado por default")
        Estado result = estadoService.obtenEstadoPorDefecto()
        return result?.nombre ?: null
    }

    static List<String> findCitiesByStateName(String stateName) {
        log.debug("obteniendo lista de ciudades con nombre estado: ${stateName}")
        def results = municipioService.listaMunicipiosPorEstado(stateName)
        results.collect {
            it.nombre
        }
    }

    static List<LinkedHashMap<String, String>> findLocationsByStateNameAndCityName(String stateName, String cityName) {
        log.debug("obteniendo localidades con nombre estado: ${stateName} y nombre ciudad: ${cityName}")
        def results = localidadService.listaLocalidadesPorEstadoYMunicipio(stateName, cityName)
        results.collect {
            [
                    location: it?.usuario,
                    zipcode: it?.codigo
            ]
        }
    }

    static List<Address> findAddresesByZipcode(String zipcode) {
        log.debug("obteniendo lista de address con zipcode: ${zipcode}")
        def results = localidadService.listaLocalidadesPorCodigo(zipcode)
        results?.collect {
            new Address(
                    zipcode: it?.codigo,
                    location: it?.usuario,
                    city: it?.municipio?.nombre,
                    state: it?.municipio?.estado?.nombre
            )
        }
    }

    static Customer getCustomer(Integer id) {
        log.debug("obteniendo cliente id: ${id}")
        def result = clienteService.obtenerCliente(id)
        Customer.toCustomer(result)
    }

    static List<Customer> findCustomers(Customer sample) {
        log.debug("obteniendo lista de customer similar a: ${sample}")
        def results = clienteService.buscarCliente(sample?.name, sample?.fathersName, sample?.mothersName)
        log.debug("se obtiene lista con: ${results?.size()} customers")
        results.collect {
            Customer.toCustomer(it)
        }
    }

    static Customer addCustomer(Customer customer, boolean editar) {
        log.debug("registrando cliente: ${customer?.dump()}")
        if (StringUtils.isNotBlank(customer?.name)) {
            Estado estado = estadoService.obtenEstadoPorNombre(customer?.address?.state)
            def results = localidadService.listaLocalidadesPorCodigoYNombre(customer.address?.zipcode, customer.address?.location)
            log.debug("resultados de localidades: ${results*.usuario}")
            def localidad = results?.any() ? results?.first() : null
            Cliente cliente = new Cliente()
            if(customer?.id){
                cliente.id = customer.id
            }   
            cliente.nombre = customer.name
            cliente.apellidoPaterno = customer.fathersName
            cliente.apellidoMaterno = customer.mothersName
            cliente.titulo = customer.title
            cliente.sexo = customer.gender?.equals(GenderType.MALE)
            cliente.fechaNacimiento = customer.dob
            cliente.direccion = customer.address?.primary
            cliente.colonia = customer.address?.location
            cliente.codigo = customer.address?.zipcode
            cliente.rfc = customer.rfc ?: customer.type?.rfc
            cliente.idEstado = localidad?.idEstado ?: estado?.id
            cliente.idLocalidad = localidad?.idLocalidad
            def homeContact = customer.contacts?.find {
                ContactType.HOME_PHONE.equals(it?.type)
            }
            cliente.telefonoCasa = homeContact?.primary
            def emailContact = customer.contacts?.find {
                ContactType.EMAIL.equals(it?.type)
            }
            cliente.email = emailContact?.primary
            cliente.udf1 = customer.age
            cliente = clienteService.agregarCliente(cliente, editar)
            customer.id = cliente?.id
            if (CustomerType.FOREIGN.equals(customer.type)) {
                ClientePais clientePais = new ClientePais(
                        id: customer.id,
                        ciudad: customer.address?.city,
                        pais: StringUtils.trimToEmpty(customer.address?.country).trim().length() > 0 ?customer.address?.country.toUpperCase(): ''
                )
                clientePais = clienteService.agregarClientePais(clientePais)
                cliente.clientePais = clientePais
            }
            return customer
        }
        return null
    }

    static Customer findDefaultCustomer() {
        log.debug("obteniendo customer por default")
        Customer.toCustomer(clienteService.obtenerClientePorDefecto())
    }

    static List<LinkedHashMap<String, Object>> findAllCustomersTitles() {
        log.debug("obteniendo lista de titulos")
        def results = clienteService.listarTitulosClientes()
        results?.collect {
            [
                    title: it?.titulo,
                    gender: it?.sexoTitulo
            ]
        }
    }

    static List<String> findAllCustomersDomains() {
        log.debug("obteniendo lista de dominios")
        def results = clienteService.listarDominiosClientes()
        results?.collect {
            it?.nombre
        }
    }


    static List<LinkedHashMap<String, Object>> findAllConventions( String clave ) {
        log.debug("obteniendo lista de convenios")
        def results = convenioService.obtenerConvenios( clave )
        results?.collect {
            [
                    id: it?.id,
                    iniciales: it?.inicialesIc
            ]
        }
    }

    static String countryCustomer( Order order ) {
        String paisCliente = ''
        Cliente cliente = clienteService.obtenerCliente( order.customer.id )
        if(cliente != null){
            paisCliente = cliente.clientePais.pais
        }
        return paisCliente
    }

    static List<String> countries( ) {
        List<String> lstPaises = new ArrayList<>()
        List<Paises> paises = paisesService.obtenerPaises()
        for(Paises pais : paises){
            lstPaises.add( pais.pais )
        }
        lstPaises.add(TAG_OTROS_PAISES)
        return lstPaises
    }

    static List<String> states( ) {
        List<String> lstEstados = new ArrayList<>()
        List<Rep> estados = paisesService.obtenerEstados()
        lstEstados.add('')
        for(Rep estado : estados){
            lstEstados.add( estado.nombre )
        }
        return lstEstados
    }

    static void saveOrderCountries( String pais ){
        paisesService.guardarOrdenPais( pais )
    }

    static void saveCountries( String pais ){
        paisesService.guardarPais( pais )
    }
}
