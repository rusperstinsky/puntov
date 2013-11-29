package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "cliente_pais", schema = "public" )
public class ClientePais implements Serializable {

    private static final long serialVersionUID = 4992942609966672499L;

    @Id
    @Column( name = "id_cliente" )
    private Integer id;

    @Column( name = "ciudad" )
    private String ciudad;

    @Column( name = "pais" )
    private String pais;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha" )
    private Date fecha;

    @PrePersist
    private void onPrePersist() {
        fecha = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fecha = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        ciudad = StringUtils.trimToEmpty( ciudad );
        pais = StringUtils.trimToEmpty( pais );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad( String ciudad ) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais( String pais ) {
        this.pais = pais;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }
}
