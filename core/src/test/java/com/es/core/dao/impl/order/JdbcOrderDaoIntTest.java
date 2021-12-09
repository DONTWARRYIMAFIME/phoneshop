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

    private static final Order order = new Order();

    private static final OrderItem orderItem1 = new OrderItem();
    private static final OrderItem orderItem2 = new OrderItem();
    private static final OrderItem orderItem3 = new OrderItem();

    private static final Phone phone1 = new Phone();
    private static final Phone phone2 = new Phone();
    private static final Phone phone3 = new Phone();

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

    private void setupOrder(Order order, Long id, String secureId, String firstName, String lastName, String deliveryAddress, String contactPhoneNo, OrderStatus status) {
        order.setId(id);
        order.setSecureId(secureId);
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setDeliveryAddress(deliveryAddress);
        order.setContactPhoneNo(contactPhoneNo);
        order.setStatus(status);
    }
    
    @Before
    public void init() {
        setupPhone(phone1, 101L, "Xiaomi", "Mi 6", BigDecimal.valueOf(101.0));
        setupPhone(phone2, 102L, "Samsung", "S 20", BigDecimal.valueOf(102.0));
        setupPhone(phone3, 103L, "Iphone", "X", BigDecimal.valueOf(103.0));

        setupOrderItem(orderItem1, phone1, order, 11L);
        setupOrderItem(orderItem2, phone2, order, 12L);
        setupOrderItem(orderItem3, phone3, order, 13L);

        setupOrder(order, 1L, "913cdf30-b93c-4bbd-b1cd-0bf2099c0417", "Ulas", "Kastsiukovich", "1-May Street", "+375331111111", OrderStatus.NEW);
        order.setOrderItems(List.of(orderItem1, orderItem2, orderItem3));
    }

    @Test
    public void testGetOrderByCorrectSecureId() {
        assertEquals(Optional.of(order), orderDao.getBySecureId("913cdf30-b93c-4bbd-b1cd-0bf2099c0417"));
    }

    @Test
    public void testGetOrderByIncorrectSecureId() {
        assertEquals(Optional.empty(), orderDao.getBySecureId("something"));
    }

    @Test
    public void testInsertOrderWithId() {
        Order order = new Order();
        setupOrder(order, 2L, "order2", "user2", "lastName2", "address2", "222222", OrderStatus.REJECTED);

        orderItem2.setOrder(order);
        order.setOrderItems(List.of(orderItem2));

        orderDao.save(order);
        orderItemDao.insertOrderItems(order);

        assertEquals(Optional.of(order), orderDao.getBySecureId("order2"));
    }

    @Test
    public void testUpdateOrderWithOutId() {
        Order order = new Order();
        setupOrder(order, null, "order3", "user3", "lastName3", "address3", "3333333", OrderStatus.NEW);

        order.setOrderItems(List.of(orderItem2));

        orderDao.save(order);

        assertNotNull(order.getId());
    }

    @Test
    public void testUpdateOrder() {
        Order order = new Order();
        setupOrder(order, 1L, "913cdf30-b93c-4bbd-b1cd-0bf2099c0417", "Ivan", "Kastsiukovich", "1-May Street", "+375331111111", OrderStatus.NEW);

        order.setOrderItems(List.of(orderItem2));

        orderDao.save(order);

        assertEquals("Ivan", orderDao.getBySecureId("913cdf30-b93c-4bbd-b1cd-0bf2099c0417").get().getFirstName());
    }

}
