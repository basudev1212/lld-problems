package stockbroker.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import stockbroker.enums.OrderStatus;
import stockbroker.enums.OrderType;
import stockbroker.enums.TransactionType;
import stockbroker.model.Order;
import stockbroker.model.Stock;
import stockbroker.model.Trade;
import stockbroker.model.User;
import stockbroker.strategy.LimitExecutionStrategy;
import stockbroker.strategy.MarketExecutionStrategy;
import stockbroker.strategy.OrderExecutionStrategy;

public class StockBrokerService {
    private static StockBrokerService instance;

    private final Map<String, User> userMap;
    private final Map<String, Stock> stockMap;
    private final Map<String, Order> orderMap;
    private final StockExchangeService exchangeService;

    private StockBrokerService() {
        this.userMap = new ConcurrentHashMap<String, User>();
        this.stockMap = new ConcurrentHashMap<String, Stock>();
        this.orderMap = new ConcurrentHashMap<String, Order>();
        this.exchangeService = StockExchangeService.getInstance();
    }

    public static StockBrokerService getInstance() {
        if (instance == null) {
            instance = new StockBrokerService();
        }
        return instance;
    }

    public void registerUser(String userId, String name, double initialBalance) {
        userMap.put(userId, new User(userId, name, initialBalance));
    }

    public void addStock(String symbol, double price) {
        stockMap.put(symbol, new Stock(symbol, price));
    }

    public User getUser(String userId) {
        return userMap.get(userId);
    }

    public Stock getStock(String symbol) {
        return stockMap.get(symbol);
    }

    public Order placeOrder(String userId, String symbol, TransactionType transactionType,
                            OrderType orderType, int quantity, Double price) {
        User user = userMap.get(userId);
        Stock stock = stockMap.get(symbol);

        if (user == null) {
            System.out.println("User not found: " + userId);
            return null;
        }
        if (stock == null) {
            System.out.println("Stock not found: " + symbol);
            return null;
        }
        if (quantity <= 0) {
            System.out.println("Quantity must be positive");
            return null;
        }
        if (orderType == OrderType.LIMIT && (price == null || price <= 0)) {
            System.out.println("Limit orders require a positive price");
            return null;
        }

        Order order = new Order(userId, stock, transactionType, orderType, quantity, price);

        if (!validateOrder(user, order, stock)) {
            return null;
        }

        orderMap.put(order.getId(), order);
        List<Trade> trades = exchangeService.submitOrder(order);
        applyTrades(trades);

        return order;
    }

    public boolean cancelOrder(String userId, String orderId) {
        Order order = orderMap.get(orderId);
        if (order == null) {
            System.out.println("Order not found: " + orderId);
            return false;
        }
        if (!order.getUserId().equals(userId)) {
            System.out.println("Order does not belong to user: " + userId);
            return false;
        }
        if (order.getOrderStatus() == OrderStatus.FILLED
                || order.getOrderStatus() == OrderStatus.CANCELLED) {
            System.out.println("Order cannot be cancelled in status: " + order.getOrderStatus());
            return false;
        }

        order.cancel();
        return exchangeService.cancelOrder(order);
    }

    public boolean canExecuteImmediately(Order order, Stock stock) {
        OrderExecutionStrategy strategy;
        if (order.getOrderType() == OrderType.LIMIT) {
            strategy = new LimitExecutionStrategy();
        } else {
            strategy = new MarketExecutionStrategy();
        }
        return strategy.canExecute(order, stock.getPrice());
    }

    private boolean validateOrder(User user, Order order, Stock stock) {
        double referencePrice;
        if (order.getOrderType() == OrderType.LIMIT) {
            referencePrice = order.getPrice();
        } else {
            referencePrice = stock.getPrice();
        }

        if (order.getTransactionType() == TransactionType.BUY) {
            double requiredBalance = referencePrice * order.getQuantity();
            if (!user.getAccount().hasBalance(requiredBalance)) {
                System.out.println("Insufficient balance. Required: " + requiredBalance
                        + ", available: " + user.getAccount().getBalance());
                return false;
            }
        } else {
            if (!user.getAccount().hasShares(stock.getSymbol(), order.getQuantity())) {
                System.out.println("Insufficient shares of " + stock.getSymbol());
                return false;
            }
        }
        return true;
    }

    private void applyTrades(List<Trade> trades) {
        for (int i = 0; i < trades.size(); i++) {
            Trade trade = trades.get(i);
            User buyer = userMap.get(trade.getBuyOrder().getUserId());
            User seller = userMap.get(trade.getSellOrder().getUserId());
            if (buyer == null || seller == null) {
                continue;
            }

            double tradeValue = trade.getPrice() * trade.getQuantity();
            buyer.getAccount().debit(tradeValue);
            buyer.getAccount().addShares(trade.getStock().getSymbol(), trade.getQuantity());

            seller.getAccount().removeShares(trade.getStock().getSymbol(), trade.getQuantity());
            seller.getAccount().credit(tradeValue);

            trade.getStock().setPrice(trade.getPrice());
        }
    }
}
