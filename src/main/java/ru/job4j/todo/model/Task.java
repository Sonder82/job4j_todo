package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

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
    private Timestamp created = new Timestamp(System.currentTimeMillis());

    @Getter
    @Setter
    private boolean done;

}

