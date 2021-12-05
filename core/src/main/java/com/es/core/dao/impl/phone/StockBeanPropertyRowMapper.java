package com.es.core.dao.impl.phone;

import com.es.core.dao.PhoneDao;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StockBeanPropertyRowMapper extends BeanPropertyRowMapper<Stock> {
    private PhoneDao phoneDao;

    public StockBeanPropertyRowMapper(PhoneDao phoneDao) {
        super(Stock.class);
        this.phoneDao = phoneDao;
    }

    @Override
    public Stock mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Long phoneId = rs.getLong("phoneId");
        Phone phone = phoneDao.get(phoneId).orElseThrow(() -> new PhoneNotFoundException(phoneId));

        Stock stock = new Stock();
        stock.setPhone(phone);
        stock.setStock(rs.getInt("stock"));
        stock.setReserved(rs.getInt("reserved"));

        return stock;
    }
}
