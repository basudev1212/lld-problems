package atm.exception;

public class InsufficientCashException extends RuntimeException {

    private final int requestedAmount;
    private final int shortfall;

    public InsufficientCashException(int requestedAmount, int shortfall) {
        super("Cannot dispense " + requestedAmount + ": short by " + shortfall
                + " given the denominations currently available in the ATM.");
        this.requestedAmount = requestedAmount;
        this.shortfall = shortfall;
    }

    public int getRequestedAmount() {
        return requestedAmount;
    }

    public int getShortfall() {
        return shortfall;
    }
}
