package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HbmCategoryRepository implements CategoryRepository {
    private final CrudRepository crudRepository;

    @Override
    public List<Category> findAll() {
        return crudRepository.query(
                "FROM Category ORDER BY id", Category.class
        );
    }

    @Override
    public List<Category> findByIdList(List<Integer> listId) {
        return crudRepository.query(
                "FROM Category WHERE id in :fId", Category.class,
                Map.of("fId", listId));
    }
}
