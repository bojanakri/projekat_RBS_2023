package com.zuehlke.securesoftwaredevelopment.repository;

import com.zuehlke.securesoftwaredevelopment.config.AuditLogger;
import com.zuehlke.securesoftwaredevelopment.config.Entity;
import com.zuehlke.securesoftwaredevelopment.domain.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentRepository {

    private static final Logger LOG = LoggerFactory.getLogger(CommentRepository.class);


    private DataSource dataSource;

    public CommentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void create(Comment comment) {
        String query = "insert into comments(movieId, userId, comment) values (?,?,?)";

        try (Connection connection = dataSource.getConnection();
             //Statement statement = connection.createStatement();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, comment.getMovieId());
            statement.setInt(2, comment.getUserId());
            statement.setString(3, comment.getComment());
            statement.executeUpdate();
            AuditLogger.getAuditLogger(CommentRepository.class).auditChange(
                    new Entity(
                            "comment.add",
                            comment.getUserId().toString(),
                            "Before ---",
                            comment.getComment()
                    )
            );
        } catch (SQLException e) {
            //LOG.warn("Comm adding failed", e); this showed sql insert statement, so not good solution
            LOG.warn("Comment "+comment.getComment() + "adding failed because of error made by user " + comment.getUserId().toString());
        }
    }

    public List<Comment> getAll(String movieId) {
        List<Comment> commentList = new ArrayList<>();
        String query = "SELECT movieId, userId, comment FROM comments WHERE movieId = " + movieId;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                commentList.add(new Comment(rs.getInt(1), rs.getInt(2), rs.getString(3)));
            }
        } catch (SQLException e) {
            LOG.warn("Getting all comments for movie " + movieId + " failed!");
        }
        return commentList;
    }
}
