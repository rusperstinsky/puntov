package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "jb_externos", schema = "public" )
public class Externo implements Serializable {

    private static final long serialVersionUID = -6196159190484292142L;

    @Id
    @Column( name = "id" )
    private Integer id;

    @Column( name = "factura" )
    private String factura;

    @Column( name = "origen" )
    private String origen;

    @Column( name = "id_cliente" )
    private Integer idCliente;

    @Column( name = "lente", length = 10 )
    private String lente;

    @Column( name = "armazon", length = 20 )
    private String armazon;

    @Column( name = "material" )
    private String material;

    @Column( name = "forma" )
    private String forma;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_entrega" )
    private Date fechaEntrega;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_promesa" )
    private Date fechaPromesa;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_factura" )
    private Date fechaFactura;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_cliente", updatable = false, insertable = false )
    private Cliente cliente;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "factura", updatable = false, insertable = false )
    private Trabajo trabajo;

    @PostLoad
    private void trim() {
        factura = StringUtils.trimToEmpty( factura );
        origen = StringUtils.trimToEmpty( origen );
        lente = StringUtils.trimToEmpty( lente );
        armazon = StringUtils.trimToEmpty( armazon );
        material = StringUtils.trimToEmpty( material );
        forma = StringUtils.trimToEmpty( forma );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura( String factura ) {
        this.factura = factura;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen( String origen ) {
        this.origen = origen;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente( Integer idCliente ) {
        this.idCliente = idCliente;
    }

    public String getLente() {
        return lente;
    }

    public void setLente( String lente ) {
        this.lente = lente;
    }

    public String getArmazon() {
        return armazon;
    }

    public void setArmazon( String armazon ) {
        this.armazon = armazon;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial( String material ) {
        this.material = material;
    }

    public String getForma() {
        return forma;
    }

    public void setForma( String forma ) {
        this.forma = forma;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega( Date fechaEntrega ) {
        this.fechaEntrega = fechaEntrega;
    }

    public Date getFechaPromesa() {
        return fechaPromesa;
    }

    public void setFechaPromesa( Date fechaPromesa ) {
        this.fechaPromesa = fechaPromesa;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura( Date fechaFactura ) {
        this.fechaFactura = fechaFactura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente( Cliente cliente ) {
        this.cliente = cliente;
    }

    public Trabajo getTrabajo() {
        return trabajo;
    }

    public void setTrabajo( Trabajo trabajo ) {
        this.trabajo = trabajo;
    }
}
