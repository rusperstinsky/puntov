package mx.lux.pos.ui.resources

import org.springframework.core.io.ClassPathResource

class ImageLibrary {

  def static final String IMG_FIRST_MID = "img/first_24.png"
  def static final String IMG_LAST_MID = "img/last_24.png"
  def static final String IMG_NEXT_MID = "img/next_24.png"
  def static final String IMG_PREV_MID = "img/prev_24.png"
  def static final String IMG_SEARCH_BIG = "img/search_32.png"
  def static final String IMG_DOWN_BIG = "img/down_24.png"
  def static final String IMG_UP_BIG = "img/up_24.png"
  def static final String IMG_CIRCLE_MID = "img/circulo.png"
  
  static URL getURL( pResourcePath ) {
//    URL url = new File(pResourcePath).toURI().toURL()
    URL url = new ClassPathResource( pResourcePath )?.URL
  }

}
