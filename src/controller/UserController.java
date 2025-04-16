package controller;

import dao.UserDAO;
import model.User;

/**
 * Contrôleur pour la gestion des utilisateurs
 * Fait le lien entre la vue et le modèle
 */
public class UserController {
    private UserDAO userDAO;

    /**
     * Constructeur
     */
    public UserController() {
        this.userDAO = new UserDAO();
    }

    /**
     * Vérifie si un nom d'utilisateur existe déjà
     * @param username Le nom d'utilisateur à vérifier
     * @return true si le nom d'utilisateur existe, false sinon
     */
    public boolean checkIfUserExists(String username) {
        return userDAO.usernameExists(username);
    }

    /**
     * Inscrit un nouvel utilisateur dans la base de données
     * @param user L'utilisateur à inscrire
     * @return true si l'inscription a réussi, false sinon
     */
    public boolean registerUser(User user) {
        return userDAO.registerUser(user);
    }

    /**
     * Authentifie un utilisateur
     * @param username Le nom d'utilisateur
     * @param password Le mot de passe
     * @return L'utilisateur authentifié ou null si échec
     */
    public User authenticateUser(String username, String password) {
        return userDAO.authenticate(username, password);
    }
}