package ru.otus.hw11.core.model;

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
public class PhoneDataSet {
    @Id
    @Column(length = 36)
    private String uuid;
    @Column(name = "phone_number")
    private String number;
}
