package representation;

import domain.Product;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "product")
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

    @XmlElement
    public String getId(){
        return product.getId().toString();
    }

    @XmlElement
    public String getUri(){
        return uriInfo.getAbsolutePath()+"/"+product.getId().toString();
    }

    @XmlElement
    public double getPrice(){
        return product.getPrice();
    }

}
