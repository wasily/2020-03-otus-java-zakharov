package ru.otus.hw11.core.model;


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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @OneToMany(targetEntity = PhoneDataSet.class, cascade = {MERGE, PERSIST, DETACH, REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private Set<PhoneDataSet> phones = new HashSet<>();

    @OneToOne(targetEntity = AddressDataSet.class, cascade = {MERGE, PERSIST, DETACH, REFRESH}, fetch = FetchType.LAZY)
    private AddressDataSet address;
}

