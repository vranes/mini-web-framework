package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Anotacija je dostupna u runtime-u
 */
@Retention(RetentionPolicy.RUNTIME)
/*
 * Mozemo anotirati samo atribute ovom anotacijom
 */
@Target(ElementType.TYPE)
public @interface Controller {
}
