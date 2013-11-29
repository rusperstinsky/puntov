package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table( name = "depositos", schema = "public" )
public class Deposito implements Serializable {

    private static final long serialVersionUID = -7448395413475939594L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "depositos_id_seq" )
    @SequenceGenerator( name = "depositos_id_seq", sequenceName = "depositos_id_seq" )
    @Column( name = "id" )
    private Integer id;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_cierre", nullable = false )
    private Date fechaCierre;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_deposito", nullable = false )
    private Date fechaDeposito;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_ingreso", nullable = false )
    private Date fechaIngreso;

    @Column( name = "num_deposito", nullable = false )
    private Integer numeroDeposito;

    @Column( name = "tipo_deposito", length = 20 )
    private String tipoDeposito;

    @Column( name = "id_banco", length = 2, nullable = false )
    private String idBanco;

    @Column( name = "referencia", length = 30 )
    private String referencia;

    @Column( name = "emp", length = 5 )
    private String idEmpleado;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "monto" )
    private BigDecimal monto;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaModificacion = new Date();

    @OneToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "emp", insertable = false, updatable = false )
    private Empleado empleado;

    @PrePersist
    private void onPrePersist() {
        fechaModificacion = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaModificacion = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        tipoDeposito = StringUtils.trimToEmpty( tipoDeposito );
        idBanco = StringUtils.trimToEmpty( idBanco );
        referencia = StringUtils.trimToEmpty( referencia );
        idEmpleado = StringUtils.trimToEmpty( idEmpleado );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre( Date fechaCierre ) {
        this.fechaCierre = fechaCierre;
    }

    public Date getFechaDeposito() {
        return fechaDeposito;
    }

    public void setFechaDeposito( Date fechaDeposito ) {
        this.fechaDeposito = fechaDeposito;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso( Date fechaIngreso ) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getNumeroDeposito() {
        return numeroDeposito;
    }

    public void setNumeroDeposito( Integer numeroDeposito ) {
        this.numeroDeposito = numeroDeposito;
    }

    public String getTipoDeposito() {
        return tipoDeposito;
    }

    public void setTipoDeposito( String tipoDeposito ) {
        this.tipoDeposito = tipoDeposito;
    }

    public String getIdBanco() {
        return idBanco;
    }

    public void setIdBanco( String idBanco ) {
        this.idBanco = idBanco;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia( String referencia ) {
        this.referencia = referencia;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado( String idEmpleado ) {
        this.idEmpleado = idEmpleado;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto( BigDecimal monto ) {
        this.monto = monto;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion( Date fechaModificacion ) {
        this.fechaModificacion = fechaModificacion;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado( Empleado empleado ) {
        this.empleado = empleado;
    }
}
