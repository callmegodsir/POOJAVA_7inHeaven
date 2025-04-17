package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import dao.ProduitMarqueDAO;
import dao.ProduitMarqueDAOImpl;
import model.ProduitMarque;

public class GestionPromotionPanel extends JPanel {
    private JComboBox<ProduitMarque> produitCombo;
    private JSpinner quantiteSpinner;
    private JSpinner prixSpinner;
    private ProduitMarqueDAO dao;

    public GestionPromotionPanel() {
        dao = new ProduitMarqueDAOImpl();
        setLayout(new BorderLayout());

        // Panel de formulaire
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Sélection produit
        formPanel.add(new JLabel("Produit:"));
        produitCombo = new JComboBox<>();
        chargerProduits();
        formPanel.add(produitCombo);

        // Quantité pour promotion
        formPanel.add(new JLabel("Quantité requise:"));
        quantiteSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 100, 1));
        formPanel.add(quantiteSpinner);

        // Prix promotionnel
        formPanel.add(new JLabel("Prix groupe:"));
        prixSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.5));
        formPanel.add(prixSpinner);

        add(formPanel, BorderLayout.CENTER);

        // Bouton de sauvegarde
        JButton btnSauvegarder = new JButton("Sauvegarder promotion");
        btnSauvegarder.addActionListener(this::sauvegarderPromotion);
        add(btnSauvegarder, BorderLayout.SOUTH);
    }

    private void chargerProduits() {
        produitCombo.removeAllItems();
        for (ProduitMarque pm : dao.trouverTous()) {
            produitCombo.addItem(pm);
        }
    }

    private void sauvegarderPromotion(ActionEvent e) {
        ProduitMarque pm = (ProduitMarque) produitCombo.getSelectedItem();
        int quantite = (int) quantiteSpinner.getValue();
        double prix = (double) prixSpinner.getValue();

        if (pm != null) {
            dao.mettreAJourPromotion(pm.getId(), prix, quantite);
            JOptionPane.showMessageDialog(this, "Promotion mise à jour !");
        }
    }
}