package edu.upc.eetac.dsa.grouptalk.dao;

import edu.upc.eetac.dsa.grouptalk.entity.Groups;
import edu.upc.eetac.dsa.grouptalk.entity.GroupsCollection;

import java.sql.SQLException;

/**
 * Created by Carolina on 24/10/2015.
 */
public interface GroupsDAO {
    public Groups createGroup(String id, String theme) throws SQLException;
    public Groups getGroupById(String id) throws SQLException;
    public GroupsCollection getGroups() throws SQLException;
    public Groups updateGroup(String id, String theme) throws SQLException;
    public boolean deleteGroup(String id) throws SQLException;
}
