package gymmaster3000.member.domain.valueobject;

import java.lang.annotation.*;

/**
 * Custom annotation highlighting that the annotated object validates itself upon creation.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SelfValidating {

}
