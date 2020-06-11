package org.example.resources;


import org.example.dao.VisitorDao;
import org.example.domain.Visitor;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("visitor")
public class VisitorResource {

    @Inject
    VisitorDao visitorDao;

//    @GET
//    public Visitor Login(String email, String passWord) throws Exception {
//        Visitor visitor = visitorDao.read(email);
//        if(visitor == null){
//            throw new Exception("Visitor does Not Exists");
//        }
//        if(visitor.getPassword().equals(passWord)){
//            return visitor;
//        } else if(!visitor.getPassword().equals(passWord)){
//            throw new Exception("Password is invalid");
//        } else{
//            throw new Exception("unknown Error");
//        }
//    }
    //for debug, must be removed
    @GET
    public Visitor getVisitor(){
        return visitorDao.read("test@test.nl");
    }
}
