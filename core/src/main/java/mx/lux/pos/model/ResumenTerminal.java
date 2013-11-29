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
@Table( name = "resumen_terminales", schema = "public" )
public class ResumenTerminal implements Serializable {

    private static final long serialVersionUID = 3496709136624198421L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "resumen_terminales_id_seq" )
    @SequenceGenerator( name = "resumen_terminales_id_seq", sequenceName = "resumen_terminales_id_seq" )
    @Column( name = "id" )
    private Integer id;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_cierre" )
    private Date fechaCierre;

    @Column( name = "id_term" )
    private String idTerminal;

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "total" )
    private BigDecimal total;

    @OneToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_sucursal", insertable = false, updatable = false )
    private Sucursal sucursal;

    @OneToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_term", referencedColumnName = "descripcion", insertable = false, updatable = false )
    private Terminal terminal;

    @PostLoad
    private void onPostLoad() {
        idTerminal = StringUtils.trimToEmpty( idTerminal );
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

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal( String idTerminal ) {
        this.idTerminal = idTerminal;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer idSucursal ) {
        this.idSucursal = idSucursal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal( BigDecimal total ) {
        this.total = total;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal( Sucursal sucursal ) {
        this.sucursal = sucursal;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal( Terminal terminal ) {
        this.terminal = terminal;
    }
}
