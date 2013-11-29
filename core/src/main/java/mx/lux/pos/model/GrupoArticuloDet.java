package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@Table( name = "grupo_articulo_det", schema = "public" )
public class GrupoArticuloDet implements Serializable, Comparable<GrupoArticuloDet> {

    private static final long serialVersionUID = 3319043056401548535L;

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "grupo_articulo_det_id_seq" )
    @SequenceGenerator( schema = "public", sequenceName = "grupo_articulo_det_id_seq",
            name = "grupo_articulo_det_id_seq", allocationSize = 1 )
    @Column( name = "id" )
    protected Integer id;

    @Column( name = "id_grupo" )
    private Integer idGrupo;

    @Column( name = "articulo" )
    private String articulo;

    // Internal Methods
    @PostLoad
    protected void onPostLoad() {
        articulo = StringUtils.trimToEmpty( articulo ).toUpperCase();
    }

    // Public Methods

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo( Integer pIdGrupo ) {
        this.idGrupo = pIdGrupo;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo( String pArticulo ) {
        this.articulo = pArticulo;
    }

    // Entity
    public int compareTo( GrupoArticuloDet pGrupoArtDet ) {
        int result = this.getIdGrupo().compareTo( pGrupoArtDet.getIdGrupo() );
        if ( result == 0 ) this.getArticulo().compareTo( pGrupoArtDet.getArticulo() );
        return result;
    }

    public boolean equals( Object pObj ) {
        boolean result = false;
        if ( pObj instanceof GrupoArticuloDet ) {
            result = this.getIdGrupo().equals( ( ( GrupoArticuloDet ) pObj ).getIdGrupo() );
        } else if ( pObj instanceof GrupoArticulo ) {
            result = this.getIdGrupo().equals( ( ( GrupoArticulo ) pObj ).getIdGrupo() );
        } else if ( pObj instanceof Integer ) {
            result = this.getIdGrupo().equals( ( Integer ) pObj );
        }
        return result;
    }

    public int hashCode() {
        return this.getArticulo().hashCode();
    }

    public String toString() {
        String str = String.format( "Grupo:%d -> Articulo:%s", this.getIdGrupo(), this.getArticulo() );
        return str;
    }
}
