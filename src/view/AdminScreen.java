package view;

import javax.swing.*;
import java.awt.*;
import dao.ProduitMarqueDAO;
import dao.ProduitMarqueDAOImpl;
import model.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AdminScreen extends JFrame {
    private final User adminUser;

    public AdminScreen(User user) {
        this.adminUser = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Panel Administrateur - 7 in Heaven");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane onglets = new JTabbedPane();

        // Onglets
        onglets.addTab("📦 Inventaire", new GestionStockPanel());
        onglets.addTab("🎯 Promotions", new GestionPromotionPanel());
        onglets.addTab("👥 Clients", new GestionClientsPanel());
        onglets.addTab("📊 Statistiques", new StatistiquesPanel());

        add(onglets);
    }

    // Classe interne pour la gestion du stock
    class GestionStockPanel extends JPanel {
        public GestionStockPanel() {
            setLayout(new BorderLayout());

            ProduitMarqueDAO dao = new ProduitMarqueDAOImpl();
            List<Object[]> stockData = dao.getStockData();

            String[] colonnes = {"ID", "Produit", "Marque", "Prix", "Stock", "Dernière modif"};
            DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 4; // Seul le stock est éditable
                }
            };

            for (Object[] row : stockData) {
                model.addRow(row);
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            JButton btnSave = new JButton("Sauvegarder modifications");
            btnSave.addActionListener(e -> {
                for (int i = 0; i < model.getRowCount(); i++) {
                    int id = (int) model.getValueAt(i, 0);
                    int stock = (int) model.getValueAt(i, 4);
                    // Utiliser mettreAJourStock() au lieu de updateStock()
                    dao.mettreAJourStock(id, stock);
                }
                JOptionPane.showMessageDialog(this, "Stock mis à jour !");
            });
            add(btnSave, BorderLayout.SOUTH);
        }
    }

    // Classe interne pour les statistiques
    class StatistiquesPanel extends JPanel {
        public StatistiquesPanel() {
            setLayout(new BorderLayout());

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            ProduitMarqueDAO dao = new ProduitMarqueDAOImpl();

            // Récupération des données
            List<Object[]> ventesData = dao.getTopVentes();
            for (Object[] data : ventesData) {
                dataset.addValue((Number) data[1], "Ventes", data[0].toString());
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Top des produits vendus",
                    "Produits",
                    "Quantité vendue",
                    dataset
            );

            ChartPanel chartPanel = new ChartPanel(chart);
            add(chartPanel, BorderLayout.CENTER);
        }
    }

    // Méthode de vérification des droits
    public static boolean verifierDroits(User user) {
        return user != null && "admin".equals(user.getRole());
    }
}