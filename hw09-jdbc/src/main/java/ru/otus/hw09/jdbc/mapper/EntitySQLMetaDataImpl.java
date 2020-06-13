package ru.otus.hw09.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        var allFieldsNames = entityClassMetaData.getAllFields().stream().map(Field::getName)
                .sorted(Comparator.naturalOrder()).collect(Collectors.joining(","));
        var idFieldName = entityClassMetaData.getIdField().getName();
        var result = new StringBuilder().append("select ")
                .append(allFieldsNames).append(" from ")
                .append(entityClassMetaData.getName()).append(" where ")
                .append(idFieldName).append(" = ?");
        return result.toString();
    }

    @Override
    public String getInsertSql() {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName).sorted(Comparator.naturalOrder())
                .collect(Collectors.joining(","));
        var count = entityClassMetaData.getFieldsWithoutId().size();
        var result = new StringBuilder().append("insert into ").append(entityClassMetaData.getName())
                .append("(").append(fieldsWithoutId).append(")").append(" values (");
        result.append("?,".repeat(count));
        result.deleteCharAt(result.length() - 1).append(")");
        return result.toString();
    }

    @Override
    public String getUpdateSql() {
        var params = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName).sorted(Comparator.naturalOrder())
                .map(name -> name + " = ?").collect(Collectors.joining(","));
        var result = new StringBuilder().append("update ").append(entityClassMetaData.getName()).append(" set ")
                .append(params).append(" where ").append(entityClassMetaData.getIdField().getName()).append(" = ?");
        return result.toString();
    }
}
