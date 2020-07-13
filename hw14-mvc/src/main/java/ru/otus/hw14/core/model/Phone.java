package ru.otus.hw14.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "phones")
public class Phone {
    @Id
    @Column(length = 36)
    private String uuid;
    @Column(name = "phone_number")
    private String number;
}
