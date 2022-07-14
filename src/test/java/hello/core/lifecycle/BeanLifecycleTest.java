package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifecycleTest {

    @Test
    public void lifeCycleTest() {
        ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = applicationContext.getBean(NetworkClient.class);
        applicationContext.close();
    }

    @Configuration
    static class LifeCycleConfig {
        // 스프링 빈은 간단하게 다음과 같은 라이프사이클을 가진다.
        // 객체 생성 -> 의존관계 주입
        // 스프링 빈은 객체를 생성하고, 의존관계 주입이 다 끝난 다음에야 필요한 데이터를 사용할 수 있는 준비가 완료된다.
        // 따라서 초기화 작업은 의존관계 주입이 모두 완료되고 난 다음에 호출해야 한다.
        // 그런데 개발자가 의존관계 주입이 모두 완료된 시점을 어떻게 알 수 있을까?
        // 스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려주는 다양한 기능을 제공한다.
        // 또한 스프링은 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 준다. 따라서 안전하게 종료 작업을 진행할 수 있다.
        @Bean(/*initMethod = "init"*//*, destroyMethod = "close"*/)
        // 메소드 방식
        // 메서드 이름을 자유롭게 줄 수 있다.
        // 스프링 빈이 스프링 코드에 의존하지 않는다.
        // 코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있다

        // @Bean의 destroyMethod 속성에는 아주 특별한 기능이 있다.
        // 라이브러리는 대부분 close , shutdown 이라는 이름의 종료 메서드를 사용한다.
        // @Bean의 destroyMethod 는 기본값이 (inferred) (추론)으로 등록되어 있다.
        // 이 추론 기능은 close , shutdown 라는 이름의 메서드를 자동으로 호출해준다.
        // 이름 그대로 종료 메서드를 추론해서 호출해준다.
        // 따라서 직접 스프링 빈으로 등록하면 종료 메서드는 따로 적어주지 않아도 잘 동작한다.
        // 추론 기능을 사용하기 싫으면 destroyMethod="" 처럼 빈 공백을 지정하면 된다.
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
        // 스프링 빈의 이벤트 라이프사이클
        // 스프링컨테이너생성 -> 스프링빈생성(생성자 주입 일부는 이때) -> 의존관계주입 -> 초기화콜백 -> 사용 -> 소멸전콜백 -> 스프링 종료
        // 초기화 콜백: 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
        // 소멸전 콜백: 빈이 소멸되기 직전에 호출
    }

}
