package hello.core.scope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = applicationContext.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = applicationContext.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2 .getCount()).isEqualTo(1);
        // 일반 프로토타입 빈만 쓰면 호출시마다 새로운 인스턴스를 생성하기 때문에
        // 필드로 선언되어 있는 count 도 매번 0으로 초기화 된 인스턴스를 사용하게 된다.
        // 그래서 계속 1로 되는것!
    }

    @Test
    void singletonClientUsePrototype() {
        // clientBean 은 싱글톤이므로, 보통 스프링 컨테이너 생성 시점에 함께 생성되고, 의존관계 주입도 발생한다.
        // 1. clientBean 은 의존관계 자동 주입을 사용한다. 주입 시점에 스프링 컨테이너에 프로토타입 빈을 요청한다.
        // 2. 스프링 컨테이너는 프로토타입 빈을 생성해서 clientBean 에 반환한다. 프로토타입 빈의 count 필드 값은 0이다.
        // 이제 clientBean 은 프로토타입 빈을 내부 필드에 보관한다. (정확히는 참조값을 보관한다.)
        // 클라이언트 A는 clientBean 을 스프링 컨테이너에 요청해서 받는다.싱글톤이므로 항상 같은 clientBean 이 반환된다.
        // 3. 클라이언트 A는 clientBean.logic() 을 호출한다.
        // 4. clientBean 은 prototypeBean의 addCount() 를 호출해서 프로토타입 빈의 count를 증가한다. count 값이 1이 된다.
        // 클라이언트 B는 clientBean 을 스프링 컨테이너에 요청해서 받는다.싱글톤이므로 항상 같은 clientBean 이 반환된다.
        // 여기서 중요한 점이 있는데, clientBean 이 내부에 가지고 있는 프로토타입 빈은 이미 과거에 주입이 끝난 빈이다.
        // 주입 시점에 스프링 컨테이너에 요청해서 프로토타입 빈이 새로 생성이 된 것이지, 사용 할 때마다 새로 생성되는 것이 아니다!
        // 5. 클라이언트 B는 clientBean.logic() 을 호출한다.
        // 6. clientBean 은 prototypeBean의 addCount() 를 호출해서 프로토타입 빈의 count를 증가한다.
        // 원래 count 값이 1이었으므로 2가 된다.
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = applicationContext.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);
        ClientBean clientBean2 = applicationContext.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);

    }

    @Scope("singleton")
    static class ClientBean {
        // 스프링 빈이 등록될 때, PrototypeBean 을 호출하여 할당한다.
        // 이때 주입이 된 후에 싱글톤으로 생성된 ClientBean 을 사용하기 때문에 프로토타입 이어도 동일한 빈을 사용하는것.
        // private final PrototypeBean prototypeBean;

        // application context 기능중 DL(Dependency Lookup) 기능만 추출해서 사용하는 클래스
        // ObjectFactory: 기능이 단순, 별도의 라이브러리 필요 없음, 스프링에 의존
        // ObjectProvider: ObjectFactory 상속, 옵션, 스트림 처리등 편의 기능이 많고, 별도의 라이브러리 필요 없음,
        // 스프링에 의존
        // @Autowired
        // private ObjectProvider<PrototypeBean> prototypeBeanProvider;
        // @Autowired
        // private ObjectFactory<PrototypeBean> prototypeBeanFactory;

        // 스프링에 의존하지 않고 자바 표준임. 다른 컨테이너에서도 사용할 수 있다.
        // 별도의 라이브러리가 필요함
        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        /* @Autowired
        ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }*/

        public int logic() {
            // PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("init + " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("destroy");
        }
    }

}
