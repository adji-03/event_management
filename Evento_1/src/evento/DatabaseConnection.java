package evento;

import java.sql.*;

import javax.swing.table.DefaultTableModel;
import static jdk.nashorn.internal.runtime.Debug.id;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/event_management"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 
    private int id;
    private String userId;

    // Méthode pour établir la connexion
    public Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL non trouvé", e);
        }
    }

    // Ajouter un utilisateur (sécurisé)
    public void addUser(String name, String email, String password, String role, String username, boolean emailVerified) {
        String query = "INSERT INTO users(name, email, password, username, email_verified, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = this.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password); // ⚠️ À crypter avant d'insérer !
            stmt.setString(4, username);
            stmt.setBoolean(5, emailVerified);
            stmt.setString(6, role);
            stmt.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    // Ajouter un événement (correction)
    public void addEvent(String title, String description, String date, String time, String location, int maxParticipants) {
        String query = "INSERT INTO events(title, description, date, time, location, max_participants) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = this.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, date);
            stmt.setString(4, time);
            stmt.setString(5, location);
            stmt.setInt(6, maxParticipants);
            stmt.executeUpdate();
            System.out.println("Événement ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }

    // Vérifier si un utilisateur existe (sécurisé)
    public boolean isUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = this.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // ⚠️ Devrait être haché et comparé
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Erreur de vérification de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    // Supprimer un événement
    public void deleteEvent(int id) {
        String query = "DELETE FROM events WHERE id = ?";
        try (Connection connection = this.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Événement supprimé avec succès !" : "Événement introuvable !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'événement : " + e.getMessage());
        }
    }

    // Mettre à jour un événement
    public void updateEvent(int id, String title, String description, String date, String time, String location, int maxParticipants) {
        String query = "UPDATE events SET title = ?, description = ?, date = ?, time = ? WHERE id = ?";
        try (Connection connection = this.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, date);
            stmt.setString(4, time);
            stmt.setInt(7, id);

            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Événement mis à jour avec succès !" : "Événement introuvable !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'événement : " + e.getMessage());
        }
    }

    // Afficher les utilisateurs
    public void printUsers() {
        String query = "SELECT * FROM users";
        try (Connection connection = this.connect();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Nom: " + resultSet.getString("name"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Username: " + resultSet.getString("username"));
                System.out.println("Email vérifié: " + resultSet.getBoolean("email_verified"));
                System.out.println("-------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
    }

    // Afficher les événements
    public void printEvents() {
        String query = "SELECT * FROM events";
        try (Connection connection = this.connect();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Titre: " + resultSet.getString("title"));
                System.out.println("Description: " + resultSet.getString("description"));
                System.out.println("Date: " + resultSet.getString("date"));
                System.out.println("Heure: " + resultSet.getString("time"));
                System.out.println("-------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
    }

    void setData(User us) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void printUserData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void showEventsInEventManager(DefaultTableModel t) {
    String query = "SELECT * FROM events";

    try (Connection connection = this.connect();
         Statement stmt = connection.createStatement();
         ResultSet resultSet = stmt.executeQuery(query)) {

        t.setRowCount(0); 
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            String date = resultSet.getString("date");
            String time = resultSet.getString("time");

            if (time == null) {
                time = "Heure non définie"; 
            }

            t.addRow(new Object[]{id, title, description, date, time});
        }

    } catch (SQLException e) {
        System.out.println("❌ Erreur lors de la récupération des événements : " + e.getMessage());
        e.printStackTrace();  // Ajout pour un meilleur débogage
    }
}

void updateEvent(int id, String title, String description, String date, String time) {
    if (!eventExists(id)) {
        System.out.println("⚠️ Aucun événement trouvé avec cet ID !");
        return; // Sortir de la méthode si l'ID n'existe pas
    }

    String query = "UPDATE events SET title = ?, description = ?, date = ?, time = ? WHERE id = ?";

    try (Connection connection = this.connect();
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setString(1, title);
        stmt.setString(2, description);
        stmt.setString(3, date);
        stmt.setString(4, time);
        stmt.setInt(5, id);

        int rows = stmt.executeUpdate();

        System.out.println(rows > 0 ? "✅ Événement mis à jour avec succès !" : "⚠️ Aucun événement trouvé !");
    } catch (SQLException e) {
        System.out.println("❌ Erreur lors de la mise à jour de l'événement : " + e.getMessage());
    }
}


void findAndUpdateEvent(int id, String title, String description, String date, String time) {
    System.out.println("🔎 DEBUG: Recherche de l'événement avec ID: " + id);


    if (eventExists(id)) {
        updateEvent(id, title, description, date, time);
    } else {
        System.out.println("⚠️ Aucun événement trouvé avec cet ID !");
    }
}

void addEvent(String title, String description, String date, String time) {
    String query = "INSERT INTO events (title, description, date, time) VALUES (?, ?, ?, ?)";

    try (Connection connection = this.connect();
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setString(1, title);
        stmt.setString(2, description);
        stmt.setString(3, date);
        stmt.setString(4, time);
        stmt.executeUpdate();

        System.out.println("✅ Événement ajouté avec succès !");
    } catch (SQLException e) {
        System.out.println("❌ Erreur lors de l'ajout de l'événement : " + e.getMessage());
        e.printStackTrace();
    }
}
boolean eventExists(int id) {
    String query = "SELECT id FROM events WHERE id = ?";
    try (Connection connection = this.connect();
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();

        return resultSet.next();  // Retourne true si un résultat est trouvé

    } catch (SQLException e) {
        System.out.println("❌ Erreur lors de la vérification de l'événement : " + e.getMessage());
        return false;
    }
}

    boolean insertUser(String name, String username, String email, String password) {
        String query = "INSERT INTO users (name, username, email, password ) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, email);
            pstmt.setString(4, password);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Utilisateur ajouté avec succès !");
                return true; // ✅ Succès
            } else {
                System.out.println("⚠️ Aucun utilisateur inséré !");
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'insertion de l'utilisateur !");
            e.printStackTrace();
        }

        return false; // ❌ Échec
        
    }


    Connection getConnection() {
        try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        return null;
    }
    }

    public void insertUser(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void updateEvent(String title, String description, String date, String time) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.updateEvent(id, title, description, date, time); //To change body of generated methods, choose Tools | Templates.
    }

    boolean registerUserToEvent(String username, int eventId) {
        try (Connection con = getConnection()) {
        String query = "INSERT INTO registrations (user_id, event_id) VALUES (?, ?)";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, userId);
        stmt.setInt(2, eventId);
        
        int result = stmt.executeUpdate();
        return result > 0;  // Si l'insertion a réussi, return true
    } catch (SQLException e) {
        e.printStackTrace();
        return false;  // Si une erreur se produit, return false
    }
    }

    boolean isUserRegisteredToEvent(String username, int eventId) {
    String query = "SELECT r.id FROM registrations r JOIN users u ON r.user_id = u.id WHERE u.username = ? AND r.event_id = ?";

    try (Connection connection = this.connect();
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setString(1, username);
        stmt.setInt(2, eventId);
        ResultSet resultSet = stmt.executeQuery();

        return resultSet.next(); // ✅ Retourne true si une inscription existe
    } catch (SQLException e) {
        System.out.println("❌ Erreur lors de la vérification de l'inscription : " + e.getMessage());
        return false;
    }
}


    private static class model {

        private static void setRowCount(int i) {
            DefaultTableModel model = new DefaultTableModel();
            model.setRowCount(i);//To change body of generated methods, choose Tools | Templates.
        }

        private static void addRow(Object[] object) {
           DefaultTableModel model = new DefaultTableModel();
           model.addRow(object); //To change body of generated methods, choose Tools | Templates.
        }

        public model() {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Titre");
            model.addColumn("Description");
            model.addColumn("Date");
            model.addColumn("Heure");
            

// Appelez showEventsInEventManager(model) pour remplir le modèle.

        }
    }
 }

