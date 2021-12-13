package com.es.core.service;

import com.es.core.exception.StockNotFoundException;
import com.es.core.model.phone.Stock;

public interface StockService {
    Stock getStock(Long phoneId) throws StockNotFoundException;
    void save(Stock stock);
    void changeStockToReserved(Long phoneId, int quantity);
    void changeReservedToStock(Long phoneId, int quantity);
    void removeReserved(Long phoneId, int quantity);
}