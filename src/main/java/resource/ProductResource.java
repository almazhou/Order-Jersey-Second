package resource;

import domain.Product;
import org.bson.types.ObjectId;
import repository.ProductRepository;
import representation.ProductRef;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@Path("/products")
public class ProductResource {
    @Inject
    ProductRepository productRepository;

    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public List<ProductRef> getProducts(@Context UriInfo uriInfo){
        List<Product> products = productRepository.getProducts();
        List<ProductRef> collect = products.stream().map(product -> new ProductRef(product, uriInfo)).collect(Collectors.toList());
        return collect;
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public ProductRef getProductById(@Context UriInfo uriInfo,@PathParam("id") String id){
        ObjectId objectId = new ObjectId(id);
        Product productById = productRepository.getProductById(objectId);
        return new ProductRef(productById,uriInfo);
    }
}
