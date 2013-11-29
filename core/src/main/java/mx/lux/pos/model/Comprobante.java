package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "nota_factura", schema = "public" )
public class Comprobante implements Serializable {

    private static final long serialVersionUID = 5101745149373530531L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "nota_factura_id_seq" )
    @SequenceGenerator( name = "nota_factura_id_seq", sequenceName = "nota_factura_id_seq" )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_fiscal" )
    private String idFiscal;

    @Column( name = "rfc" )
    private String rfc;

    @Column( name = "ticket" )
    private String ticket;

    @Column( name = "id_empleado" )
    private String idEmpleado;

    @Column( name = "id_factura" )
    private String idFactura;

    @Column( name = "factura" )
    private String factura;

    @Column( name = "tipo" )
    private String tipo;

    @Column( name = "factura_ori" )
    private String idOrigen;

    @Column( name = "id_cliente" )
    private String idCliente;

    @Column( name = "razon" )
    private String razon;

    @Column( name = "estatus" )
    private String estatus;

    @Column( name = "tipo_fac" )
    private String tipoFactura;

    @Column( name = "nombre" )
    private String nombre;

    @Column( name = "calle" )
    private String calle;

    @Column( name = "noexterior" )
    private String exterior;

    @Column( name = "nointerior" )
    private String interior;

    @Column( name = "colonia" )
    private String colonia;

    @Column( name = "localidad" )
    private String localidad;

    @Column( name = "referencia" )
    private String referencia;

    @Column( name = "municipio" )
    private String municipio;

    @Column( name = "estado" )
    private String estado;

    @Column( name = "pais" )
    private String pais;

    @Column( name = "codigopostal" )
    private String codigoPostal;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_impresion" )
    private Date fechaImpresion;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod" )
    private Date fechaModificacion;

    @Column( name = "importe" )
    private String importe;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "subtotal" )
    private BigDecimal subtotal;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "impuestos" )
    private BigDecimal impuestos;

    @Column( name = "email" )
    private String email;

    @Column( name = "url" )
    private String url;

    @Column( name = "xml" )
    private String xml;

    @Column( name = "rx" )
    private String rx;

    @Column( name = "paciente" )
    private String paciente;

    @Column( name = "forma_pago" )
    private String formaPago;

    @Column( name = "metodo_pago" )
    private String metodoPago;

    @Column( name = "observaciones" )
    private String observaciones;

    @Column( name = "sello" )
    private String sello;

    @Column( name = "cadena_original" )
    private String cadenaOriginal;

    @OneToMany
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_fiscal", referencedColumnName = "id_fiscal", insertable = false, updatable = false )
    private Set<DetalleComprobante> detalles = new HashSet<DetalleComprobante>();

    @PrePersist
    private void onPrePersist() {
        fechaImpresion = new Date();
        fechaModificacion = fechaImpresion;
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaModificacion = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        idFiscal = StringUtils.trimToEmpty( idFiscal );
        rfc = StringUtils.trimToEmpty( rfc );
        ticket = StringUtils.trimToEmpty( ticket );
        idEmpleado = StringUtils.trimToEmpty( idEmpleado );
        idFactura = StringUtils.trimToEmpty( idFactura );
        factura = StringUtils.trimToEmpty( factura );
        tipo = StringUtils.trimToEmpty( tipo );
        idOrigen = StringUtils.trimToEmpty( idOrigen );
        idCliente = StringUtils.trimToEmpty( idCliente );
        razon = StringUtils.trimToEmpty( razon );
        estatus = StringUtils.trimToEmpty( estatus );
        tipoFactura = StringUtils.trimToEmpty( tipoFactura );
        nombre = StringUtils.trimToEmpty( nombre );
        calle = StringUtils.trimToEmpty( calle );
        exterior = StringUtils.trimToEmpty( exterior );
        interior = StringUtils.trimToEmpty( interior );
        colonia = StringUtils.trimToEmpty( colonia );
        localidad = StringUtils.trimToEmpty( localidad );
        referencia = StringUtils.trimToEmpty( referencia );
        municipio = StringUtils.trimToEmpty( municipio );
        estado = StringUtils.trimToEmpty( estado );
        pais = StringUtils.trimToEmpty( pais );
        codigoPostal = StringUtils.trimToEmpty( codigoPostal );
        importe = StringUtils.trimToEmpty( importe );
        email = StringUtils.trimToEmpty( email );
        url = StringUtils.trimToEmpty( url );
        xml = StringUtils.trimToEmpty( xml );
        rx = StringUtils.trimToEmpty( rx );
        paciente = StringUtils.trimToEmpty( paciente );
        formaPago = StringUtils.trimToEmpty( formaPago );
        metodoPago = StringUtils.trimToEmpty( metodoPago );
        observaciones = StringUtils.trimToEmpty( observaciones );
        sello = StringUtils.trimToEmpty( sello );
        cadenaOriginal = StringUtils.trimToEmpty( cadenaOriginal );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getIdFiscal() {
        return idFiscal;
    }

    public void setIdFiscal( String idFiscal ) {
        this.idFiscal = idFiscal;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc( String rfc ) {
        this.rfc = rfc;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket( String ticket ) {
        this.ticket = ticket;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado( String idEmpleado ) {
        this.idEmpleado = idEmpleado;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura( String idFactura ) {
        this.idFactura = idFactura;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura( String factura ) {
        this.factura = factura;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String tipo ) {
        this.tipo = tipo;
    }

    public String getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen( String idOrigen ) {
        this.idOrigen = idOrigen;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente( String idCliente ) {
        this.idCliente = idCliente;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon( String razon ) {
        this.razon = razon;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus( String estatus ) {
        this.estatus = estatus;
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura( String tipoFactura ) {
        this.tipoFactura = tipoFactura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre ) {
        this.nombre = nombre;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle( String calle ) {
        this.calle = calle;
    }

    public String getExterior() {
        return exterior;
    }

    public void setExterior( String exterior ) {
        this.exterior = exterior;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior( String interior ) {
        this.interior = interior;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia( String colonia ) {
        this.colonia = colonia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad( String localidad ) {
        this.localidad = localidad;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia( String referencia ) {
        this.referencia = referencia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio( String municipio ) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado( String estado ) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais( String pais ) {
        this.pais = pais;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal( String codigoPostal ) {
        this.codigoPostal = codigoPostal;
    }

    public Date getFechaImpresion() {
        return fechaImpresion;
    }

    public void setFechaImpresion( Date fechaImpresion ) {
        this.fechaImpresion = fechaImpresion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion( Date fechaModificacion ) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte( String importe ) {
        this.importe = importe;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal( BigDecimal subtotal ) {
        this.subtotal = subtotal;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos( BigDecimal impuestos ) {
        this.impuestos = impuestos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public String getXml() {
        return xml;
    }

    public void setXml( String xml ) {
        this.xml = xml;
    }

    public String getRx() {
        return rx;
    }

    public void setRx( String rx ) {
        this.rx = rx;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente( String paciente ) {
        this.paciente = paciente;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago( String formaPago ) {
        this.formaPago = formaPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago( String metodoPago ) {
        this.metodoPago = metodoPago;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones( String observaciones ) {
        this.observaciones = observaciones;
    }

    public Set<DetalleComprobante> getDetalles() {
        return detalles;
    }

    public void setDetalles( Set<DetalleComprobante> detalles ) {
        this.detalles = detalles;
    }

    public String getSello() {
        return sello;
    }

    public void setSello(String sello) {
        this.sello = sello;
    }

    public String getCadenaOriginal() {
        return cadenaOriginal;
    }

    public void setCadenaOriginal(String cadenaOriginal) {
        this.cadenaOriginal = cadenaOriginal;
    }
}
