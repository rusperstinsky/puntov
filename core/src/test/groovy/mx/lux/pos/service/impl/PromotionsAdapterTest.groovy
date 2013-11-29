package mx.lux.pos.service.impl

import spock.lang.Specification
import mx.lux.pos.service.io.PromotionsAdapter
import mx.lux.pos.service.business.PromotionImportTask

class PromotionsAdapterTest extends Specification {


    def 'Prueba Resolver Archivo'( ){

        setup:
        String ruta = ('/home/paso/por_recibir')

        when:
        PromotionImportTask task = new PromotionImportTask()
        List<PromotionsAdapter> archivo = task.run( ruta )
        println archivo.first().idPromocion

        then:
        archivo.first().idPromocion != null

    }


    def 'Prueba Adapter'( ){

        setup:
        String record = ('|127|COMBO|SOMOS LECTORES|24/11/12|31/12/12|1|1181,1182,1183||1|0|0|1|A|*|*|*|'
                        +'BV,CL,DY,DD,DG,LU,OK,PO,PD,RB,RV,VR,VG|0|0|B|*|*|*|500|0||DESCONTADO|POLY-X|')

        when:
        PromotionsAdapter adapter = new PromotionsAdapter( record )
        println adapter.toString()

        then:
        adapter.idPromocion == 127
        adapter.idGenerico == "A"
        adapter.articulo == "*"
        adapter.marca.length() > 4

    }
}
