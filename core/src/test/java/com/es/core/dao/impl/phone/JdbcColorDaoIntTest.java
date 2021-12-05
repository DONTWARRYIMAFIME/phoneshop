package com.es.core.dao.impl.phone;

import com.es.core.dao.impl.phone.JdbcColorDao;
import com.es.core.model.phone.Color;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:/context/testApplicationContext-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcColorDaoIntTest {
    @Resource
    private JdbcColorDao jdbcColorDao;

    private static final Color color1 = new Color();
    private static final Color color2 = new Color();
    private static final Color color3 = new Color();

    private void setupColor(Color color, Long id, String code) {
        color.setId(id);
        color.setCode(code);
    }

    @Before
    public void setup() {
        setupColor(color1, 1L, "white");
        setupColor(color2, 35L, "pink");
        setupColor(color3, 36L, "gold");
    }

    @Test
    public void testGetColorById() {
        assertEquals(Optional.of(color1), jdbcColorDao.get(1L));
    }

    @Test
    public void testSaveAll() {
        jdbcColorDao.saveAll(Set.of(color2, color3));
        assertEquals(Optional.of(color2), jdbcColorDao.get(35L));
        assertEquals(Optional.of(color3), jdbcColorDao.get(36L));
    }
}
