package atm.chainofresponsibility;

public class TwoHundredCashHandler extends AbstractCashHandler {

    public TwoHundredCashHandler(int availableCount) {
        super(200, availableCount);
    }
}
