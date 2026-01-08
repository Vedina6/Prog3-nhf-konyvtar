package classes;


import java.time.LocalDate;
import java.util.Objects;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


//Rendeles osztaly
public class Order {
    private User user;
    private Book book;
    private LocalDate orderDate;

    public Order(User user, Book book, LocalDate orderDate) {
        this.user = user;
        this.book = book;
        this.orderDate = orderDate;
    }
//Getterek
    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }
//Setterek
    public void setUser(User user) {
        this.user = user;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
            return Objects.equals(user, order.user) &&
            Objects.equals(book, order.book) &&
            Objects.equals(orderDate, order.orderDate);
}

    @Override
    public int hashCode() {
        return Objects.hash(user, book, orderDate);
}

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user +
                ", book=" + book +
                ", orderDate=" + orderDate +
                '}';
    }

    
}
