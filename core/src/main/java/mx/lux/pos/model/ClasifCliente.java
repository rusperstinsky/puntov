package mx.lux.pos.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "clasif_cli", schema = "public" )
public class ClasifCliente implements Serializable {

    private static final long serialVersionUID = -8823679094129170160L;

    @Id
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_factura" )
    private String idFactura;

    @Column( name = "id_cliente" )
    private Integer idCliente;

    @Column( name = "id_clasif_cli" )
    private Integer idClasifCliente;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod" )
    private Date fechaModificacion;

    @Column( name = "id_sucursal" )
    private Integer idSucursal;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura( String idFactura ) {
        this.idFactura = idFactura;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente( Integer idCliente ) {
        this.idCliente = idCliente;
    }

    public Integer getIdClasifCliente() {
        return idClasifCliente;
    }

    public void setIdClasifCliente( Integer idClasifCliente ) {
        this.idClasifCliente = idClasifCliente;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion( Date fechaModificacion ) {
        this.fechaModificacion = fechaModificacion;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer idSucursal ) {
        this.idSucursal = idSucursal;
    }

}
