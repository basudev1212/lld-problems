package stockbroker.model;

import java.util.UUID;

public class Trade {
    private final String id;
    private final Order buyOrder;
    private final Order sellOrder;
    private final Stock stock;
    private final int quantity;
    private final double price;

    public Trade(Order buyOrder, Order sellOrder, int quantity, double price) {
        this.id = UUID.randomUUID().toString();
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.stock = buyOrder.getStock();
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public Order getBuyOrder() {
        return buyOrder;
    }

    public Order getSellOrder() {
        return sellOrder;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
