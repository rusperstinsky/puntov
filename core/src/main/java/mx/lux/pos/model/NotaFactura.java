package mx.lux.pos.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table( name = "nota_factura", schema = "public" )
public class NotaFactura implements Serializable {

    private static final long serialVersionUID = -818367407661398459L;

    @Id
    @Column( name = "id_fiscal" )
    private String id;

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
    private String facturaOri;

    @Column( name = "id_cliente" )
    private String idCliente;

    @Column( name = "razon" )
    private String razon;

    @Column( name = "estatus" )
    private String estatus;

    @Column( name = "tipo_fac" )
    private String tipoFac;

    @Column( name = "nombre" )
    private String nombre;

    @Column( name = "calle" )
    private String calle;

    @Column( name = "noexterior" )
    private String noExterior;

    @Column( name = "nointerior" )
    private String noInterior;

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
    private Date fechaMod;

    @Column( name = "importe" )
    private String importe;

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


    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
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

    public String getFacturaOri() {
        return facturaOri;
    }

    public void setFacturaOri( String facturaOri ) {
        this.facturaOri = facturaOri;
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

    public String getTipoFac() {
        return tipoFac;
    }

    public void setTipoFac( String tipoFac ) {
        this.tipoFac = tipoFac;
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

    public String getNoExterior() {
        return noExterior;
    }

    public void setNoExterior( String noExterior ) {
        this.noExterior = noExterior;
    }

    public String getNoInterior() {
        return noInterior;
    }

    public void setNoInterior( String noInterior ) {
        this.noInterior = noInterior;
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

    public String getImporte() {
        return importe;
    }

    public void setImporte( String importe ) {
        this.importe = importe;
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

    public Date getFechaImpresion() {
        return fechaImpresion;
    }

    public void setFechaImpresion( Date fechaImpresion ) {
        this.fechaImpresion = fechaImpresion;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod( Date fechaMod ) {
        this.fechaMod = fechaMod;
    }

}
