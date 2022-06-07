package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTest {

    @Test
    void autowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    static class TestBean {

        // 호출 안됨
        @Autowired(required = false)    // 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
        public void setNoBean1(Member member) {
            System.out.println("setNoBean1  = " + member);
        }

        // null 호출
        @Autowired
        public void setNoBean2(@Nullable Member member) {
            // 자동 주입할 대상이 없으면 null이 입력된다.
            System.out.println("setNoBean2 = " + member);
        }

        //  Optional.empty 호출
        @Autowired(required = false)
        public void setNoBean3(Optional<Member> member) {
            // 자동 주입할 대상이 없으면 Optional.empty가 입력된다.
            System.out.println("setNoBean3 = " + member);
        }

    }

}
