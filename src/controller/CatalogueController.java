package controller;

import dao.ProduitMarqueDAO;
import dao.ProduitMarqueDAOImpl;
import model.ProduitMarque;
import java.util.List;

public class CatalogueController {
    private ProduitMarqueDAO pmDao = new ProduitMarqueDAOImpl();

    public List<ProduitMarque> getTousLesProduits() {
        return pmDao.trouverTous();
    }

    public List<ProduitMarque> getProduitsParMarque(int marqueId) {
        return pmDao.trouverParMarque(marqueId);
    }
}