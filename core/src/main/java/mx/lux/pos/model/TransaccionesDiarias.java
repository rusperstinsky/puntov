package mx.lux.pos.model;

import java.util.ArrayList;
import java.util.List;

public class TransaccionesDiarias {

    private String marca;
    private Integer cantidad;

    public TransaccionesDiarias(String marca){
        this.marca = marca;
        cantidad = 0;
    }

    public void AcumulaTransacciones( TransInvDetalle detalle ){
        cantidad = cantidad+detalle.getCantidad();
    }


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
