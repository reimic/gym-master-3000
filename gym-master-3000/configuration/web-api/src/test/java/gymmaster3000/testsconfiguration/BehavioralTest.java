package gymmaster3000.testsconfiguration;

import org.junit.jupiter.api.Test;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Test
public @interface BehavioralTest {

    int value() default 0;

}
