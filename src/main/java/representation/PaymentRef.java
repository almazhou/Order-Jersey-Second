package representation;

import domain.Payment;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "payment")
public class PaymentRef {
    private  Payment payment;
    private  UriInfo uriInfo;

    public PaymentRef(Payment payment, UriInfo uriInfo) {

        this.payment = payment;
        this.uriInfo = uriInfo;
    }

    public PaymentRef() {
    }

    @XmlElement
    public double getAmount(){
        return payment.getAmount();
    }
    @XmlElement
    public String getId(){
        return payment.getId().toString();
    }

    @XmlElement
    public String getUri(){
        return uriInfo.getAbsolutePath()+"/"+payment.getId();
    }
}
