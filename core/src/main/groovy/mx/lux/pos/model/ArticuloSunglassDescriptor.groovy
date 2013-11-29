package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

public enum ArticuloSunglassDescriptor {

  sku("IDCODIGO"),
  partNbr("NUMPARTE"),
  description("DESCRIPCION"),
  family("FAMILIA"),
  group("GRUPO"),
  price("PRECIOVENTA1"),
  redPrice("<RedPrice>"),
  revDate("FECHAREVISION"),
  revUser("CLAVEUSUARIO"),
  colorCode("<ColorCode>"),
  colorDesc("COLORARMAZON"),
  idBarras("IDBARRAS"),
  type("TIPO"),
  subtype("<Subtype>"),
  brand("MARCAABREVIADA"),
  supplier("PROVEEDOR"),
  idLensDesign("<IdLensDesign>")

  private final String tag
  Integer index

  ArticuloSunglassDescriptor(String pTag) {
    this.tag = StringUtils.trimToNull(pTag).toUpperCase()
    this.index = null
  }

  boolean equals(String pTag) {
    boolean result = false;
    if (this.tag != null) {
      result = this.tag.equals(StringUtils.trimToEmpty(pTag).toUpperCase())
    }
    return result
  }

  static Integer getMaxIndex() {
    Integer max = 0
    for (ArticuloSunglassDescriptor descriptor : ArticuloSunglassDescriptor.values()) {
      if (descriptor.indexValid) {
        max = Math.max(max, descriptor.index)
      }
    }
    return max
  }

  boolean isIndexValid() {
    return (this.index != null)
  }

  static void reset() {
    for (ArticuloSunglassDescriptor descriptor : ArticuloSunglassDescriptor.values()) {
      descriptor.index = null
    }
  }

  String toString( ) {
    return String.format("%s(%s): %d", this.name(), this.tag, this.index)
  }
}