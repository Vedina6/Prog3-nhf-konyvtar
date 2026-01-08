package classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;


//Konyvtar osztaly
public class Library implements Serializable {
    private List<Book> books; 


    public Library() {
        this.books = new ArrayList<>();
    }
//Teszthez szukseges
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }
    public List<User> getUsers() {
        return users;
    }

    //hozzaadas
    public void addBook(Book book) {
        books.add(book);
    }
    //torles
    public void removeBook(Book book) {
        books.remove(book);
    }

    //kereses
    public List<Book> search(String criteria, String value) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            switch (criteria.toLowerCase()) {
                case "title":
                    if (book.getTitle().equalsIgnoreCase(value)) {
                        results.add(book);
                    }
                    break;
                case "author":
                    if (book.getAuthor().equalsIgnoreCase(value)) {
                        results.add(book);
                    }
                    break;
                case "genre":
                    if (book.getGenre().equalsIgnoreCase(value)) {
                        results.add(book);
                    }
                    break;
                case "year":
                    try {
                        int year = Integer.parseInt(value);
                        if (book.getYear() == year) {
                            results.add(book);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid year format.");
                    }
                    break;
            }
        }
        return results;
    }
    

    //Mentes jsonba
    public void saveToFile(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(books, writer);
            System.out.println("Books saved successfully." );
        } catch (IOException e) {
            System.err.println("Error saving books to file: " + e.getMessage());
        }
    }

    //Betoltes jsonbol
    public void loadFromJson(String filename) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filename)) {
            Type bookListType = new TypeToken<List<Book>>() {}.getType();
            books = gson.fromJson(reader, bookListType);
            System.out.println("Books loaded successfully from JSON file.");
        } catch (IOException e) {
            System.err.println("Error loading books from JSON file: " + e.getMessage());
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    private List<Order> orders = new ArrayList<>();

    //Rendeles hozzaadas
    public void addOrder(Order order) {
        orders.add(order);
        System.out.println("Order added.");
 }

 //Rendeles mentese Jsonba
 public void saveOrdersToJson(String filename) {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) 
        .setPrettyPrinting()
        .create();

    try (Writer writer = new FileWriter(filename)) {
        gson.toJson(orders, writer);
        System.out.println("Orders saved successfully to JSON file.");
    } catch (IOException e) {
        System.err.println("Error saving orders to JSON file: " + e.getMessage());
    }
}
//User adatok kinyerése
    public String getUserDataFromFile(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) { 
                    String storedUsername = parts[0].trim();
                    String password = parts[1].trim();
                    String email = parts[2].trim();
                    String fullName = parts[3].trim();
    
                    if (storedUsername.equals(username)) {
                        return "Username: " + storedUsername + "\n" +
                               "Password: " + password + "\n" +
                               "Email: " + email + "\n" +
                               "Full Name: " + fullName;
                    }
                }
            }
        } catch (IOException e) {
            
        }
        return null; 
    }


//jelszo lekerese
    protected String getAdminPasswordFromFile(String adminUsername) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(adminUsername)) {
                    return parts[1].trim(); 
                }
            }
        } catch (IOException e) {
            //JOptionPane.showMessageDialog(this, "Error reading user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null; 
    }

    

    public List<Order> getOrders() {
        return orders;
}

    @Override
    public String toString() {
        return "Library{" +
                "books=" + books +
                '}';
    }
}
