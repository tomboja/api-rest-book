package cs599.edu.miu.boja.qr.api.books.util;

import cs599.edu.miu.boja.qr.api.books.domain.entity.BookEntity;

import java.util.List;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 8/16/25
 */

public class BookEntityDataSeedUtil {
    // Let's have a static method to seed some book data that generates a list of 10 BookEntity objects
    // Use Builder pattern to create BookEntity objects

    public static List<BookEntity> seedBookEntities() {
        return List.of(
                BookEntity.builder().isbn13("978-3-16-148410-0").title("Effective Java").author("Joshua Bloch").publisher("Addison-Wesley").publicationYear("2018").genre("Programming").language("English").description("A comprehensive guide to best practices in Java programming.").build(),
                BookEntity.builder().isbn13("978-0-321-35668-0").title("Clean Code").author("Robert C. Martin").publisher("Prentice Hall").publicationYear("2008").genre("Software Engineering").language("English").description("A handbook of agile software craftsmanship.").build(),
                BookEntity.builder().isbn13("978-0-201-53082-7").title("Design Patterns: Elements of Reusable Object-Oriented Software").author("Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides").publisher("Addison-Wesley Professional").publicationYear("1994").genre("Software Design").language("English").description("A catalog of common design patterns in object-oriented software development.").build(),
                BookEntity.builder().isbn13("978-1-491-94728-2").title("Java Concurrency in Practice").author("Brian Goetz").publisher("Addison-Wesley").publicationYear("2006").genre("Programming").language("English").description("A practical guide to using concurrency in Java applications.").build(),
                BookEntity.builder().isbn13("978-0-596-52068-7").title("Head First Design Patterns").author("Eric Freeman, Bert Bates, Kathy Sierra, Elisabeth Robson").publisher("O'Reilly Media").publicationYear("2004").genre("Software Design").language("English").description("A visually rich guide to design patterns in Java.").build(),
                BookEntity.builder().isbn13("978-1-59327-584-6").title("Automate the Boring Stuff with Python").author("Al Sweigart").publisher("No Starch Press").publicationYear("2015").genre("Programming").language("English").description("Practical programming for total beginners.").build(),
                BookEntity.builder().isbn13("978-0-13-235088-4").title("Clean Architecture").author("Robert C. Martin").publisher("Prentice Hall").publicationYear("2017").genre("Software Engineering").language("English").description("A guide to software structure and design.").build(),
                BookEntity.builder().isbn13("978-0-13-468599-1").title("Effective Modern C++").author("Scott Meyers").publisher("O'Reilly Media").publicationYear("2014").genre("Programming").language("English").description("Specific ways to improve your modern C++ code.").build(),
                BookEntity.builder().isbn13("978-0-321-93418-1").title("Refactoring: Improving the Design of Existing Code").author("Martin Fowler").publisher("Addison-Wesley").publicationYear("2018").genre("Software Engineering").language("English").description("Techniques for refactoring codebases.").build(),
                BookEntity.builder().isbn13("978-1-118-06331-6").title("The Pragmatic Programmer").author("Andrew Hunt, David Thomas").publisher("Addison-Wesley").publicationYear("1999").genre("Programming").language("English").description("Classic tips for effective software development.").build()
        );
    }
}
