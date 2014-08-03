package representation;

import domain.OrderItem;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderItem")
public class OrderItemRef {

    private  OrderItem item;
    private  UriInfo uriInfo;

    public OrderItemRef(OrderItem item, UriInfo uriInfo) {

        this.item = item;
        this.uriInfo = uriInfo;
    }

    public OrderItemRef() {
    }
    @XmlElement
    public int getQuantity(){
        return item.getQuantity();
    }

    @XmlElement
    public String getUri(){
        return uriInfo.getBaseUri()+"/products"+item.getProductId();
    }


}
