package com.es.core.dao.impl.order;

import com.es.core.dao.OrderDao;
import com.es.core.dao.impl.JdbcAbstractDao;
import com.es.core.model.order.Order;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

@Repository
public class JdbcOrderDao extends JdbcAbstractDao<Order> implements OrderDao {
    private static final String GET_ORDER_BY_SECURE_ID = "SELECT * FROM orders JOIN orderItems " +
            "ON orders.id = orderItems.orderId WHERE orders.secureId = :secureId";
    private static final String INSERT_ORDER = "INSERT INTO orders (id, secureId, subtotal, deliveryPrice, " +
            "totalPrice, firstName, lastName, deliveryAddress, contactPhoneNo, status, additionalInfo) " +
            "VALUES (:id, :secureId, :subtotal, :deliveryPrice, :totalPrice, :firstName, :lastName, " +
            ":deliveryAddress, :contactPhoneNo, :status, :additionalInfo)";
    private static final String UPDATE_ORDER = "UPDATE orders SET subtotal = :subtotal, " +
            "deliveryPrice = :deliveryPrice, totalPrice = :totalPrice, firstName = :firstName, lastName = :lastName, " +
            "deliveryAddress = :deliveryAddress, contactPhoneNo = :contactPhoneNo, status = :status, " +
            "additionalInfo = :additionalInfo WHERE id = :id";

    @Resource
    private OrderBeanPropertyRowMapper orderBeanPropertyRowMapper;

    @Override
    public Optional<Order> getBySecureId(String secureId) {
        return super.get(GET_ORDER_BY_SECURE_ID, new MapSqlParameterSource("secureId", secureId), orderBeanPropertyRowMapper);
    }

    @Override
    public void save(Order order) {
        getBySecureId(order.getSecureId()).ifPresentOrElse(o -> update(order), () -> insert(order));
    }

    private void insert(Order order) {
        Long newId = super.save(INSERT_ORDER, getSqlParameterSource(order), new GeneratedKeyHolder());

        if (order.getId() == null) {
            order.setId(newId);
        }
    }

    private void update(Order order) {
        super.save(UPDATE_ORDER, getSqlParameterSource(order));
    }

    private SqlParameterSource getSqlParameterSource(Order order) {
        return new MapSqlParameterSource()
                .addValue("id", order.getId())
                .addValue("secureId", order.getSecureId())
                .addValue("subtotal", order.getSubtotal())
                .addValue("deliveryPrice", order.getDeliveryPrice())
                .addValue("totalPrice", order.getTotalPrice())
                .addValue("firstName", order.getFirstName())
                .addValue("lastName", order.getLastName())
                .addValue("deliveryAddress", order.getDeliveryAddress())
                .addValue("contactPhoneNo", order.getContactPhoneNo())
                .addValue("status", order.getStatus().toString())
                .addValue("additionalInfo", order.getAdditionalInfo());
    }


}
