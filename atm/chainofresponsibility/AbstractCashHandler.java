package atm.chainofresponsibility;

public abstract class AbstractCashHandler implements CashDispenseHandler {

    private final int denomination;
    private int availableCount;
    private CashDispenseHandler next;

    protected AbstractCashHandler(int denomination, int availableCount) {
        this.denomination = denomination;
        this.availableCount = availableCount;
    }

    @Override
    public void setNextLevel(CashDispenseHandler next) {
        this.next = next;
    }

    @Override
    public synchronized void dispense(int amount) {
        if (amount >= denomination) {
            int notesToDispense = Math.min(amount / denomination, availableCount);
            int remaining = amount - (notesToDispense * denomination);
            availableCount -= notesToDispense;

            if (remaining > 0 && next != null) {
                next.dispense(remaining);
            }
        } else if (next != null) {
            next.dispense(amount);
        }
    }

    // Read-only mirror of dispense(): walks the same chain without touching
    // availableCount, so it can be used as a pre-check before actually dispensing.
    @Override
    public synchronized boolean canDispense(int amount) {
        if (amount == 0) {
            return true;
        }

        int notesToUse = Math.min(amount / denomination, availableCount);
        int remaining = amount - (notesToUse * denomination);

        if (remaining == 0) {
            return true;
        }
        if (next != null) {
            return next.canDispense(remaining);
        }
        return false;
    }
}
