package stockbroker.model;

import java.util.Comparator;
import java.util.UUID;
import stockbroker.enums.OrderStatus;
import stockbroker.enums.OrderType;
import stockbroker.enums.TransactionType;
import stockbroker.state.FilledState;
import stockbroker.state.OpenState;
import stockbroker.state.OrderState;
import stockbroker.state.PartiallyFilledState;

public class Order {
    private final String id;
    private final String userId;
    private final OrderType orderType;
    private final Stock stock;
    private final int quantity;
    private final Double price;
    private final TransactionType transactionType;
    private final long timestamp;

    private int filledQuantity;
    private OrderStatus orderStatus;
    private OrderState state;

    public Order(String userId, Stock stock, TransactionType transactionType,
                 OrderType orderType, int quantity, Double price) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.stock = stock;
        this.transactionType = transactionType;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
        this.filledQuantity = 0;
        this.orderStatus = OrderStatus.OPEN;
        this.state = new OpenState();
        this.timestamp = System.nanoTime();
    }

    public static Comparator<Order> bidComparator() {
        return new Comparator<Order>() {
            @Override
            public int compare(Order first, Order second) {
                int priceCompare = Double.compare(second.getPrice(), first.getPrice());
                if (priceCompare != 0) {
                    return priceCompare;
                }
                return Long.compare(first.getTimestamp(), second.getTimestamp());
            }
        };
    }

    public static Comparator<Order> askComparator() {
        return new Comparator<Order>() {
            @Override
            public int compare(Order first, Order second) {
                int priceCompare = Double.compare(first.getPrice(), second.getPrice());
                if (priceCompare != 0) {
                    return priceCompare;
                }
                return Long.compare(first.getTimestamp(), second.getTimestamp());
            }
        };
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public int getFilledQuantity() {
        return filledQuantity;
    }

    public int getRemainingQuantity() {
        return quantity - filledQuantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void applyFill(int fillQuantity) {
        if (fillQuantity <= 0 || fillQuantity > getRemainingQuantity()) {
            throw new IllegalArgumentException("Invalid fill quantity: " + fillQuantity);
        }

        filledQuantity += fillQuantity;
        if (filledQuantity == quantity) {
            orderStatus = OrderStatus.FILLED;
            state = new FilledState();
        } else {
            orderStatus = OrderStatus.PARTIALLY_FILLED;
            state = new PartiallyFilledState();
        }
    }

    public void cancel() {
        state.cancel(this);
    }

    public void handle() {
        state.handle(this);
    }
}
