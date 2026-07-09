package atm.service;

import atm.chainofresponsibility.CashDispenseHandler;

public class CashDispenser {

    private final CashDispenseHandler chain;

    public CashDispenser(CashDispenseHandler chain) {
        this.chain = chain;
    }

    public boolean canDispense(int amount) {
        return chain.canDispense(amount);
    }

    public void dispense(int amount) {
        chain.dispense(amount);
    }
}
