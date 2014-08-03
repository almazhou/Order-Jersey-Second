package resource;

import domain.Order;
import domain.User;
import org.bson.types.ObjectId;
import repository.UserRepository;
import representation.OrderRef;

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

@Path("/users/{id}/orders")
public class UserResource {
    @Inject
    UserRepository userRepository;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<OrderRef> getAllOrders(@Context UriInfo uriInfo, @PathParam("id") String id) {
        ObjectId userId = new ObjectId(id);
        User userById = userRepository.getUserById(userId);
        List<Order> orders = userById.getOrders();
        List<OrderRef> orderRefs = orders.stream().map(order -> new OrderRef(order,uriInfo)).collect(Collectors.toList());
        return orderRefs;

    }
}
