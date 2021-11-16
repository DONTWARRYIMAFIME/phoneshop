package com.es.core.dao.impl;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PhoneBeanPropertyRowMapper extends BeanPropertyRowMapper<Phone> {
    private static final String GET_PHONE_WITH_COLOR = "SELECT * FROM colors " +
            "JOIN phone2color ON colors.id = phone2color.colorId " +
            "WHERE phone2color.phoneId = :phoneId";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PhoneBeanPropertyRowMapper(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(Phone.class);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Phone mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Phone phone = super.mapRow(rs, rowNumber);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("phoneId", phone.getId());
        Set<Color> colors = new HashSet<>(namedParameterJdbcTemplate.query(GET_PHONE_WITH_COLOR, sqlParameterSource, new BeanPropertyRowMapper<>(Color.class)));
        phone.setColors(colors);

        return phone;
    }
}
