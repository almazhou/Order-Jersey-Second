package representation;

import domain.Order;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
public class OrderRef {
    private  Order order;
    private  UriInfo uriInfo;


    public OrderRef(Order order, UriInfo uriInfo) {
        this.order = order;
        this.uriInfo = uriInfo;
    }

    public OrderRef() {
    }

    @XmlElement
    public String getId(){
        return order.getId().toString();
    }

    @XmlElement
    public String getUserId(){
        return order.getUserId().toString();
    }

    @XmlElement
    public double getPrice(){
        return order.getPrice();
    }

    @XmlElement
    public String getDeliveryAddress(){
        return order.getDeliveryAddress();
    }

    @XmlElement
    public String getUri(){
        return uriInfo.getAbsolutePath()+"/"+order.getId().toString();
    }



}
