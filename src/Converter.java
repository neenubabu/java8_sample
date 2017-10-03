/**
 * Created by S416995 on 03/07/2017.
 */

@FunctionalInterface
interface Converter<F, T> {
    T convert(F source);
}