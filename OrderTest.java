package classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

public class OrderTest {

     private Library library;

    @BeforeEach
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testAddOrder() {
        User user = new User("testUser", "password");
        Book book = new Book("Test Book", "Test Author", 2022, "Test Genre");
        Order order = new Order(user, book, LocalDate.now());

        library.addOrder(order);

        List<Order> orders = library.getOrders();
        assertEquals(1, orders.size(), "There should be one order in the list.");
        assertEquals(order, orders.get(0), "The added order should match the retrieved order.");
    }

    @Test
    public void testGetOrders() {
        User user1 = new User("user1", "pass1");
        User user2 = new User("user2", "pass2");
        Book book1 = new Book("Book 1", "Author 1", 2020, "Genre 1");
        Book book2 = new Book("Book 2", "Author 2", 2021, "Genre 2");

        Order order1 = new Order(user1, book1, LocalDate.of(2023, 1, 1));
        Order order2 = new Order(user2, book2, LocalDate.of(2023, 2, 1));

        library.addOrder(order1);
        library.addOrder(order2);

        List<Order> orders = library.getOrders();
        assertEquals(2, orders.size(), "The orders list should contain 2 orders.");
        assertEquals(order1, orders.get(0), "The first order should match order1.");
        assertEquals(order2, orders.get(1), "The second order should match order2.");
    }

    @Test
    public void testOrderConstructorAndGetters() {
        User user = new User("testUser", "password123");
        Book book = new Book("Test Title", "Test Author", 2022, "Test Genre");
        LocalDate date = LocalDate.now();

        Order order = new Order(user, book, date);

        assertEquals(user, order.getUser());
        assertEquals(book, order.getBook());
        assertEquals(date, order.getOrderDate());
    }

    @Test
    public void testOrderEquality() {
        User user = new User("testUser", "password123");
        Book book = new Book("Test Title", "Test Author", 2022, "Test Genre");
        LocalDate date = LocalDate.now();

        Order order1 = new Order(user, book, date);
        Order order2 = new Order(user, book, date);
        Order order3 = new Order(new User("otherUser", "pass"), book, date);

        assertEquals(order1, order2);
        assertNotEquals(order1, order3);
    }

    @Test
    public void testOrderHashCode() {
        User user = new User("testUser", "password123");
        Book book = new Book("Test Title", "Test Author", 2022, "Test Genre");
        LocalDate date = LocalDate.now();

        Order order1 = new Order(user, book, date);
        Order order2 = new Order(user, book, date);

        assertEquals(order1.hashCode(), order2.hashCode());
    }
}

