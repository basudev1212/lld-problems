package atm.chainofresponsibility;

public interface CashDispenseHandler {
    void setNextLevel(CashDispenseHandler next);
    void dispense(int amount);
    boolean canDispense(int amount);
}
