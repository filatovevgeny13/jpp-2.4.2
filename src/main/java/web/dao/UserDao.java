package web.dao;

import web.model.User;

import java.util.List;

public interface UserDao {
    void save(User user);

    void delete(int id);

    void update(User user);

    List<User> getAllUsers();

    User findById(int id);

    User findByEmail(String email);
}
