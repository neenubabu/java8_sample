/**
 * Created by S416995 on 03/07/2017.
 */
@Hint("hint1")
@Hint("hint2")
class Person {
    String firstName;
    String lastName;

    Person() {}

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}