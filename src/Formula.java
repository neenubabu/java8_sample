/**
 * Created by S416995 on 03/07/2017.
 */
interface Formula {
    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}