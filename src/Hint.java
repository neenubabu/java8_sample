import java.lang.annotation.Repeatable;

/**
 * Created by S416995 on 03/07/2017.
 */
@Repeatable(Hints.class)
@interface Hint {
    String value();
}