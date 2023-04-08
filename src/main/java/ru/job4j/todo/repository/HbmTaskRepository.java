package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
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

    private final CrudRepository crudRepository;

    @Override
    public Task add(Task task) {
        crudRepository.run(session -> session.persist(task));
        return task;
    }

    @Override
    public boolean update(Task task) {
        try {
            crudRepository.run(session -> session.merge(task));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateFieldDone(int id) {
        try {
            crudRepository.run(
                    "UPDATE Task SET done = :fDone WHERE id = :fId",
                    Map.of("fId", id)
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try {
            crudRepository.run(
                    "DELETE Task WHERE id = :fId",
                    Map.of("fId", id)
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Collection<Task> findAll() {
        return crudRepository.query(
                "FROM Task ORDER BY id", Task.class
        );
    }

    @Override
    public Collection<Task> findByDone(boolean key) {
        return crudRepository.query(
                "FROM Task WHERE done = :fDone", Task.class,
                Map.of("fDone", key)
        );
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "FROM Task WHERE id = :fId", Task.class,
                Map.of("fId", id)
        );
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
