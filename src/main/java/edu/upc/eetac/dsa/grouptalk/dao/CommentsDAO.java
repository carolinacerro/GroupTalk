package edu.upc.eetac.dsa.grouptalk.dao;


import edu.upc.eetac.dsa.grouptalk.entity.Comments;
import edu.upc.eetac.dsa.grouptalk.entity.CommentsCollection;

import java.sql.SQLException;

/**
 * Created by Carolina on 24/10/2015.
 */
public interface CommentsDAO {
    public Comments createComment(String id, String groupid, String title, String comment) throws SQLException;
    public Comments getCommentById(String id) throws SQLException;
    public CommentsCollection getComments() throws SQLException;
    public Comments updateComment(String id, String title, String comment) throws SQLException;
    public boolean deleteComment(String id) throws SQLException;
}
