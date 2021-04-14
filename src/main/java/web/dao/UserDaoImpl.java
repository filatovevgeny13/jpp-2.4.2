package web.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web.model.User;

import javax.persistence.*;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(User user) {
        em.persist(user);
    }

    @Override
    public void delete(int id) {
        User user = em.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", (long) id).getSingleResult();
        em.remove(user);
    }

    @Override
    public void update(User user) {
        em.merge(user);
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    @Override
    public User findById(int id) {
        return em.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", (long) id).getSingleResult();
    }

    @Override
    public User findByEmail(String email) {
        return em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email).getSingleResult();
    }
}
