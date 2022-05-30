package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {

    public static void main(String[] args) {
        // MemberService memberService = new MemberServiceImpl();

        //AppConfig appConfig = new AppConfig();
        // MemberService memberService = appConfig.memberService();
        // MemberServiceImpl 을 직접 할당해준 방식에서 AppConfig 방식으로 변경하였다.

        // 스프링 컨테이너 -> 어노테이션 기반으로 스프링 컨테이너 구성
        // AppConfig에 있는 @Bean 붙은 얘들을 객체로 생성해서 스프링 컨테이너에 담음
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);

        System.out.println("new member = " + member.getName());
        System.out.println("find member = " + findMember.getName());
    }

}
