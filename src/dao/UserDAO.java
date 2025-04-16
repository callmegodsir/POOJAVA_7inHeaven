package dao;

import model.User;
import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe d'accès aux données pour les utilisateurs
 * Gère les opérations CRUD sur la table Utilisateurs
 */
public class UserDAO {

    /**
     * Authentifie un utilisateur avec son nom d'utilisateur et mot de passe
     * @param username Le nom d'utilisateur
     * @param password Le mot de passe en clair (sera hashé avec SHA-256)
     * @return L'utilisateur si authentifié, null sinon
     */
    public User authenticate(String username, String password) {
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Hachage du mot de passe avec SHA-256 (comme dans la base de données)
            String hashedPassword = hashPassword(password);

            // Établir la connexion
            connection = DatabaseConfig.getConnection();

            // Requête SQL pour vérifier l'authentification
            String query = "SELECT * FROM Utilisateurs WHERE nom_utilisateur = ? AND mot_de_passe = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, hashedPassword);

            // Exécuter la requête
            resultSet = statement.executeQuery();

            // Vérifier si un utilisateur correspond
            if (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom_utilisateur"),
                        resultSet.getString("mot_de_passe"),
                        resultSet.getString("role"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    /**
     * Vérifie si un nom d'utilisateur existe déjà
     * @param username Le nom d'utilisateur à vérifier
     * @return true si le nom d'utilisateur existe, false sinon
     */
    public boolean usernameExists(String username) {
        boolean exists = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Établir la connexion
            connection = DatabaseConfig.getConnection();

            // Requête SQL pour vérifier l'existence du nom d'utilisateur
            String query = "SELECT COUNT(*) FROM Utilisateurs WHERE nom_utilisateur = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);

            // Exécuter la requête
            resultSet = statement.executeQuery();

            // Vérifier si un résultat a été trouvé
            if (resultSet.next()) {
                exists = resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return exists;
    }

    /**
     * Inscrit un nouvel utilisateur dans la base de données
     * @param user L'utilisateur à inscrire
     * @return true si l'inscription a réussi, false sinon
     */
    public boolean registerUser(User user) {
        boolean success = false;
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Établir la connexion
            connection = DatabaseConfig.getConnection();

            // Requête SQL pour insérer un nouvel utilisateur
            String query = "INSERT INTO Utilisateurs (nom_utilisateur, mot_de_passe, role, nom, prenom, email) VALUES (?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Paramètres de la requête
            statement.setString(1, user.getUsername());
            statement.setString(2, hashPassword(user.getPassword()));
            statement.setString(3, user.getRole().toLowerCase());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getFirstName());
            statement.setString(6, user.getEmail());

            // Exécuter la requête
            int rowsInserted = statement.executeUpdate();
            success = rowsInserted > 0;

            // Récupérer l'ID généré
            if (success) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    /**
     * Hache un mot de passe avec l'algorithme SHA-256
     * @param password Le mot de passe en clair
     * @return Le mot de passe haché
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            // Convertir le tableau de bytes en représentation hexadécimale
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}