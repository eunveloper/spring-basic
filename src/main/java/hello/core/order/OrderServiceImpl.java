package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    /* 필드 주입
     코드가 간결해서 많은 개발자들을 유혹하지만 외부에서 변경이 불가능해서 테스트 하기 힘들다는 치명적 단점이 있다.
     순수한 자바 테스트 코드에는 @Autowired 가 동작하지 않기 때문에 NPE 가 나게된다.
     DI 프레임워크가 없으면 아무것도 할 수 없다.
     사용하지 말자!
        논외1. 애플리케이션의 실제 코드와 관게없는 테스트코드
        논외2. 스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별한 용도로 사용 */
    @Autowired
    private /*final*/ MemberRepository memberRepository;

    @Autowired
    private /*final*/ DiscountPolicy discountPolicy;
    // 인터페이스에만 의존! - DIP

    @Autowired  // 생성자가 한개만 있는경우는 생략해도 자동 주입된다.
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        /* 생성자 주입
         이름 그대로 생성자를 통해서 의존 관계를 주입 받는 방법
         생성자 호출 시점에 딱 한번만 호출되는 것이 보장
         불변, 필수 의존관계에 사용된다.
         코드를 잘보면 외부에서 memberRepository 등을 변경할 방법이 없다.
         스프링이 빈을 등록하기 위해서 객제를 생성할 때 호출되고, 그때 의존성 주입이 일어남 */
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Autowired  // 주입할 대상이 없으면 오류가 나는데, 오류를 막으려면 (required = false) 지정하면 됨
    public void setMemberRepository(MemberRepository memberRepository) {
        /* 수정자 주입
         setter 라고 불리는 필드의 값을 변경하는 수정자 메서드를 통해서 의존관계를 주입하는 방법
         선택, 변경 가능성이 있는 의존관계에 사용
         자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법 */
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        /* 일반 메서드 주입
         한번에 여러 필드를 주입 받을 수 있다.
         일반적으로 잘 사용하지 않는다. */
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
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
