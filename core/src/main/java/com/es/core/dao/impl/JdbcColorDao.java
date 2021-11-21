package com.es.core.dao.impl;

import com.es.core.dao.ColorDao;
import com.es.core.model.phone.Color;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class JdbcColorDao extends JdbcAbstractDao<Color> implements ColorDao {
    private static final String GET_COLOR_BY_ID = "SELECT * FROM colors WHERE id = :id";
    private static final String FIND_ALL_COLOR_IDS = "SELECT id FROM colors";
    private static final String INSERT_COLOR = "INSERT INTO colors (id, code) VALUES (?, ?)";

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Color> get(Long key) {
        return super.get(GET_COLOR_BY_ID, new MapSqlParameterSource("id", key), new BeanPropertyRowMapper<>(Color.class));
    }

    @Override
    public void saveAll(Set<Color> colors) {
        List<Long> colorIds = jdbcTemplate.queryForList(FIND_ALL_COLOR_IDS, Long.class);

        List<Object[]> batchColors = colors.stream()
                .filter(color -> !colorIds.contains(color.getId()))
                .map(color -> new Object[]{ color.getId(), color.getCode() })
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(INSERT_COLOR, batchColors);
    }
}
