package com.es.core.dao.impl.order;

import com.es.core.dao.OrderDao;
import com.es.core.dao.OrderItemDao;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration("classpath:/context/testApplicationContext-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JdbcOrderDaoIntTest {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderItemDao orderItemDao;

    private static final Order order1 = new Order();
    private static final Order order2 = new Order();
    private static final Order order3 = new Order();

    private static final OrderItem orderItem1 = new OrderItem();
    private static final OrderItem orderItem2 = new OrderItem();
    private static final OrderItem orderItem3 = new OrderItem();
    private static final OrderItem orderItem4 = new OrderItem();
    private static final OrderItem orderItem5 = new OrderItem();
    private static final OrderItem orderItem6 = new OrderItem();


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

    private static void setupOrderItem(OrderItem orderItem, Phone phone, Order order, Long quantity) {
        orderItem.setPhone(phone);
        orderItem.setOrder(order);
        orderItem.setQuantity(quantity);
    }

    private void setupOrder(Order order, Long id, String secureId, String firstName, String lastName, String deliveryAddress, String contactPhoneNo, OrderStatus status, LocalDateTime dateTime) {
        order.setId(id);
        order.setSecureId(secureId);
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setDeliveryAddress(deliveryAddress);
        order.setContactPhoneNo(contactPhoneNo);
        order.setStatus(status);
        order.setDateTime(dateTime);
    }
    
    @Before
    public void init() {
        setupPhone(phone1, 101L, "Xiaomi", "Mi 6", BigDecimal.valueOf(101.0));
        setupPhone(phone2, 102L, "Samsung", "S 20", BigDecimal.valueOf(102.0));
        setupPhone(phone3, 103L, "Iphone", "X", BigDecimal.valueOf(103.0));
        setupPhone(phone4, 104L, "Huawei", "P 20", BigDecimal.valueOf(104.0));
        setupPhone(phone5, 105L, "Asus", "ZenPhone 2", BigDecimal.valueOf(105.0));

        setupOrderItem(orderItem1, phone1, order1, 11L);
        setupOrderItem(orderItem2, phone2, order1, 12L);
        setupOrderItem(orderItem3, phone3, order1, 13L);
        setupOrderItem(orderItem4, phone4, order2, 2L);
        setupOrderItem(orderItem5, phone4, order3, 1L);
        setupOrderItem(orderItem6, phone5, order3, 2L);

        setupOrder(order1, 1L, "913cdf30-b93c-4bbd-b1cd-0bf2099c0417", "Ulas", "Kastsiukovich", "1-May Street", "+375331111111", OrderStatus.NEW, Timestamp.valueOf("2021-12-08 01:46:45.147412900").toLocalDateTime());
        setupOrder(order2, 2L, "a000c9fe-9119-4d7b-9477-3f9329b49188", "Anton", "Martinov", "Melisha 5", "+375332222222", OrderStatus.DELIVERED, Timestamp.valueOf("2015-4-09 12:31:05.163261253").toLocalDateTime());
        setupOrder(order3, 3L, "2bac50d9-6066-42e6-966c-d8ffc5e21a78", "Valya", "Protasenya", "Ostrovskogo 1", "+375333333333", OrderStatus.REJECTED, Timestamp.valueOf("2011-11-11 05:01:13.185643500").toLocalDateTime());

        order1.setOrderItems(List.of(orderItem1, orderItem2, orderItem3));
        order2.setOrderItems(List.of(orderItem4));
        order3.setOrderItems(List.of(orderItem5, orderItem6));
    }

    @Test
    public void testFindAllOrders() {
        assertEquals(List.of(order1, order2, order3), orderDao.findAll());
    }

    @Test
    public void testGetOrderByCorrectId() {
        assertEquals(Optional.of(order1), orderDao.getById(1L));
    }

    @Test
    public void testGetOrderByCorrectSecureId() {
        assertEquals(Optional.of(order1), orderDao.getBySecureId("913cdf30-b93c-4bbd-b1cd-0bf2099c0417"));
    }

    @Test
    public void testGetOrderByIncorrectSecureId() {
        assertEquals(Optional.empty(), orderDao.getBySecureId("something"));
    }

    @Test
    public void testInsertOrderWithId() {
        Order order = new Order();
        setupOrder(order, 10L, "order2", "user2", "lastName2", "address2", "222222", OrderStatus.REJECTED, LocalDateTime.now());

        orderItem2.setOrder(order);
        order.setOrderItems(List.of(orderItem2));

        orderDao.save(order);
        orderItemDao.insertOrderItems(order);

        assertEquals(Optional.of(order), orderDao.getBySecureId("order2"));
    }

    @Test
    public void testUpdateOrderWithOutId() {
        Order order = new Order();
        setupOrder(order, null, "order3", "user3", "lastName3", "address3", "3333333", OrderStatus.NEW, LocalDateTime.now());

        order.setOrderItems(List.of(orderItem2));

        orderDao.save(order);

        assertNotNull(order.getId());
    }

    @Test
    public void testUpdateOrder() {
        Order order = new Order();
        setupOrder(order, 1L, "913cdf30-b93c-4bbd-b1cd-0bf2099c0417", "Ivan", "Kastsiukovich", "1-May Street", "+375331111111", OrderStatus.NEW, LocalDateTime.now());

        order.setOrderItems(List.of(orderItem2));

        orderDao.save(order);

        assertEquals("Ivan", orderDao.getBySecureId("913cdf30-b93c-4bbd-b1cd-0bf2099c0417").get().getFirstName());
    }

}
