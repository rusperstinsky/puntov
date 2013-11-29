package mx.lux.pos.model;

import java.util.ArrayList;
import java.util.List;

public class CierreTerminales {

    private String idTerminal;
    private List<ResumenDiario> detTerminales;

    public CierreTerminales( String terminal ){
        idTerminal = terminal;
        detTerminales = new ArrayList<ResumenDiario>();
    }

    public void AcumulaTerminales( ResumenDiario resumen ){
        detTerminales.add( resumen );
    }


    public List<ResumenDiario> getDetTerminales() {
        return detTerminales;
    }

    public void setDetTerminales(List<ResumenDiario> detTerminales) {
        this.detTerminales = detTerminales;
    }

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(String idTerminal) {
        this.idTerminal = idTerminal;
    }
}
