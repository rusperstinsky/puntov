package mx.lux.pos.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( name = "trans_inv_det", schema = "public" )
public class TransInvDetalle implements Serializable {

    private static final long serialVersionUID = -2805877783021045640L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "trans_inv_det_num_reg_seq" )
    @Column( name = "num_reg" )
    @SequenceGenerator( schema = "public", sequenceName = "trans_inv_det_num_reg_seq",
            name = "trans_inv_det_num_reg_seq" )
    private Integer numReg;

    @Column( name = "id_tipo_trans" )
    private String idTipoTrans;

    @Column( name = "folio" )
    private Integer folio;

    @Column( name = "linea" )
    private Integer linea;

    @Column( name = "sku" )
    private Integer sku;

    @Column( name = "tipo_mov" )
    private String tipoMov;

    @Column( name = "cantidad" )
    private Integer cantidad;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumns( value = {@JoinColumn(name = "id_tipo_trans", referencedColumnName = "id_tipo_trans", insertable = false, updatable = false),
            @JoinColumn(name = "folio", referencedColumnName = "folio", insertable = false, updatable = false)} )
    private TransInv transInv ;


    public Integer getNumReg() {
        return numReg;
    }

    public void setNumReg( Integer numReg ) {
        this.numReg = numReg;
    }

    public String getIdTipoTrans() {
        return idTipoTrans;
    }

    public void setIdTipoTrans( String idTipoTrans ) {
        this.idTipoTrans = idTipoTrans;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio( Integer folio ) {
        this.folio = folio;
    }

    public Integer getLinea() {
        return linea;
    }

    public void setLinea( Integer linea ) {
        this.linea = linea;
    }

    public Integer getSku() {
        return sku;
    }

    public void setSku( Integer sku ) {
        this.sku = sku;
    }

    public String getTipoMov() {
        return tipoMov;
    }

    public void setTipoMov( String tipoMov ) {
        this.tipoMov = tipoMov;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad( Integer cantidad ) {
        this.cantidad = cantidad;
    }

    public TransInv getTransInv() {
        return transInv;
    }

    public void setTransInv( TransInv transInv ) {
        this.transInv = transInv;
    }

    public boolean equals( Object pObj ) {
        boolean result = false;
        if ( pObj instanceof TransInvDetalle ) {
            result = this.getIdTipoTrans().equals( ( ( TransInvDetalle ) pObj ).getIdTipoTrans() )
                    && this.getFolio().equals( ( ( TransInvDetalle ) pObj ).getFolio() )
                    && this.getLinea().equals( ( ( TransInvDetalle ) pObj ).getLinea() );
        } else if ( pObj instanceof TransInv ) {
            result = this.getIdTipoTrans().equals( ( ( TransInv ) pObj ).getIdTipoTrans() )
                    && this.getFolio().equals( ( ( TransInv ) pObj ).getFolio() );
        }
        return result;
    }

    public int hashCode() {
        return String.format( "%s:%08d", this.getIdTipoTrans(), this.getFolio() ).hashCode();
    }

    public String toString() {
        return String.format( "[%s:%06d:%02d] %d  Cant:%d", this.getIdTipoTrans(), this.getFolio(), this.getLinea(),
                this.getSku(), this.getCantidad() );
    }
}

