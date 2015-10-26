package edu.upc.eetac.dsa.grouptalk.dao;

import edu.upc.eetac.dsa.grouptalk.entity.Comments;
import edu.upc.eetac.dsa.grouptalk.entity.CommentsCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Carolina on 24/10/2015.
 */
public class CommentsDAOImpl implements CommentsDAO {

    @Override
    public Comments createComment(String id, String groupid, String title, String comment) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
          try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(CommentsDAOQuery.CREATE_COMMENTS);
            stmt.setString(1, id);
            stmt.setString(2, groupid);
            stmt.setString(3, title);
            stmt.setString(4, comment);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getCommentById(id);
    }

    @Override
    public Comments getCommentById(String id) throws SQLException {
        Comments comment = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CommentsDAOQuery.GET_COMMENTS_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                comment = new Comments();
                comment.setId(rs.getString("id"));
                comment.setTitle("Title");
                comment.setComment(rs.getString("comment"));
                comment.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                comment.setLastModified(rs.getTimestamp("last_modified").getTime());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return comment;
    }

    @Override
    public CommentsCollection getComments() throws SQLException {
        CommentsCollection commentsCollection = new CommentsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(CommentsDAOQuery.GET_COMMENTS);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Comments comment = new Comments();
                comment.setId(rs.getString("id"));
                comment.setTitle(rs.getString("Title"));
                comment.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                comment.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    commentsCollection.setNewestTimestamp(comment.getLastModified());
                    first = false;
                }
                commentsCollection.setOldestTimestamp(comment.getLastModified());
                commentsCollection.getComments().add(comment);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return commentsCollection;
    }

    @Override
    public Comments updateComment(String id, String title, String comment) throws SQLException {
        Comments comments = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CommentsDAOQuery.UPDATE_COMMENTS);
            stmt.setString(1, comment);
            stmt.setString(2, id);

            int rows = stmt.executeUpdate();
            if (rows == 1)
                comments = getCommentById(id);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return comments;
    }

    @Override
    public boolean deleteComment(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CommentsDAOQuery.DELETE_COMMENTS);
            stmt.setString(1, id);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

}
