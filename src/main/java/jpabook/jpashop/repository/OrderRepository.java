package jpabook.jpashop.repository;

import javax.persistence.EntityManager;

import com.querydsl.core.BooleanBuilder;

import com.querydsl.jpa.impl.JPAQuery;
import jpabook.jpashop.domain.QOrder;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;
import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

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

        JPAQuery<Order> query = new JPAQuery<Order>(em);

        QOrder qOrder = new QOrder("o");

        BooleanBuilder builder = new BooleanBuilder();

        if(StringUtils.hasText(orderSearch.getMemberName())){
            builder.and(qOrder.member.name.eq(orderSearch.getMemberName()));
        }
        if(orderSearch.getOrderStatus()!= null){
            builder.and(qOrder.status.eq(orderSearch.getOrderStatus()));
        }


        List<Order> orders =
            query.from(qOrder).where(builder).fetch();

        /*List<Order> orders = em.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status" +
                        " and m.name like :name",
                Order.class).setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName()).getResultList();*/
        return orders;
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o"
                + " join fetch o.member m"
                + " join fetch o.delivery d", Order.class
        ).getResultList();
    }
}
