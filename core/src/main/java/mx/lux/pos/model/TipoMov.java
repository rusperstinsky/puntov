package mx.lux.pos.model;

public enum TipoMov {
    ISSUE( "S", -1 ),
    RECEIPT( "E", 1 );

    private String codigo;
    private Integer factorES;

    TipoMov( String pCodigo, Integer pFactor ) {
        codigo = pCodigo.trim().toUpperCase();
        factorES = pFactor;
    }

    public String getCodigo() {
        return codigo;
    }

    public Integer getFactor() {
        return factorES;
    }

    public static TipoMov parse( String pString ) {
        TipoMov found = null;
        for ( TipoMov tipo : values() ) {
            if ( tipo.getCodigo().equalsIgnoreCase( pString ) || tipo.toString().equalsIgnoreCase( pString ) ) {
                found = tipo;
                break;
            }
        }
        return found;
    }
}
