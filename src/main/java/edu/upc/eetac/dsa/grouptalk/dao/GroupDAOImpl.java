package edu.upc.eetac.dsa.grouptalk.dao;

import edu.upc.eetac.dsa.grouptalk.entity.Groups;
import edu.upc.eetac.dsa.grouptalk.entity.GroupsCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Carolina on 24/10/2015.
 */
public class GroupDAOImpl implements GroupsDAO {
    @Override
    public Groups createGroup (String id, String theme) throws SQLException {
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

            stmt = connection.prepareStatement(GroupsDAOQuery.CREATE_GROUP);
            stmt.setString(1, id);
            stmt.setString(2, theme);
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
        return getGroupById(id);
    }

    @Override
    public Groups getGroupById(String id) throws SQLException {
        Groups group = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupsDAOQuery.GET_GROUP_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                group = new Groups();
                group.setId(rs.getString("id"));
                group.setTheme("Theme");
                group.setDescription(rs.getString("Description"));
                            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return group;
    }

    @Override
    public GroupsCollection getGroups() throws SQLException {
        GroupsCollection groupsCollection = new GroupsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(CommentsDAOQuery.GET_COMMENTS);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Groups group = new Groups();
                group.setId(rs.getString("id"));
                group.setTheme(rs.getString("Theme"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return groupsCollection;
    }

    @Override
    public Groups updateGroup(String id, String theme) throws SQLException {
        Groups comments = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupsDAOQuery.UPDATE_GROUPS);
            stmt.setString(1, id);
            stmt.setString(2, theme);

            int rows = stmt.executeUpdate();
            if (rows == 1)
                comments = getGroupById(id);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return comments;
    }

    @Override
    public boolean deleteGroup(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupsDAOQuery.DELETE_GROUPS);
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
