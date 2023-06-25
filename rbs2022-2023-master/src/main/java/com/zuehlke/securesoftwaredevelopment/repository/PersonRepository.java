package com.zuehlke.securesoftwaredevelopment.repository;

import com.zuehlke.securesoftwaredevelopment.config.AuditLogger;
import com.zuehlke.securesoftwaredevelopment.config.Entity;
import com.zuehlke.securesoftwaredevelopment.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PersonRepository.class);
    private static final AuditLogger auditLogger = AuditLogger.getAuditLogger(PersonRepository.class);

    private DataSource dataSource;

    public PersonRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Person> getAll() {
        List<Person> personList = new ArrayList<>();
        String query = "SELECT id, firstName, lastName, email FROM persons";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                personList.add(createPersonFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOG.warn("Getting list of persons failed!", e);
        }
        return personList;
    }

    public List<Person> search(String searchTerm){
        List<Person> personList = new ArrayList<>();
        String query = "SELECT id, firstName, lastName, email FROM persons WHERE UPPER(firstName) like UPPER('%" + searchTerm + "%')" +
                " OR UPPER(lastName) like UPPER('%" + searchTerm + "%')";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                personList.add(createPersonFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOG.warn("Searching for a list of persons with search term: " + searchTerm + " failed!");
        }
        return personList;
    }

    public Person get(String personId) {
        String query = "SELECT id, firstName, lastName, email FROM persons WHERE id = " + personId;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                return createPersonFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOG.warn("Getting person with id: " + personId + " failed!");
        }

        return null;
    }

    public void delete(int personId) {
        String query = "DELETE FROM persons WHERE id = " + personId;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(query);
            AuditLogger.getAuditLogger(PersonRepository.class).audit("Deleted user with id " + personId);
        } catch (SQLException e) {
            LOG.warn("Deleting person with id: " + personId + " failed!");
        }
    }

    private Person createPersonFromResultSet(ResultSet rs){
        try{
            int id = rs.getInt(1);
            String firstName = rs.getString(2);
            String lastName = rs.getString(3);
            String email = rs.getString(4);
            return new Person("" + id, firstName, lastName, email);
        }catch (SQLException e) {
            LOG.warn("Creating person with ResultSet failed!");
        }
        return null;
    }

    public void update(Person personUpdate) {
        Person personFromDb = get(personUpdate.getId());
        String query = "UPDATE persons SET firstName = ?, lastName = '" + personUpdate.getLastName() + "', email = ? where id = " + personUpdate.getId();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            String firstName = personUpdate.getFirstName() != null ? personUpdate.getFirstName() : personFromDb.getFirstName();
            String email = personUpdate.getEmail() != null ? personUpdate.getEmail() : personFromDb.getEmail();
            statement.setString(1, firstName);
            statement.setString(2, email);
            statement.executeUpdate();
            if (!personFromDb.getFirstName().equals(personUpdate.getFirstName())){
                AuditLogger.getAuditLogger(PersonRepository.class).auditChange(
                        new Entity("person.firstName", String.valueOf(personFromDb.getId()), String.valueOf(personFromDb.getFirstName()), String.valueOf(personUpdate.getFirstName()))
                );
            }
            if (!personFromDb.getLastName().equals(personUpdate.getLastName())) {
                AuditLogger.getAuditLogger(PersonRepository.class).auditChange(
                        new Entity("person.lastName", String.valueOf(personFromDb.getId()), String.valueOf(personFromDb.getLastName()), String.valueOf(personUpdate.getLastName()))
                );
            }
            if (!personFromDb.getEmail().equals(personUpdate.getEmail())) {
                AuditLogger.getAuditLogger(PersonRepository.class).auditChange(
                        new Entity("person.email", String.valueOf(personFromDb.getId()), String.valueOf(personFromDb.getEmail()), String.valueOf(personUpdate.getEmail()))
                );
            }
        } catch (SQLException e) {
            LOG.warn("Updating person with id: " + personFromDb.getId() + " failed!");
        }
    }
}
