package mx.lux.pos.model;

import mx.lux.pos.model.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table( name = "retorno", schema = "public" )
public class Retorno implements Serializable {


    private static final long serialVersionUID = 9190794480951873710L;

    @Id
    @Column( name = "id_transaccion" )
    private Integer id;

    @Column( name = "ticket_origen" )
    private String ticketOrigen;

    @Column( name = "ticket_destino" )
    private String ticketDestino;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "monto" )
    private BigDecimal monto;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTicketOrigen() {
        return ticketOrigen;
    }

    public void setTicketOrigen(String ticketOrigen) {
        this.ticketOrigen = ticketOrigen;
    }

    public String getTicketDestino() {
        return ticketDestino;
    }

    public void setTicketDestino(String ticketDestino) {
        this.ticketDestino = ticketDestino;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
