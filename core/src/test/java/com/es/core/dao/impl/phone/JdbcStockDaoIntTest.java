package com.es.core.dao.impl.phone;

import com.es.core.dao.PhoneDao;
import com.es.core.dao.StockDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:context/testApplicationContext-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcStockDaoIntTest {
    @Resource
    private StockDao stockDao;
    @Resource
    private PhoneDao phoneDao;

    private static final Phone phone1 = new Phone();
    private static final Phone phone2 = new Phone();

    private static final Stock stock1 = new Stock();
    private static final Stock stock2 = new Stock();

    private static void setupPhone(Phone phone, Long id, String brand, String model, BigDecimal price) {
        phone.setId(id);
        phone.setBrand(brand);
        phone.setModel(model);
        phone.setPrice(price);
    }

    private static void setupStock(Stock stock, Phone phone, Integer stockValue, Integer reserved) {
        stock.setPhone(phone);
        stock.setStock(stockValue);
        stock.setReserved(reserved);
    }

    @BeforeClass
    public static void setUpClass() {
        setupPhone(phone1, 101L, "Xiaomi", "Mi 6", BigDecimal.valueOf(101));
        setupPhone(phone2, 102L, "Samsung", "S 20", BigDecimal.valueOf(102));
        setupStock(stock1, phone1, 101, 0);
        setupStock(stock2, phone2, 102, 0);
    }

    @Test
    public void testGetStock() {
        assertEquals(Optional.of(stock1), stockDao.get(101L));
    }

    @Test
    public void testInsertStock() {
        Phone phone = new Phone();
        Stock stock = new Stock();
        setupPhone(phone, 109L, "LeEco", "Z3", BigDecimal.valueOf(109));
        setupStock(stock, phone, 5, 2);

        phoneDao.save(phone);
        stockDao.save(stock);

        assertEquals(Optional.of(stock), stockDao.get(phone.getId()));
    }

    @Test
    public void testUpdateStock() {
        Stock stock = new Stock();
        setupStock(stock, phone2, 5, 2);

        stockDao.save(stock);

        assertEquals(Optional.of(stock), stockDao.get(phone2.getId()));
    }
}