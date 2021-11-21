package com.es.core.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Repository
public abstract class JdbcAbstractDao<T> {
    @Resource
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<T> findAll(String sqlQuery, SqlParameterSource sqlParameterSource, RowMapper<T> rowMapper) {
        return namedParameterJdbcTemplate.query(sqlQuery, sqlParameterSource, rowMapper);
    }

    public Optional<T> get(String sqlQuery, SqlParameterSource sqlParameterSource, RowMapper<T> rowMapper) {
        try {
            T item = namedParameterJdbcTemplate.queryForObject(sqlQuery, sqlParameterSource, rowMapper);
            return Optional.of(item);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(String namedParameterSqlQuery, SqlParameterSource namedParams) {
        namedParameterJdbcTemplate.update(namedParameterSqlQuery, namedParams);
    }

    public long save(String namedParameterSqlQuery, SqlParameterSource namedParams, KeyHolder keyHolder) {
        namedParameterJdbcTemplate.update(namedParameterSqlQuery, namedParams, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void delete(String sqlQuery, SqlParameterSource sqlParameterSource) {
        namedParameterJdbcTemplate.update(sqlQuery, sqlParameterSource);
    }

}
