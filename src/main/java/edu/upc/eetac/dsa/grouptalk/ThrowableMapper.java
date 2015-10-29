package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.entity.GroupTalkError;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Carolina on 29/10/2015.
 */
@Provider
public class ThrowableMapper implements ExceptionMapper {

    @Override
    public Response toResponse(Throwable throwable) {
        throwable.printStackTrace();
        GroupTalkError error = new GroupTalkError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), throwable.getMessage());
        return Response.status(error.getStatus()).entity(error).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
