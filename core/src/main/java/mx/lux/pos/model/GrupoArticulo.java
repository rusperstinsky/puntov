package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Cacheable
@Table( name = "grupo_articulo", schema = "public" )
public class GrupoArticulo implements Serializable, Comparable<GrupoArticulo> {

    private static final long serialVersionUID = -9032895941251294109L;

    @Id
    @Column( name = "id_grupo" )
    private Integer idGrupo;

    @Column( name = "descripcion" )
    private String descripcion;

    @Transient
    private Set<GrupoArticuloDet> articuloSet = new HashSet<GrupoArticuloDet>();

    // Internal Methods
    @PostLoad
    protected void onPostLoad() {
        descripcion = StringUtils.trimToEmpty( descripcion );
    }

    // Public Properties
    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo( Integer pIdGrupo ) {
        this.idGrupo = pIdGrupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String pDescripcion ) {
        this.descripcion = StringUtils.trimToEmpty( pDescripcion );
    }

    public Set<GrupoArticuloDet> getArticuloSet() {
        return articuloSet;
    }

    public void setArticuloSet( Collection<GrupoArticuloDet> pArticuloSet ) {
        this.articuloSet.addAll( pArticuloSet );
    }

    // Other Methods
    public void add( GrupoArticuloDet pArticuloDet ) {
        this.getArticuloSet().add( pArticuloDet );
    }

    public void add( List<GrupoArticuloDet> pListaArticuloDet ) {
        for ( GrupoArticuloDet det : pListaArticuloDet ) {
            this.add( det );
        }
    }

    // Entity
    public int compareTo( GrupoArticulo pGrupoArticulo ) {
        return this.getDescripcion().compareToIgnoreCase( pGrupoArticulo.getDescripcion() );
    }

    public boolean equals( Object pObj ) {
        boolean result = false;
        if ( pObj instanceof GrupoArticulo ) {
            result = this.getIdGrupo().equals( ( ( GrupoArticulo ) pObj ).getIdGrupo() );
//    } else if ( pObj instanceof GrupoArticuloDet ) {
//      result = this.getIdGrupo( ).equals( ( (GrupoArticuloDet) pObj ).getIdGrupo( ) ); 
        } else if ( pObj instanceof Integer ) {
            result = this.getIdGrupo().equals( ( Integer ) pObj );
        }
        return result;
    }

    public int hashCode() {
        return this.getIdGrupo().hashCode();
    }

    public String toString() {
        String str = String.format( "[%d] %s", this.getIdGrupo(), this.getDescripcion() );
        if ( this.getArticuloSet().size() > 0 ) {
            if ( this.getArticuloSet().size() < 10 ) {
                String strGrupo = "";
                for ( GrupoArticuloDet det : this.getArticuloSet() ) {
                    strGrupo += ( strGrupo.length() > 0 ? ", " : "" ) + det.getArticulo();
                }
                str = String.format( "%s {%s}", str, strGrupo );
            } else {
                str = String.format( "%s - %d articulos", this.getArticuloSet().size() );
            }
        }
        return str;
    }

}
