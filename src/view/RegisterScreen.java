package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import controller.UserController;
import model.User;

/**
 * Écran d'inscription pour l'application 7 in Heaven
 * Permet aux utilisateurs de créer un nouveau compte
 */
public class RegisterScreen extends JFrame {

    // Constantes
    private static final String APP_NAME = "7 in Heaven";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 650;
    private static final Color LIGHT_BG_COLOR = new Color(0, 128, 97);
    private static final Color PRIMARY_COLOR = new Color(237, 27, 45);

    // Composants de l'interface
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JComboBox<String> userTypeComboBox;
    private JButton registerButton;
    private JButton backButton;

    // Référence à l'écran de connexion
    private LoginScreen loginScreen;

    // Contrôleur utilisateur
    private UserController userController;

    public RegisterScreen(LoginScreen loginScreen) {
        this.loginScreen = loginScreen;
        this.userController = new UserController();

        // Configuration de la fenêtre
        setTitle(APP_NAME + " - Inscription");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création du panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(LIGHT_BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        // En-tête avec titre
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panneau d'inscription
        JPanel registerPanel = createRegisterPanel();
        mainPanel.add(registerPanel, BorderLayout.CENTER);

        // Panneau de bas de page
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Ajout du panneau principal à la fenêtre
        setContentPane(mainPanel);
    }

    /**
     * Crée le panneau d'en-tête avec le titre
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(LIGHT_BG_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Création de compte");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    /**
     * Crée le panneau d'inscription avec les champs de formulaire
     */
    private JPanel createRegisterPanel() {
        // Panneau principal d'inscription avec espacement
        JPanel registerContainerPanel = new JPanel(new BorderLayout());
        registerContainerPanel.setBackground(LIGHT_BG_COLOR);

        // Panneau de formulaire d'inscription
        JPanel registerFormPanel = new JPanel(new GridBagLayout());
        registerFormPanel.setBackground(Color.WHITE);
        registerFormPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));

        // Configuration du layout manager
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;

        // Information
        JLabel instructionLabel = new JLabel("Veuillez remplir tous les champs pour créer votre compte");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerFormPanel.add(instructionLabel, gbc);

        // Type d'utilisateur
        JLabel userTypeLabel = new JLabel("Type de compte:");
        userTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 5, 5, 5);
        registerFormPanel.add(userTypeLabel, gbc);

        String[] userTypes = {"client", "admin"};
        userTypeComboBox = new JComboBox<>(userTypes);
        userTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 5, 10, 5);
        registerFormPanel.add(userTypeComboBox, gbc);

        // Identifiant
        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 5, 5, 5);
        registerFormPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 5, 10, 5);
        registerFormPanel.add(usernameField, gbc);

        // Mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 5, 5, 5);
        registerFormPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 5, 10, 5);
        registerFormPanel.add(passwordField, gbc);

        // Confirmation du mot de passe
        JLabel confirmPasswordLabel = new JLabel("Confirmer le mot de passe:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 5, 5, 5);
        registerFormPanel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 5, 10, 5);
        registerFormPanel.add(confirmPasswordField, gbc);

        // Prénom
        JLabel firstNameLabel = new JLabel("Prénom:");
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 9;
        gbc.insets = new Insets(10, 5, 5, 5);
        registerFormPanel.add(firstNameLabel, gbc);

        firstNameField = new JTextField(20);
        firstNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 5, 10, 5);
        registerFormPanel.add(firstNameField, gbc);

        // Nom
        JLabel lastNameLabel = new JLabel("Nom:");
        lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 11;
        gbc.insets = new Insets(10, 5, 5, 5);
        registerFormPanel.add(lastNameLabel, gbc);

        lastNameField = new JTextField(20);
        lastNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 12;
        gbc.insets = new Insets(0, 5, 10, 5);
        registerFormPanel.add(lastNameField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 13;
        gbc.insets = new Insets(10, 5, 5, 5);
        registerFormPanel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 14;
        gbc.insets = new Insets(0, 5, 10, 5);
        registerFormPanel.add(emailField, gbc);

        // Boutons (dans un panneau séparé pour les centrer)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Bouton d'inscription
        registerButton = new JButton("S'inscrire");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(PRIMARY_COLOR);
        registerButton.setForeground(PRIMARY_COLOR);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setPreferredSize(new Dimension(120, 35));
        buttonPanel.add(registerButton);

        // Bouton retour
        backButton = new JButton("Retour");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setForeground(PRIMARY_COLOR);
        backButton.setPreferredSize(new Dimension(120, 35));
        buttonPanel.add(backButton);

        gbc.gridy = 15;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        registerFormPanel.add(buttonPanel, gbc);

        // Ajouter les listeners pour les boutons
        addActionListeners();

        // Centrer le formulaire
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(LIGHT_BG_COLOR);
        centerPanel.add(registerFormPanel);

        // Ajouter un JScrollPane pour permettre le défilement
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(LIGHT_BG_COLOR);
        registerContainerPanel.add(scrollPane, BorderLayout.CENTER);

        return registerContainerPanel;
    }

    /**
     * Ajoute les écouteurs d'événements aux boutons
     */
    private void addActionListeners() {
        // Listener pour le bouton d'inscription
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    registerUser();
                }
            }
        });

        // Listener pour le bouton retour
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                loginScreen.setVisible(true);
            }
        });
    }

    /**
     * Valide les champs du formulaire
     */
    private boolean validateForm() {
        // Vérifier que tous les champs sont remplis
        if (usernameField.getText().isEmpty() ||
                new String(passwordField.getPassword()).isEmpty() ||
                new String(confirmPasswordField.getPassword()).isEmpty() ||
                firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() ||
                emailField.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Tous les champs sont obligatoires.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Vérifier que les mots de passe correspondent
        if (!new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
            JOptionPane.showMessageDialog(this,
                    "Les mots de passe ne correspondent pas.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Vérifier le format de l'email (validation simple)
        if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez entrer une adresse email valide.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Vérifier si le nom d'utilisateur existe déjà
        if (userController.checkIfUserExists(usernameField.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Ce nom d'utilisateur existe déjà. Veuillez en choisir un autre.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Enregistre l'utilisateur dans la base de données
     */
    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) userTypeComboBox.getSelectedItem();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();

        // Créer un nouvel objet utilisateur
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole(role);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);

        // Essayer d'enregistrer l'utilisateur
        try {
            boolean success = userController.registerUser(newUser);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Compte " + role + " créé avec succès pour " + username + ".",
                        "Inscription réussie", JOptionPane.INFORMATION_MESSAGE);

                // Fermer l'écran d'inscription et revenir à l'écran de connexion
                dispose();
                loginScreen.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la création du compte. Veuillez réessayer.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur technique: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Création du panneau de bas de page
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(LIGHT_BG_COLOR);

        JLabel footerLabel = new JLabel("© 2025 7 in Heaven - Tous droits réservés");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(100, 100, 100));

        footerPanel.add(footerLabel);

        return footerPanel;
    }
}