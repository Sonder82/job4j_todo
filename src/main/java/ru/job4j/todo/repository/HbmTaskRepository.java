package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.*;

@Repository
@AllArgsConstructor
public class HbmTaskRepository implements TaskRepository, AutoCloseable {

    /**
     * В поле создаем объект {@link StandardServiceRegistry}
     * Метод configure читает файл hibernate.cfg.xml и выполняет инициализацию пула и кешей.
     */
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private static final Logger LOG = LoggerFactory.getLogger(HbmTaskRepository.class.getName());

    private final CrudRepository crudRepository;

    @Override
    public Optional<Task> add(Task task) {
        Optional<Task> rsl = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(task));
            rsl = Optional.of(task);
        } catch (Exception e) {
            LOG.error("Error message: " + e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public boolean update(Task task) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.merge(task));
            rsl = true;
        } catch (Exception e) {
            LOG.error("Error message: " + e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public boolean updateFieldDone(int id) {
        boolean rsl = false;
        try {
            crudRepository.run(
                    "Update Task SET done = :fDone  WHERE id = :fId",
                    Map.of("fId", id, "fDone", true)
            );
            rsl = true;
        } catch (Exception e) {
            LOG.error("Error message: " + e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public boolean deleteById(int id) {
        boolean rsl = false;
        try {
            crudRepository.run(
                    "DELETE Task WHERE id = :fId",
                    Map.of("fId", id)
            );
            rsl = true;
        } catch (Exception e) {
            LOG.error("Error message: " + e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public Collection<Task> findAll() {
        return crudRepository.query(
                "SELECT DISTINCT f FROM Task f JOIN FETCH f.priority JOIN FETCH f.categories", Task.class
        );
    }

    @Override
    public Collection<Task> findByDone(boolean key) {
        return crudRepository.query(
                "FROM Task f JOIN FETCH f.priority JOIN FETCH f.categories WHERE f.done = :fDone", Task.class,
                Map.of("fDone", key)
        );
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "FROM Task f JOIN FETCH f.priority JOIN FETCH f.categories WHERE f.id = :fId", Task.class,
                Map.of("fId", id)
        );
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
