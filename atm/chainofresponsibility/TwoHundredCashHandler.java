package atm.chainofresponsibility;

import java.util.Map;

public class TwoHundredCashHandler extends AbstractCashHandler {

    public TwoHundredCashHandler(Map<Integer, Integer> denominationCounts) {
        super(200, denominationCounts);
    }
}
