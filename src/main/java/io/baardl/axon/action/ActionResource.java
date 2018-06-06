package io.baardl.axon.action;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("action")
public class ActionResource {

    private final ActionService actionService;

    public ActionResource(ActionService actionService) {
        this.actionService = actionService;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newAction(String body) {
        actionService.create(body);
        return Response.ok().build();
    }
}
