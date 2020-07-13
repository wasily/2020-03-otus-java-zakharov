package ru.otus.hw14.core.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@NamedEntityGraph(
        name = "userEntityGraph",
        attributeNodes = {
                @NamedAttributeNode("address"),
                @NamedAttributeNode("phones")
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_generator")
    @SequenceGenerator(name = "users_seq_generator", sequenceName = "users_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password")
    private String password;

    @OneToMany(targetEntity = Phone.class, cascade = {MERGE, PERSIST, DETACH, REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private Set<Phone> phones = new HashSet<>();

    @OneToOne(targetEntity = Address.class, cascade = {MERGE, PERSIST, DETACH, REFRESH}, fetch = FetchType.LAZY)
    private Address address;
}

