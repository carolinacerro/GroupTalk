package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.dao.GroupDAOImpl;
import edu.upc.eetac.dsa.grouptalk.dao.GroupsDAO;
import edu.upc.eetac.dsa.grouptalk.entity.AuthToken;
import edu.upc.eetac.dsa.grouptalk.entity.Groups;
import edu.upc.eetac.dsa.grouptalk.entity.GroupsCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Carolina on 29/10/2015.
 */
@Path("groups")
public class GroupResource {
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.GROUPTALK_GROUPS)
    public Response.Status createGroup(@FormParam("admin") String admin, @FormParam("theme") String theme, @FormParam("description") String description, @Context UriInfo uriInfo) throws URISyntaxException, SQLException {
        if (theme == null || description == null)
            throw new BadRequestException("all parameters are mandatory");
        GroupsDAO groupDAO = new GroupDAOImpl();
        Groups groups = null;
        AuthToken authenticationToken = null;
        if (admin!=null || admin!="") {
            try {
                groups = groupDAO.createGroup(theme, description);
            } catch (SQLException e) {
                throw new InternalServerErrorException();
            }
            return Response.Status.ACCEPTED;
        }
        else{
            System.out.println("You must be admin user for create groups");
            return Response.Status.BAD_REQUEST;
        }
    }

    @GET
    @Produces(GroupTalkMediaType.GROUPTALK_GROUPS_COLLECTION)
    public GroupsCollection getStings(){
        GroupsCollection groupsCollection=null;
        GroupsDAO groupsDAO = new GroupDAOImpl();
        try {
            groupsCollection = groupsDAO.getGroups();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        return groupsCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(GroupTalkMediaType.GROUPTALK_GROUPS)
    public Groups getSting(@PathParam("id") String id){
        Groups groups = null;
        GroupsDAO groupDAO = new GroupDAOImpl();
        try {
            groups=groupDAO.getGroupById(id);
            if(groups == null)
                throw new NotFoundException("Sting with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return groups;
    }

    @Path("/{admin}/{id}")
    @PUT
    @Consumes(GroupTalkMediaType.GROUPTALK_GROUPS)
    @Produces(GroupTalkMediaType.GROUPTALK_GROUPS)
    public Groups updateGroup(@PathParam("id") String id, @PathParam("admin") String admin, Groups groups) {
        if(admin != null || admin!=""){
            if (groups == null)
                throw new BadRequestException("entity is null");

            GroupsDAO groupDAO = new GroupDAOImpl();
            try {
                groups = groupDAO.updateGroup(id, groups.getTheme());
                if (groups == null)
                    throw new NotFoundException("Sting with id = " + id + " doesn't exist");
            } catch (SQLException e) {
                throw new InternalServerErrorException();
            }
            return groups;
        }
        else{
            throw new ForbiddenException("You must be the administrator.");
        }
    }

    @Path("/{admin}/{id}")
    @DELETE
    public void deleteGroup(@PathParam("id") String id, @PathParam("admin") String admin) {
        GroupsDAO groupDAO = new GroupDAOImpl();
        try {
            if(admin == null || admin=="")
                throw new ForbiddenException("Operation not allowed, you must be the administrator");
            if(!groupDAO.deleteGroup(id))
                throw new NotFoundException("Group with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}