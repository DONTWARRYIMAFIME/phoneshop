package com.es.core.dao.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.search.SearchStructure;
import org.h2.util.StringUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class JdbcPhoneDao extends JdbcAbstractDao<Phone> implements PhoneDao {
    private static final String FIND_PHONE_BY_ID = "SELECT * FROM phones WHERE id = :id";
    private static final String FIND_ALL_PHONES = "SELECT * FROM phones " +
            "JOIN stocks on phones.id = stocks.phoneId " +
            "WHERE stocks.stock > 0 AND phones.price IS NOT NULL OFFSET :offset LIMIT :limit";
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

    private static final String SEARCH_QUERY_WITH_SORT = "SELECT * FROM phones " +
            "JOIN stocks on phones.id = stocks.phoneId " +
            "WHERE stocks.stock > 0 AND phones.price IS NOT NULL " +
            "AND (LOWER(phones.brand) LIKE '%%%s%%' OR LOWER(phones.model) LIKE '%%%s%%') " +
            "ORDER BY %s %s OFFSET :offset limit :limit";
    private static final String SEARCH_QUERY = "SELECT * FROM phones " +
            "JOIN stocks on phones.id = stocks.phoneId " +
            "WHERE stocks.stock > 0 AND phones.price IS NOT NULL " +
            "AND (LOWER(phones.brand) LIKE '%%%s%%' OR LOWER(phones.model) LIKE '%%%s%%') " +
            "OFFSET :offset limit :limit";
    private static final String SORT = "SELECT * FROM phones " +
            "JOIN stocks on phones.id = stocks.phoneId " +
            "WHERE stocks.stock > 0 AND phones.price IS NOT NULL " +
            "ORDER BY %s %s OFFSET :offset limit :limit";

    private static final String COUNT_WITH_QUERY = "SELECT COUNT(*) FROM phones AS p JOIN stocks AS s ON p.id = s.phoneId " +
            "WHERE s.stock > 0 AND p.price IS NOT NULL AND (lower(p.brand) LIKE :query " +
            "OR lower(p.model) LIKE :query)";
    private static final String COUNT = "SELECT COUNT(*) FROM phones AS p JOIN stocks AS s ON p.id = s.phoneId " +
            "WHERE s.stock > 0 AND p.price IS NOT NULL";

    private static final String INSERT_PHONE_COLORS = "INSERT INTO phone2color (phoneId, colorId) " +
            "VALUES (:phoneId, :colorId)";
    private static final String DELETE_PHONE_COLORS = "DELETE FROM phone2color WHERE phoneId = :phoneId";

    @Resource
    private PhoneBeanPropertyRowMapper phoneBeanPropertyRowMapper;

    @Override
    public Optional<Phone> get(final Long key) {
        return super.get(FIND_PHONE_BY_ID, new MapSqlParameterSource("id", key), phoneBeanPropertyRowMapper);
    }

    @Override
    public List<Phone> findAll(SearchStructure searchStructure, int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException(String.format("Illegal value of offset: %d or limit:%d%n", offset, limit));
        }

        String sql = FIND_ALL_PHONES;
        if (searchStructure != null) {
            String query = searchStructure.getQuery();
            String sortBy = searchStructure.getSortField() != null ? searchStructure.getSortField().toString() : null;
            String orderBy = searchStructure.getOrder() != null ? searchStructure.getOrder().toString() : null;

            if (!StringUtils.isNullOrEmpty(query) && !StringUtils.isNullOrEmpty(sortBy) && !StringUtils.isNullOrEmpty(orderBy)) {
                sql = String.format(SEARCH_QUERY_WITH_SORT, query, query, sortBy, orderBy);
            } else if (!StringUtils.isNullOrEmpty(sortBy) && !StringUtils.isNullOrEmpty(orderBy)) {
                sql = String.format(SORT, sortBy, orderBy);
            } else if (!StringUtils.isNullOrEmpty(query)) {
                sql = String.format(SEARCH_QUERY, query, query);
            }
        }

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(Map.of("offset", offset, "limit", limit));
        return super.findAll(sql, sqlParameterSource, phoneBeanPropertyRowMapper);
    }

    @Override
    public void save(final Phone phone) {
        get(phone.getId()).ifPresentOrElse(p -> update(phone), () -> insert(phone));
    }

    @Override
    public void updatePhoneColors(Long phoneId, Set<Color> colors) {
        super.delete(DELETE_PHONE_COLORS, new MapSqlParameterSource("phoneId", phoneId));

        SqlParameterSource[] sqlParameterSources = colors.stream()
                .map(color -> new MapSqlParameterSource()
                        .addValue("phoneId", phoneId)
                        .addValue("colorId", color.getId())).toArray(MapSqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(INSERT_PHONE_COLORS, sqlParameterSources);
    }

    @Override
    public long count(String query) {
        String sql = StringUtils.isNullOrEmpty(query) ? COUNT : COUNT_WITH_QUERY;

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("query", "%" + query + "%");
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Long.class);
    }

    private void insert(final Phone phone) {
        Long newId = super.save(INSERT_PHONE, new BeanPropertySqlParameterSource(phone), new GeneratedKeyHolder());

        if (phone.getId() == null) {
            phone.setId(newId);
        }
    }

    private void update(final Phone phone) {
        super.save(UPDATE_PHONE, new BeanPropertySqlParameterSource(phone));
    }

}
