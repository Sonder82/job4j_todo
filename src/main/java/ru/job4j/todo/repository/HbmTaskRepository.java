package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmTaskRepository implements TaskRepository, AutoCloseable {

    /**
     * В поле создаем объект {@link StandardServiceRegistry}
     * Метод configure читает файл hibernate.cfg.xml и выполняет инициализацию пула и кешей.
     */
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    /**
     * Объект конфигуратор {@link SessionFactory}
     */
    private final SessionFactory sf;

    @Override
    public Task add(Task task) {
        Session session = sf.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public boolean update(Task task) {
        boolean result = false;
        Session session = sf.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            var query = session.createQuery(
                    "UPDATE Task SET description = :fDescription, created = :fCreated, done = :fDone WHERE id = :fId");

            query.setParameter("fDescription", task.getDescription());
            query.setParameter("fCreated", task.getCreated());
            query.setParameter("fDone", task.isDone());
            query.setParameter("fId", task.getId());
            result = query.executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean result = false;
        Session session = sf.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            var query = session.createQuery(
                    "DELETE Task WHERE id = :fId");
            query.setParameter("fId", id);
            result = query.executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Collection<Task> findAll() {
        List<Task> taskList = new ArrayList<>();
        Session session = sf.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "FROM Task ORDER BY id", Task.class);
            taskList = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return taskList;
    }

    @Override
    public Collection<Task> findByDone(boolean key) {
        List<Task> taskList = new ArrayList<>();
        Session session = sf.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "FROM Task WHERE done = :fDone", Task.class);
            query.setParameter("fDone", key);
            taskList = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return taskList;
    }

    @Override
    public Optional<Task> findById(Integer id) {
        Optional<Task> task = Optional.empty();
        Session session = sf.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            var query = session.createQuery(
                    "FROM Task WHERE id = :fId", Task.class);
            query.setParameter("fId", id);
            task = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public void close()  {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
