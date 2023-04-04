package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {

    Task add(Task task);

    boolean update(Task task);

    boolean deleteById(Integer id);

    Collection<Task> findAll();

    Collection<Task> findByDone(boolean key);

    Optional<Task> findById(Integer id);

    boolean updateFieldDone(Integer id);
}
