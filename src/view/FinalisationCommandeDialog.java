package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dao.*;
import model.Commande;
import model.LigneCommande;
import model.ProduitMarque;
import model.User;

public class FinalisationCommandeDialog extends JDialog {

    private static final int DIALOG_WIDTH = 600;
    private static final int DIALOG_HEIGHT = 500;

    private final Map<String, Integer> panierProduits;
    private final Map<String, Double> prixProduits;
    private final Map<String, double[]> reductionsProduits;
    private final User utilisateurConnecte;
    private final double totalPanier;
    private final double totalReductions;

    private JTextArea resumeTextArea;
    private JButton confirmerButton;
    private JButton annulerButton;
    private JComboBox<String> modePaiementCombo;
    private JTextField adresseLivraisonField;

    public FinalisationCommandeDialog(JFrame parent, User utilisateur,
                                      Map<String, Integer> panier,
                                      Map<String, Double> prix,
                                      Map<String, double[]> reductions,
                                      double total,
                                      double remises) {
        super(parent, "Finalisation de la Commande", true);
        this.panierProduits = panier;
        this.prixProduits = prix;
        this.reductionsProduits = reductions;
        this.utilisateurConnecte = utilisateur;
        this.totalPanier = total;
        this.totalReductions = remises;

        initialiserUI();
    }

    private void initialiserUI() {
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        // Panel d'en-tête
        JPanel headerPanel = creerPanelEnTete();
        add(headerPanel, BorderLayout.NORTH);

        // Panel central avec résumé de commande
        JPanel centerPanel = creerPanelCentral();
        add(centerPanel, BorderLayout.CENTER);

        // Panel de pied avec boutons
        JPanel footerPanel = creerPanelPied();
        add(footerPanel, BorderLayout.SOUTH);

        // Remplir le résumé de la commande
        actualiserResumeCommande();
    }

    private JPanel creerPanelEnTete() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(240, 240, 250));

        JLabel titreLabel = new JLabel("Finalisation de votre commande", JLabel.CENTER);
        titreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titreLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        infoPanel.setOpaque(false);

        JLabel clientLabel = new JLabel("Client: " + utilisateurConnecte.getFirstName() + " " + utilisateurConnecte.getLastName(), JLabel.CENTER);
        clientLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(clientLabel);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        JLabel dateLabel = new JLabel("Date: " + sdf.format(new Date()), JLabel.CENTER);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(dateLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel creerPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Résumé de la commande
        JLabel resumeLabel = new JLabel("Résumé de votre commande:", JLabel.LEFT);
        resumeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(resumeLabel, BorderLayout.NORTH);

        resumeTextArea = new JTextArea();
        resumeTextArea.setEditable(false);
        resumeTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resumeTextArea.setLineWrap(true);
        resumeTextArea.setWrapStyleWord(true);
        resumeTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(resumeTextArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Options de livraison et paiement
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        optionsPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        optionsPanel.add(new JLabel("Mode de paiement:"));
        modePaiementCombo = new JComboBox<>(new String[]{"Carte bancaire", "PayPal", "Apple Pay", "Google Pay"});
        optionsPanel.add(modePaiementCombo);

        optionsPanel.add(new JLabel("Adresse de livraison:"));
        adresseLivraisonField = new JTextField();
        optionsPanel.add(adresseLivraisonField);

        panel.add(optionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel creerPanelPied() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(240, 240, 250));

        // Prix total
        JPanel prixPanel = new JPanel();
        prixPanel.setLayout(new BoxLayout(prixPanel, BoxLayout.Y_AXIS));
        prixPanel.setOpaque(false);

        JLabel reductionsLabel = new JLabel(String.format("Réductions: %.2f €", totalReductions));
        reductionsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        reductionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        prixPanel.add(reductionsLabel);

        JLabel totalLabel = new JLabel(String.format("Total à payer: %.2f €", totalPanier));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        prixPanel.add(totalLabel);

        panel.add(prixPanel, BorderLayout.WEST);

        // Boutons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setOpaque(false);

        annulerButton = new JButton("Annuler");
        annulerButton.addActionListener(e -> dispose());
        buttonsPanel.add(annulerButton);

        confirmerButton = new JButton("Confirmer la commande");
        confirmerButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmerButton.addActionListener(e -> sauvegarderCommande());
        buttonsPanel.add(confirmerButton);

        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }

    private void actualiserResumeCommande() {
        if (panierProduits.isEmpty()) {
            resumeTextArea.setText("Votre panier est vide.");
            return;
        }

        StringBuilder resumeBuilder = new StringBuilder();

        for (Map.Entry<String, Integer> entry : panierProduits.entrySet()) {
            String produit = entry.getKey();
            int quantite = entry.getValue();

            if (!prixProduits.containsKey(produit)) {
                continue;
            }

            double prixUnitaire = prixProduits.get(produit);
            resumeBuilder.append(produit).append(" x ").append(quantite);

            double prixNormal = prixUnitaire * quantite;
            double prixFinal = prixNormal;

            // Calcul des réductions si applicables
            if (reductionsProduits.containsKey(produit)) {
                double[] reduction = reductionsProduits.get(produit);
                if (reduction != null && reduction.length == 2) {
                    int quantiteReduction = (int) reduction[0];
                    double prixReduction = reduction[1];

                    if (quantite >= quantiteReduction) {
                        int nombreLots = quantite / quantiteReduction;
                        int unitesSeparees = quantite % quantiteReduction;

                        prixFinal = nombreLots * prixReduction + unitesSeparees * prixUnitaire;

                        resumeBuilder.append(" (").append(nombreLots).append(" lot(s) + ")
                                .append(unitesSeparees).append(" unité(s))");
                    }
                }
            }

            resumeBuilder.append(String.format(" : %.2f €", prixFinal)).append("\n\n");
        }

        resumeTextArea.setText(resumeBuilder.toString());
    }

    private void sauvegarderCommande() {
        try {
            if (adresseLivraisonField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez saisir une adresse de livraison.",
                        "Informations manquantes",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Créer l'objet commande
            Commande nouvelleCommande = new Commande();
            nouvelleCommande.setClientId(utilisateurConnecte.getId());
            nouvelleCommande.setStatut("En préparation");
            nouvelleCommande.setTotal(totalPanier);
            nouvelleCommande.setDateCommande(new Date());

            // Sauvegarder la commande en base
            CommandeDAO commandeDAO = new CommandeDAOImpl();
            int commandeId = commandeDAO.ajouter(nouvelleCommande);

            if (commandeId <= 0) {
                throw new Exception("Erreur lors de la création de la commande");
            }

            nouvelleCommande.setId(commandeId);

            // Sauvegarder les lignes de commande
            LigneCommandeDAO ligneCommandeDAO = new LigneCommandeDAOImpl();
            ProduitMarqueDAO produitMarqueDAO = new ProduitMarqueDAOImpl();

            // Créer un mapping temporaire des noms de produits vers les IDs de ProduitMarque
            Map<String, Integer> produitMarqueIds = new HashMap<>();
            for (ProduitMarque pm : produitMarqueDAO.trouverTous()) {
                String nomComplet = pm.getProduit().getNom();
                produitMarqueIds.put(nomComplet, pm.getId());
            }

            // Ajouter les lignes de commande
            for (Map.Entry<String, Integer> entry : panierProduits.entrySet()) {
                String nomProduit = entry.getKey();
                int quantite = entry.getValue();
                ProduitMarqueDAO pmDao = new ProduitMarqueDAOImpl();
                ProduitMarque pm = pmDao.trouverParNomProduit(nomProduit); // À implémenter si nécessaire
                if (pm != null) {
                    pmDao.decrementerStock(pm.getId(), quantite);
                }
                if (!produitMarqueIds.containsKey(nomProduit) || !prixProduits.containsKey(nomProduit)) {
                    continue; // Ignorer les produits qui ne sont pas dans la base
                }

                LigneCommande ligne = new LigneCommande();
                ligne.setCommandeId(commandeId);
                ligne.setProduitMarqueId(produitMarqueIds.get(nomProduit));
                ligne.setQuantite(quantite);
                ligne.setPrixUnitaire(prixProduits.get(nomProduit));

                ligneCommandeDAO.ajouter(ligne);
            }

            // Afficher confirmation et fermer
            JOptionPane.showMessageDialog(this,
                    "Votre commande a été enregistrée avec succès!\n" +
                            "Numéro de commande: " + commandeId,
                    "Commande confirmée",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

            // Optionnel: ouvrir l'historique des commandes
            ouvrirHistoriqueCommandes();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Une erreur est survenue lors de l'enregistrement de la commande: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ouvrirHistoriqueCommandes() {
        HistoriqueCommandeDialog historique = new HistoriqueCommandeDialog(
                (JFrame) SwingUtilities.getWindowAncestor(getParent()),
                utilisateurConnecte);
        historique.setVisible(true);
    }
}