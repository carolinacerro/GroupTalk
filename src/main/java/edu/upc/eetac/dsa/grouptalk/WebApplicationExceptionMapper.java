package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.entity.GroupTalkError;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Carolina on 29/10/2015.
 */
@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException e) {
        GroupTalkError error = new GroupTalkError(e.getResponse().getStatus(), e.getMessage());
        return Response.status(error.getStatus()).entity(error).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}