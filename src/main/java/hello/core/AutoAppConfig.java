package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
// @Component 붙은 클래스를 스캔하여 스프링 빈으로 등록한다.
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        // includeFilters : 컴포넌트 스캔 대상을 추가로 지정
        // excludeFilters : 컴포넌트 스캔에서 제외할 대상을 지정
        classes = Configuration.class),
        // @Configuration 이 붙은 설정 정보도 빈으로 등록되기 때문에 (내부에서 @Component 가 있기 때)
        // excludeFilters 를 이용하여 스캔 대상에서 임시로 제거했다 (AppConfig, TestConfig 때문에)
        basePackages = "hello.core"
        // 탐색할 패키지의 시작 위치를 지정. 이 패키지를 포함해서 하위 패키지를 모두 탐색
        // 만약 지정하지 않으면 @ComponentScan이 붙은 설정 정보 클래스의 패키지가 시작 위치가 됨.
        /* 권장하는 방법
         패키지 위치를 지정하지 않고, 설정 정보 클래스의 위치는 프로젝트 최상단에 두는것.
         최근 스프링 부트도 이 방법을 기본으로 제공
         스프링 부투를 사용하면 프로젝트 시작 루트 위치에 기본으로 만들어지는 시작 정보에 적힌
         @SpringBootApplication 내에 @ComponentScan 이 있음 */
)
// Component Scan 기본대상
// 1. @Component : 컴포넌트 스캔에서 사용
// 2. @Controller : 스프링 MVC 컨트롤러에서 사용
// 3. @Service : 스프링 비즈니스 로직에서 사용
// 4. @Repository : 스프링 데이터 접근 계층에서 사용
// 5. @Configuration : 스프링 설정 정보에서 사용
public class AutoAppConfig {

    @Bean(name = "memoryMemberRepository")
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

}
