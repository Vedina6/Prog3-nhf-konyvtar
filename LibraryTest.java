package classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

public class LibraryTest {

    private Library library;

    @BeforeEach
    public void setUp() {
        library = new Library();
        library.addBook(new Book("Book 1", "Author 1", 2020, "Genre 1"));
        library.addBook(new Book("Book 2", "Author 2", 2021, "Genre 2"));
    }

    @Test
    public void testAddBook() {
        Book book = new Book("Test Title", "Test Author", 2022, "Test Genre");
        library.addBook(book);
        assertEquals(3, library.getBooks().size());
        assertTrue(library.getBooks().contains(book));
    }

    @Test
    public void testRemoveBook() {
        Book book = new Book("Test Title", "Test Author", 2022, "Test Genre");
        library.addBook(book);
        library.removeBook(book);
        assertEquals(2, library.getBooks().size());
    }

    @Test
    public void testSearchBookByTitle() {
        Book book1 = new Book("Apple", "Author1", 2020, "Scifi");
        Book book2 = new Book("Glass", "Author2", 2021, "Romance");
        library.addBook(book1);
        library.addBook(book2);

        List<Book> results = library.search("title", "Glass");
        assertEquals(1, results.size());
    }

      @Test
    public void testSaveToFile() throws IOException {
        Path tempFile = Files.createTempFile("books", ".json");
        library.saveToFile(tempFile.toString());
        assertTrue(Files.exists(tempFile));
        Gson gson = new Gson();
        try (Reader reader = new FileReader(tempFile.toFile())) {
            Type bookListType = new TypeToken<List<Book>>() {}.getType();
            List<Book> books = gson.fromJson(reader, bookListType);

            assertEquals(2, books.size());
            assertEquals("Book 1", books.get(0).getTitle());
            assertEquals("Book 2", books.get(1).getTitle());
        }
        Files.delete(tempFile); 
}

    @Test
    public void testLoadFromJson() throws IOException {
        Path tempFile = Files.createTempFile("booksJson", ".json");
        try (Writer writer = new FileWriter(tempFile.toFile())) {
            writer.write("[{\"title\":\"Book 4\",\"author\":\"Author 4\",\"year\":2018,\"genre\":\"Genre 4\"}]");
        }

        library.loadFromJson(tempFile.toString());

        assertEquals(1, library.getBooks().size());
        assertEquals("Book 4", library.getBooks().get(0).getTitle());

        Files.delete(tempFile); 
    }

    @Test
    public void libraryInitTest() {
        Library library = new Library();
        assertNotNull(library, "Library initialization failed!");
        assertTrue(library.getBooks().isEmpty(), "Library should start with no books.");
}
}


