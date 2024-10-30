package persistence;

import database.DatabaseServer;
import database.H2;
import jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.sql.dml.QueryBuilderDML;
import persistence.sql.sample.Person;
import persistence.sql.ddl.QueryBuilderDDL;

import java.lang.reflect.Field;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Starting application...");
        try {
            final DatabaseServer server = new H2();
            server.start();

            final JdbcTemplate jdbcTemplate = new JdbcTemplate(server.getConnection());

            QueryBuilderDDL queryBuilderDDL = QueryBuilderDDL.getInstance();
            jdbcTemplate.execute(queryBuilderDDL.buildCreateDdl(Person.class));

            QueryBuilderDML queryBuilderDML = QueryBuilderDML.getInstance();
            Person person = getPerson();
            jdbcTemplate.execute(queryBuilderDML.insert(person));
            jdbcTemplate.execute(queryBuilderDML.findById(person));
            jdbcTemplate.execute(queryBuilderDML.deleteById(person));

            jdbcTemplate.execute(queryBuilderDDL.buildDropDdl(Person.class));

            server.stop();
        } catch (Exception e) {
            logger.error("Error occurred", e);
        } finally {
            logger.info("Application finished");
        }
    }

    private static Person getPerson() throws Exception{
        Person person = new Person();

        Field idField = person.getClass().getDeclaredField("id");
        Field nameField = person.getClass().getDeclaredField("name");
        Field ageField = person.getClass().getDeclaredField("age");
        Field emailField = person.getClass().getDeclaredField("email");

        idField.setAccessible(true);
        nameField.setAccessible(true);
        ageField.setAccessible(true);
        emailField.setAccessible(true);

        idField.set(person, 1L);
        nameField.set(person, "hyeongyeong");
        ageField.set(person, 30);
        emailField.set(person, "kohy0329");

        return person;
    }
}
