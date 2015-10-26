package edu.upc.eetac.dsa.grouptalk.dao;

/**
 * Created by Carolina on 24/10/2015.
 */
public interface CommentsDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_COMMENTS = "insert into comments (id, groupid, title, comment) values (UNHEX(?), unhex(?), ?, ?)";
    public final static String GET_COMMENTS_BY_ID = "select hex(s.id) as id, hex(s.creator) as creator, s.title, s.comment, s.creation_timestamp, s.last_modified, u.fullname from comments s, users u where s.id=unhex(?) and u.id=s.creator";
    public final static String GET_COMMENTS = "select hex(id) as id, hex(creator) as creator, title, creation_timestamp, last_modified from stings";
    public final static String UPDATE_COMMENTS = "update comments set comment=? id=unhex(?) ";
    public final static String DELETE_COMMENTS = "delete from comments where id=unhex(?)";
}
