package mx.lux.pos.ui.model

interface ICorporateKeyVerifier {

  Boolean requestVerify( String pCorporateKey, Double pDiscountPct )
  
}
