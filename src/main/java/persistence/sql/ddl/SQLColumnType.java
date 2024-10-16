package persistence.sql.ddl;

import java.util.Arrays;

public enum SQLColumnType implements ColumnType {
    BIGINT(Long.class, "bigint"),
    VARCHAR(String.class, "varchar2"),
    INTEGER(Integer.class, "INT"),
    ;

    private final Class<?> javaType;
    private final String queryDefinition;

    SQLColumnType(Class<?> javaType, String columnType) {
        this.javaType = javaType;
        this.queryDefinition = columnType;
    }

    public static SQLColumnType of(Class<?> javaType) {
        return Arrays.stream(SQLColumnType.values())
                .filter(type -> type.getJavaType() == javaType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(javaType + "과 매칭되는 타입이 존재하지 않습니다."));
    }

    public String getQueryDefinition() {
        return queryDefinition;
    }

    public Class<?> getJavaType() {
        return javaType;
    }
}
