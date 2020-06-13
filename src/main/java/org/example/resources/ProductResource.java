package org.example.resources;

import org.example.dao.ProductDao;
import org.example.domain.Product;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("products")
public class ProductResource {

    @Inject
    ProductDao productDao;

    @GET
    public List<Product> get(){
        return productDao.readAllProducts();
    }
}
