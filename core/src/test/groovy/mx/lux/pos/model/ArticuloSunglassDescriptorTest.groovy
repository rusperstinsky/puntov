package mx.lux.pos.model

import spock.lang.Specification

class ArticuloSunglassDescriptorTest extends Specification {

  def "Test Equals to String"() {
    expect:
      assert descriptor.equals(tag) == expected

    where:
      descriptor | tag | expected
      ArticuloSunglassDescriptor.sku     | "sku"          | false
      ArticuloSunglassDescriptor.sku     | "idCodigo"     | true
      ArticuloSunglassDescriptor.partNbr | "NUM_PARTE"    | false
      ArticuloSunglassDescriptor.partNbr | "NUMPARTE"     | true
      ArticuloSunglassDescriptor.price   | "Precio"       | false
      ArticuloSunglassDescriptor.price   | "PrecioVenta1" | true
      ArticuloSunglassDescriptor.brand   | "Brand"        | false
      ArticuloSunglassDescriptor.brand   | "Tipo"         | false
      ArticuloSunglassDescriptor.brand   | "Marca"        | true
      ArticuloSunglassDescriptor.family  | "Familia"      | true
      ArticuloSunglassDescriptor.family  | "Generico"     | false

  }

  def "Test index assign"() {
    setup:
      List<String> list = ["IDCODIGO", "descripcion", "field_1", "FAMILIA", "MARCA" ]
      ArticuloSunglassDescriptor.reset()

    when:
      Integer ix = 0
      for (String tag in list) {
        for (ArticuloSunglassDescriptor descriptor in ArticuloSunglassDescriptor.values()) {
          if (descriptor.equals(tag)) {
            descriptor.index = ix
            break
          }
        }
        ix++
      }

      for (ArticuloSunglassDescriptor descriptor in ArticuloSunglassDescriptor.values()) {
        println descriptor
      }

    then:
      ArticuloSunglassDescriptor.family.index == 3
      ArticuloSunglassDescriptor.brand.index == 4
      ArticuloSunglassDescriptor.sku.index == 0
      ArticuloSunglassDescriptor.description.index == 1
      ArticuloSunglassDescriptor.partNbr.indexValid == false
      ArticuloSunglassDescriptor.maxIndex == 4

  }

}
