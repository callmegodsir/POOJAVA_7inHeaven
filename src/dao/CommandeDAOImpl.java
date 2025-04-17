package dao;
import model.Commande;
import config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAOImpl implements CommandeDAO {
    @Override
    public int ajouter(Commande commande) {
        String sql = "INSERT INTO commandes (client_id, date_commande, statut, total) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, commande.getClientId());
            pstmt.setTimestamp(2, new Timestamp(commande.getDateCommande().getTime()));
            pstmt.setString(3, commande.getStatut());
            pstmt.setDouble(4, commande.getTotal());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Commande> trouverParClientId(int clientId) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE client_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                commandes.add(new Commande(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getTimestamp("date_commande"),
                        rs.getString("statut"),
                        rs.getDouble("total")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }
}