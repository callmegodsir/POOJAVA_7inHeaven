package dao;

import model.ProduitMarque;
import java.util.List;

public interface ProduitMarqueDAO {
    // Méthodes existantes
    List<ProduitMarque> trouverTous();
    List<ProduitMarque> trouverParMarque(int marqueId);
    ProduitMarque trouverParId(int id);
    void mettreAJourStock(int id, int nouveauStock);
    void decrementerStock(int id, int quantite);
    ProduitMarque trouverParNomProduit(String nomProduit);

    // Nouvelles méthodes
    List<Object[]> getStockData();
    List<Object[]> getTopVentes();
    void updateStock(int id, int nouveauStock);
}