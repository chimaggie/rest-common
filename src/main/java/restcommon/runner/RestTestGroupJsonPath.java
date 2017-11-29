package restcommon.runner;

import java.lang.annotation.*;

/**
 * Created by maggie on 4/20/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RestTestGroupJsonPath {
    String value();
}
