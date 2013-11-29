package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "acuses", schema = "public" )
public class Acuse implements Serializable {

    private static final long serialVersionUID = -2205269031427697594L;

    private static final String FMT_TO_STRING = "[%d] %s Intentos:%,d  (%s)";

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "acuses_id_acuse_seq" )
    @SequenceGenerator( name = "acuses_id_acuse_seq", sequenceName = "acuses_id_acuse_seq" )
    @Column( name = "id_acuse" )
    private Integer id;

    @Column( name = "contenido" )
    private String contenido;

    @Column( name = "fecha_carga" )
    private Date fechaCarga;

    @Column( name = "fecha_acuso" )
    private Date fechaAcuso;

    @Column( name = "id_tipo" )
    private String idTipo;

    @Column( name = "folio", length = 20 )
    private String folio;

    @Column( name = "intentos" )
    private Integer intentos = 0;

    @PrePersist
    private void onPrePersist() {
        fechaCarga = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaCarga = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        contenido = StringUtils.trimToEmpty( contenido );
        idTipo = StringUtils.trimToEmpty( idTipo );
        folio = StringUtils.trimToEmpty( folio );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido( String contenido ) {
        this.contenido = contenido;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga( Date fechaCarga ) {
        this.fechaCarga = fechaCarga;
    }

    public Date getFechaAcuso() {
        return fechaAcuso;
    }

    public void setFechaAcuso( Date fechaAcuso ) {
        this.fechaAcuso = fechaAcuso;
    }

    public String getIdTipo() {
        return idTipo;
    }

    public void setIdTipo( String idTipo ) {
        this.idTipo = idTipo;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio( String folio ) {
        this.folio = folio;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos( Integer intentos ) {
        this.intentos = intentos;
    }

    public String toString( ) {
        String contents = StringUtils.trimToEmpty(this.contenido);
        if (contents.length() > 20) {
            contents = contents.substring(0, 20);
        }
        return String.format( FMT_TO_STRING, this.getId(), this.getIdTipo(), this.getIntentos(),
                contents);
    }
}
