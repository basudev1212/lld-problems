package atm.chainofresponsibility;

import java.util.Map;

public class FiveHundredCashHandler extends AbstractCashHandler {

    public FiveHundredCashHandler(Map<Integer, Integer> denominationCounts) {
        super(500, denominationCounts);
    }
}
