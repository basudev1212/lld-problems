package atm.state;

import atm.ATMSystem;
import atm.enums.OperationType;
import atm.model.Account;
import atm.model.Card;

public class CardInsertedState implements ATMState {

    @Override
    public void insertCard(ATMSystem atmSystem, Card card) {
        throw new IllegalStateException("A card is already inserted.");
    }

    @Override
    public void enterPIN(ATMSystem atmSystem, String PIN) {
        Account account = atmSystem.getAtm().getBankingService().getAccount(atmSystem.getCurrentCard());
        if (!atmSystem.getAtm().getBankingService().validatePin(account, PIN)) {
            throw new IllegalStateException("Invalid PIN.");
        }
        atmSystem.setCurrentAccount(account);
        atmSystem.setState(new AuthenticatedState());
    }

    @Override
    public void selectOperation(ATMSystem atmSystem, OperationType operationType, int amount) {
        throw new IllegalStateException("Enter your PIN before selecting an operation.");
    }

    @Override
    public void ejectCard(ATMSystem atmSystem) {
        atmSystem.setCurrentCard(null);
        atmSystem.setState(new IdleState());
    }
}
