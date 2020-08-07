package hw16.core.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_generator")
    @SequenceGenerator(name = "users_seq_generator", sequenceName = "users_seq", allocationSize = 1)
    @Column(name = "id")
    @JsonProperty(value = "id")
    private long id;

    @Column(name = "name")
    @JsonProperty(value = "name")
    private String name;

    @Column(name = "login", unique = true, nullable = false)
    @JsonProperty(value = "login")
    private String login;

    @Column(name = "password")
    @JsonProperty(value = "password")
    private String password;
}

