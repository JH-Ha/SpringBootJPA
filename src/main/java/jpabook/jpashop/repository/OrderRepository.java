package jpabook.jpashop.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        List<Order> orders = em.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status" +
                        " and m.name like :name",
                Order.class).setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName()).getResultList();
        return orders;
    }
}
