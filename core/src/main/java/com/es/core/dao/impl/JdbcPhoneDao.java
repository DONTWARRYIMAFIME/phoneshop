package com.es.core.dao.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.model.phone.Phone;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcPhoneDao implements PhoneDao {
    private static final String FIND_PHONE_BY_ID = "SELECT * FROM phones WHERE id = ?";
    private static final String FIND_ALL_PHONES = "SELECT * FROM phones OFFSET ? LIMIT ?";
    private static final String INSERT_PHONE = "INSERT INTO phones (id, brand, model, price, displaySizeInches, " +
            "weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, " +
            "displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, " +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
            "VALUES (:id, :brand, :model, :price, :displaySizeInches, :weightGr, :lengthMm, :widthMm, :heightMm, " +
            ":announced, :deviceType, :os, :displayResolution, :pixelDensity, :displayTechnology, " +
            ":backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, :batteryCapacityMah, " +
            ":talkTimeHours, :standByTimeHours, :bluetooth, :positioning, :imageUrl, :description)";
    private static final String UPDATE_PHONE = "UPDATE phones SET brand = :brand, model = :model, price = :price, " +
            "displaySizeInches = :displaySizeInches, weightGr = :weightGr, lengthMm = :lengthMm, widthMm = :widthMm, " +
            "heightMm = :heightMm, announced = :announced, deviceType = :deviceType, os = :os, " +
            "displayResolution = :displayResolution, pixelDensity = :pixelDensity, " +
            "displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, " +
            "frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, internalStorageGb = :internalStorageGb, " +
            "batteryCapacityMah = :batteryCapacityMah, talkTimeHours = :talkTimeHours, " +
            "standByTimeHours = :standByTimeHours, bluetooth = :bluetooth, positioning = :positioning, " +
            "imageUrl = :imageUrl, description = :description WHERE id = :id";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private PhoneBeanPropertyRowMapper phoneBeanPropertyRowMapper;

    @Override
    public Optional<Phone> get(final Long key) {
        try {
            Phone phone = jdbcTemplate.queryForObject(FIND_PHONE_BY_ID, phoneBeanPropertyRowMapper, key);
            return Optional.of(phone);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Phone> findAll(int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException(String.format("Illegal value of offset: %d or limit:%d%n", offset, limit));
        }

        return jdbcTemplate.query(FIND_ALL_PHONES, phoneBeanPropertyRowMapper, offset, limit);
    }

    @Override
    public void save(final Phone phone) {
        get(phone.getId()).ifPresentOrElse(p -> update(phone), () -> insert(phone));
    }

    private void insert(final Phone phone) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_PHONE, new BeanPropertySqlParameterSource(phone), keyHolder);

        if (phone.getId() == null) {
            phone.setId(keyHolder.getKey().longValue());
        }
    }

    private void update(final Phone phone) {
        namedParameterJdbcTemplate.update(UPDATE_PHONE, new BeanPropertySqlParameterSource(phone));
    }

}
