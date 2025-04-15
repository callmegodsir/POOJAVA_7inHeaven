package dao;

import model.User;
import java.util.List;

public interface UserDAO {
    User getById(int id);
    List<User> getAll();
    void save(User user);
    void update(User user);
    void delete(int id);
    User findByUsername(String username);
}