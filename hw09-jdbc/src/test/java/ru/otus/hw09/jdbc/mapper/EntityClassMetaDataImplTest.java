package ru.otus.hw09.jdbc.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw09.core.model.Account;
import ru.otus.hw09.core.model.User;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityClassMetaDataImplTest {

    private EntityClassMetaData<User> userClassMetaData;
    private EntityClassMetaData<Account> accountClassMetaData;

    @BeforeEach
    void setUp() {
        userClassMetaData = new EntityClassMetaDataImpl<>(User.class);
        accountClassMetaData = new EntityClassMetaDataImpl<>(Account.class);
    }

    @Test
    void shouldReturnName() {
        assertEquals("user", userClassMetaData.getName());
        assertEquals("account", accountClassMetaData.getName());
    }

    @Test
    void shouldReturnIdField() {
        assertEquals("id", userClassMetaData.getIdField().getName());
        assertEquals("no", accountClassMetaData.getIdField().getName());
    }

    @Test
    void shouldReturnAllFields() {
        List<String> userAllFields = userClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.toList());
        Assertions.assertThat(userAllFields).containsExactlyInAnyOrderElementsOf(List.of("id", "name"));

        List<String> accountAllFields = accountClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.toList());
        Assertions.assertThat(accountAllFields).containsExactlyInAnyOrderElementsOf(List.of("no", "type", "rest"));
    }

    @Test
    void shouldReturnFieldsWithoutId() {
        List<String> userFieldsWithoutId = userClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.toList());
        Assertions.assertThat(userFieldsWithoutId).containsExactlyInAnyOrderElementsOf(List.of("name"));

        List<String> accountFieldsWithoutId = accountClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.toList());
        Assertions.assertThat(accountFieldsWithoutId).containsExactlyInAnyOrderElementsOf(List.of("type", "rest"));
    }

    @Test
    void shouldReturnConstructorWithNoParameters() {
        assertEquals(0, userClassMetaData.getConstructor().getParameterCount());
        assertEquals(0, accountClassMetaData.getConstructor().getParameterCount());
    }


}