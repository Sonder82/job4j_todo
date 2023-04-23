package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.HbmPriorityRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SimplePriorityService implements PriorityService {

    private final HbmPriorityRepository hbmPriorityRepository;

    @Override
    public List<Priority> findAll() {
        return hbmPriorityRepository.findAll();
    }

}
