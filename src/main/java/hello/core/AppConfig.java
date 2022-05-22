package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

public class AppConfig {

    // 객체이 생성과 연결은 AppConfig 가 담당한다.
    // MemberServiceImpl 은 MemberRepository 인 추상에만 의존하면 된다.

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
        // 생성자 주입 방식
        // AppConfig 객체는 MemberServiceImpl 입장에서 보면
        // 의존관계를 마치 외부에서 주입해주는 것 같다고 해서 DI 우리말로 의존관계 주입이라고 한다.
    }

    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    private MemberRepository memberRepository() {
        return new MemoryMemberRepository();
        // 역할을 한번더 구분하여 만듬으로서 정책이 바뀌면 해당 부분만 변경하면 된다.
        // 예를들어 새로운 Member 용 데이터베이스가 생겨 기능이 변경되어야 하면
        // 이 메소드에서 리턴하는 구현클래스만 변경해주면 된다!
    }

    private DiscountPolicy discountPolicy() {
        // return new FixDiscountPolicy();
        return new RateDiscountPolicy();
        // 할인 정책이 바뀐다면 이 부분만 변경해주면 된다! - OCP
        // 클라이언트 코드인 OrderServiceImpl 을 포함해서 사용영역의 어떤 코드도 변경할 필요가 없다.
    }

}
