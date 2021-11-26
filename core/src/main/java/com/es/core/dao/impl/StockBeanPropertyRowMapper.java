package com.es.core.dao.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.exception.EntityNotFoundException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StockBeanPropertyRowMapper extends BeanPropertyRowMapper<Stock> {
    private PhoneDao phoneDao;

    public StockBeanPropertyRowMapper(PhoneDao phoneDao) {
        super(Stock.class);
        this.phoneDao = phoneDao;
    }

    @Override
    public Stock mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Long phoneId = rs.getLong("phoneId");
        Phone phone = phoneDao.get(phoneId).orElseThrow(() -> new EntityNotFoundException("Phone", phoneId));

        Stock stock = new Stock();
        stock.setPhone(phone);
        stock.setStock(rs.getInt("stock"));
        stock.setReserved(rs.getInt("reserved"));

        return stock;
    }
}
