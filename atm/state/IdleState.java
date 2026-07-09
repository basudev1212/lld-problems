package atm.state;

import atm.ATMSystem;
import atm.enums.OperationType;
import atm.model.Card;

public class IdleState implements ATMState {

    @Override
    public void insertCard(ATMSystem atmSystem, Card card) {
        atmSystem.setCurrentCard(card);
        atmSystem.setState(new CardInsertedState());
    }

    @Override
    public void enterPIN(ATMSystem atmSystem, String PIN) {
        throw new IllegalStateException("Insert a card before entering a PIN.");
    }

    @Override
    public void selectOperation(ATMSystem atmSystem, OperationType operationType, int amount) {
        throw new IllegalStateException("Insert a card before selecting an operation.");
    }

    @Override
    public void ejectCard(ATMSystem atmSystem) {
        throw new IllegalStateException("No card is inserted.");
    }
}
