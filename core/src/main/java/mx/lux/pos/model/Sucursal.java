package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "sucursales", schema = "public" )
public class Sucursal implements Serializable {

    private static final long serialVersionUID = 4025356042284041635L;

    @Id
    @Column( name = "id_sucursal" )
    private Integer id;

    @Column( name = "nombre" )
    private String nombre;

    @Column( name = "direccion" )
    private String direccion;

    @Column( name = "colonia" )
    private String colonia;

    @Column( name = "localidad", length = 3 )
    private String idLocalidad;

    @Column( name = "id_estado", length = 2 )
    private String idEstado;

    @Column( name = "cp", length = 5 )
    private String cp;

    @Column( name = "telefonos" )
    private String telefonos;

    @Column( name = "id_gerente", length = 13 )
    private String idGerente;

    @Column( name = "letra_ascii" )
    private Integer letraAscii;

    @Column( name = "num_factura" )
    private Integer numFactura;

    @Column( name = "sears" )
    private boolean sears;

    @Column( name = "por100_anticipo" )
    private Integer por100Anticipo;

    @Column( name = "impresion_fact" )
    private boolean impresionFact;

    @Column( name = "serie_rep_ventas" )
    private Integer serieRepVentas;

    @Column( name = "num_rep_ventas" )
    private Integer numRepVentas;

    @Column( name = "serie_num_orden" )
    private boolean serieNumOrden;

    @Column( name = "id_sync", length = 1 )
    private String idSync = "1";

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod" )
    private Date fechaModificado = new Date();

    @Column( name = "id_mod", length = 13 )
    private String idModificado = "0";

    @Column( name = "centro_costos", length = 6 )
    private String centroCostos;

    @Column( name = "marca" )
    private String marca;

    @Column( name = "ciudad" )
    private String ciudad;

    @Column( name = "domingo" )
    private boolean domingo;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_estado", insertable = false, updatable = false )
    private Estado estado;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumns( {
            @JoinColumn( name = "id_estado", referencedColumnName = "id_estado", insertable = false, updatable = false ),
            @JoinColumn( name = "localidad", referencedColumnName = "id_localidad", insertable = false, updatable = false ) } )
    private Municipio municipio;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_gerente", insertable = false, updatable = false )
    private Empleado gerente;

    @PostLoad
    private void trim() {
        nombre = StringUtils.trimToEmpty( nombre );
        direccion = StringUtils.trimToEmpty( direccion );
        colonia = StringUtils.trimToEmpty( colonia );
        idLocalidad = StringUtils.trimToEmpty( idLocalidad );
        idEstado = StringUtils.trimToEmpty( idEstado );
        cp = StringUtils.trimToEmpty( cp );
        telefonos = StringUtils.trimToEmpty( telefonos );
        idGerente = StringUtils.trimToEmpty( idGerente );
        idSync = StringUtils.trimToEmpty( idSync );
        idModificado = StringUtils.trimToEmpty( idModificado );
        centroCostos = StringUtils.trimToEmpty( centroCostos );
        marca = StringUtils.trimToEmpty( marca );
        ciudad = StringUtils.trimToEmpty( ciudad );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre ) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion( String direccion ) {
        this.direccion = direccion;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia( String colonia ) {
        this.colonia = colonia;
    }

    public String getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad( String idLocalidad ) {
        this.idLocalidad = idLocalidad;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado( String idEstado ) {
        this.idEstado = idEstado;
    }

    public String getCp() {
        return cp;
    }

    public void setCp( String cp ) {
        this.cp = cp;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos( String telefonos ) {
        this.telefonos = telefonos;
    }

    public String getIdGerente() {
        return idGerente;
    }

    public void setIdGerente( String idGerente ) {
        this.idGerente = idGerente;
    }

    public Integer getLetraAscii() {
        return letraAscii;
    }

    public void setLetraAscii( Integer letraAscii ) {
        this.letraAscii = letraAscii;
    }

    public Integer getNumFactura() {
        return numFactura;
    }

    public void setNumFactura( Integer numFactura ) {
        this.numFactura = numFactura;
    }

    public boolean isSears() {
        return sears;
    }

    public void setSears( boolean sears ) {
        this.sears = sears;
    }

    public Integer getPor100Anticipo() {
        return por100Anticipo;
    }

    public void setPor100Anticipo( Integer por100Anticipo ) {
        this.por100Anticipo = por100Anticipo;
    }

    public boolean isImpresionFact() {
        return impresionFact;
    }

    public void setImpresionFact( boolean impresionFact ) {
        this.impresionFact = impresionFact;
    }

    public Integer getSerieRepVentas() {
        return serieRepVentas;
    }

    public void setSerieRepVentas( Integer serieRepVentas ) {
        this.serieRepVentas = serieRepVentas;
    }

    public Integer getNumRepVentas() {
        return numRepVentas;
    }

    public void setNumRepVentas( Integer numRepVentas ) {
        this.numRepVentas = numRepVentas;
    }

    public boolean isSerieNumOrden() {
        return serieNumOrden;
    }

    public void setSerieNumOrden( boolean serieNumOrden ) {
        this.serieNumOrden = serieNumOrden;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync( String idSync ) {
        this.idSync = idSync;
    }

    public Date getFechaModificado() {
        return fechaModificado;
    }

    public void setFechaModificado( Date fechaModificado ) {
        this.fechaModificado = fechaModificado;
    }

    public String getIdModificado() {
        return idModificado;
    }

    public void setIdModificado( String idModificado ) {
        this.idModificado = idModificado;
    }

    public String getCentroCostos() {
        return centroCostos;
    }

    public void setCentroCostos( String centroCostos ) {
        this.centroCostos = centroCostos;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca( String marca ) {
        this.marca = marca;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad( String ciudad ) {
        this.ciudad = ciudad;
    }

    public boolean isDomingo() {
        return domingo;
    }

    public void setDomingo( boolean domingo ) {
        this.domingo = domingo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado( Estado estado ) {
        this.estado = estado;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio( Municipio municipio ) {
        this.municipio = municipio;
    }

    public Empleado getGerente() {
        return gerente;
    }

    public void setGerente( Empleado gerente ) {
        this.gerente = gerente;
    }
}
