package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
import mx.lux.pos.model.InstitucionIc

@Bindable
@ToString
@EqualsAndHashCode
class AgreementItem {

    Integer id
    String descripcion
    String tipoConvenio
    boolean mejorPrecio
    String estatusConvenio

    static AgreementItem toAgreementItem( InstitucionIc cotizacion ) {
        if ( cotizacion?.id ) {
            AgreementItem conv = new AgreementItem(
                    id: cotizacion.id,
                    descripcion: cotizacion.inicialesIc,
                    tipoConvenio: cotizacion.tipoConvenio,
                    mejorPrecio: cotizacion.mejorPrecio,
                    estatusConvenio: cotizacion.estatusConvenio
            )
            return conv
        }
        return null
    }
}
