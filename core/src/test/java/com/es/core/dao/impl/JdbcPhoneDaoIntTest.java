package com.es.core.dao.impl;

import com.es.core.dao.ColorDao;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.search.SearchStructure;
import com.es.core.model.search.SortField;
import com.es.core.model.search.SortOrder;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration("classpath:/context/testApplicationContext-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JdbcPhoneDaoIntTest {
    @Resource
    private JdbcPhoneDao jdbcPhoneDao;
    @Resource
    private ColorDao colorDao;

    private static final Phone phone1 = new Phone();
    private static final Phone phone2 = new Phone();
    private static final Phone phone3 = new Phone();
    private static final Phone phone4 = new Phone();
    private static final Phone phone5 = new Phone();

    private static void setupPhone(Phone phone, Long id, String brand, String model, BigDecimal price) {
        phone.setId(id);
        phone.setBrand(brand);
        phone.setModel(model);
        phone.setPrice(price);
    }

    @BeforeClass
    public static void setUpClass() {
        setupPhone(phone1, 101L, "Xiaomi", "Mi 6", BigDecimal.valueOf(101.0));
        setupPhone(phone2, 102L, "Samsung", "S 20", BigDecimal.valueOf(102.0));
        setupPhone(phone3, 103L, "Iphone", "X", BigDecimal.valueOf(103.0));
        setupPhone(phone4, 104L, "Huawei", "P 20", BigDecimal.valueOf(104.0));
        setupPhone(phone5, 105L, "Asus", "ZenPhone 2", BigDecimal.valueOf(105.0));
    }

    @Test
    public void testGetWithCorrectId() {
        assertEquals(Optional.of(phone1), jdbcPhoneDao.get(101L));
    }

    @Test
    public void testGetWithIncorrectId() {
        assertEquals(Optional.empty(), jdbcPhoneDao.get(-1L));
    }

    @Test
    public void testFindAllWithCorrectLimitAndOffset() {
        assertEquals(List.of(phone1, phone2), jdbcPhoneDao.findAll(null, 0, 2));
    }

    @Test
    public void testSearchByQuery() {
        assertEquals(List.of(phone1), jdbcPhoneDao.findAll(new SearchStructure("xiaomi", null, null), 0, 10));
    }

    @Test
    public void testSortByPriceAsc() {
        List<Phone> expected = List.of(phone1, phone2, phone3, phone4, phone5);
        List<Phone> actual = jdbcPhoneDao.findAll(new SearchStructure(null, SortField.PRICE, SortOrder.ASC), 0, 5);

        assertEquals(expected, actual);
    }

    @Test
    public void testSortByBrandDesc() {
        List<Phone> expected = List.of(phone1, phone2, phone3, phone4, phone5);
        List<Phone> actual = jdbcPhoneDao.findAll(new SearchStructure(null, SortField.BRAND, SortOrder.DESC), 0, 5);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAllWithIncorrectOffset() {
        jdbcPhoneDao.findAll(null, -1, Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAllWithIncorrectLimit() {
        jdbcPhoneDao.findAll(null, 0, -1);
    }

    @Test
    public void testSavePhoneWithId() {
        Phone phone = new Phone();
        setupPhone(phone, 103L, "Iphone", "X", BigDecimal.valueOf(103));

        jdbcPhoneDao.save(phone);

        assertEquals(Long.valueOf(103L), phone.getId());
        assertEquals("Iphone", phone.getBrand());
        assertEquals("X", phone.getModel());
    }

    @Test
    public void testSavePhoneWithOutId() {
        Phone phone = new Phone();
        setupPhone(phone, null, "Huawei", "P20", BigDecimal.valueOf(104));

        jdbcPhoneDao.save(phone);

        assertNotNull(phone.getId());
        assertEquals("Huawei", phone.getBrand());
        assertEquals("P20", phone.getModel());
    }

    @Test
    public void testUpdatePhone() {
        Phone phone = new Phone();
        setupPhone(phone, 102L, "Google", "Pixel 6", BigDecimal.valueOf(102));

        jdbcPhoneDao.save(phone);

        assertNotNull(phone.getId());
        assertEquals("Google", phone.getBrand());
        assertEquals("Pixel 6", phone.getModel());
    }

    @Test
    public void testCount() {
        assertEquals(5L, jdbcPhoneDao.count(null));
    }

    @Test
    public void testUpdatePhoneColors() {
        Color gray = colorDao.get(3L).get();
        Color yellow = colorDao.get(4L).get();

        jdbcPhoneDao.updatePhoneColors(106L, Set.of(gray, yellow));

        assertEquals(Set.of(gray, yellow), jdbcPhoneDao.get(106L).get().getColors());
    }

}