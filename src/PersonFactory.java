/**
 * Created by S416995 on 03/07/2017.
 */
interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}
