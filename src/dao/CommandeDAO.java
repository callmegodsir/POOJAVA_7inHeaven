package dao;
import model.Commande;
import java.util.List;

public interface CommandeDAO {
    int ajouter(Commande commande);
    List<Commande> trouverParClientId(int clientId);
}