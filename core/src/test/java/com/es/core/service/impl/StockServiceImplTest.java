package com.es.core.service.impl;

import com.es.core.dao.StockDao;
import com.es.core.exception.StockNotFoundException;
import com.es.core.model.phone.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceImplTest {
    @Mock
    private StockDao stockDao;
    @Mock
    private Stock stock1;
    @Mock
    private Stock stock2;

    @InjectMocks
    private StockServiceImpl stockService;

    private void setupStock(Stock stock, int stockValue, int reserved) {
        when(stock.getStock()).thenReturn(stockValue);
        when(stock.getReserved()).thenReturn(reserved);
    }

    @Before
    public void init() {
        when(stockDao.get(101L)).thenReturn(Optional.of(stock1));
        when(stockDao.get(102L)).thenReturn(Optional.of(stock2));

        setupStock(stock1, 101, 95);
        setupStock(stock2, 102, 101);
    }

    @Test
    public void testGetStockWithCorrectPhoneId() {
        assertEquals(stock1, stockService.getStock(101L));
    }

    @Test(expected = StockNotFoundException.class)
    public void testGetStockWithIncorrectPhoneId() {
        stockService.getStock(-5L);
    }

    @Test
    public void testSaveStock() {
        stockService.save(stock2);
        verify(stockDao).save(stock2);
    }

    @Test
    public void testChangeStockToReserved() {
        stockService.changeStockToReserved(101L, 5);

        verify(stock1).setStock(96);
        verify(stock1).setReserved(100);
        verify(stockDao).save(stock1);
    }

    @Test
    public void testChangeReservedToStock() {
        stockService.changeReservedToStock(102L, 5);

        verify(stock2).setStock(107);
        verify(stock2).setReserved(96);
        verify(stockDao).save(stock2);
    }

    @Test
    public void testRemoveReserved() {
        stockService.removeReserved(101L, 90);

        verify(stock1).setReserved(5);
        verify(stockDao).save(stock1);
    }
}
