package edu.upc.eetac.dsa.grouptalk.dao;

/**
 * Created by Carolina on 24/10/2015.
 */
public interface GroupsDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_GROUP = "insert into groups (id, theme,description) values (UNHEX(?), ?, ?)";
    public final static String GET_GROUP_BY_ID = "select hex(g.id) as id, g.theme, g.description, from groups g where g.id=unhex(?)";
    public final static String GET_GROUPS = "select hex(id) as id, theme, description from groups";
    public final static String UPDATE_GROUPS = "update groups set theme=?, description=? where id=unhex(?) ";
    public final static String DELETE_GROUPS = "delete from groups where id=unhex(?)";
}
