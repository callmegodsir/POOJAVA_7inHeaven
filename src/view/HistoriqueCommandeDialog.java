package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import model.Commande;
import model.User;
import dao.CommandeDAO;
import dao.CommandeDAOImpl;

public class HistoriqueCommandeDialog extends JDialog {

    private final User utilisateur;
    private JTable commandesTable;
    private JTextArea detailsTextArea;
    private List<Commande> listeCommandes;

    public HistoriqueCommandeDialog(JFrame parent, User utilisateur) {
        super(parent, "Historique des Commandes", true);
        this.utilisateur = utilisateur;
        initialiserUI();
        chargerCommandes();
    }

    private void initialiserUI() {
        setSize(800, 600);
        setLocationRelativeTo(getOwner());
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] colonnes = {"N° Commande", "Date", "Statut", "Total"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);
        commandesTable = new JTable(model);
        commandesTable.getSelectionModel().addListSelectionListener(e -> afficherDetailsCommande());
        mainPanel.add(new JScrollPane(commandesTable), BorderLayout.CENTER);

        detailsTextArea = new JTextArea();
        detailsTextArea.setEditable(false);
        mainPanel.add(new JScrollPane(detailsTextArea), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void chargerCommandes() {
        CommandeDAO commandeDAO = new CommandeDAOImpl();
        listeCommandes = commandeDAO.trouverParClientId(utilisateur.getId());
        DefaultTableModel model = (DefaultTableModel) commandesTable.getModel();
        model.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Commande cmd : listeCommandes) {
            model.addRow(new Object[]{
                    cmd.getId(),
                    sdf.format(cmd.getDateCommande()),
                    cmd.getStatut(),
                    String.format("%.2f €", cmd.getTotal())
            });
        }
    }

    private void afficherDetailsCommande() {
        int selectedRow = commandesTable.getSelectedRow();
        if (selectedRow < 0 || selectedRow >= listeCommandes.size()) return;

        Commande commande = listeCommandes.get(selectedRow);
        StringBuilder sb = new StringBuilder();
        sb.append("Commande n°").append(commande.getId()).append("\n");
        sb.append("Date: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(commande.getDateCommande())).append("\n");
        sb.append("Statut: ").append(commande.getStatut()).append("\nTotal: ").append(String.format("%.2f €", commande.getTotal()));
        detailsTextArea.setText(sb.toString());
    }
}