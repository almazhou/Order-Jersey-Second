package representation;

import domain.Product;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductRef {
    private Product product;
    private UriInfo uriInfo;

    public ProductRef(Product product, UriInfo uriInfo) {
        this.product = product;
        this.uriInfo = uriInfo;
    }

    public ProductRef() {
    }

    @XmlElement
    public String getName(){
        return product.getName();
    }



}
