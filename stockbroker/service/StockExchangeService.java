package stockbroker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import stockbroker.enums.OrderStatus;
import stockbroker.enums.OrderType;
import stockbroker.enums.TransactionType;
import stockbroker.model.Order;
import stockbroker.model.Trade;

public class StockExchangeService {
    private static StockExchangeService instance;

    private final Map<String, PriorityQueue<Order>> buyOrders;
    private final Map<String, PriorityQueue<Order>> sellOrders;
    private final Map<String, Object> symbolLocks;

    private StockExchangeService() {
        buyOrders = new ConcurrentHashMap<String, PriorityQueue<Order>>();
        sellOrders = new ConcurrentHashMap<String, PriorityQueue<Order>>();
        symbolLocks = new ConcurrentHashMap<String, Object>();
    }

    public static StockExchangeService getInstance() {
        if (instance == null) {
            instance = new StockExchangeService();
        }
        return instance;
    }

    public List<Trade> submitOrder(Order order) {
        String symbol = order.getStock().getSymbol();
        synchronized (getSymbolLock(symbol)) {
            List<Trade> trades = match(order);

            if (order.getRemainingQuantity() > 0
                    && order.getOrderStatus() != OrderStatus.CANCELLED
                    && order.getOrderStatus() != OrderStatus.FILLED) {
                if (order.getOrderType() == OrderType.MARKET) {
                    order.setOrderStatus(OrderStatus.FAILED);
                } else {
                    addToBook(order);
                }
            }
            return trades;
        }
    }

    public List<Trade> match(Order incomingOrder) {
        List<Trade> trades = new ArrayList<Trade>();
        String symbol = incomingOrder.getStock().getSymbol();

        if (incomingOrder.getTransactionType() == TransactionType.BUY) {
            PriorityQueue<Order> asks = getOrCreateSellBook(symbol);
            matchAgainstBook(incomingOrder, asks, trades, true);
        } else {
            PriorityQueue<Order> bids = getOrCreateBuyBook(symbol);
            matchAgainstBook(incomingOrder, bids, trades, false);
        }

        return trades;
    }

    private void matchAgainstBook(Order incomingOrder, PriorityQueue<Order> oppositeBook,
                                  List<Trade> trades, boolean incomingIsBuy) {
        while (incomingOrder.getRemainingQuantity() > 0 && !oppositeBook.isEmpty()) {
            Order restingOrder = oppositeBook.peek();

            if (!canCross(incomingOrder, restingOrder)) {
                break;
            }

            int tradeQuantity = incomingOrder.getRemainingQuantity();
            if (restingOrder.getRemainingQuantity() < tradeQuantity) {
                tradeQuantity = restingOrder.getRemainingQuantity();
            }

            double executionPrice = restingOrder.getPrice();

            incomingOrder.applyFill(tradeQuantity);
            restingOrder.applyFill(tradeQuantity);

            Order buyOrder;
            Order sellOrder;
            if (incomingIsBuy) {
                buyOrder = incomingOrder;
                sellOrder = restingOrder;
            } else {
                buyOrder = restingOrder;
                sellOrder = incomingOrder;
            }

            Trade trade = new Trade(buyOrder, sellOrder, tradeQuantity, executionPrice);
            trades.add(trade);

            if (restingOrder.getRemainingQuantity() == 0) {
                oppositeBook.poll();
            }
        }
    }

    private boolean canCross(Order incomingOrder, Order restingOrder) {
        if (incomingOrder.getOrderType() == OrderType.MARKET) {
            return true;
        }

        if (incomingOrder.getTransactionType() == TransactionType.BUY) {
            return restingOrder.getPrice() <= incomingOrder.getPrice();
        }
        return restingOrder.getPrice() >= incomingOrder.getPrice();
    }

    public boolean cancelOrder(Order order) {
        String symbol = order.getStock().getSymbol();
        synchronized (getSymbolLock(symbol)) {
            if (order.getTransactionType() == TransactionType.BUY) {
                return removeFromBook(buyOrders, symbol, order);
            }
            return removeFromBook(sellOrders, symbol, order);
        }
    }

    public void placeBuyOrder(Order order) {
        String symbol = order.getStock().getSymbol();
        synchronized (getSymbolLock(symbol)) {
            addToBuyBook(order);
        }
    }

    public void placeSellOrder(Order order) {
        String symbol = order.getStock().getSymbol();
        synchronized (getSymbolLock(symbol)) {
            addToSellBook(order);
        }
    }

    public PriorityQueue<Order> getBuyOrders(String symbol) {
        PriorityQueue<Order> orders = buyOrders.get(symbol);
        if (orders == null) {
            return new PriorityQueue<Order>(Order.bidComparator());
        }
        return orders;
    }

    public PriorityQueue<Order> getSellOrders(String symbol) {
        PriorityQueue<Order> orders = sellOrders.get(symbol);
        if (orders == null) {
            return new PriorityQueue<Order>(Order.askComparator());
        }
        return orders;
    }

    private void addToBook(Order order) {
        if (order.getTransactionType() == TransactionType.BUY) {
            addToBuyBook(order);
        } else {
            addToSellBook(order);
        }
    }

    private void addToBuyBook(Order order) {
        String symbol = order.getStock().getSymbol();
        PriorityQueue<Order> book = getOrCreateBuyBook(symbol);
        book.add(order);
    }

    private void addToSellBook(Order order) {
        String symbol = order.getStock().getSymbol();
        PriorityQueue<Order> book = getOrCreateSellBook(symbol);
        book.add(order);
    }

    private PriorityQueue<Order> getOrCreateBuyBook(String symbol) {
        if (!buyOrders.containsKey(symbol)) {
            buyOrders.put(symbol, new PriorityQueue<Order>(Order.bidComparator()));
        }
        return buyOrders.get(symbol);
    }

    private PriorityQueue<Order> getOrCreateSellBook(String symbol) {
        if (!sellOrders.containsKey(symbol)) {
            sellOrders.put(symbol, new PriorityQueue<Order>(Order.askComparator()));
        }
        return sellOrders.get(symbol);
    }

    private boolean removeFromBook(Map<String, PriorityQueue<Order>> book, String symbol, Order order) {
        PriorityQueue<Order> orders = book.get(symbol);
        if (orders == null) {
            return false;
        }
        return orders.remove(order);
    }

    private Object getSymbolLock(String symbol) {
        if (!symbolLocks.containsKey(symbol)) {
            symbolLocks.put(symbol, new Object());
        }
        return symbolLocks.get(symbol);
    }
}
