package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainWindow extends JFrame {

    private static final String APP_NAME = "7 in Heaven";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 1200;
    private static final int LOGO_WIDTH = 106;  // Nouvelle largeur du logo
    private static final int LOGO_HEIGHT =110; // Nouvelle hauteur du logo
    private String userType;
    public MainWindow(String userType) {
        // Configuration de base de la fenêtre
        this.userType = userType;

        setTitle(APP_NAME + " - " + (userType.equals("admin") ? "Administration" : "Boutique"));
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Création du panneau d'en-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(220, 220, 230));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Logo redimensionné en haut à gauche
        JLabel logoLabel = new JLabel(loadLogo());
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Titre centré
        JLabel titleLabel = new JLabel(APP_NAME, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Configuration du contenu principal
        JPanel mainContent = new JPanel();
        mainContent.setBackground(new Color(240, 240, 245));

        // Ajout des composants à la fenêtre
        add(headerPanel, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
    }

    private ImageIcon loadLogo() {
        try {
            URL imagePath = getClass().getResource("/logo.png");

            if (imagePath == null) {
                throw new RuntimeException("Fichier logo.png introuvable dans le dossier resources/images");
            }

            // Redimensionnement de l'image
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image scaledImage = originalIcon.getImage()
                    .getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);

            return new ImageIcon(scaledImage);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur critique :\n" + e.getMessage(),
                    "Erreur de chargement",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return null;
        }
    }

    public void display() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}