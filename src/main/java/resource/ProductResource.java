package resource;

import domain.Product;
import org.bson.types.ObjectId;
import repository.ProductRepository;
import representation.ProductRef;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
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

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response saveProduct(@Context UriInfo uriInfo, Form form){
        String name = form.asMap().getFirst("name");
        double price = Double.parseDouble(String.valueOf(form.asMap().getFirst("price")));
        Product product = new Product(name, price);
        productRepository.saveProduct(product);

        ObjectId id = product.getId();
        String productId = id == null ? "0" : id.toString();

        return Response.created(URI.create(uriInfo.getAbsolutePath()+"/"+ productId)).build();
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
