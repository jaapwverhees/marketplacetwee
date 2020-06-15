package org.example.resources;


import org.example.dao.VisitorDao;
import org.example.domain.Visitor;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("visitor")
public class VisitorResource {

    @Inject
    VisitorDao visitorDao;


    @GET
    public Visitor Login(@QueryParam("email") String email, @QueryParam("password") String password) throws Exception {
        Visitor visitor = visitorDao.read(email);
        loginValidator(visitor, password);
        return visitor;
    }

    //FIXME add additional validation. add password generation. add mail service for password.
    @POST
    public Visitor Post(Visitor visitor) {
        return visitorDao.create(visitor);
    }

    void loginValidator(Visitor visitor, String password) throws Exception {
        if (visitor == null) {
            throw new Exception("Visitor does Not Exists");
        } else if (!visitor.getPassword().equals(password)) {
            throw new Exception("Password is invalid");
        }
    }
}
