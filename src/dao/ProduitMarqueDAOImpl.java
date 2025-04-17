package dao;

import config.DatabaseConfig;
import model.ProduitMarque;
import model.Produit;
import model.Marque;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitMarqueDAOImpl implements ProduitMarqueDAO {

    @Override
    public List<ProduitMarque> trouverTous() {
        List<ProduitMarque> produits = new ArrayList<>();
        String sql = "SELECT pm.id, p.nom, m.nom as marque, pm.prix_unitaire, "
                + "pm.prix_vente_groupé, pm.quantité_vente_groupé, pm.stock "
                + "FROM ProduitMarques pm "
                + "JOIN Produits p ON pm.produit_id = p.id "
                + "JOIN Marques m ON pm.marque_id = m.id";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produits.add(mapProduitMarque(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération produits: " + e.getMessage());
        }
        return produits;
    }

    @Override
    public List<ProduitMarque> trouverParMarque(int marqueId) {
        List<ProduitMarque> produits = new ArrayList<>();
        String sql = "SELECT * FROM ProduitMarques WHERE marque_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, marqueId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                produits.add(mapProduitMarque(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche par marque: " + e.getMessage());
        }
        return produits;
    }

    @Override
    public ProduitMarque trouverParId(int id) {
        String sql = "SELECT * FROM ProduitMarques WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return mapProduitMarque(rs);
        } catch (SQLException e) {
            System.err.println("Erreur recherche par ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void mettreAJourStock(int id, int nouveauStock) {
        String sql = "UPDATE ProduitMarques SET stock = ?, derniere_modification = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nouveauStock);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur mise à jour stock: " + e.getMessage());
        }
    }
    @Override
    public void mettreAJourPromotion(int id, double prixGroupe, int quantiteGroupe) {
        String sql = "UPDATE ProduitMarques SET prix_vente_groupé = ?, quantité_vente_groupé = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, prixGroupe);
            stmt.setInt(2, quantiteGroupe);
            stmt.setInt(3, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour promotion: " + e.getMessage());
        }
    }

    @Override
    public ProduitMarque trouverParNomProduit(String nomProduit) {
        String sql = "SELECT pm.*, p.nom, m.nom as marque "
                + "FROM ProduitMarques pm "
                + "JOIN Produits p ON pm.produit_id = p.id "
                + "JOIN Marques m ON pm.marque_id = m.id "
                + "WHERE p.nom = ? LIMIT 1";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomProduit);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return mapProduitMarque(rs);
        } catch (SQLException e) {
            System.err.println("Erreur recherche par nom: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void decrementerStock(int id, int quantite) {
        String sql = "UPDATE ProduitMarques SET stock = stock - ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantite);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur décrementation stock: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> getStockData() {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT pm.id, p.nom, m.nom as marque, pm.prix_unitaire, pm.stock, pm.derniere_modification "
                + "FROM ProduitMarques pm "
                + "JOIN Produits p ON pm.produit_id = p.id "
                + "JOIN Marques m ON pm.marque_id = m.id";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                data.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("marque"),
                        rs.getDouble("prix_unitaire"),
                        rs.getInt("stock"),
                        rs.getTimestamp("derniere_modification")
                });
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération stock: " + e.getMessage());
        }
        return data;
    }

    @Override
    public List<Object[]> getTopVentes() {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT p.nom, SUM(lc.quantité) AS total "
                + "FROM LignesCommande lc "
                + "JOIN ProduitMarques pm ON pm.id = lc.produit_marque_id "
                + "JOIN Produits p ON p.id = pm.produit_id "
                + "GROUP BY p.nom ORDER BY total DESC LIMIT 10";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                data.add(new Object[]{
                        rs.getString("nom"),
                        rs.getInt("total")
                });
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération top ventes: " + e.getMessage());
        }
        return data;
    }

    @Override
    public void updateStock(int id, int nouveauStock) {
        mettreAJourStock(id, nouveauStock);
    }

    private ProduitMarque mapProduitMarque(ResultSet rs) throws SQLException {
        ProduitMarque pm = new ProduitMarque();
        pm.setId(rs.getInt("id"));
        pm.setPrixUnitaire(rs.getDouble("prix_unitaire"));
        pm.setPrixGroupe(rs.getDouble("prix_vente_groupé"));
        pm.setQuantiteGroupe(rs.getInt("quantité_vente_groupé"));
        pm.setStock(rs.getInt("stock"));

        Produit p = new Produit();
        p.setNom(rs.getString("nom"));
        pm.setProduit(p);

        Marque m = new Marque();
        m.setNom(rs.getString("marque"));
        pm.setMarque(m);

        return pm;
    }
}