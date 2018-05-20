package top.inotwant.annocation;

import java.lang.annotation.*;

/**
 * AOP 的 切面 注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    Class<? extends Annotation> value();

}
