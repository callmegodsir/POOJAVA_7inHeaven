package dao;

import model.ProduitMarque;
import java.util.List;

public interface ProduitMarqueDAO {
    List<ProduitMarque> trouverTous();
    List<ProduitMarque> trouverParMarque(int marqueId);
    ProduitMarque trouverParId(int id);
    void mettreAJourStock(int id, int nouveauStock);
}