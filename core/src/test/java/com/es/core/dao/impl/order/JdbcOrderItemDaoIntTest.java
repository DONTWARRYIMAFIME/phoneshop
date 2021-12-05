package com.es.core.dao.impl.order;

import com.es.core.dao.OrderDao;
import com.es.core.dao.OrderItemDao;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:/context/testApplicationContext-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcOrderItemDaoIntTest {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderItemDao orderItemDao;

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
    }

    @Test
    public void testInsertOrderItems() {
        Order order = new Order();

        setupOrderItem(orderItem1, phone1, order, 11L);
        setupOrderItem(orderItem2, phone2, order, 12L);
        setupOrderItem(orderItem3, phone3, order, 13L);

        setupOrder(order, 101L, "testOrder", "user", "lastName", "address", "1111111", OrderStatus.DELIVERED);
        order.setOrderItems(List.of(orderItem1, orderItem2, orderItem3));

        orderDao.save(order);
        orderItemDao.insertOrderItems(order);

        assertEquals(Optional.of(order), orderDao.getBySecureId("testOrder"));
    }

}
