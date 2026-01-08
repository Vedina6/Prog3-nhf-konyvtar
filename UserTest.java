package classes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserConstructorAndGetters() {
        User user = new User("username", "password");
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
    
    }

    @Test
    public void testUserEquality() {
        User user1 = new User("user1", "pass1");
        User user2 = new User("user1", "pass1");
        User user3 = new User("user3", "pass3");
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    public void testSetUsername() {
        User user = new User("oldUsername", "password123");
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
    }
    @Test
    public void testSetPassword() {
        User user = new User("username", "oldPassword");
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }
    @Test
    public void testToString() {
        
        User user = new User("username", "password123");
        String expected = "User{username='username', password='password123'}";
        assertEquals(expected, user.toString(), "The toString method output is incorrect.");
    }

}
