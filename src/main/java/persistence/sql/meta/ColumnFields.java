package persistence.sql.meta;

import org.h2.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ColumnFields {

    private final List<ColumnField> columnFields;

    public ColumnFields(Class<?> clazz) {
        this.columnFields = extract(clazz);
    }

    private List<ColumnField> extract(Class<?> clazz) {
        if(columnFields.stream().noneMatch(ColumnField::isPrimaryKey)) {
            throw new IllegalArgumentException("Entity에 Id로 정의된 column이 존재하지 않습니다.");
        }

        return Arrays.stream(clazz.getDeclaredFields())
                .map(ColumnField::new)
                .filter(ColumnField::isNotTransient)
                .collect(Collectors.toList());
    }

    public List<ColumnField> getColumnFields() {
        return Collections.unmodifiableList(columnFields);
    }

    public List<String> getDeclaredFieldNames() {
        return columnFields
                .stream().map(ColumnField::getName).collect(Collectors.toList());
    }

    public List<ColumnField> getPrimary() {
        return columnFields.stream().filter(ColumnField::isPrimaryKey).collect(Collectors.toList());
    }

    public String getColumnClause() {
        return getDeclaredFieldNames().stream()
                .collect(Collectors.joining(", "));
    }

    public String valueClause(Object object) {
        List<Field> fields = columnFields.stream().map(ColumnField::getField).collect(Collectors.toList());
        fields.forEach(field -> field.setAccessible(true));
        return fields.stream().map(field-> {
            try {
                return String.format("\'%s\'", field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining(", "));
    }

    public String whereClauses(Object object) {
        List<ColumnField> primaryFields = new ColumnFields(object.getClass()).getPrimary();

        return primaryFields.stream().map(columnField -> {
            try {
                columnField.getField().setAccessible(true);
                return String.format("%s=%s", columnField.getName(), columnField.getField().get(object));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining(" and "));
    }
}
