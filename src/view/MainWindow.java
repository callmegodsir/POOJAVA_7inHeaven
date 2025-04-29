package view;

import javax.swing.*;
import java.awt.*;
import dao.ProduitMarqueDAO;
import dao.ProduitMarqueDAOImpl;
import model.ProduitMarque;
import model.User;
import java.util.List;
import java.awt.event.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.border.*;
import java.text.Normalizer;

public class MainWindow extends JFrame {

    private static final String APP_NAME = "7 in Heaven";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private static final int LOGO_WIDTH = 106;
    private static final int LOGO_HEIGHT = 110;
    private static final int ICON_SIZE = 40;
    private final String userType;
    private final User currentUser;

    private JPanel produitsPanel;
    private JPanel resumeCommandePanel;
    private JPanel grillePanel; // Make grillePanel an instance variable

    private JLabel clientNomLabel;
    private JLabel dateHeureLabel;
    private JTextArea resumeArticlesArea;
    private JLabel prixTotalLabel;
    private JLabel reductionsLabel;

    private Map<String, Integer> panierProduits = new HashMap<>();
    private Map<String, Double> prixProduits = new HashMap<>();
    private Map<String, double[]> reductionsProduits = new HashMap<>();

    private List<ProduitMarque> allProduits; // Store all products for filtering

    private JComboBox<String> marquesCombo; // Make combo box accessible
    private JTextField rechercheField; // Make search field accessible


    public MainWindow(User loggedInUser) {
        this.userType = loggedInUser.getRole();
        this.currentUser = loggedInUser;

        initializeUI();
        initialiserProduitsDepuisBDD(); // Load products and display initially
    }

    private void initializeUI() {
        setTitle(APP_NAME + " - " + (userType.equals("admin") ? "Administration" : "Boutique"));
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();

        if ("admin".equals(userType)) {
            JButton adminButton = new JButton("Panel Admin");
            adminButton.addActionListener(e -> new AdminScreen(currentUser).setVisible(true));
            headerPanel.add(adminButton, BorderLayout.EAST);
        }

        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        produitsPanel = createProduitsPanel(); // This now creates grillePanel as well
        mainPanel.add(produitsPanel, BorderLayout.CENTER);

        resumeCommandePanel = createResumeCommandePanel();
        mainPanel.add(resumeCommandePanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(220, 220, 230));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ImageIcon logoIcon = loadScaledImage("/logo.png", LOGO_WIDTH, LOGO_HEIGHT);
        if (logoIcon != null) {
            headerPanel.add(new JLabel(logoIcon), BorderLayout.WEST);
        } else {
            headerPanel.add(new JLabel("Logo"), BorderLayout.WEST);
        }

        JLabel titleLabel = new JLabel(APP_NAME, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton userButton = createUserButton();
        headerPanel.add(userButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createUserButton() {
        JButton button = new JButton();
        ImageIcon userIcon = loadScaledImage("/utilisateur.png", ICON_SIZE, ICON_SIZE);
        if (userIcon != null) {
            button.setIcon(userIcon);
        } else {
            button.setText("User");
        }
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setToolTipText("Informations du compte");

        button.addActionListener(e -> {
            showUserInfo(e);
            HistoriqueCommandeDialog historiqueDialog = new HistoriqueCommandeDialog(this, currentUser);
            historiqueDialog.setVisible(true);
        });

        return button;
    }

    private void showUserInfo(ActionEvent e) {
        JPanel userPanel = new JPanel(new GridLayout(4, 1)); // Increased rows for new label
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        userPanel.add(new JLabel("Type de compte: " + (userType.equals("admin") ? "Administrateur" : "Utilisateur")));
        userPanel.add(new JLabel("Nom: " + currentUser.getFirstName() + " " + currentUser.getLastName()));
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

        JPanel recherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recherchePanel.add(new JLabel("Rechercher: "));
        rechercheField = new JTextField(20); // Initialize instance variable
        recherchePanel.add(rechercheField);

        marquesCombo = new JComboBox<>(new String[]{"Toutes les marques"}); // Initialize instance variable
        // Populate marquesCombo later after fetching products
        recherchePanel.add(new JLabel("Marque: "));
        recherchePanel.add(marquesCombo);

        JButton rechercheButton = new JButton("Filtrer");
        recherchePanel.add(rechercheButton);

        panel.add(recherchePanel, BorderLayout.NORTH);

        grillePanel = new JPanel(new GridLayout(0, 3, 15, 15)); // Initialize instance variable
        grillePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(grillePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Add action listener for filtering
        ActionListener filterAction = e -> updateProductGrid();
        rechercheButton.addActionListener(filterAction);
        marquesCombo.addActionListener(filterAction); // Filter when combo box changes
        rechercheField.addActionListener(filterAction); // Filter when Enter is pressed in search field

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

        JPanel headerPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        headerPanel.setOpaque(false);

        JLabel titreLabel = new JLabel("Résumé de la Commande", JLabel.CENTER);
        titreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titreLabel);

        clientNomLabel = new JLabel("Client: " + currentUser.getFirstName() + " " + currentUser.getLastName(), JLabel.CENTER);
        clientNomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(clientNomLabel);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateHeureLabel = new JLabel("Date: " + sdf.format(new Date()), JLabel.CENTER);
        dateHeureLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(dateHeureLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

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

        commanderButton.addActionListener(e -> {
            if (panierProduits.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Votre panier est vide. Veuillez ajouter des produits avant de finaliser.",
                        "Panier vide",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            double total = 0;
            double reductions = 0;

            try {
                total = Double.parseDouble(prixTotalLabel.getText().replace("Total: ", "").replace(" €", ""));
                reductions = Double.parseDouble(reductionsLabel.getText().replace("Réductions: ", "").replace(" €", ""));
            } catch (NumberFormatException ex) {
                System.err.println("Erreur lors de la conversion des montants: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Erreur de calcul du total.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            FinalisationCommandeDialog finalisationDialog = new FinalisationCommandeDialog(
                    this,
                    currentUser,
                    panierProduits,
                    prixProduits,
                    reductionsProduits,
                    total,
                    reductions
            );
            finalisationDialog.setVisible(true);

            // Check if the dialog is closed AND the command was likely successful (dialog is not visible anymore)
            // A more robust way would be for the dialog to return a status
            if (!finalisationDialog.isVisible()) {
                // Assume command was successful or cancelled, reset cart
                panierProduits.clear();
                prixProduits.clear();
                reductionsProduits.clear();
                updateResumeCommande();
                // Reset spinners in the product grid
                resetSpinners();
            }
        });

        footerPanel.add(commanderButton);

        panel.add(footerPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void resetSpinners() {
        if (grillePanel == null) return;
        for (Component comp : grillePanel.getComponents()) {
            if (comp instanceof JPanel) { // Each product is a JPanel
                JPanel productPanel = (JPanel) comp;
                for (Component innerComp : productPanel.getComponents()) {
                    if (innerComp instanceof JPanel) { // Spinner is usually inside another panel
                        JPanel quantitePanel = (JPanel) innerComp;
                        for (Component qComp : quantitePanel.getComponents()) {
                            if (qComp instanceof JSpinner) {
                                JSpinner spinner = (JSpinner) qComp;
                                spinner.setValue(0); // Reset to 0
                            }
                        }
                    }
                }
            }
        }
    }


    private ImageIcon loadScaledImage(String path, int width, int height) {
        try {
            URL imageUrl = getClass().getResource(path);
            if (imageUrl == null) {
                System.err.println("Image non trouvée: " + path);
                // Try loading default image if specific one not found
                URL defaultUrl = getClass().getResource("/default.png");
                if(defaultUrl != null) {
                    return new ImageIcon(new ImageIcon(defaultUrl).getImage()
                            .getScaledInstance(width, height, Image.SCALE_SMOOTH));
                }
                return null; // Return null if default also not found
            }

            return new ImageIcon(new ImageIcon(imageUrl).getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            System.err.println("Erreur de chargement d'image: " + path + " - " + e.getMessage());
            // Try loading default image on error
            try {
                URL defaultUrl = getClass().getResource("/default.png");
                if(defaultUrl != null) {
                    return new ImageIcon(new ImageIcon(defaultUrl).getImage()
                            .getScaledInstance(width, height, Image.SCALE_SMOOTH));
                }
            } catch (Exception e2) {
                System.err.println("Erreur chargement image par défaut: " + e2.getMessage());
            }
            return null;
        }
    }

    private String getLastLogin() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(new Date());
    }


    public static String genererNomImage(String nomProduit) {
        String normalise = Normalizer.normalize(nomProduit.toLowerCase(Locale.FRENCH), Normalizer.Form.NFD);
        String sansAccents = normalise.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String nomFichier = sansAccents.replaceAll("[^a-z0-9]+", "_").replaceAll("^_|_$", "");
        if (nomFichier.isEmpty()) {
            return "default.png"; // fallback for names that become empty
        }
        return nomFichier + ".png";
    }

    private JPanel createProduitPanel(ProduitMarque pm) {
        String nom = pm.getProduit().getNom();
        String marque = pm.getMarque().getNom();
        double prix = pm.getPrixUnitaire();
        double[] reduction = null;
        if (pm.getQuantiteGroupe() > 0 && pm.getPrixGroupe() != null) {
            reduction = new double[]{pm.getQuantiteGroupe(), pm.getPrixGroupe()};
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        String nomImage = genererNomImage(nom);
        String cheminImage = "/" + nomImage;

        ImageIcon produitIcon = loadScaledImage(cheminImage, 100, 100);
        // Default image is now handled inside loadScaledImage

        JLabel imageLabel = produitIcon != null ? new JLabel(produitIcon) : new JLabel("Image indisponible");
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(imageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel nomLabel = new JLabel(nom);
        nomLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nomLabel);

        JLabel marqueLabel = new JLabel("Marque: " + marque);
        marqueLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        marqueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(marqueLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel prixLabel = new JLabel(String.format("Prix: %.2f €", prix));
        prixLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(prixLabel);

        if (reduction != null) {
            JLabel reductionLabel = new JLabel(String.format("Offre: %.0f pour %.2f €", reduction[0], reduction[1]));
            reductionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            reductionLabel.setForeground(new Color(0, 128, 0));
            reductionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(reductionLabel);
        }

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel quantitePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        quantitePanel.setOpaque(false);
        quantitePanel.add(new JLabel("Quantité:"));

        // Set initial value based on current cart
        int initialQuantity = panierProduits.getOrDefault(nom, 0);
        SpinnerModel spinnerModel = new SpinnerNumberModel(initialQuantity, 0, 100, 1);
        JSpinner quantiteSpinner = new JSpinner(spinnerModel);
        quantiteSpinner.setPreferredSize(new Dimension(60, 25));
        quantitePanel.add(quantiteSpinner);

        panel.add(quantitePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        final double[] finalReduction = reduction; // Need final variable for lambda
        quantiteSpinner.addChangeListener(e -> {
            int quantite = (int) quantiteSpinner.getValue();
            updatePanier(nom, quantite, prix, finalReduction);
        });

        return panel;
    }


    private void updatePanier(String produit, int quantite, double prix, double[] reduction) {
        try {
            if (quantite > 0) {
                panierProduits.put(produit, quantite);
                prixProduits.put(produit, prix);
                if (reduction != null && reduction.length == 2) {
                    reductionsProduits.put(produit, reduction);
                } else {
                    reductionsProduits.remove(produit);
                }
            } else {
                panierProduits.remove(produit);
                prixProduits.remove(produit);
                reductionsProduits.remove(produit);
            }

            updateResumeCommande();
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du panier: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateResumeCommande() {
        try {
            if (panierProduits.isEmpty()) {
                resumeArticlesArea.setText("Votre panier est vide.");
                prixTotalLabel.setText("Total: 0.00 €");
                reductionsLabel.setText("Réductions: 0.00 €");
                return;
            }

            StringBuilder resumeBuilder = new StringBuilder();
            double total = 0;
            double totalReductions = 0;

            // Sort items alphabetically for consistent display
            List<String> sortedProducts = new ArrayList<>(panierProduits.keySet());
            Collections.sort(sortedProducts);


            for (String produit : sortedProducts) {
                int quantite = panierProduits.get(produit);

                if (!prixProduits.containsKey(produit)) {
                    System.err.println("Erreur: Prix manquant pour le produit " + produit);
                    continue;
                }

                double prixUnitaire = prixProduits.get(produit);

                resumeBuilder.append(produit).append(" x ").append(quantite);

                double prixNormal = prixUnitaire * quantite;
                double prixFinal = prixNormal;

                if (reductionsProduits.containsKey(produit)) {
                    double[] reduction = reductionsProduits.get(produit);
                    if (reduction != null && reduction.length == 2) {
                        int quantiteReduction = (int) reduction[0];
                        double prixReduction = reduction[1];

                        if (quantite > 0 && quantiteReduction > 0 && quantite >= quantiteReduction) {
                            int nombreLots = quantite / quantiteReduction;
                            int unitesSeparees = quantite % quantiteReduction;

                            prixFinal = nombreLots * prixReduction + unitesSeparees * prixUnitaire;
                            double economie = prixNormal - prixFinal;
                            if (economie > 0.005) { // Avoid displaying tiny rounding differences
                                totalReductions += economie;
                                resumeBuilder.append(String.format(" (Offre %.0fx appliquée)", reduction[0]));
                            }
                        }
                    }
                }

                resumeBuilder.append(String.format(" : %.2f €", prixFinal)).append("\n");
                total += prixFinal;
            }

            resumeArticlesArea.setText(resumeBuilder.toString().trim());
            prixTotalLabel.setText(String.format("Total: %.2f €", total));
            reductionsLabel.setText(String.format("Réductions: %.2f €", totalReductions));

            // Scroll to the top of the text area
            resumeArticlesArea.setCaretPosition(0);

        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du résumé: " + e.getMessage());
            e.printStackTrace();
            resumeArticlesArea.setText("Erreur lors du calcul du panier.");
        }
    }

    private void initialiserProduitsDepuisBDD() {
        try {
            ProduitMarqueDAO pmDao = new ProduitMarqueDAOImpl();
            allProduits = pmDao.trouverTous(); // Store all products

            if (allProduits == null || allProduits.isEmpty()) {
                allProduits = new ArrayList<>(); // Ensure list is not null
                System.err.println("Aucun produit trouvé dans la BDD. Chargement des données de démo.");
                ajouterProduitsDemonstration(); // Add demo data to allProduits
            }

            // Populate brand filter combo box
            Set<String> marques = new TreeSet<>(); // Use TreeSet for sorted order
            for (ProduitMarque pm : allProduits) {
                marques.add(pm.getMarque().getNom());
            }
            marquesCombo.removeAllItems(); // Clear default items if any
            marquesCombo.addItem("Toutes les marques");
            for (String marque : marques) {
                marquesCombo.addItem(marque);
            }


            // Initial display with all products
            updateProductGrid();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation des produits depuis BDD: " + e.getMessage());
            e.printStackTrace();
            allProduits = new ArrayList<>(); // Ensure list is not null
            ajouterProduitsDemonstration(); // Use demo data as fallback
            updateProductGrid(); // Display demo data
        }
    }

    // Method to update the product grid based on filters
    private void updateProductGrid() {
        if (grillePanel == null || allProduits == null) {
            System.err.println("Grille ou liste produits non initialisée.");
            return;
        }

        String selectedBrand = (String) marquesCombo.getSelectedItem();
        String searchTerm = rechercheField.getText().trim().toLowerCase();

        grillePanel.removeAll(); // Clear the grid

        for (ProduitMarque pm : allProduits) {
            boolean brandMatch = "Toutes les marques".equals(selectedBrand) || pm.getMarque().getNom().equalsIgnoreCase(selectedBrand);
            boolean searchMatch = searchTerm.isEmpty() || pm.getProduit().getNom().toLowerCase().contains(searchTerm);

            if (brandMatch && searchMatch) {
                grillePanel.add(createProduitPanel(pm)); // Recreate panel with current cart quantity
            }
        }

        // If no products match, display a message (optional)
        if (grillePanel.getComponentCount() == 0) {
            JLabel noResultsLabel = new JLabel("Aucun produit trouvé.", JLabel.CENTER);
            noResultsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            // You might want to add this label directly to the scrollpane's viewport or handle layout differently
            grillePanel.setLayout(new BorderLayout()); // Change layout to center label
            grillePanel.add(noResultsLabel, BorderLayout.CENTER);
        } else {
            // Reset to grid layout if products were found
            int cols = 3; // Or calculate based on width
            int rows = (int) Math.ceil((double) grillePanel.getComponentCount() / cols);
            grillePanel.setLayout(new GridLayout(rows, cols, 15, 15));
        }


        grillePanel.revalidate();
        grillePanel.repaint();
    }


    // Méthode pour ajouter des produits de démonstration en cas d'erreur avec la BDD
    private void ajouterProduitsDemonstration() {
        // This method now just adds demo data to the allProduits list
        // The actual display is handled by updateProductGrid
        System.out.println("Ajout des produits de démonstration...");
        // Simulate ProduitMarque objects for consistency
        // You'll need placeholder Produit and Marque objects or adapt this
        // For simplicity, assuming you have constructors or setters
        // NOTE: Replace with actual object creation based on your model classes
        /* Example (adjust based on your actual Model classes):
        model.Marque nike = new model.Marque(1, "Nike");
        model.Produit basket = new model.Produit(1, "Baskets Running", "...", 10);
        allProduits.add(new ProduitMarque(1, basket, nike, 89.99, 3, 249.99));

        model.Marque adidas = new model.Marque(2, "Adidas");
        model.Produit chaussure = new model.Produit(2, "Chaussures de Sport", "...", 15);
        allProduits.add(new ProduitMarque(2, chaussure, adidas, 79.99, 2, 139.99));
        // ... add more demo products
        */
        JOptionPane.showMessageDialog(this, "Impossible de charger les produits. Données de démonstration affichées.", "Erreur de chargement", JOptionPane.WARNING_MESSAGE);
    }

    public void display() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}