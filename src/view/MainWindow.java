package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.border.*;

public class MainWindow extends JFrame {

    private static final String APP_NAME = "7 in Heaven";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private static final int LOGO_WIDTH = 106;
    private static final int LOGO_HEIGHT = 110;
    private static final int ICON_SIZE = 40;
    private final String userType;

    // Panels principaux
    private JPanel produitsPanel;
    private JPanel resumeCommandePanel;

    // Composants pour le résumé de commande
    private JLabel clientNomLabel;
    private JLabel dateHeureLabel;
    private JTextArea resumeArticlesArea;
    private JLabel prixTotalLabel;
    private JLabel reductionsLabel;

    // Map pour stocker les produits et leurs quantités sélectionnées
    private Map<String, Integer> panierProduits = new HashMap<>();
    private Map<String, Double> prixProduits = new HashMap<>();
    private Map<String, double[]> reductionsProduits = new HashMap<>(); // [quantité pour réduction, prix réduit]

    public MainWindow(String userType) {
        this.userType = userType;
        initializeUI();
        // Initialiser quelques produits de démonstration
        initialiserDonneesDemonstration();
    }

    private void initializeUI() {
        // Configuration de base
        setTitle(APP_NAME + " - " + (userType.equals("admin") ? "Administration" : "Boutique"));
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Construction de l'interface
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Panel principal avec séparation entre produits et résumé
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Grille de produits (2/3 gauche)
        produitsPanel = createProduitsPanel();
        mainPanel.add(produitsPanel, BorderLayout.CENTER);

        // Résumé de commande (1/3 droit)
        resumeCommandePanel = createResumeCommandePanel();
        mainPanel.add(resumeCommandePanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(220, 220, 230));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Logo
        headerPanel.add(new JLabel(loadScaledImage("/logo.png", LOGO_WIDTH, LOGO_HEIGHT)), BorderLayout.WEST);

        // Titre
        JLabel titleLabel = new JLabel(APP_NAME, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Bouton utilisateur
        JButton userButton = createUserButton();
        headerPanel.add(userButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createUserButton() {
        JButton button = new JButton();
        button.setIcon(loadScaledImage("/utilisateur.png", ICON_SIZE, ICON_SIZE));
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setToolTipText("Informations du compte");

        button.addActionListener(this::showUserInfo);

        return button;
    }

    private void showUserInfo(ActionEvent e) {
        JPanel userPanel = new JPanel(new GridLayout(3, 1));
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        userPanel.add(new JLabel("Type de compte: " + (userType.equals("admin") ? "Administrateur" : "Utilisateur")));
        userPanel.add(new JLabel("Dernière connexion: " + getLastLogin()));
        userPanel.add(new JLabel("Statut: Actif"));

        JOptionPane.showMessageDialog(this,
                userPanel,
                "Informations du compte",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createProduitsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Titre de la section
        JLabel titreLabel = new JLabel("Catalogue de Produits");
        titreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titreLabel, BorderLayout.NORTH);

        // Zone de recherche et filtres
        JPanel recherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recherchePanel.add(new JLabel("Rechercher: "));
        JTextField rechercheField = new JTextField(20);
        recherchePanel.add(rechercheField);

        JComboBox<String> marquesCombo = new JComboBox<>(new String[]{"Toutes les marques", "Nike", "Adidas", "Puma"});
        recherchePanel.add(new JLabel("Marque: "));
        recherchePanel.add(marquesCombo);

        JButton rechercheButton = new JButton("Rechercher");
        recherchePanel.add(rechercheButton);

        panel.add(recherchePanel, BorderLayout.NORTH);

        // Grille de produits
        JPanel grillePanel = new JPanel(new GridLayout(0, 3, 15, 15));
        grillePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Les produits seront ajoutés dynamiquement ici

        // Wrap la grille dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(grillePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Stocker la référence à la grille pour ajouter des produits plus tard
        panel.putClientProperty("grillePanel", grillePanel);

        return panel;
    }

    private JPanel createResumeCommandePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(WINDOW_WIDTH / 3, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(new Color(245, 245, 250));

        // En-tête du résumé
        JPanel headerPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        headerPanel.setOpaque(false);

        JLabel titreLabel = new JLabel("Résumé de la Commande", JLabel.CENTER);
        titreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titreLabel);

        clientNomLabel = new JLabel("Client: Jean Dupont", JLabel.CENTER);
        clientNomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(clientNomLabel);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateHeureLabel = new JLabel("Date: " + sdf.format(new Date()), JLabel.CENTER);
        dateHeureLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(dateHeureLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Contenu du résumé
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        resumeArticlesArea = new JTextArea();
        resumeArticlesArea.setEditable(false);
        resumeArticlesArea.setLineWrap(true);
        resumeArticlesArea.setWrapStyleWord(true);
        resumeArticlesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resumeArticlesArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resumeArticlesArea.setText("Votre panier est vide.");

        JScrollPane scrollPane = new JScrollPane(resumeArticlesArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(contentPanel, BorderLayout.CENTER);

        // Pied du résumé (prix total, réductions, bouton)
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        reductionsLabel = new JLabel("Réductions: 0.00 €");
        reductionsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        reductionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerPanel.add(reductionsLabel);
        footerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        prixTotalLabel = new JLabel("Total: 0.00 €");
        prixTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        prixTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerPanel.add(prixTotalLabel);
        footerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton commanderButton = new JButton("Finaliser la commande");
        commanderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        commanderButton.setFont(new Font("Arial", Font.BOLD, 14));
        commanderButton.setPreferredSize(new Dimension(200, 40));
        commanderButton.setMaximumSize(new Dimension(200, 40));
        footerPanel.add(commanderButton);

        panel.add(footerPanel, BorderLayout.SOUTH);

        return panel;
    }

    private ImageIcon loadScaledImage(String path, int width, int height) {
        try {
            URL imageUrl = getClass().getResource(path);
            if (imageUrl == null) throw new RuntimeException("Image non trouvée: " + path);

            return new ImageIcon(new ImageIcon(imageUrl).getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur de chargement: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return new ImageIcon(); // Icône vide
        }
    }

    private String getLastLogin() {
        // Méthode simulée - À remplacer par une vraie implémentation
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(new Date());
    }

    // Méthode pour créer un panneau produit individuel
    private JPanel createProduitPanel(String nom, String marque, double prix, double[] reduction) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Image du produit
        JLabel imageLabel = new JLabel(loadScaledImage("/logo.png", 100, 100));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(imageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Nom du produit
        JLabel nomLabel = new JLabel(nom);
        nomLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nomLabel);

        // Marque
        JLabel marqueLabel = new JLabel("Marque: " + marque);
        marqueLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        marqueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(marqueLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Prix
        JLabel prixLabel = new JLabel(String.format("Prix: %.2f €", prix));
        prixLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(prixLabel);

        // Information de réduction si disponible
        if (reduction != null) {
            JLabel reductionLabel = new JLabel(String.format("Offre: %.0f pour %.2f €", reduction[0], reduction[1]));
            reductionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            reductionLabel.setForeground(new Color(0, 128, 0));
            reductionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(reductionLabel);
        }
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Spinner pour la quantité
        JPanel quantitePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        quantitePanel.setOpaque(false);
        quantitePanel.add(new JLabel("Quantité:"));

        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
        JSpinner quantiteSpinner = new JSpinner(spinnerModel);
        quantiteSpinner.setPreferredSize(new Dimension(60, 25));
        quantitePanel.add(quantiteSpinner);

        panel.add(quantitePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Ajouter un écouteur sur le spinner pour mettre à jour le panier
        quantiteSpinner.addChangeListener(e -> {
            int quantite = (int) quantiteSpinner.getValue();
            updatePanier(nom, quantite, prix, reduction);
        });

        return panel;
    }

    // Mettre à jour le panier avec le produit et sa quantité
    private void updatePanier(String produit, int quantite, double prix, double[] reduction) {
        if (quantite > 0) {
            panierProduits.put(produit, quantite);
            prixProduits.put(produit, prix);
            if (reduction != null) {
                reductionsProduits.put(produit, reduction);
            }
        } else {
            panierProduits.remove(produit);
            prixProduits.remove(produit);
            reductionsProduits.remove(produit);
        }

        updateResumeCommande();
    }

    // Mettre à jour le résumé de la commande
    private void updateResumeCommande() {
        if (panierProduits.isEmpty()) {
            resumeArticlesArea.setText("Votre panier est vide.");
            prixTotalLabel.setText("Total: 0.00 €");
            reductionsLabel.setText("Réductions: 0.00 €");
            return;
        }

        StringBuilder resumeBuilder = new StringBuilder();
        double total = 0;
        double totalReductions = 0;

        for (Map.Entry<String, Integer> entry : panierProduits.entrySet()) {
            String produit = entry.getKey();
            int quantite = entry.getValue();
            double prixUnitaire = prixProduits.get(produit);

            resumeBuilder.append(produit).append(" x ").append(quantite);

            double prixNormal = prixUnitaire * quantite;
            double prixFinal = prixNormal;

            // Calcul des réductions si applicables
            if (reductionsProduits.containsKey(produit)) {
                double[] reduction = reductionsProduits.get(produit);
                int quantiteReduction = (int) reduction[0];
                double prixReduction = reduction[1];

                if (quantite >= quantiteReduction) {
                    int nombreLots = quantite / quantiteReduction;
                    int unitesSeparees = quantite % quantiteReduction;

                    prixFinal = nombreLots * prixReduction + unitesSeparees * prixUnitaire;
                    double economie = prixNormal - prixFinal;
                    totalReductions += economie;

                    resumeBuilder.append(" (").append(nombreLots).append(" lot(s) + ")
                            .append(unitesSeparees).append(" unité(s))");
                }
            }

            resumeBuilder.append(String.format(" : %.2f €", prixFinal)).append("\n\n");
            total += prixFinal;
        }

        resumeArticlesArea.setText(resumeBuilder.toString());
        prixTotalLabel.setText(String.format("Total: %.2f €", total));
        reductionsLabel.setText(String.format("Réductions: %.2f €", totalReductions));
    }

    // Méthode pour initialiser des données de démonstration
    private void initialiserDonneesDemonstration() {
        JPanel grillePanel = (JPanel) produitsPanel.getClientProperty("grillePanel");

        // Ajouter des produits de démonstration - ATTENTION: double[] pour les réductions
        grillePanel.add(createProduitPanel("Briquet Jetable", "BIC", 0.50, new double[]{10.0, 4.00}));
        grillePanel.add(createProduitPanel("T-Shirt Basic", "Nike", 25.00, new double[]{3.0, 60.00}));
        grillePanel.add(createProduitPanel("Chaussettes Sport", "Adidas", 8.50, new double[]{5.0, 30.00}));
        grillePanel.add(createProduitPanel("Casquette", "Puma", 15.00, null));
        grillePanel.add(createProduitPanel("Bouteille d'Eau", "Nike", 12.00, new double[]{2.0, 20.00}));
        grillePanel.add(createProduitPanel("Ballon de Foot", "Adidas", 30.00, null));
        grillePanel.add(createProduitPanel("Sac de Sport", "Puma", 45.00, null));
        grillePanel.add(createProduitPanel("Montre Sport", "Nike", 120.00, null));
        grillePanel.add(createProduitPanel("Baskets Running", "Adidas", 95.00, null));

        // Mettre à jour le layout
        grillePanel.revalidate();
        grillePanel.repaint();
    }

    public void display() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}