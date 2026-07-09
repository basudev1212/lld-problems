package atm.model;

import atm.service.BankingService;
import atm.service.CashDispenser;

public class ATM {

    private static volatile ATM instance;

    private final String atmId;
    private final String location;
    private final CashDispenser cashDispenser;
    private final BankingService bankingService;

    private ATM(String atmId, String location, CashDispenser cashDispenser, BankingService bankingService) {
        this.atmId = atmId;
        this.location = location;
        this.cashDispenser = cashDispenser;
        this.bankingService = bankingService;
    }

    public static ATM getInstance(String atmId, String location, CashDispenser cashDispenser, BankingService bankingService) {
        if (instance == null) {
            synchronized (ATM.class) {
                if (instance == null) {
                    instance = new ATM(atmId, location, cashDispenser, bankingService);
                }
            }
        }
        return instance;
    }

    public static ATM getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ATM has not been initialized yet. Call getInstance(atmId, location, cashDispenser, bankingService) first.");
        }
        return instance;
    }

    public String getAtmId() {
        return atmId;
    }

    public String getLocation() {
        return location;
    }

    public CashDispenser getCashDispenser() {
        return cashDispenser;
    }

    public BankingService getBankingService() {
        return bankingService;
    }
}
