import sun.text.resources.cldr.uz.FormatData_uz_Latn;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by S416995 on 03/07/2017.
 */
public class BasicJava8 {

    static int outerStaticNum;
    int outerNum;

    public static void main(String[] args){
        List<String> names  = new ArrayList<String>();
        names.add("neenu");
        names.add("jemil");
        names.add("nithin");

        Collections.sort(names,(a,b)->b.compareTo(a));
        names.forEach(k -> System.out.print(k));

        defaultMethodsForInterfaces();
        functionalInterfaces();
        assessingLambaScopes();
        assessingFieldAndStaticVariables();

        //Built-in Functional Interfaces
        predicates();
        functions();
        consumers();
        comparators();
        optionals();
        streams();
        //parallelStreams();
        map();
        dateApi();
        annotations();


    }

    private static void annotations() {
        Hint hint = Person.class.getAnnotation(Hint.class);
        System.out.println("annotation of person class: "+hint);                   // null

        Hints hints1 = Person.class.getAnnotation(Hints.class);
        System.out.println("length annotation of person class: "+hints1);  // 2

        Hint[] hints2 = Person.class.getAnnotationsByType(Hint.class);
        System.out.println("length annotation of person class: "+hints2.length);  // 2

    }

    private static void dateApi() {
        //Clocks are aware of a timezone and may be used instead of System.currentTimeMillis() to retrieve the current milliseconds.
        // Such an instantaneous point on the time-line is also represented by the class Instant.
        // Instants can be used to create legacy java.util.Date objects.
        Clock clock = Clock.systemDefaultZone();
        System.out.println("clock "+clock);
        long millis = clock.millis();
        System.out.println("millis from clock "+millis);


        Instant instant = clock.instant();
        System.out.println("instant from clock "+instant);

        Date legacyDate = Date.from(instant);
        System.out.println("legacyDate from instant "+legacyDate);


        //Timezones are represented by a ZoneId. They can easily be accessed via static factory methods.
        //Timezones define the offsets which are important to convert between instants and local dates and times.
        System.out.println(ZoneId.getAvailableZoneIds());
        // prints all available timezone ids

        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");
        System.out.println("Europe/Berlin : " +zone1.getRules());
        System.out.println("Brazil/East : " +zone2.getRules());

        //LocalTime represents a time without a timezone, e.g. 10pm or 17:30:15.
        //The following example creates two local times for the timezones defined above.
        //Then we compare both times and calculate the difference in hours and minutes between both times.

        LocalTime now1 = LocalTime.now(zone1);
        LocalTime now2 = LocalTime.now(zone2);

        System.out.println(zone1 +" is before  " +  zone2 +" "+ now1.isBefore(now2));  // false

        long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
        long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

        System.out.println(zone1 +" hour diff from  " +  zone2 + hoursBetween);       // -3
        System.out.println(zone1 +" mins diff from  " +  zone2 +minutesBetween);     // -239

        //LocalTime comes with various factory method to simplify the creation of new instances, including parsing of time strings.
        LocalTime late = LocalTime.of(23, 59, 59);
        System.out.println("local time :"+late);       // 23:59:59

        DateTimeFormatter germanFormatter =
                DateTimeFormatter
                        .ofLocalizedTime(FormatStyle.SHORT)
                        .withLocale(Locale.GERMAN);

        LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
        System.out.println("local tine with german parser : "+leetTime);   // 13:37

        //LocalDateTime represents a date-time. It combines date and time as seen in the above sections into one instance.
        //LocalDateTime is immutable and works similar to LocalTime and LocalDate.
        // We can utilize methods for retrieving certain fields from a date-time:

        LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

        DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
        System.out.println("dayOfWeek  from localdatetime : "+dayOfWeek);      // WEDNESDAY

        Month month = sylvester.getMonth();
        System.out.println("month  from localdatetime : "+month);          // DECEMBER

        long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
        System.out.println("minute  from localdatetime :" +minuteOfDay);    // 1439


        Instant instant2 = sylvester
                .atZone(ZoneId.systemDefault())
                .toInstant();

        Date legacyDate2 = Date.from(instant2);
        System.out.println("legacyDate from instant converted from localdatetime : "+legacyDate);     // Wed Dec 31 23:59:59 CET 2014

        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("MMM dd, yyyy - HH:mm");

        LocalDateTime parsed = LocalDateTime.parse("Nov 03, 2014 - 07:13", formatter);
        System.out.println("LocalDateTime from string by formatter : "+parsed);     // Wed Dec 31 23:59:59 CET 2014

        String string = formatter.format(parsed);
        System.out.println("parser date : "
                + string);     // Nov 03, 2014 - 07:13
    }

    private static void map() {
        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);
        }

        map.forEach((id, val) -> System.out.println(val));
        map.computeIfPresent(3, (num, val) -> val + num);
        System.out.println("compute if present" + map.get(3));

        // val33

        map.computeIfPresent(9, (num, val) -> null);
        System.out.println("map.containsKey "+map.containsKey(9));     // false
        System.out.println("compute if present" + map.get(9));


        map.computeIfAbsent(23, num -> "val" + num);
        System.out.println("map.containsKey "+map.containsKey(23));     // false
        System.out.println("compute if absent" + map.get(23));

        map.computeIfAbsent(3, num -> "bam");
        System.out.println("map.containsKey "+map.containsKey(3));     // false
        System.out.println("compute if absent" + map.get(3));
        //Next, we learn how to remove entries for a a given key, only if it's currently mapped to a given value:
        map.remove(3, "val3");
        map.get(3);             // val33
        System.out.println("map.remove when value dint match" + map.get(3));


        map.remove(3, "val33");
        map.get(3);             // null
        System.out.println("map.remove when value  matched" + map.get(3));

        System.out.println( " map.getOrDefault for 43" + map.getOrDefault(42, "not found"));
        System.out.println( " map.getOrDefault for 5" + map.getOrDefault(5, "not found"));

    /*Merge either put the key/value into the map if no entry for the key exists,
     or the merging function will be called to change the existing value.*/
        System.out.println( "before map.merge for 9 " + map.get(9));

        map.merge(9, "val9", (value, newValue) -> value.concat(newValue));
        System.out.println( " map.merge for 9 " + map.get(9));

        map.merge(9, "concat", (value, newValue) -> value.concat(newValue));
        System.out.println( " map.merge for 9 " + map.get(9));


    }

    private static void parallelStreams() {
        int max = 1000000;
        List<String> values = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        sequentialSort(values);
        long t0 = System.nanoTime();

        long count = values.parallelStream().sorted().count();
        System.out.println(count);

        long t1 = System.nanoTime();

        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("parallel sort took: %d ms", millis));
    }

    private static void sequentialSort(List<String> values) {
        long t0 = System.nanoTime();

        long count = values.stream().sorted().count();
        System.out.println(count);

        long t1 = System.nanoTime();

        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("sequential sort took: %d ms", millis));
    }

    private static void streams() {
        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");
        /*Filter accepts a predicate to filter all elements of the stream.
        This operation is intermediate which enables us to call another stream operation (forEach) on the result.*/
        /* Sorted is an intermediate operation which returns a sorted view of the stream.
        The elements are sorted in natural order unless you pass a custom comparator*/
        stringCollection
                .stream()
                .sorted()
                .filter((s) -> s.startsWith("a"))
                .forEach(System.out::println);
        /*Keep in mind that sorted does only create a sorted view of the stream without manipulating the ordering of the backed collection.
        The ordering of stringCollection is untouched:*/
        List<String> intCollection = new ArrayList<>();
        intCollection.add("001");
        intCollection.add("010");
        intCollection.add("111");
        intCollection.add("223");
        intCollection.add("020");
        intCollection.add("009");
        intCollection.add("090");
        intCollection.add("070");
        //The intermediate operation map converts each element into another object via the given function.
        intCollection
                .stream()
                .map(Integer::parseInt)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(System.out::println);

        /*Match:Various matching operations can be used to check whether a certain predicate matches the stream.
        All of those operations are terminal and return a boolean result*/
        boolean anyStartsWithA =
                stringCollection
                        .stream().anyMatch((s)->s.startsWith("a"));

        System.out.println("anyStartsWithA " +anyStartsWithA);      // true

        boolean allStartsWithA =
                stringCollection
                        .stream()
                        .allMatch((s) -> s.startsWith("a"));

        System.out.println("allStartsWithA "+allStartsWithA);      // false

        boolean noneStartsWithZ =
                stringCollection
                        .stream()
                        .noneMatch((s) -> s.startsWith("z"));

        System.out.println("noneStartsWithZ " +noneStartsWithZ);
        //Count is a terminal operation returning the number of elements in the stream as a long.
        long count =
                stringCollection
                        .stream()
                        .filter((s) -> s.startsWith("b"))
                        .count();
        System.out.println("StartsWith b count " +count);

        //reduce operation performs a reduction on the elements of the stream with the given function.
        // The result is an Optional holding the reduced value.
        Optional<String> reduced = stringCollection
                .stream().reduce((s1, s2) -> s1 + "#" + s2);
        reduced.ifPresent(System.out::println);
    }

    private static void optionals() {
        /*Optional is a simple container for a value which may be null or non-null.
        Think of a method which may return a non-null result but sometimes return nothing.
         Instead of returning null you return an Optional in Java 8.*/
        Optional<String> optional = Optional.of("bam");
        optional.isPresent();           // true
        optional.get();                 // "bam"
        optional.orElse("fallback");    // "bam"

        optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
    }

    private static void comparators() {

        Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

        Person p1 = new Person("John", "Doe");
        Person p2 = new Person("Alice", "Wonderland");

        System.out.println("comparator.compare " +comparator.compare(p1, p2));             // > 0
        System.out.println("comparator.reversed().compare "+ comparator.reversed().compare(p1, p2));  // < 0
    }

    private static void consumers() {
        /*Consumers represents operations to be performed on a single input argument.*/
        List<String> names  = new ArrayList<String>();
        names.add("neenu");
        names.add("jemil");
        names.add("nithin");
        Consumer<String> consumer= i-> System.out.println("/"+i);
        printList(names, consumer);
    }

    private static void functions() {
        /*Functions accept one argument and produce a result.*/
        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);

        /*Default methods can be used to chain multiple functions together (compose, andThen).*/
        System.out.println("functions toInteger :"+ toInteger.apply("0456"));
        System.out.println("functions toInteger  and back to string:"+ backToString.apply("0123"));

    }

    private static void predicates() {
        /*Predicates are boolean-valued functions of one argument.
        The interface contains various default methods for composing predicates to complex logical terms (and, or, negate)*/
        Predicate<String> predicate = (s) -> s.length() > 0;
        System.out.println("predicate.test :"  +predicate.test("foo"));
        System.out.println("predicate.negate().test :"  +predicate.negate().test("foo"));

        Predicate<Boolean> nonNull = Objects::nonNull;
        System.out.println("nonNull.test :"  +nonNull.test(null));
        System.out.println("nonNull.negate().test :"  +nonNull.negate().test(null));
        Predicate<Boolean> isNull = Objects::isNull;
        System.out.println("isNull.test :"  +isNull.test(null));
        System.out.println("isNull.negate().test :"  +isNull.negate().test(null));

        Predicate<String> isEmpty = String::isEmpty;
        System.out.println("isEmpty.test :"  +isEmpty.test(""));
        System.out.println("isEmpty.negate().test :"  +isEmpty.negate().test(""));
        Predicate<String> isNotEmpty = isEmpty.negate();
        System.out.println("isNotEmpty.test :"  +isNotEmpty.test(""));
        System.out.println("isNotEmpty.negate().test :"  +isNotEmpty.negate().test(""));

        Predicate<List> isListEmpty = List::isEmpty;
        List newList = new ArrayList();
        System.out.println("isListEmpty.test :"  +isListEmpty.test(newList));
        System.out.println("isListEmpty.negate().test :"  +isListEmpty.negate().test(newList));

    }

    private static void defaultMethodsForInterfaces() {
        /*Java 8 enables us to add non-abstract method implementations to interfaces by utilizing the default keyword.
         This feature is also known as Extension Methods*/
        /*Besides the abstract method calculate the interface Formula also defines the default method sqrt.
         Concrete classes only have to implement the abstract method calculate.
         The default method sqrt can be used out of the box.*/
        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a);
            }
     };

        System.out.println("default method Extenision : " +formula.calculate(100));     // 100.0
        System.out.println("default method : " + formula.sqrt(100));
        //Default methods cannot be accessed from within lambda expressions. The following code does not compile:
       // Formula formula = (a) -> sqrt( a * 100);


    }

    private static void assessingFieldAndStaticVariables() {

        // this will not compile as assigning values to static variable is not possible in lambda

        //Converter<Integer, String> stringConverter1 = (from) -> {

        //outerNum = 23;

        //return String.valueOf(from);

        //};
        /* this is ok */
        Converter<Integer, String> stringConverter2 = (from) -> {
            outerStaticNum = 72;
            return String.valueOf(from);
        };
    }

    private static void functionalInterfaces() {
    /* to accept any  value and do the action: functional interface
     * We can use arbitrary interfaces as lambda expressions as long as the interface only contains one abstract method.
     * To ensure that your interface meet the requirements, you should add the @FunctionalInterface annotation.
     * The compiler is aware of this annotation and throws a compiler error as soon as you try to add a second abstract method declaration to the interface. */
        Converter<String,Integer> converter = (a)-> Integer.parseInt(a);
        Integer converted = converter.convert("123");
        System.out.println("functional interface converter :" +converted);


        /*The above example code can be further simplified by utilizing static method reference*/
        Converter<String, Integer> intConverter = Integer::valueOf;
        Integer integer= converter.convert("123");
        System.out.println("Static method reference converter :" + +integer);
        /*Java 8 enables you to pass references of methods or constructors via the :: keyword.
         The above example shows how to reference a static method. But we can also reference object methods:*/
        Something something = new Something();
        Converter<String, String> objectMethodConverter = something::startsWith;
        String begins =  objectMethodConverter.convert("JAVA");
        System.out.println("Object  method reference converter :" +begins);

       /* Let's see how the :: keyword works for constructors.*/
        PersonFactory<Person> personFactory = Person::new;
        Person person =  personFactory.create("JAVA","WORLD");
        System.out.println("Person created with constructor :: reference :" +person.firstName + " "+ person.lastName);
    }



    private static void assessingLambaScopes() {
    /*We can read final local variables from the outer scope of lambda expressions*/
        final int num = 1;
        Converter<Integer, String> stringConverter =
                (from) -> String.valueOf(from + num);

        System.out.println("final converted value is : " + stringConverter.convert(2));
        //But different to anonymous objects the variable num does not have to be declared final. This code is also valid.
        //Writing to num from within the lambda expression is also prohibited.
        int nonFinal = 1;
        Converter<Integer, String> stringConverter2 =
                (from) -> String.valueOf(from + nonFinal);

        System.out.println("non final converted value is : " + stringConverter2.convert(2));
    }

    public static void printList(List<String> listOfStrings, Consumer<String> consumer){
        for(String string:listOfStrings){
            consumer.accept(string);
        }
    }
}
