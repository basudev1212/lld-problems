package atm.service;

import atm.chainofresponsibility.CashDispenseHandler;
import atm.chainofresponsibility.FiveHundredCashHandler;
import atm.chainofresponsibility.HundredCashHandler;
import atm.chainofresponsibility.TwoHundredCashHandler;
import atm.exception.InsufficientCashException;
import atm.strategy.DenominationStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashDispenser {

    private final Map<Integer, Integer> denominationCounts;
    private final DenominationStrategy strategy;

    public CashDispenser(DenominationStrategy strategy) {
        this.denominationCounts = new HashMap<>();
        this.strategy = strategy;
    }

    public void loadCash(int denomination, int count) {
        denominationCounts.merge(denomination, count, Integer::sum);
    }

    /**
     * Dispenses the given amount, or throws {@link InsufficientCashException} and leaves
     * the ATM's cash counts untouched. A dry run against a throwaway copy of the counts
     * decides whether the amount is fully payable *before* the real counts are touched,
     * so a failed request never leaves the dispenser partially emptied.
     */
    public void dispense(int amount) {
        Map<Integer, Integer> simulatedCounts = new HashMap<>(denominationCounts);
        int shortfall = runChain(simulatedCounts, amount);
        if (shortfall > 0) {
            throw new InsufficientCashException(amount, shortfall);
        }

        runChain(denominationCounts, amount);
    }

    private int runChain(Map<Integer, Integer> counts, int amount) {
        CashDispenseHandler chainHead = buildChain(counts);
        if (chainHead == null) {
            return amount;
        }
        return chainHead.handleRequest(amount);
    }

    private CashDispenseHandler buildChain(Map<Integer, Integer> counts) {
        List<Integer> order = strategy.getDenominationOrder(counts.keySet());
        CashDispenseHandler head = null;
        CashDispenseHandler previous = null;

        for (int denomination : order) {
            CashDispenseHandler handler = createHandler(denomination, counts);
            if (head == null) {
                head = handler;
            } else {
                previous.setNextLevel(handler);
            }
            previous = handler;
        }
        return head;
    }

    private CashDispenseHandler createHandler(int denomination, Map<Integer, Integer> counts) {
        switch (denomination) {
            case 500:
                return new FiveHundredCashHandler(counts);
            case 200:
                return new TwoHundredCashHandler(counts);
            case 100:
                return new HundredCashHandler(counts);
            default:
                throw new IllegalArgumentException("Unsupported denomination: " + denomination);
        }
    }
}
