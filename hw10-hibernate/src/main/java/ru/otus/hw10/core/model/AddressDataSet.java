package ru.otus.hw10.core.model;

import lombok.Data;

import java.util.UUID;

@Data
public class AddressDataSet {
    private UUID uuid;
    private String street;
}
