package dao;

import model.LigneCommande;
import config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAOImpl implements LigneCommandeDAO {

    @Override
    public List<LigneCommande> trouverParCommandeId(int commandeId) {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT * FROM LignesCommande WHERE commande_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, commandeId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LigneCommande ligne = new LigneCommande(
                        rs.getInt("id"),
                        rs.getInt("commande_id"),
                        rs.getInt("produit_marque_id"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix_unitaire")
                );
                lignes.add(ligne);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lignes;
    }

    @Override
    public int ajouter(LigneCommande ligneCommande) {
        String sql = "INSERT INTO LignesCommande (commande_id, produit_marque_id, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, ligneCommande.getCommandeId());
            pstmt.setInt(2, ligneCommande.getProduitMarqueId());
            pstmt.setInt(3, ligneCommande.getQuantite());
            pstmt.setDouble(4, ligneCommande.getPrixUnitaire());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}