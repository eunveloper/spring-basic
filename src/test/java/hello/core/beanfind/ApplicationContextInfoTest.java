package hello.core.beanfind;

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        // iter + tab = 자동으로 반복문 생성해줌
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            // soutv = 원하는 변수 출력
            System.out.println("beanDefinitionName = " + beanDefinitionName);
            System.out.println("bean = " + bean);
        }

    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            // 커멘드 + 옵션 + v = 리턴 객체 자동 변수화
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
            // ROLE_APPLICATION : 일반적으로 사용자가 정의한 빈
            // ROLE_INFRASTRUCTURE : 스프링이 내부에서 사용하는 빈
            // 개발자가 직접 애플리케이션 개발을 위해서 만든 빈일때
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("beanDefinitionName = " + beanDefinitionName);
                System.out.println("bean = " + bean);
            }
        }

    }

}
