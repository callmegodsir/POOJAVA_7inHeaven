package dao;

import config.DatabaseConfig;
import model.ProduitMarque;
import model.Produit;
import model.Marque; // Ajout d'import manquant
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
                ProduitMarque pm = new ProduitMarque();
                pm.setId(rs.getInt("id"));

                // Correction des noms de méthodes
                pm.setPrixGroupe(rs.getDouble("prix_vente_groupé"));
                pm.setQuantiteGroupe(rs.getInt("quantité_vente_groupé"));

                pm.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                pm.setStock(rs.getInt("stock"));

                Produit p = new Produit();
                p.setNom(rs.getString("nom"));
                pm.setProduit(p);

                Marque m = new Marque();
                m.setNom(rs.getString("marque"));
                pm.setMarque(m);

                produits.add(pm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

    // Implémentation des méthodes manquantes de l'interface
    @Override
    public List<ProduitMarque> trouverParMarque(int marqueId) {
        // Implémentez cette méthode
        return new ArrayList<>();
    }

    @Override
    public ProduitMarque trouverParId(int id) {
        // Implémentez cette méthode
        return null;
    }

    @Override
    public void mettreAJourStock(int id, int nouveauStock) {
        // Implémentez cette méthode
    }
}