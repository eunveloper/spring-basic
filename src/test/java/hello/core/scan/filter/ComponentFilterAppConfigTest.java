package hello.core.scan.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

public class ComponentFilterAppConfigTest {

    @Test
    void filterScan() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);

        BeanA beanA = ac.getBean("beanA", BeanA.class);
        Assertions.assertNotNull(beanA);

        Assertions.assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> ac.getBean("beanB", BeanB.class));

    }

    @Configuration
    @ComponentScan(
            /* FilterType 옵션
             1. ANNOTATION: 기본값, 애노테이션을 인식해서 동작한다.
                ex) org.example.SomeAnnotation
             2. ASSIGNABLE_TYPE: 지정한 타입과 자식 타입을 인식해서 동작한다.
                ex) org.example.SomeClass
             3. ASPECTJ: AspectJ 패턴 사용
                ex) org.example..*Service+
             4. REGEX: 정규 표현식
                ex) org\.example\.Default.*
             5. CUSTOM: TypeFilter 이라는 인터페이스를 구현해서 처리
                ex) org.example.MyTypeFilter */
            includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
            excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
    )
    static class ComponentFilterAppConfig {

    }
}
