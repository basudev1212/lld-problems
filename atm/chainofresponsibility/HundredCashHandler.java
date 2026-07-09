package atm.chainofresponsibility;

import java.util.Map;

public class HundredCashHandler extends AbstractCashHandler {

    public HundredCashHandler(Map<Integer, Integer> denominationCounts) {
        super(100, denominationCounts);
    }
}
