package mx.lux.pos.service

import mx.lux.pos.model.Apertura

public interface OpenSalesDayService {

    Apertura findSalesDay( Date pDate )

    Apertura register( Date pOpeningForDate, Double pCurrentAmt, Double pCurrentAmtUsd, String pRemarks )

}