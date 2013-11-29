package mx.lux.pos.ui.model.adapter

import org.apache.commons.lang3.StringUtils

import javax.swing.ComboBoxModel
import javax.swing.event.ListDataListener

class BrandListModelAdapter implements ComboBoxModel {

  private static final String ALL_BRANDS = "[Todas]"
  
  List<String> list
  List<String> itemList
  List<ListDataListener> listeners 
  String selectedItem 
  String selected
  
  BrandListModelAdapter() {
    this.setList( new ArrayList<String>( ) )
    this.setItemList( new ArrayList<String>( ) )
    this.setListeners( new ArrayList<ListDataListener>( ) )
    selected = null 
  }

  // Internal Methods
  private static String asItem( String pBrand ) {
    return String.format( "%s", StringUtils.trimToEmpty( pBrand ).toUpperCase( ) ).trim( )
  }
  
  protected String find( String pItem ) {
    String found = null
    for ( String b : this.list ) {
      if ( pItem.equals( asItem( b ) ) ) {
        found = b
        break
      }
    }
    return found
  }
  
  protected void setItemList( List<String> pList ) {
    this.itemList = pList
  }
  
  protected void setList( List<String> pList ) {
    this.list = pList
  }
  
  protected void setListeners( List<ListDataListener> pListeners ) {
    this.listeners = pListeners
  }

  protected void sync() {
    this.itemList.clear()
    this.itemList.add( ALL_BRANDS )
    for ( String b : this.list ) {
      this.itemList.add( asItem( b ) )
    }
  }
  
  // Public Methods
  void addListDataListener( ListDataListener pListener ) {
    if ( !this.listeners.contains( pListener ) ) {
      this.listeners.add( pListener )
    }
  }

  String getElementAt( int pIndex ) {
    String element = null
    if ( ( pIndex >= 0 ) && ( pIndex < this.getSize( ) ) ) {
      element = this.itemList[ pIndex ]
    }
    return element
  }

  String getSelectedBrand( ) {
    return this.selected
  }
  
  String getSelectedItem( ) {
    return this.selectedItem
  }

  int getSize( ) {
    return this.itemList.size( )
  }

  Boolean isAllSelected( ) {
    return ALL_BRANDS.equals( this.selectedItem )
  }
  
  void removeListDataListener( ListDataListener pListener ) {
    while ( this.listeners.contains( pListener ) ) {
      this.listeners.remove( pListener )
    }
  }

  void setSelectedItem( Object pAnItem ) {
    this.selectedItem = null
    this.selected = null
    if ( pAnItem instanceof String ) {
      this.selectedItem = ( pAnItem as String )
      this.selected = find( this.selectedItem )
    }
  }

  void setupBrandList( Collection<String> pBrandSet ) {
    this.list.clear()
    this.list.addAll( pBrandSet )
    this.sync( )
  }
  
}
