package persistence.sql.ddl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.sql.meta.ColumnField;
import persistence.sql.meta.entityType.H2ColumnType;
import persistence.sql.sample.Person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ColumnTest {

    private static final Logger logger = LoggerFactory.getLogger(ColumnTest.class);

    @Test
    @DisplayName("Person Class로 Column 생성 : id Field에 대하여 name, Column Type, primary Key 여부가 잘 변환되었는지 확인")
    void createColumnClassWithPersonClass_id() throws Exception {
        ColumnField idColumn = new ColumnField(Person.class.getDeclaredField("id"));
        logger.info("{} 의 타입은 {}, primary 는 {}", idColumn.getName(), idColumn.getColumnType().getJavaType(), idColumn.isPrimaryKey());
        assertAll(
                () -> assertThat(idColumn.getColumnType()).isEqualTo(H2ColumnType.BIGINT),
                () -> assertThat(idColumn.getName()).isEqualTo("id"),
                () -> assertThat(idColumn.isPrimaryKey()).isTrue()
        );
    }

    @Test
    @DisplayName("Person Class로 Column 생성 : name Field에 대하여 name, Column Type, primary Key 여부가 잘 변환되었는지 확인")
    void createColumnClassWithPersonClass_name() throws Exception {
        ColumnField nameColumn = new ColumnField(Person.class.getDeclaredField("name"));
        logger.info("{} 의 타입은 {}, primary 는 {}", nameColumn.getName(), nameColumn.getColumnType().getJavaType(), nameColumn.isPrimaryKey());
        assertAll(
                () -> assertThat(nameColumn.getColumnType()).isEqualTo(H2ColumnType.VARCHAR),
                () -> assertThat(nameColumn.getName()).isEqualTo("nick_name"),
                () -> assertThat(nameColumn.isPrimaryKey()).isFalse()
        );
    }

    @Test
    @DisplayName("Person Class로 Column 생성 : age Field에 대하여 name, Column Type, primary Key 여부가 잘 변환되었는지 확인")
    void createColumnClassWithPersonClass_age() throws Exception {
        ColumnField ageColumn = new ColumnField(Person.class.getDeclaredField("age"));
        logger.info("{} 의 타입은 {}, primary 는 {}", ageColumn.getName(), ageColumn.getColumnType().getJavaType(), ageColumn.isPrimaryKey());
        assertAll(
                () -> assertThat(ageColumn.getColumnType()).isEqualTo(H2ColumnType.INTEGER),
                () -> assertThat(ageColumn.getName()).isEqualTo("old"),
                () -> assertThat(ageColumn.isPrimaryKey()).isFalse()
        );
    }
}
