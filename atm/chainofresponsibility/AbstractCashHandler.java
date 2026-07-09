package atm.chainofresponsibility;

import java.util.Map;

public abstract class AbstractCashHandler implements CashDispenseHandler {

    private final int denomination;
    private final Map<Integer, Integer> denominationCounts;
    private CashDispenseHandler next;

    protected AbstractCashHandler(int denomination, Map<Integer, Integer> denominationCounts) {
        this.denomination = denomination;
        this.denominationCounts = denominationCounts;
    }

    @Override
    public void setNextLevel(CashDispenseHandler next) {
        this.next = next;
    }

    @Override
    public int handleRequest(int amount) {
        int availableCount = denominationCounts.getOrDefault(denomination, 0);
        int notesNeeded = amount / denomination;
        int notesToDispense = Math.min(notesNeeded, availableCount);
        int dispensedAmount = notesToDispense * denomination;
        denominationCounts.put(denomination, availableCount - notesToDispense);

        int remaining = amount - dispensedAmount;
        if (remaining > 0 && next != null) {
            return next.handleRequest(remaining);
        }
        return remaining;
    }
}
