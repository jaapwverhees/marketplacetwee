package org.example.resources;


import org.example.dao.VisitorDao;
import org.example.domain.Visitor;
import org.example.util.PasswordAuthentication;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("visitor")
public class VisitorResource {

    @Inject
    private PasswordAuthentication authentication;

    @Inject
    private VisitorDao visitorDao;


    @GET
    public Visitor Login(@QueryParam("email") String email, @QueryParam("password") String password) throws Exception {
        Visitor visitor = visitorDao.read(email);
        loginValidator(visitor, password);
        return visitor;
    }

    //TODO add password generation. add mail service for password.
    @POST
    public Visitor Post(Visitor visitor) {
        if(visitor.valid()){
            visitor.setPassword(authentication.hash(visitor.getPassword()));
            return visitorDao.create(visitor);
        }
        return null;
    }

    void loginValidator(Visitor visitor, String password) throws Exception {
        if (visitor == null) {
            throw new Exception("Visitor does Not Exists");
        } else if (!authentication.authenticate(password.toCharArray(), visitor.getPassword())) {
            throw new Exception("Password is invalid");
        }
    }
}
