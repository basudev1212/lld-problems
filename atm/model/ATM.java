package atm.model;

import atm.chainofresponsibility.CashDispenseHandler;
import atm.chainofresponsibility.FiveHundredCashHandler;
import atm.chainofresponsibility.HundredCashHandler;
import atm.chainofresponsibility.TwoHundredCashHandler;
import atm.service.BankingService;
import atm.service.CashDispenser;

public class ATM {

    private static ATM instance;

    private final BankingService bankingService;
    private final CashDispenser cashDispenser;

    // No constructor args: the single instance builds its own dependencies,
    // so there's no "first call vs. every call after" asymmetry to get wrong.
    private ATM() {
        this.bankingService = new BankingService();

        CashDispenseHandler fiveHundred = new FiveHundredCashHandler(2);
        CashDispenseHandler twoHundred = new TwoHundredCashHandler(2);
        CashDispenseHandler hundred = new HundredCashHandler(5);
        fiveHundred.setNextLevel(twoHundred);
        twoHundred.setNextLevel(hundred);

        this.cashDispenser = new CashDispenser(fiveHundred);
    }

    public static synchronized ATM getInstance() {
        if (instance == null) {
            instance = new ATM();
        }
        return instance;
    }

    public BankingService getBankingService() {
        return bankingService;
    }

    public CashDispenser getCashDispenser() {
        return cashDispenser;
    }
}
