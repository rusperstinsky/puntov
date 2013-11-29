package mx.lux.pos.ui.model.adapter

import mx.lux.pos.model.Generico

import javax.swing.ComboBoxModel
import javax.swing.event.ListDataListener

class GenreListModelAdapter implements ComboBoxModel {

  private static final String ALL_GENRES = "[Todos]"
  
  List<Generico> list
  List<String> itemList
  List<ListDataListener> listeners 
  String selectedItem 
  Generico selected
  
  GenreListModelAdapter() {
    this.setList( new ArrayList<Generico>( ) )
    this.setItemList( new ArrayList<String>( ) )
    this.setListeners( new ArrayList<ListDataListener>( ) )
    selected = null 
  }

  // Internal Methods
  private static String asItem( Generico pGenre ) {
    return String.format( "(%s) %s", pGenre.id, pGenre.descripcion ).trim( )
  }
  
  protected Generico find( String pItem ) {
    Generico found = null
    for ( Generico g : this.list ) {
      if ( pItem.equals( asItem( g ) ) ) {
        found = g
        break
      }
    }
    return found
  }
  
  protected void setItemList( List<String> pList ) {
    this.itemList = pList
  }
  
  protected void setList( List<Generico> pList ) {
    this.list = pList
  }
  
  protected void setListeners( List<ListDataListener> pListeners ) {
    this.listeners = pListeners
  }

  protected void sync() {
    this.itemList.clear() 
    this.itemList.add( ALL_GENRES )
    for ( Generico g : this.list ) {
      this.itemList.add( asItem( g ) )
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

  String getSelectedItem( ) {
    return this.selectedItem
  }

  Generico getSelectedGenre( ) {
    return this.selected
  }
  
  int getSize( ) {
    return this.itemList.size( )
  }

  Boolean isAllSelected( ) {
    return ALL_GENRES.equals( this.selectedItem )  
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

  void setupGenreList( Collection<Generico> pGenreSet ) {
    this.list.clear()
    this.list.addAll( pGenreSet )
    this.sync( )
  }
  
}
