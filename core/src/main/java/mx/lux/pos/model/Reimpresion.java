package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "reimpresion", schema = "public" )
public class Reimpresion implements Serializable {

    private static final long serialVersionUID = -3539764397838488807L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "reimpresion_id_seq" )
    @SequenceGenerator( name = "reimpresion_id_seq", sequenceName = "reimpresion_id_seq" )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "nota", length = 2, nullable = false )
    private String nota;

    @Column( name = "id_nota" )
    private String idNota;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha", nullable = false )
    private Date fecha;

    @Column( name = "empleado", length = 13 )
    private String idEmpleado;

    @Column( name = "factura" )
    private String factura;

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
        nota = StringUtils.trimToEmpty( nota );
        idNota = StringUtils.trimToEmpty( idNota );
        idEmpleado = StringUtils.trimToEmpty( idEmpleado );
        factura = StringUtils.trimToEmpty( factura );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getNota() {
        return nota;
    }

    public void setNota( String nota ) {
        this.nota = nota;
    }

    public String getIdNota() {
        return idNota;
    }

    public void setIdNota( String idNota ) {
        this.idNota = idNota;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado( String idEmpleado ) {
        this.idEmpleado = idEmpleado;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura( String factura ) {
        this.factura = factura;
    }
}
