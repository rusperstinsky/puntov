package mx.lux.pos.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

@Entity
@Cacheable
@Table( name = "tipo_trans_inv", schema = "public" )
public class TipoTransInv implements Serializable {

    private static final long serialVersionUID = 1215505798639676725L;

    @Id
    @Column( name = "id_tipo_trans" )
    private String idTipoTrans;

    @Column( name = "descripcion" )
    private String descripcion;

    @Column( name = "tipo_mov" )
    private String tipoMov;

    @Transient
    private TipoMov tipoMovObj;

    @Column( name = "ultimo_folio" )
    private Integer ultimoFolio;

    @PostLoad
    protected void trim() {
        this.idTipoTrans = StringUtils.trimToEmpty( this.idTipoTrans );
        this.descripcion = StringUtils.trimToEmpty( this.descripcion );
        this.tipoMov = StringUtils.trimToEmpty( this.tipoMov );
        this.tipoMovObj = TipoMov.parse( this.tipoMov );
    }

    public String getIdTipoTrans() {
        return this.idTipoTrans;
    }

    public void setIdTipoTrans( String pIdTipoTrans ) {
        this.idTipoTrans = StringUtils.trimToEmpty( pIdTipoTrans ).toUpperCase();
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion( String pDescripcion ) {
        this.descripcion = pDescripcion.trim();
    }

    public String getTipoMov() {
        return this.tipoMov;
    }

    public void setTipoMov( String pTipoMov ) {
        this.tipoMov = StringUtils.trimToEmpty( pTipoMov ).toUpperCase();
    }

    public TipoMov getTipoMovObj() {
        return tipoMovObj;
    }

    public Integer getUltimoFolio() {
        return this.ultimoFolio;
    }

    public void setUltimoFolio( Integer pUltimoFolio ) {
        this.ultimoFolio = pUltimoFolio;
    }

    public String toString() {
        return String.format( "[%s] %s  UltFolio:%,d", this.getIdTipoTrans(), this.getDescripcion(), this.getUltimoFolio() );
    }
}
