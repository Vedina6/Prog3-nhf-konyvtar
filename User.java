package classes;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


//User osztaly
public class User implements Serializable{  
    private String username;
    private String password;


    public User(String username, String password) { 
        this.username = username;
        this.password = password;
      
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//bejelentkezes ellenorzes
    public boolean authenticate(String username, String password) {   
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    User user = (User) obj;
    return username.equals(user.username);
}


    @Override
    public int hashCode() {
        return Objects.hash(username, password);
}


    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
}
}



