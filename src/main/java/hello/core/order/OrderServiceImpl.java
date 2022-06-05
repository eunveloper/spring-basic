package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    // 인터페이스에만 의존! - DIP

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    // private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    /* 추상(인터페이스)의존과 더불어 구체(구현클래스)도 함께 의존한다. 이는 DIP 위반!
     할인정책을 변경하려면 클라이언트인 OrderServiceImpl 코드를 변경해야 한다. 이는 OCP 위반! */

    // private DiscountPolicy discountPolicy;
    /* 인터페이스에만 의존하도록 코드 변경
     하지만 구현체가 없기 때문에 NPE 가 발생!
     즉, 누군가가 클라이언트인 OrderServiceImpl 에 DiscountPolicy 의 구현객체를 대신 생성하고 주입해야함. */

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
