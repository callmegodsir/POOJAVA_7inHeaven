package dao;

import model.User;
import java.util.List;

public interface ClientDAO {
    List<User> trouverTousClients();
    void ajouterClient(User client);
    void modifierClient(User client);
    void supprimerClient(int id);
}