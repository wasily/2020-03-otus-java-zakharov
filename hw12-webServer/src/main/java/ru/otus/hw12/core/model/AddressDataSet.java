package ru.otus.hw12.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "addresses")
public class AddressDataSet {
    @Id
    @Column(length = 36)
    private String uuid;
    @Column(name = "street")
    private String street;
}
