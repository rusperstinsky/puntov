package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@Table( name = "codigoxx", schema = "public" )
public class Localidad implements Serializable {

    private static final long serialVersionUID = 2073888498473605858L;

    @Id
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_estado", length = 2 )
    private String idEstado;

    @Column( name = "clase", length = 2 )
    private String clase;

    @Column( name = "ciudad", length = 2 )
    private String ciudad;

    @Column( name = "id_localidad", length = 3 )
    private String idLocalidad;

    @Column( name = "usuario", length = 60 )
    private String usuario;

    @Column( name = "reparto", length = 1 )
    private String reparto;

    @Column( name = "servicios", length = 1 )
    private String servicios;

    @Column( name = "oficina", length = 5 )
    private String oficina;

    @Column( name = "asentamien", length = 2 )
    private String asentamiento;

    @Column( name = "codigo", length = 5 )
    private String codigo;

    @Column( name = "actualiza" )
    private Integer actualiza;

    @Column( name = "cor", length = 5 )
    private String cor;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumns( {
            @JoinColumn( name = "id_estado", referencedColumnName = "id_estado", insertable = false, updatable = false ),
            @JoinColumn( name = "id_localidad", referencedColumnName = "id_localidad", insertable = false, updatable = false ) } )
    private Municipio municipio;

    @PostLoad
    private void trim() {
        idEstado = StringUtils.trimToNull( idEstado );
        clase = StringUtils.trimToNull( clase );
        ciudad = StringUtils.trimToNull( ciudad );
        idLocalidad = StringUtils.trimToNull( idLocalidad );
        usuario = StringUtils.trimToNull( usuario );
        oficina = StringUtils.trimToNull( oficina );
        asentamiento = StringUtils.trimToNull( asentamiento );
        codigo = StringUtils.trimToNull( codigo );
        cor = StringUtils.trimToNull( cor );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado( String idEstado ) {
        this.idEstado = idEstado;
    }

    public String getClase() {
        return clase;
    }

    public void setClase( String clase ) {
        this.clase = clase;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad( String ciudad ) {
        this.ciudad = ciudad;
    }

    public String getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad( String idLocalidad ) {
        this.idLocalidad = idLocalidad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario( String usuario ) {
        this.usuario = usuario;
    }

    public String getReparto() {
        return reparto;
    }

    public void setReparto( String reparto ) {
        this.reparto = reparto;
    }

    public String getServicios() {
        return servicios;
    }

    public void setServicios( String servicios ) {
        this.servicios = servicios;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina( String oficina ) {
        this.oficina = oficina;
    }

    public String getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento( String asentamiento ) {
        this.asentamiento = asentamiento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo( String codigo ) {
        this.codigo = codigo;
    }

    public Integer getActualiza() {
        return actualiza;
    }

    public void setActualiza( Integer actualiza ) {
        this.actualiza = actualiza;
    }

    public String getCor() {
        return cor;
    }

    public void setCor( String cor ) {
        this.cor = cor;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio( Municipio municipio ) {
        this.municipio = municipio;
    }
}
