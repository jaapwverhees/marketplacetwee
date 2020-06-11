package org.example.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("helloworld")
public class HelloWorldResource {

    @GET
    public String getHelloWorld() { return "Hallo"; }

    @GET
    @Path("ok")
    public Response ok(@QueryParam("lastname") String lastname) {
        return Response.ok().entity(lastname).build();
    }

    @GET
    @Path("/hello/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response hello(@PathParam("id") String id) {
        return Response.ok().entity("<hello>HOI" + id + "</hello>").build();
    }

    @GET
    @Path("/error")
    public Response get() {
        return Response.serverError().entity("Something went terribly wrong!").build();
    }



}
