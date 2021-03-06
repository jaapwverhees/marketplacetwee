package org.example.init;

import org.example.dao.ProductDao;
import org.example.domain.Product;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Path("init")
public class Init {

    @Inject
    private ProductDao productDao;

    @GET
    public String start() {
        List<Product> products = generateSixProducts();
        products.add(generateUniqueProduct("toyota"));
        products.add(generateUniqueProduct("frisbee"));
        products.add(generateUniqueProduct("viaduct"));
        for (Product product : products) {
            productDao.create(product);
        }
        return "database initialized";
    }

    private List<Product> generateSixProducts() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            products.add(generateGenericProduct());
        }
        return products;
    }

    private Product generateGenericProduct() {
        return Product.builder()
                .name("product")
                .price(22.22)
                .thumbnail(extractBytes())
                .build();
    }

    private Product generateUniqueProduct(String name) {
        return Product.builder()
                .name(name)
                .price(22.22)
                .thumbnail(extractBytes())
                .build();
    }

    public byte[] extractBytes() {
        File file = new File("C:\\Users\\jaapw\\Desktop\\markplace 2.0\\images\\image.jpg");
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            System.out.println("oops");
        }
        return null;
    }
}
