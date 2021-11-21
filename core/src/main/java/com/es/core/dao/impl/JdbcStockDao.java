package com.es.core.dao.impl;

import com.es.core.dao.StockDao;
import com.es.core.model.phone.Stock;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcStockDao extends JdbcAbstractDao<Stock> implements StockDao {
    private static final String FIND_STOCK_BY_PHONE_ID = "SELECT * FROM stocks WHERE phoneId = :phoneId";
    private static final String INSERT_STOCK = "INSERT INTO stocks (phoneId, stock, reserved) " +
            "VALUES (:phoneId, :stock, :reserved)";
    private static final String UPDATE_STOCK = "UPDATE stocks SET stock = :stock, reserved = :reserved " +
            "WHERE phoneId = :phoneId";

    @Resource
    private StockBeanPropertyRowMapper stockBeanPropertyRowMapper;

    @Override
    public Optional<Stock> get(Long phoneId) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("phoneId", phoneId);
        return super.get(FIND_STOCK_BY_PHONE_ID, sqlParameterSource, stockBeanPropertyRowMapper);
    }

    @Override
    public void save(Stock stock) {
        get(stock.getPhone().getId()).ifPresentOrElse(s -> update(stock), () -> insert(stock));
    }

    private void insert(Stock stock) {
        super.save(INSERT_STOCK, getSqlStockParams(stock));
    }

    private void update(Stock stock) {
        super.save(UPDATE_STOCK, getSqlStockParams(stock));
    }

    private SqlParameterSource getSqlStockParams(Stock stock) {
        return new MapSqlParameterSource(Map.of(
                "phoneId", stock.getPhone().getId(),
                "stock", stock.getStock(),
                "reserved", stock.getReserved()));
    }

}
