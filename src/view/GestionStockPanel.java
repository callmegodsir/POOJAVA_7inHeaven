package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import dao.ProduitMarqueDAO;
import dao.ProduitMarqueDAOImpl;

public class GestionStockPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private ProduitMarqueDAO dao;

    public GestionStockPanel() {
        dao = new ProduitMarqueDAOImpl();
        setLayout(new BorderLayout());

        // Configuration du modèle de tableau
        String[] colonnes = {"ID", "Produit", "Marque", "Prix", "Stock", "Dernière modif"};
        model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Seul le stock est éditable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Integer.class;
                return super.getColumnClass(columnIndex);
            }
        };

        // Chargement des données initiales
        chargerDonnees();

        // Création du tableau avec rendu personnalisé
        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setShowGrid(true);
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(200, 220, 255));
        table.setPreferredScrollableViewportSize(new Dimension(800, 400));
        table.setFillsViewportHeight(true);

        // Panel de défilement
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Bouton d'ajout de stock
        JButton btnAjouterStock = new JButton("Ajouter Stock");
        btnAjouterStock.addActionListener(this::gererAjoutStock);
        buttonPanel.add(btnAjouterStock);

        // Bouton de sauvegarde
        JButton btnSauvegarder = new JButton("Sauvegarder modifications");
        btnSauvegarder.addActionListener(this::sauvegarderModifications);
        buttonPanel.add(btnSauvegarder);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void chargerDonnees() {
        model.setRowCount(0); // Réinitialiser le modèle
        List<Object[]> stockData = dao.getStockData();

        for (Object[] row : stockData) {
            model.addRow(new Object[]{
                    row[0], // ID
                    row[1], // Produit
                    row[2], // Marque
                    String.format("%.2f €", row[3]), // Prix
                    row[4], // Stock
                    row[5]  // Dernière modification
            });
        }
    }

    private void sauvegarderModifications(ActionEvent e) {
        for (int i = 0; i < model.getRowCount(); i++) {
            int id = (int) model.getValueAt(i, 0);
            int nouveauStock = (int) model.getValueAt(i, 4);

            try {
                dao.mettreAJourStock(id, nouveauStock);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la mise à jour du stock: " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        chargerDonnees(); // Rafraîchir les données
        JOptionPane.showMessageDialog(this,
                "Stock mis à jour avec succès !",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void gererAjoutStock(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un produit",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Création du formulaire d'ajout
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Quantité à ajouter:"));
        panel.add(spinner);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Ajout de stock",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            int quantiteAjout = (int) spinner.getValue();
            int stockActuel = (int) model.getValueAt(selectedRow, 4);
            int idProduit = (int) model.getValueAt(selectedRow, 0);

            // Mise à jour locale
            model.setValueAt(stockActuel + quantiteAjout, selectedRow, 4);

            // Mise à jour en base
            dao.mettreAJourStock(idProduit, stockActuel + quantiteAjout);
        }
    }

    // Méthode pour formater la date
    private String formaterDate(java.sql.Timestamp timestamp) {
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }
}