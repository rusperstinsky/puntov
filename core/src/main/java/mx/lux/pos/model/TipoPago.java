package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@Table( name = "tipo_pago", schema = "public" )
public class TipoPago implements Serializable {

    private static final long serialVersionUID = 3210492465218086802L;

    @Id
    @Column( name = "id_pago", length = 5 )
    private String id;

    @Column( name = "descripcion" )
    private String descripcion;

    @Column( name = "tipo_soi", length = 2 )
    private String tipoSoi;

    @Column( name = "tipo_con", length = 5 )
    private String tipoCon;

    @Column( name = "f1", length = 20 )
    private String f1;

    @Column( name = "f2", length = 20 )
    private String f2;

    @Column( name = "f3", length = 20 )
    private String f3;

    @Column( name = "f4", length = 20 )
    private String f4;

    @Column( name = "f5", length = 20 )
    private String f5;

    @PostLoad
    private void trim() {
        descripcion = StringUtils.trimToEmpty( descripcion );
        tipoSoi = StringUtils.trimToEmpty( tipoSoi );
        tipoCon = StringUtils.trimToEmpty( tipoCon );
        f1 = StringUtils.trimToEmpty( f1 );
        f2 = StringUtils.trimToEmpty( f2 );
        f3 = StringUtils.trimToEmpty( f3 );
        f4 = StringUtils.trimToEmpty( f4 );
        f5 = StringUtils.trimToEmpty( f5 );
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = StringUtils.trimToEmpty( id ).toUpperCase();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String descripcion ) {
        this.descripcion = descripcion;
    }

    public String getTipoSoi() {
        return tipoSoi;
    }

    public void setTipoSoi( String tipoSoi ) {
        this.tipoSoi = tipoSoi;
    }

    public String getTipoCon() {
        return tipoCon;
    }

    public void setTipoCon( String tipoCon ) {
        this.tipoCon = tipoCon;
    }

    public String getF1() {
        return f1;
    }

    public void setF1( String f1 ) {
        this.f1 = f1;
    }

    public String getF2() {
        return f2;
    }

    public void setF2( String f2 ) {
        this.f2 = f2;
    }

    public String getF3() {
        return f3;
    }

    public void setF3( String f3 ) {
        this.f3 = f3;
    }

    public String getF4() {
        return f4;
    }

    public void setF4( String f4 ) {
        this.f4 = f4;
    }

    public String getF5() {
        return f5;
    }

    public void setF5( String f5 ) {
        this.f5 = f5;
    }


    public boolean equals( Object obj ) {
        boolean result = false;
        if ( obj instanceof TipoPago ) {
            result = this.getId().equalsIgnoreCase( ( ( TipoPago ) obj ).getId() );
        } else if( obj instanceof String ) {
            result = this.getId().trim().equalsIgnoreCase( ( ( String ) obj ).trim() );
        }

        return result;
    }
}
