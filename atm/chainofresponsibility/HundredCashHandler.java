package atm.chainofresponsibility;

public class HundredCashHandler extends AbstractCashHandler {

    public HundredCashHandler(int availableCount) {
        super(100, availableCount);
    }
}
