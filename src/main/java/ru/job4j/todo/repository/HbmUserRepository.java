package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmUserRepository implements UserRepository {

    private final SessionFactory sf;

    @Override
    public Optional<User> save(User user) {
        Optional<User> rsl = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            rsl = Optional.ofNullable(user);
        } catch (Exception e) {
           session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> user = Optional.empty();
        Session session = sf.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            var query = session.createQuery(
                    "FROM User WHERE login = :fLogin AND password = :fPassword", User.class);
            query.setParameter("fLogin", login);
            query.setParameter("fPassword", password);
            user = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return user;
    }
}
