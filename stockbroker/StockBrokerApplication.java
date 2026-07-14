package stockbroker;

import stockbroker.enums.OrderType;
import stockbroker.enums.TransactionType;
import stockbroker.model.Order;
import stockbroker.model.User;
import stockbroker.service.StockBrokerService;

public class StockBrokerApplication {

    public static void main(String[] args) {
        StockBrokerService brokerService = StockBrokerService.getInstance();

        brokerService.addStock("AAPL", 150.0);
        brokerService.registerUser("user-1", "Alice", 10000.0);
        brokerService.registerUser("user-2", "Bob", 10000.0);

        User bob = brokerService.getUser("user-2");
        if (bob != null) {
            bob.getAccount().addShares("AAPL", 50);
        }

        System.out.println("=== Stock Broker ===");

        Order buyOrder = brokerService.placeOrder(
                "user-1", "AAPL", TransactionType.BUY, OrderType.LIMIT, 10, 149.0);
        if (buyOrder != null) {
            System.out.println("Alice placed buy order: " + buyOrder.getId()
                    + " status=" + buyOrder.getOrderStatus());
        }

        Order sellOrder = brokerService.placeOrder(
                "user-2", "AAPL", TransactionType.SELL, OrderType.LIMIT, 10, 148.0);
        if (sellOrder != null) {
            System.out.println("Bob placed sell order: " + sellOrder.getId()
                    + " status=" + sellOrder.getOrderStatus());
        }

        User alice = brokerService.getUser("user-1");
        if (alice != null) {
            System.out.println("Alice balance: " + alice.getAccount().getBalance()
                    + ", AAPL shares: " + alice.getAccount().getShareCount("AAPL"));
        }

        bob = brokerService.getUser("user-2");
        if (bob != null) {
            System.out.println("Bob balance: " + bob.getAccount().getBalance()
                    + ", AAPL shares: " + bob.getAccount().getShareCount("AAPL"));
        }
    }
}
