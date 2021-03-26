package annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//since annotation is called at runtime
@Retention(RetentionPolicy.RUNTIME)
//since annotation is applied on fields
@Target(ElementType.FIELD)
public @interface Column {
}
