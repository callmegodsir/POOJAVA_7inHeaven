package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import dao.ClientDAO;
import dao.ClientDAOImpl;
import model.User;
import javax.swing.table.DefaultTableModel;

public class GestionClientsPanel extends JPanel {
    private JTable table;
    private ClientDAO dao;
    private DefaultTableModel model;

    public GestionClientsPanel() {
        dao = new ClientDAOImpl();
        setLayout(new BorderLayout());

        // Tableau
        String[] colonnes = {"ID", "Nom", "Prénom", "Email", "Nom d'utilisateur"};
        model = new DefaultTableModel(colonnes, 0);
        chargerDonnees(model);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel de boutons
        JPanel buttonPanel = new JPanel();

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterClient());

        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(e -> modifierClient());

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(e -> supprimerClient());

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void chargerDonnees(DefaultTableModel model) {
        model.setRowCount(0);
        for (User client : dao.trouverTousClients()) {
            model.addRow(new Object[]{
                    client.getId(),
                    client.getLastName(),
                    client.getFirstName(),
                    client.getEmail(),
                    client.getUsername()
            });
        }
    }

    private void ajouterClient() {
        // Fenêtre de dialogue pour saisir les infos
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom:"));
        panel.add(prenomField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Nom d'utilisateur:"));
        panel.add(usernameField);
        panel.add(new JLabel("Mot de passe:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Nouveau client",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            User nouveauClient = new User();
            nouveauClient.setLastName(nomField.getText());
            nouveauClient.setFirstName(prenomField.getText());
            nouveauClient.setEmail(emailField.getText());
            nouveauClient.setUsername(usernameField.getText());
            nouveauClient.setPassword(new String(passwordField.getPassword()));

            dao.ajouterClient(nouveauClient);
            chargerDonnees((DefaultTableModel) table.getModel());
        }
    }

    private void modifierClient() {
        // Logique similaire à ajouterClient avec pré-remplissage
    }

    private void supprimerClient() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) table.getValueAt(row, 0);
            dao.supprimerClient(id);
            chargerDonnees((DefaultTableModel) table.getModel());
        }
    }
}