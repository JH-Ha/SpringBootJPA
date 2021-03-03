package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@ActiveProfiles("test")
public class MemberServiceTest {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberRepository memberRepositoryOld;

	@Autowired
	EntityManager em;

	@Test
	public void 회원가입() {
		// given
		Member member = new Member();
		member.setName("kim");

		// when
		Long saveId = memberService.join(member);
		// then
		em.flush();
		assertEquals(member, memberRepositoryOld.findOne(saveId));
	}

	@Test(expected = IllegalStateException.class)
	public void 중복_회원_제외() throws Exception {
		// fail("Not yet implemented");
		Member member1 = new Member();
		member1.setName("kim1");

		Member member2 = new Member();
		member2.setName("kim1");

		memberService.join(member1);
		memberService.join(member2);

		fail("예외가 발생해야 한다.");
	}

}
