package resource;

import domain.Order;
import domain.OrderItem;
import domain.Payment;
import domain.User;
import org.bson.types.ObjectId;
import repository.ProductRepository;
import repository.UserRepository;
import representation.OrderItemRef;
import representation.OrderRef;
import representation.PaymentRef;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/users/{id}/orders")
public class OrderResource {
    @Inject
    UserRepository userRepository;

    @Inject
    ProductRepository productRepository;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<OrderRef> getAllOrders(@Context UriInfo uriInfo, @PathParam("id") String id) {
        ObjectId userId = new ObjectId(id);
        User userById = userRepository.getUserById(userId);
        List<Order> orders = userById.getOrders();
        List<OrderRef> orderRefs = orders.stream().map(order -> new OrderRef(order, uriInfo)).collect(Collectors.toList());
        return orderRefs;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response placeOrder(Map orderParams, @Context UriInfo uriInfo, @PathParam("id") String id) {
        String deliverAddress = (String) orderParams.get("deliverAddress");
        List<Map> orderItems = (List<Map>) orderParams.get("orderItems");
        List<OrderItem> items = orderItems.stream().map(item -> new OrderItem(productRepository.getProductById(new ObjectId((String) item.get("id"))), (Integer) item.get("quantity"))).collect(Collectors.toList());
        Order order = new Order(deliverAddress, items);

        User userById = userRepository.getUserById(new ObjectId(id));
        userRepository.placeOrder(userById, order);
        String location = uriInfo.getAbsolutePath() + "/" + order.getId().toString();
        return Response.created(URI.create(location)).build();
    }

    @GET
    @Path("/{orderId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public OrderRef getOrderById(@Context UriInfo uriInfo, @PathParam("id") String id, @PathParam("orderId") String orderId) {
        ObjectId userId = new ObjectId(id);
        User userById = userRepository.getUserById(userId);
        ObjectId orderObjectId = new ObjectId(orderId);
        Order order = userById.getOrderById(orderObjectId);
        return new OrderRef(order, uriInfo);
    }

    @GET
    @Path("/{orderId}/payment")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public PaymentRef getPayment(@Context UriInfo uriInfo, @PathParam("id") String id, @PathParam("orderId") String orderId) {
        User userById = userRepository.getUserById(new ObjectId(id));
        Order orderById = userById.getOrderById(new ObjectId(orderId));
        Payment payment = orderById.getPayment();
        return new PaymentRef(payment,uriInfo);
    }
    @GET
    @Path("/{orderId}/order-items")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<OrderItemRef> getOrderItems(@Context UriInfo uriInfo, @PathParam("id") String id, @PathParam("orderId") String orderId) {
        User userById = userRepository.getUserById(new ObjectId(id));
        Order orderById = userById.getOrderById(new ObjectId(orderId));
        List<OrderItem> orderItems = orderById.getOrderItems();
        List<OrderItemRef> orderItemsRef = orderItems.stream().map(item -> new OrderItemRef(item, uriInfo)).collect(Collectors.toList());
        return  orderItemsRef;
    }

    @POST
    @Path("/{orderId}/payment")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Response createPayment(@Context UriInfo uriInfo, @PathParam("id") String id, @PathParam("orderId") String orderId,Form form) {
        User userById = userRepository.getUserById(new ObjectId(id));
        Order orderById = userById.getOrderById(new ObjectId(orderId));
        String amount = form.asMap().getFirst("amount");
        Payment payment = new Payment(Double.valueOf(amount).doubleValue());
        userRepository.payOrder(userById,orderById,payment);
        return Response.created(uriInfo.getAbsolutePath()).build();
    }


}
