package hello.core.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("mainDiscountPolicy")
// @Qualifier("mainDiscountPolicy") 문자를 바로 적으면 컴파일 시 타입 체크가 안된다.
// 그럴때 어노테이션을 만들어서 문제를 해결할 수 있다.
public @interface MainDiscountPolicy {

    /* 사용하고 싶은 곳에 @MainDiscountPolicy 달면 된다!
     기존에 @Qualifier 어노테이션 달던 곳에!
     즉 생성하는 곳과 사용하는 곳 두곳에 추가하여 사용하면 된다. */

    /* 애노테이션에는 상속이라는 개념이 없다.
     이렇게 여러 애노테이션을 모아서 사용하는 기능은 스프링이 지원해주는 기능이다.
     @Qulifier 뿐만 아니라 다른 애노테이션들도 함께 조합해서 사용할 수 있다.
     단적으로 @Autowired도 재정의 할 수 있다.
     물론 스프링이 제공하는 기능을 뚜렷한 목적 없이 무분별하게 재정의 하는 것은 유지보수에 더 혼란만 가중할 수 있다. */

}
