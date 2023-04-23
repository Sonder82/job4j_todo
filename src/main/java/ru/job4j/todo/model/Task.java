package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @EqualsAndHashCode.Include
    private int id;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private LocalDateTime created = LocalDateTime.now();

    @Getter
    @Setter
    private boolean done;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "todo_user_id")
    private User user;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id")
    private Priority priority;

    @Getter
    @Setter
    @ManyToMany @JoinTable(
            name = "categories_tasks",
            joinColumns = { @JoinColumn(name = "task_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    private List<Category> categories = new ArrayList<>();
}

