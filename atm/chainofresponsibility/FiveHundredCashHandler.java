package atm.chainofresponsibility;

public class FiveHundredCashHandler extends AbstractCashHandler {

    public FiveHundredCashHandler(int availableCount) {
        super(500, availableCount);
    }
}
