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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration 을 붙이면 바이트코드를 조작하는 CGLIB 기술을 사용해서 싱글톤을 보장
// @Bean만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.
@Configuration  // 구성정보를 담는다는 의미의 Annotation
public class AppConfig {

    // 객체이 생성과 연결은 AppConfig 가 담당한다.
    // MemberServiceImpl 은 MemberRepository 인 추상에만 의존하면 된다.

    @Bean   // 스프링 컨테이너에 등록됨
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        // 호출할때 sout 이 찍힐것 같지만 사실은 초기 부팅단계에 최초 1회 작성된다.
        return new MemberServiceImpl(memberRepository());
        // 생성자 주입 방식
        // AppConfig 객체는 MemberServiceImpl 입장에서 보면
        // 의존관계를 마치 외부에서 주입해주는 것 같다고 해서 DI 우리말로 의존관계 주입이라고 한다.
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    // @Bean memberService -> new MemoryMemberRepository();
    // @Bean orderService -> new MemoryMemberRepository();
    // 결과적으로 각각 다른 2개의 MemoryMemberRepository 가 생성되면서 싱글톤이 깨지는 것 처럼 보인다.
    // 스프링 컨테이너는 이 문제를 어떻게 해결할까?
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
        // 역할을 한번더 구분하여 만듬으로서 정책이 바뀌면 해당 부분만 변경하면 된다.
        // 예를들어 새로운 Member 용 데이터베이스가 생겨 기능이 변경되어야 하면
        // 이 메소드에서 리턴하는 구현클래스만 변경해주면 된다!
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        // return new FixDiscountPolicy();
        return new RateDiscountPolicy();
        // 할인 정책이 바뀐다면 이 부분만 변경해주면 된다! - OCP
        // 클라이언트 코드인 OrderServiceImpl 을 포함해서 사용영역의 어떤 코드도 변경할 필요가 없다.
    }

}
