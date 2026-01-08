package classes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    public void testBookConstructorAndGetters() {
        Book book = new Book("Test Title", "Test Author", 2022, "Test Genre");
        assertEquals("Test Title", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals(2022, book.getYear());
        assertEquals("Test Genre", book.getGenre());
    }

    @Test
    public void testBookEquality() {
        Book book1 = new Book("Title", "Author", 2022, "Genre");
        Book book2 = new Book("Title", "Author", 2022, "Genre");
        Book book3 = new Book("Different Title", "Author", 2022, "Genre");
        assertEquals(book1, book2);
        assertNotEquals(book1, book3);
    }

    @Test
    public void testBookHashCode() {
        Book book1 = new Book("Title", "Author", 2022, "Genre");
        Book book2 = new Book("Title", "Author", 2022, "Genre");
        assertEquals(book1.hashCode(), book2.hashCode());
    }
    @Test
    public void testSetTitle() {
        Book book = new Book("Old Title", "Author", 2020, "Genre");
        book.setTitle("New Title");
        assertEquals("New Title", book.getTitle());
    }

    @Test
    public void testSetAuthor() {
        Book book = new Book("Title", "Old Author", 2020, "Genre");
        book.setAuthor("New Author");
        assertEquals("New Author", book.getAuthor());
    }

    @Test
    public void testSetYear() {
        Book book = new Book("Title", "Author", 2020, "Genre");
        book.setYear(2022);
        assertEquals(2022, book.getYear());
    }

    @Test
    public void testSetGenre() {
        Book book = new Book("Title", "Author", 2020, "Old Genre");
        book.setGenre("New Genre");
        assertEquals("New Genre", book.getGenre());
    }
} 

