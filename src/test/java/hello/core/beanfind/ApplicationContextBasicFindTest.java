package hello.core.beanfind;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class ApplicationContextBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        System.out.println("memberService = " + memberService);

        Assertions.assertInstanceOf(MemberService.class, memberService);
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByType() {
        MemberService memberService = ac.getBean(MemberService.class);
        System.out.println("memberService = " + memberService);

        Assertions.assertInstanceOf(MemberService.class, memberService);
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByName2() {
        MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);
        // 실제 구현체 타입으로 조회해도 무방하다.
        // 하지만 좋은 방식은 아님 -> 역할에 의존해야 하기 때문!
        System.out.println("memberService = " + memberService);

        Assertions.assertInstanceOf(MemberService.class, memberService);
    }

    @Test
    @DisplayName("빈 이름으로 조회 X")
    void findBeanByNameX() {
        // MemberService memberService = ac.getBean("noNameMemberService", MemberServiceImpl.class);
        // NoSuchBeanDefinitionException: No bean named 'noNameMemberService' available
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("noNameMemberService", MemberServiceImpl.class));
    }


}
