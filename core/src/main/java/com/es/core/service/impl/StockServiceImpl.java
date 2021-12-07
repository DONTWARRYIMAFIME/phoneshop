package com.es.core.service.impl;

import com.es.core.dao.StockDao;
import com.es.core.exception.StockNotFoundException;
import com.es.core.model.phone.Stock;
import com.es.core.service.StockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StockServiceImpl implements StockService {
    @Resource
    private StockDao stockDao;

    @Override
    public Stock getStock(Long phoneId) {
        return stockDao.get(phoneId).orElseThrow(() -> new StockNotFoundException(phoneId));
    }

    @Override
    public void save(Stock stock) {
        stockDao.save(stock);
    }

    @Override
    public void changeStockToReserved(Long phoneId, int quantity) {
        Stock stock = getStock(phoneId);

        if (quantity > stock.getStock()) {
            throw new IllegalStateException("Quantity cannot be more than stock");
        }

        stock.setStock(stock.getStock() - quantity);
        stock.setReserved(stock.getReserved() + quantity);
        stockDao.save(stock);
    }

    @Override
    public void changeReservedToStock(Long phoneId, int quantity) {
        Stock stock = getStock(phoneId);

        if (quantity > stock.getReserved()) {
            throw new IllegalStateException("Quantity cannot be more than stock reserve");
        }

        stock.setStock(stock.getStock() + quantity);
        stock.setReserved(stock.getReserved() - quantity);
        stockDao.save(stock);
    }

    @Override
    public void removeReserved(Long phoneId, int quantity) {
        Stock stock = getStock(phoneId);

        if (quantity > stock.getReserved()) {
            throw new IllegalStateException("Quantity cannot be more than stock reserve");
        }

        stock.setReserved(stock.getReserved() - quantity);
        stockDao.save(stock);
    }
}
