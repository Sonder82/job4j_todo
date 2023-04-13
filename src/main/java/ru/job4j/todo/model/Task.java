package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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

}

