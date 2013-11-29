package mx.lux.pos.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@IdClass( RelacionPlanId.class )
@Table( name = "relacion_plan", schema = "public" )
public class RelacionPlan implements Serializable {

    private static final long serialVersionUID = 2052002692361598125L;

    @Id
    @Column( name = "id_plan" )
    private String idPlan;

    @Id
    @Column( name = "id_banco_dep" )
    private String idBanco;

    @Column( name = "activo" )
    private String activo;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_banco_dep", insertable = false, updatable = false )
    private BancoDeposito bancoDeposito;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_plan", insertable = false, updatable = false )
    private Plan plan;

    public String getIdPlan() {
        return idPlan;
    }

    public void setIdPlan( String idPlan ) {
        this.idPlan = idPlan;
    }

    public String getIdBanco() {
        return idBanco;
    }

    public void setIdBanco( String idBanco ) {
        this.idBanco = idBanco;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo( String activo ) {
        this.activo = activo;
    }

    public BancoDeposito getBancoDeposito() {
        return bancoDeposito;
    }

    public void setBancoDeposito( BancoDeposito bancoDeposito ) {
        this.bancoDeposito = bancoDeposito;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan( Plan plan ) {
        this.plan = plan;
    }
}

class RelacionPlanId implements Serializable {

    private static final long serialVersionUID = 2835717944222378549L;

    private String idPlan;
    private String idBanco;
}
