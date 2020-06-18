package org.example.resources;

import org.example.dao.ProductDao;
import org.example.domain.Product;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("products")
public class ProductResource {

    @Inject
    private ProductDao productDao;

    @GET
    public List<Product> get() {
        return productDao.readAllProducts();
    }

    @POST
    public void Post(Product product) {
        productDao.create(product);
    }

    @GET
    @Path("{name}")
    public List<Product> getByName(@PathParam("name") String name){
        return productDao.readByName(name);
    }
}
