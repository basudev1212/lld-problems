package deliverypartnerstrategy;

import java.util.List;
import model.DeliveryPartner;
import model.Order;

public interface SearchDeliveryPartnerStrategy {
    public DeliveryPartner findDeliveryPartner(List<DeliveryPartner> deliveryPartners, Order order);
}
