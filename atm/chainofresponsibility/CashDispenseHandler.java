package atm.chainofresponsibility;

public interface CashDispenseHandler {
    int handleRequest(int amount);
    void setNextLevel(CashDispenseHandler next);
}
