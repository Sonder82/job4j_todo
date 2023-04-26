package ru.job4j.todo.repository;

import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

public interface UserRepository {

    Optional<User> save(User user);
    Optional<User> findByLoginAndPassword(String login, String password);
    List<TimeZone> listZone();
}
