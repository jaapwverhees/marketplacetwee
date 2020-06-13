package org.example.resources;


import org.example.dao.VisitorDao;
import org.example.domain.Visitor;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Path("visitor")
public class VisitorResource {

    @Inject
    VisitorDao visitorDao;


    @GET
    public Visitor Login(@QueryParam("email") String email, @QueryParam("password") String password) throws Exception {
        Visitor visitor = visitorDao.read(email);
        loginValiditor(visitor, password);
        return visitor;
    }

    @POST
    public void Post(Visitor visitor) {
        visitorDao.create(visitor);
    }

    void loginValiditor(Visitor visitor, String password) throws Exception {
        if (visitor == null) {
            throw new Exception("Visitor does Not Exists");
        } else if (!visitor.getPassword().equals(password)) {
            throw new Exception("Password is invalid");
        }
    }
}
