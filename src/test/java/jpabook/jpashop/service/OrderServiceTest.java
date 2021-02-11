package jpabook.jpashop.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

	@Autowired
	EntityManager em;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderRepository orderRepository;

	@Test
	public void ��ǰ�ֹ�() throws Exception {
		// given
		Member member = createMember();
		Book book = createBook("���� JPA", 10000, 10);

		// when
		Long orderId = orderService.order(member.getId(), book.getId(), 2);

		// then
		Order getOrder = orderRepository.findOne(orderId);

		assertEquals("��ǰ �ֹ��� ���´� ORDER", OrderStatus.ORDER, getOrder.getStatus());
		assertEquals("�ֹ��� ��ǰ ���� ���� ��Ȯ�ؾ� �Ѵ�.", 1, getOrder.getOrderItems().size());
		assertEquals("�ֹ� ������ ���� * �����̴�.", 10000 * 2, getOrder.getTotalPrice());
		assertEquals("�ֹ� ������ŭ ��� �پ�� �Ѵ�.", 8, book.getStockQuantity());

	}

	@Test(expected = NotEnoughStockException.class)
	public void ��ǰ�ֹ�_�������ʰ�() throws Exception {
		Member member = createMember();
		Book book = createBook("���� JPA", 10000, 10);

		int orderCount = 11;

		// when
		orderService.order(member.getId(), book.getId(), orderCount);

		fail("��� ���� ���� ���ܰ� �߻��ؾ� �Ѵ�.");
	}

	@Test
	public void �ֹ����() throws Exception {
		// given
		Member member = createMember();
		Book book = createBook("JPA", 10000, 10);
		// when

		int orderCount = 2;

		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		orderService.cancelOrder(orderId);
		// then
		Order order = orderRepository.findOne(orderId);
		assertEquals("�ֹ� ��ҽ� ���´� CANCEL�̴�", OrderStatus.CANCEL, order.getStatus());
		assertEquals("�ֹ��� ��ҵ� ��ǰ�� �׸�ŭ ��� �����ؾ��Ѵ�", 10, book.getStockQuantity());
	}

	private Member createMember() {
		Member member = new Member();
		member.setName("kim1");
		member.setAddress(new Address("����", "���", "123-123"));
		em.persist(member);
		return member;
	}

	private Book createBook(String name, int price, int stockQuantity) {
		Book book = new Book();

		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		em.persist(book);

		return book;

	}
}
