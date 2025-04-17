package dao;

import model.LigneCommande;
import java.util.List;

public interface LigneCommandeDAO {
    List<LigneCommande> trouverParCommandeId(int commandeId);
    int ajouter(LigneCommande ligneCommande);
}