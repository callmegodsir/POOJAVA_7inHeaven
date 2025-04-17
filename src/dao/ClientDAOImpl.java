package dao;

import config.DatabaseConfig;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOImpl implements ClientDAO {

    @Override
    public List<User> trouverTousClients() {
        List<User> clients = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateurs WHERE role = 'client'";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(mapClient(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération clients: " + e.getMessage());
        }
        return clients;
    }

    @Override
    public void ajouterClient(User client) {
        String sql = "INSERT INTO Utilisateurs (nom_utilisateur, mot_de_passe, role, nom, prenom, email) VALUES (?, ?, 'client', ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getUsername());
            stmt.setString(2, client.getPassword());
            stmt.setString(3, client.getLastName());
            stmt.setString(4, client.getFirstName());
            stmt.setString(5, client.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur ajout client: " + e.getMessage());
        }
    }

    @Override
    public void modifierClient(User client) {
        String sql = "UPDATE Utilisateurs SET nom_utilisateur=?, mot_de_passe=?, nom=?, prenom=?, email=? WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getUsername());
            stmt.setString(2, client.getPassword());
            stmt.setString(3, client.getLastName());
            stmt.setString(4, client.getFirstName());
            stmt.setString(5, client.getEmail());
            stmt.setInt(6, client.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur modification client: " + e.getMessage());
        }
    }

    @Override
    public void supprimerClient(int id) {
        String sql = "DELETE FROM Utilisateurs WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur suppression client: " + e.getMessage());
        }
    }

    private User mapClient(ResultSet rs) throws SQLException {
        User client = new User();
        client.setId(rs.getInt("id"));
        client.setUsername(rs.getString("nom_utilisateur"));
        client.setPassword(rs.getString("mot_de_passe"));
        client.setLastName(rs.getString("nom"));
        client.setFirstName(rs.getString("prenom"));
        client.setEmail(rs.getString("email"));
        return client;
    }
}