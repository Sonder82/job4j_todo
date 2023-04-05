package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "todo_users")
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @EqualsAndHashCode.Include
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String password;

}
