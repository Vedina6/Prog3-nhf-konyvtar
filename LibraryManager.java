package classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class LibraryManager extends JFrame {
    private Library library; 
    private JTable bookTable; 
    private DefaultTableModel tableModel; 
    private User currentUser; 

    public LibraryManager() {
        //Ablak bezarasa
        boolean loginSuccessful = showLoginDialog();
        if (!loginSuccessful) {
            UIManager.put("Panel.background", new Color(163, 182, 196));
            UIManager.put("OptionPane.background", new Color(163, 182, 196));
            JOptionPane.showMessageDialog(this, "Login required.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }


        library = new Library();
        //Kulso panel
        setTitle("Library Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        //Szin
        getContentPane().setBackground(new Color(80, 100, 147));

        //Felirat
        JLabel titleLabel = new JLabel("Library Manager", JLabel.CENTER);
        titleLabel.setFont(new Font("Rockwell", Font.BOLD, 46));
        titleLabel.setForeground(new Color(255, 255, 255));
        add(titleLabel, BorderLayout.NORTH);

        //Menu
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        setupBookTable();
        add(createMainPanel(), BorderLayout.CENTER);

        setVisible(true);
    }
//Login ablak
    protected boolean showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(400, 250);
        UIManager.put("Panel.background", new Color(163, 182, 196));
        UIManager.put("OptionPane.background", new Color(163, 182, 196));
        loginDialog.setLayout(new GridLayout(4, 1));
        loginDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Rockwell", Font.BOLD, 14));
        userLabel.setForeground(new Color(124, 132, 172));
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Rockwell", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(124, 132, 172));
        JPasswordField passwordField = new JPasswordField();
    
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(124, 132, 172));
        loginButton.setForeground(Color.BLACK); 
        loginButton.setFont(new Font("Rockwell", Font.BOLD, 14)); 
        loginButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        final boolean[] loginSuccessful = {false};
    
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
    
            if (validateLogin(username, password)) {
                currentUser = new User(username, password);
                JOptionPane.showMessageDialog(this, "Welcome, " + username + "!");
                loginSuccessful[0] = true;
                loginDialog.dispose(); 
            } else {
                UIManager.put("Panel.background", new Color(163, 182, 196));
                UIManager.put("OptionPane.background", new Color(163, 182, 196));
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        loginDialog.add(userLabel);
        loginDialog.add(usernameField);
        loginDialog.add(passwordLabel);
        loginDialog.add(passwordField);
        loginDialog.add(loginButton);
    
        loginDialog.setVisible(true);
        return loginSuccessful[0]; 
    }
//Teszthez szukseges
    public Library getLibrary() {
        return library;
}
    public String userFilePath;

    public void setUserFilePath(String filePath) {
        this.userFilePath = filePath;
}
    public void setLibrary(Library library) {
        this.library = library;
}

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
}

    
//Valid felhasznalo ellenorzese

public boolean validateLogin(String username, String password) {
    try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                String storedUsername = parts[0].trim();
                String storedPassword = parts[1].trim();
                if (storedUsername.equals(username) && storedPassword.equals(password)) {
                    return true; 
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error reading users file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return false;
}
    

//Betoltes jsonbol megvalositas
    private void loadLibraryFromJson() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            library.loadFromJson(filename);
    
            tableModel.setRowCount(0);
            for (Book book : library.getBooks()) {
                tableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getYear(), book.getGenre()});
            }

            UIManager.put("Panel.background", new Color(163, 182, 196));
            UIManager.put("OptionPane.background", new Color(163, 182, 196));
            JOptionPane.showMessageDialog(this, "Books loaded successfully from JSON file.");
        }
    }
    
//Menu elemek
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(133, 158, 218));

        JMenu fileMenu = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load from File");
        JMenuItem saveItem = new JMenuItem("Save to JSON");
        JMenuItem loadJsonItem = new JMenuItem("Load from JSON");

        loadJsonItem.addActionListener(e -> loadLibraryFromJson());
        saveItem.addActionListener(e -> saveLibrary());
        loadItem.addActionListener(e -> loadFromTxtFile());

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(loadJsonItem);

        JMenu orderMenu = new JMenu("Order");
        JMenuItem orderTableItem = new JMenuItem("Order Book from the table");
        JMenuItem orderNewItem = new JMenuItem("Order New Book");
        JMenuItem viewOrdersItem = new JMenuItem("View Orders");
        JMenuItem saveOrdersItem = new JMenuItem("Save Orders to JSON");
         

        orderTableItem.addActionListener(e -> orderBookfromTable());
        orderNewItem.addActionListener(e -> orderNewBook());
        viewOrdersItem.addActionListener(e -> showOrdersDialog());
        saveOrdersItem.addActionListener(e -> saveOrdersToJsonFile());

        orderMenu.add(orderTableItem);
        orderMenu.add(orderNewItem);
        orderMenu.add(viewOrdersItem);
        orderMenu.add(saveOrdersItem);


        JMenu booksMenu = new JMenu("Books");
        JMenuItem resetItem = new JMenuItem("Reset Table");
        JMenuItem addBookItem = new JMenuItem("Add Book");
        JMenuItem deleteBookItem = new JMenuItem("Delete Book");
        JMenuItem filterBooksItem = new JMenuItem("Filter Books");
        JMenuItem editBookItem = new JMenuItem("Edit Book");

        addBookItem.addActionListener(e -> addBook());
        resetItem.addActionListener(e -> resetBooksTable());
        deleteBookItem.addActionListener(e -> deleteBook());
        filterBooksItem.addActionListener(e -> filterBooks());
        editBookItem.addActionListener(e -> editBook());

        booksMenu.add(addBookItem);
        booksMenu.add(resetItem);
        booksMenu.add(deleteBookItem);
        booksMenu.add(filterBooksItem);
        booksMenu.add(editBookItem);

        JMenu exitMenu = new JMenu("Exit");
        JMenuItem exitItem = new JMenuItem("Exit now");
        JMenuItem CancelItem = new JMenuItem("Cancel");
        
        exitItem.addActionListener(e -> System.exit(0)); 
        
        exitMenu.add(exitItem);
        exitMenu.add(CancelItem);

        JMenu myAccountMenu = new JMenu("My Account");
        JMenuItem viewAccountItem = new JMenuItem("View Account Details");
        viewAccountItem.addActionListener(e -> viewAccountDetails());
        myAccountMenu.add(viewAccountItem);

        JMenu userManagementMenu = new JMenu("User Management");
        JMenuItem addUserItem = new JMenuItem("Add User");
        JMenuItem deleteUserItem = new JMenuItem("Delete User");
        JMenuItem modifyUserItem = new JMenuItem("Modify User");

        addUserItem.addActionListener(e -> {
            if (verifyAdminPassword()) {
                addUser();
            } else {
                JOptionPane.showMessageDialog(this, "Access Denied: Incorrect admin password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteUserItem.addActionListener(e -> {
            if (verifyAdminPassword()) {
                deleteUser();
            } else {
                JOptionPane.showMessageDialog(this, "Access Denied: Incorrect admin password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        modifyUserItem.addActionListener(e -> {
            if (verifyAdminPassword()) {
                modifyUser();
            } else {
                JOptionPane.showMessageDialog(this, "Access Denied: Incorrect admin password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        if (!isAdmin()) {
            addUserItem.setEnabled(false);
            deleteUserItem.setEnabled(false);
            modifyUserItem.setEnabled(false);
        }

        userManagementMenu.add(addUserItem);
        userManagementMenu.add(deleteUserItem);
        userManagementMenu.add(modifyUserItem);



        menuBar.add(fileMenu);
        menuBar.add(orderMenu);
        menuBar.add(booksMenu);
        menuBar.add(myAccountMenu);
        menuBar.add(userManagementMenu);
        menuBar.add(exitMenu);

        return menuBar;
    }

    
//Konyv tablazat
    private void setupBookTable() {
        String[] columnNames = {"Title", "Author", "Year", "Genre"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);
        bookTable.setBackground(new Color(245, 245, 245)); 
        bookTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bookTable.setRowHeight(30);
        bookTable.setGridColor(Color.LIGHT_GRAY); 


    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
//Konyv hozzaadasa
    protected void addBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField genreField = new JTextField();

        Object[] fields = {
                "Title:", titleField,
                "Author:", authorField,
                "Year:", yearField,
                "Genre:", genreField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Book", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                int year = Integer.parseInt(yearField.getText());
                String genre = genreField.getText();

                Book book = new Book(title, author, year, genre);
                library.addBook(book);
                tableModel.addRow(new Object[]{title, author, year, genre});
            } catch (NumberFormatException e) {
                UIManager.put("Panel.background", new Color(163, 182, 196));
                UIManager.put("OptionPane.background", new Color(163, 182, 196));
                JOptionPane.showMessageDialog(this, "Year must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
//Konyv torlese
    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
    
            String title = (String) tableModel.getValueAt(selectedRow, 0);
            String author = (String) tableModel.getValueAt(selectedRow, 1);
            int year = (int) tableModel.getValueAt(selectedRow, 2);
            String genre = (String) tableModel.getValueAt(selectedRow, 3);
    
            Book bookToRemove = new Book(title, author, year, genre);
    
            library.removeBook(bookToRemove);
    
        
            tableModel.removeRow(selectedRow);
            UIManager.put("Panel.background", new Color(163, 182, 196));
            UIManager.put("OptionPane.background", new Color(163, 182, 196));
            JOptionPane.showMessageDialog(this, "Book deleted successfully.");
        } else {
            UIManager.put("Panel.background", new Color(163, 182, 196));
            UIManager.put("OptionPane.background", new Color(163, 182, 196));
            JOptionPane.showMessageDialog(this, "Please select a book to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
//Szures a konyvekre
    private void filterBooks() {
        String criteria = JOptionPane.showInputDialog(this, "Enter filter criteria (title, author, genre, year):");
        UIManager.put("Panel.background", new Color(163, 182, 196));
        UIManager.put("OptionPane.background", new Color(163, 182, 196));
        if (criteria == null || criteria.isEmpty()) {
            UIManager.put("Panel.background", new Color(163, 182, 196));
            UIManager.put("OptionPane.background", new Color(163, 182, 196));
            JOptionPane.showMessageDialog(this, "No criteria entered. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String value = JOptionPane.showInputDialog(this, "Enter value to filter by:");
        UIManager.put("Panel.background", new Color(163, 182, 196));
        UIManager.put("OptionPane.background", new Color(163, 182, 196));
        if (value == null || value.isEmpty()) {
            UIManager.put("Panel.background", new Color(163, 182, 196));
            UIManager.put("OptionPane.background", new Color(163, 182, 196));
            JOptionPane.showMessageDialog(this, "No value entered. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!criteria.equalsIgnoreCase("title") &&
            !criteria.equalsIgnoreCase("author") &&
            !criteria.equalsIgnoreCase("genre") &&
            !criteria.equalsIgnoreCase("year")) {
            UIManager.put("Panel.background", new Color(163, 182, 196));
            UIManager.put("OptionPane.background", new Color(163, 182, 196));
            JOptionPane.showMessageDialog(this, "Invalid filter criteria. Allowed: title, author, genre, year.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        List<Book> filteredBooks = library.search(criteria, value);
    
        if (filteredBooks.isEmpty()) {
            UIManager.put("Panel.background", new Color(163, 182, 196));
            UIManager.put("OptionPane.background", new Color(163, 182, 196));
            JOptionPane.showMessageDialog(this, "No books found for the given filter criteria.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            tableModel.setRowCount(0);
            for (Book book : filteredBooks) {
                tableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getYear(), book.getGenre()});
            }
    
            int result = JOptionPane.showConfirmDialog(this, "Would you like to reset the table to show all books?", "Reset Table", JOptionPane.YES_NO_OPTION);
            UIManager.put("Panel.background", new Color(163, 182, 196));
            UIManager.put("OptionPane.background", new Color(163, 182, 196));
            if (result == JOptionPane.YES_OPTION) {
                resetBooksTable(); 
            }
        }
    }

    
 //Konyv modositas   
    private void editBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
    
            String title = (String) tableModel.getValueAt(selectedRow, 0);
            String author = (String) tableModel.getValueAt(selectedRow, 1);
            int year = (int) tableModel.getValueAt(selectedRow, 2);
            String genre = (String) tableModel.getValueAt(selectedRow, 3);
    
            Book bookToEdit = new Book(title, author, year, genre);
    
            JTextField titleField = new JTextField(bookToEdit.getTitle());
            JTextField authorField = new JTextField(bookToEdit.getAuthor());
            JTextField yearField = new JTextField(String.valueOf(bookToEdit.getYear()));
            JTextField genreField = new JTextField(bookToEdit.getGenre());
    
            Object[] fields = {
                "Title:", titleField,
                "Author:", authorField,
                "Year:", yearField,
                "Genre:", genreField
            };
    
            int result = JOptionPane.showConfirmDialog(this, fields, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
    
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String newTitle = titleField.getText();
                    String newAuthor = authorField.getText();
                    int newYear = Integer.parseInt(yearField.getText());
                    String newGenre = genreField.getText();
    
                    Book updatedBook = new Book(newTitle, newAuthor, newYear, newGenre);
                    library.removeBook(bookToEdit); 
                    library.addBook(updatedBook); 

                    tableModel.setValueAt(newTitle, selectedRow, 0);
                    tableModel.setValueAt(newAuthor, selectedRow, 1);
                    tableModel.setValueAt(newYear, selectedRow, 2);
                    tableModel.setValueAt(newGenre, selectedRow, 3);
    
                    JOptionPane.showMessageDialog(this, "Book updated successfully!");
                    UIManager.put("Panel.background", new Color(163, 182, 196));
                    UIManager.put("OptionPane.background", new Color(163, 182, 196));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Year must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    UIManager.put("Panel.background", new Color(163, 182, 196));
                    UIManager.put("OptionPane.background", new Color(163, 182, 196));
                }
            }
        } else {
            UIManager.put("Panel.background", new Color(163, 182, 196));
            UIManager.put("OptionPane.background", new Color(163, 182, 196));
            JOptionPane.showMessageDialog(this, "Please select a book to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }


//Rendeles az adatbazis konyveibol
    private void orderBookfromTable() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {

            String title = (String) tableModel.getValueAt(selectedRow, 0);
            String author = (String) tableModel.getValueAt(selectedRow, 1);
            int year = (int) tableModel.getValueAt(selectedRow, 2);
            String genre = (String) tableModel.getValueAt(selectedRow, 3);
    
            Book selectedBook = new Book(title, author, year, genre);
    
            Order newOrder = new Order(currentUser, selectedBook, java.time.LocalDate.now());
            library.addOrder(newOrder);
    
            JOptionPane.showMessageDialog(this, "Order placed successfully!\n" +
                    "User: " + currentUser.getUsername() + "\n" +
                    "Book: " + title + "\n" +
                    "Date: " + newOrder.getOrderDate());
                    UIManager.put("Panel.background", new Color(163, 182, 196));
                    UIManager.put("OptionPane.background", new Color(163, 182, 196));
            
        } else {
            UIManager.put("Panel.background", new Color(163, 182, 196));
            UIManager.put("OptionPane.background", new Color(163, 182, 196));
            JOptionPane.showMessageDialog(this, "Please select a book to order.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
//Uj konyv rendelese
    private void orderNewBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField genreField = new JTextField();
    
        Object[] fields = {
            "Title:", titleField,
            "Author:", authorField,
            "Year:", yearField,
            "Genre:", genreField
        };
    
        int result = JOptionPane.showConfirmDialog(this, fields, "New Book Order", JOptionPane.OK_CANCEL_OPTION);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                int year = Integer.parseInt(yearField.getText());
                String genre = genreField.getText();
    
                Book newBook = new Book(title, author, year, genre);
                Order newOrder = new Order(currentUser, newBook, java.time.LocalDate.now());
                library.addOrder(newOrder);
    
                JOptionPane.showMessageDialog(this, "Order placed successfully for a new book!\n" +
                        "User: " + currentUser.getUsername() + "\n" +
                        "Book: " + title + "\n" +
                        "Date: " + newOrder.getOrderDate());
                        UIManager.put("Panel.background", new Color(163, 182, 196));
                        UIManager.put("OptionPane.background", new Color(163, 182, 196));
                
            } catch (NumberFormatException e) {
                UIManager.put("Panel.background", new Color(163, 182, 196));
                UIManager.put("OptionPane.background", new Color(163, 182, 196));
                JOptionPane.showMessageDialog(this, "Year must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


//Rendelesek mentese jsonba
    private void saveOrdersToJsonFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            library.saveOrdersToJson(filename);
            JOptionPane.showMessageDialog(this, "Orders saved successfully to JSON file.");
        }
    }

//Rendelesek megtekintese
    private void showOrdersDialog() {
        JDialog ordersDialog = new JDialog(this, "View Orders", true);
        ordersDialog.setSize(600, 400);
        ordersDialog.setLayout(new BorderLayout());
    
        String[] columnNames = {"User", "Book Title", "Order Date"};
        DefaultTableModel ordersTableModel = new DefaultTableModel(columnNames, 0);
        JTable ordersTable = new JTable(ordersTableModel);
    
        for (Order order : library.getOrders()) {
            ordersTableModel.addRow(new Object[]{
                    order.getUser().getUsername(),
                    order.getBook().getTitle(),
                    order.getOrderDate()
            });
        }
    
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        ordersDialog.add(scrollPane, BorderLayout.CENTER);
    
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(124, 132, 172));
        closeButton.setForeground(Color.BLACK); 
        closeButton.setFont(new Font("Rockwell", Font.BOLD, 14)); 
        closeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        closeButton.addActionListener(e -> ordersDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
    
        ordersDialog.add(buttonPanel, BorderLayout.SOUTH);
        ordersDialog.setVisible(true);
    }
    
    
//Konyvek betoltese txt-bol
    private void loadFromTxtFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();

            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String title = parts[0];
                        String author = parts[1];
                        int year = Integer.parseInt(parts[2]);
                        String genre = parts[3];
                        Book book = new Book(title, author, year, genre);
                        library.addBook(book);
                        tableModel.addRow(new Object[]{title, author, year, genre});
                    }
                }
                JOptionPane.showMessageDialog(this, "Books loaded successfully.");
            } catch (IOException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


//User adatainak megjelenítése
    private void viewAccountDetails() {
        String username = currentUser.getUsername();
        String userData = library.getUserDataFromFile(username);

        JOptionPane.showMessageDialog(this, userData, "Account Details", JOptionPane.INFORMATION_MESSAGE);
        
    }
//Az admin edina felhasznalo
    private boolean isAdmin() {
        return currentUser != null && currentUser.getUsername().equals("edina");
    }

//Admin jelszava a User Management ful hasznalathoz
protected boolean verifyAdminPassword() {
    JPasswordField passwordField = new JPasswordField();
    int result = JOptionPane.showConfirmDialog(
        this,
        new Object[]{"Enter admin password:", passwordField},
        "Admin Verification",
        JOptionPane.OK_CANCEL_OPTION
    );

    if (result == JOptionPane.OK_OPTION) {
        String enteredPassword = new String(passwordField.getPassword());
        String adminPassword = library.getAdminPasswordFromFile("edina");
        return enteredPassword.equals(adminPassword);
    }
    return false;
}

//Felhasznalo hozzadasa(csak admin)
private void addUser() {
    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JTextField emailField = new JTextField();
    JTextField fullNameField = new JTextField();

    Object[] fields = {
        "Username:", usernameField,
        "Password:", passwordField,
        "Email:", emailField,
        "Full Name:", fullNameField
    };

    int result = JOptionPane.showConfirmDialog(this, fields, "Add User", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String fullName = fullNameField.getText();

        try (FileWriter writer = new FileWriter("users.txt", true)) {
            writer.write( username + "," + password + "," + email + "," + fullName + "\n");
            JOptionPane.showMessageDialog(this, "User added successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

//User torlese
private void deleteUser() {
    String usernameToDelete = JOptionPane.showInputDialog(this, "Enter username to delete:");
    if (usernameToDelete != null) {
        try {
            List<String> users = new ArrayList<>();
            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (!parts[0].equals(usernameToDelete)) {
                        users.add(line); 
                    } else {
                        found = true;}}}

            if (found) {
                try (FileWriter writer = new FileWriter("users.txt")) {
                    for (String user : users) {
                        writer.write(user + "\n");
                    }
                }
                JOptionPane.showMessageDialog(this, "User deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);}}}


//Felhasznalo adatainak modositasa
private void modifyUser() {
    String usernameToModify = JOptionPane.showInputDialog(this, "Enter username to modify:");
    if (usernameToModify != null) {
        try {
            List<String> users = new ArrayList<>();
            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(usernameToModify)) {
                        found = true;

                        // Új adatok bekérése
                        JTextField usernameField = new JTextField(parts[0]);
                        JPasswordField passwordField = new JPasswordField(parts[1]);
                        JTextField emailField = new JTextField(parts[2]);
                        JTextField fullNameField = new JTextField(parts[3]);

                        Object[] fields = {
                            "Username:", usernameField,
                            "Password:", passwordField,
                            "Email:", emailField,
                            "Full Name:", fullNameField
                        };

                        int result = JOptionPane.showConfirmDialog(this, fields, "Modify User", JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            String username = usernameField.getText();
                            String password = new String(passwordField.getPassword());
                            String email = emailField.getText();
                            String fullName = fullNameField.getText();
                            users.add(username + "," + password + "," + email + "," + fullName); 
                        } else {
                            users.add(line);
                        }
                    } else {
                        users.add(line); }}}

            if (found) {
                try (FileWriter writer = new FileWriter("users.txt")) {
                    for (String user : users) {
                        writer.write(user + "\n");
                    }
                }
                JOptionPane.showMessageDialog(this, "User modified successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error modifying user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }}
}

//Tabla visszaallitasa szures utan
    private void resetBooksTable() {
        tableModel.setRowCount(0);
        for (Book book : library.getBooks()) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getYear(), book.getGenre()});
        }
    }
    
//Tabla kimentese(txt-be)
    private void saveLibrary() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            library.saveToFile(filename);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryManager::new);
    }
}
