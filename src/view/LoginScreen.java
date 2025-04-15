package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;

/**
 * Écran de connexion pour l'application 7 in Heaven
 * Permet aux utilisateurs de se connecter comme client ou administrateur
 * ou de s'inscrire comme nouveau client
 */
public class LoginScreen extends JFrame {

    // Constantes
    private static final String APP_NAME = "7 in Heaven";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 1200;
    private static final Color LIGHT_BG_COLOR = new Color(0, 128, 97); // Bleu acier
    private static final Color PRIMARY_COLOR = new Color(237, 27, 45);

    // Composants de l'interface
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginScreen() {
        // Configuration de la fenêtre
        setTitle(APP_NAME + " - Connexion");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création du panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(LIGHT_BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        // En-tête avec logo et titre
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panneau de connexion
        JPanel loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, BorderLayout.CENTER);

        // Panneau de bas de page
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Ajout du panneau principal à la fenêtre
        setContentPane(mainPanel);
    }

    /**
     * Crée le panneau d'en-tête avec le logo et le titre
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(LIGHT_BG_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Logo
        ImageIcon logoIcon = createLogoIcon();
        JLabel logoLabel = new JLabel(logoIcon);
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Titre
        JLabel titleLabel = new JLabel(APP_NAME);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    /**
     * Crée le panneau de connexion avec les champs de formulaire
     */
    private JPanel createLoginPanel() {
        // Panneau principal de connexion avec espacement
        JPanel loginContainerPanel = new JPanel(new BorderLayout());
        loginContainerPanel.setBackground(LIGHT_BG_COLOR);

        // Panneau de formulaire de connexion
        JPanel loginFormPanel = new JPanel(new GridBagLayout());
        loginFormPanel.setBackground(Color.WHITE);
        loginFormPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        // Configuration du layout manager
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;

        // Titre du formulaire
        JLabel formTitleLabel = new JLabel("Connexion");
        formTitleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        formTitleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginFormPanel.add(formTitleLabel, gbc);

        // Instruction
        JLabel instructionLabel = new JLabel("Veuillez vous identifier pour accéder à votre compte");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 20, 5);
        loginFormPanel.add(instructionLabel, gbc);

        // Identifiant
        JLabel usernameLabel = new JLabel("Identifiant:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 5, 5, 5);
        loginFormPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 5, 15, 5);
        loginFormPanel.add(usernameField, gbc);

        // Mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 4;
        gbc.insets = new Insets(8, 5, 5, 5);
        loginFormPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 5, 20, 5);
        loginFormPanel.add(passwordField, gbc);

        // Bouton de connexion
        loginButton = new JButton("Se connecter");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 5, 5, 5);
        loginFormPanel.add(loginButton, gbc);

        // Bouton d'inscription
        registerButton = new JButton("Créer un compte client");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.setForeground(PRIMARY_COLOR);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 5, 5, 5);
        loginFormPanel.add(registerButton, gbc);

        // Ajouter les listeners pour les boutons
        addActionListeners();

        // Centrer le formulaire
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(LIGHT_BG_COLOR);
        centerPanel.add(loginFormPanel);

        loginContainerPanel.add(centerPanel, BorderLayout.CENTER);

        return loginContainerPanel;
    }

    /**
     * Ajoute les écouteurs d'événements aux boutons
     */
    private void addActionListeners() {
        // Listener pour le bouton de connexion
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Appeler la méthode d'authentification
                authenticate(username, password);
            }
        });

        // Listener pour le bouton d'inscription
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvrir l'écran d'inscription
                openRegistrationScreen();
            }
        });
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

    /**
     * Méthode temporaire pour créer un logo
     */
    private ImageIcon createLogoIcon() {
        // Création d'un logo temporaire (à remplacer par le vrai logo)
        BufferedImage tempLogo = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tempLogo.createGraphics();
        g2d.setColor(PRIMARY_COLOR);
        g2d.fillRect(0, 0, 64, 64);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        g2d.drawString("7", 24, 42);
        g2d.dispose();

        return new ImageIcon(tempLogo);
    }

    /**
     * Méthode d'authentification (à implémenter avec le contrôleur)
     */
    private void authenticate(String username, String password) {
        // Code temporaire pour simuler l'authentification
        if (username.equals("admin") && password.equals("admin")) {
            JOptionPane.showMessageDialog(this,
                    "Connexion réussie en tant qu'Administrateur",
                    "Connexion", JOptionPane.INFORMATION_MESSAGE);
            // Ouvrir l'interface principale en mode administrateur
            openMainWindow("admin");
        } else if (username.equals("client") && password.equals("client")) {
            JOptionPane.showMessageDialog(this,
                    "Connexion réussie en tant que Client",
                    "Connexion", JOptionPane.INFORMATION_MESSAGE);
            // Ouvrir l'interface principale en mode client
            openMainWindow("client");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Identifiant ou mot de passe incorrect",
                    "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Méthode pour ouvrir l'écran d'inscription
     */
    private void openRegistrationScreen() {
        // Cacher l'écran de connexion
        setVisible(false);

        // Créer et afficher l'écran d'inscription
        RegisterScreen registerScreen = new RegisterScreen(this);
        registerScreen.setVisible(true);
    }

    /**
     * Méthode pour ouvrir l'interface principale
     */
    private void openMainWindow(String userType) {
        // Fermer l'écran de connexion
        dispose();

        // Créer et afficher la fenêtre principale avec le type d'utilisateur
        MainWindow mainWindow = new MainWindow(userType);
        mainWindow.setVisible(true);
    }

    /**
     * Méthode pour afficher l'écran de connexion
     */
    public void display() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
}