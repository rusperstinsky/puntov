package mx.lux.pos.ui.model.adapter

import mx.lux.pos.model.InstitucionIc
import mx.lux.pos.model.Sucursal


class AgreementAdapter extends StringAdapter<InstitucionIc> {

    public String getText( InstitucionIc convenio ) {
        return String.format( "[%s] %s", convenio.id, convenio.inicialesIc );
    }
    
}
