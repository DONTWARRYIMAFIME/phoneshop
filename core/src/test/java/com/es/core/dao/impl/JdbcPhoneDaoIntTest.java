package com.es.core.dao.impl;

import com.es.core.model.phone.Phone;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration("classpath:/context/testApplicationContext-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcPhoneDaoIntTest {
    @Resource
    private JdbcPhoneDao jdbcPhoneDao;

    private static final Phone phone1 = new Phone();
    private static final Phone phone2 = new Phone();

    private static void setupPhone(Phone phone, Long id, String brand, String model) {
        phone.setId(id);
        phone.setBrand(brand);
        phone.setModel(model);
    }

    @BeforeClass
    public static void setUpClass() {
        setupPhone(phone1, 101L, "Xiaomi", "Mi 6");
        setupPhone(phone2, 102L, "Samsung", "S 20");
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
        assertEquals(List.of(phone1, phone2), jdbcPhoneDao.findAll(0, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAllWithIncorrectOffset() {
        assertEquals(List.of(), jdbcPhoneDao.findAll(-1, Integer.MAX_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindAllWithIncorrectLimit() {
        assertEquals(List.of(), jdbcPhoneDao.findAll(0, -1));
    }

    @Test
    public void testSavePhoneWithId() {
        Phone phone = new Phone();
        setupPhone(phone, 103L, "Iphone", "X");

        jdbcPhoneDao.save(phone);

        assertEquals(Long.valueOf(103L), phone.getId());
        assertEquals("Iphone", phone.getBrand());
        assertEquals("X", phone.getModel());
    }

    @Test
    public void testSavePhoneWithOutId() {
        Phone phone = new Phone();
        setupPhone(phone, null, "Huawei", "P20");

        jdbcPhoneDao.save(phone);

        assertNotNull(phone.getId());
        assertEquals("Huawei", phone.getBrand());
        assertEquals("P20", phone.getModel());
    }

    @Test
    public void testUpdatePhone() {
        Phone phone = new Phone();
        setupPhone(phone, 102L, "Google", "Pixel 6");

        jdbcPhoneDao.save(phone);

        assertNotNull(phone.getId());
        assertEquals("Google", phone.getBrand());
        assertEquals("Pixel 6", phone.getModel());
    }

}