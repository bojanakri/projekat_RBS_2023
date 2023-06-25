package com.zuehlke.securesoftwaredevelopment.repository;

import com.zuehlke.securesoftwaredevelopment.domain.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GenreRepository {

    private static final Logger LOG = LoggerFactory.getLogger(GenreRepository.class);


    private DataSource dataSource;

    public GenreRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Genre> getAll() {
        List<Genre> genreList = new ArrayList<>();
        String query = "SELECT id, name FROM genres";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                genreList.add(new Genre(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            LOG.warn("Getting list of genres failed!");
        }
        return genreList;
    }
}
