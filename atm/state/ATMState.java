package atm.state;

import atm.ATMSystem;
import atm.enums.OperationType;
import atm.model.Card;

public interface ATMState {
    void insertCard(ATMSystem atmSystem, Card card);
    void enterPIN(ATMSystem atmSystem, String PIN);
    void selectOperation(ATMSystem atmSystem, OperationType operationType, int amount);
    void ejectCard(ATMSystem atmSystem);
}
