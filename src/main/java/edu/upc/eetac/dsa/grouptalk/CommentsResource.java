package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.dao.CommentsDAO;
import edu.upc.eetac.dsa.grouptalk.dao.CommentsDAOImpl;
import edu.upc.eetac.dsa.grouptalk.entity.AuthToken;
import edu.upc.eetac.dsa.grouptalk.entity.Comments;
import edu.upc.eetac.dsa.grouptalk.entity.CommentsCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Carolina on 29/10/2015.
 */
@Path("groups/{groupid}/comments")
public class CommentsResource {
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.GROUPTALK_COMMENTS)
    public Response createSting(@PathParam("groupid") String groupid, @FormParam("title") String title, @FormParam("comment") String comment, @Context UriInfo uriInfo) throws URISyntaxException, SQLException{
        if(title==null || comment == null)
            throw new BadRequestException("all parameters are mandatory");
        CommentsDAO commentsDAO = new CommentsDAOImpl();
        Comments comments = null;
        AuthToken authenticationToken = null;
        try {
            comments=commentsDAO.createComment(securityContext.getUserPrincipal().getName(), groupid, title, comment);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + comments.getId());
        return Response.created(uri).type(GroupTalkMediaType.GROUPTALK_COMMENTS).entity(comments).build();
    }
    @GET
    @Produces(GroupTalkMediaType.GROUPTALK_COMMENTS_COLLECTION)
    public CommentsCollection getComments(){
        CommentsCollection commentsCollection = null;
        CommentsDAO commentsDAO = new CommentsDAOImpl();
        try {
            commentsCollection = commentsDAO.getComments();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        return commentsCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(GroupTalkMediaType.GROUPTALK_COMMENTS)
    public Comments getComments(@PathParam("groupid") String groupid, @PathParam("id") String id){
        Comments comments = null;
        CommentsDAO commentsDAO = new CommentsDAOImpl();
        try {
            comments = commentsDAO.getCommentById(id);
            if(comments == null)
                throw new NotFoundException("Comment with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return comments;
    }

    @Path("/{id}")
    @PUT
    @Consumes(GroupTalkMediaType.GROUPTALK_COMMENTS)
    @Produces(GroupTalkMediaType.GROUPTALK_COMMENTS)
    public Comments updateComment (@PathParam("groupid") String groupid, @PathParam("id") String id, Comments comment) {
        if (comment == null)
            throw new BadRequestException("entity is null");

        CommentsDAO commentsDAO = new CommentsDAOImpl();
        try {
            String userid = securityContext.getUserPrincipal().getName();
            if(!userid.equals(comment.getCreator())) {
                comment = commentsDAO.updateComment(id, comment.getGroupid(), comment.getComment());

                if (comment == null)
                    throw new NotFoundException("Comment with id = " + id + " doesn't exist");
            }else
                throw new ForbiddenException("operation not allowed");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return comment;

    }

    @Path("/{id}")
    @DELETE
    public void deleteComment(@PathParam("id") String id, @PathParam("groupid") String groupid) {
        String userid = securityContext.getUserPrincipal().getName();
        CommentsDAO commentsDAO= new CommentsDAOImpl();
        try {
            String ownerid = commentsDAO.getCommentById(id).getCreator();
            if(!userid.equals(ownerid))
                throw new ForbiddenException("operation not allowed");
            if(!commentsDAO.deleteComment(id))
                throw new NotFoundException("Sting with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

}