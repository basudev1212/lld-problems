package atm;

import atm.enums.OperationType;
import atm.model.Account;
import atm.model.ATM;
import atm.model.Card;
import atm.state.ATMState;
import atm.state.IdleState;

public class ATMSystem {

    private final ATM atm;
    private ATMState currentState;
    private Card currentCard;
    private Account currentAccount;

    public ATMSystem(ATM atm) {
        this.atm = atm;
        this.currentState = new IdleState();
    }

    public void insertCard(Card card) {
        currentState.insertCard(this, card);
    }

    public void enterPIN(String pin) {
        currentState.enterPIN(this, pin);
    }

    public void selectOperation(OperationType operationType) {
        currentState.selectOperation(this, operationType, 0);
    }

    public void selectOperation(OperationType operationType, int amount) {
        currentState.selectOperation(this, operationType, amount);
    }

    public void ejectCard() {
        currentState.ejectCard(this);
    }

    public ATM getAtm() {
        return atm;
    }

    public ATMState getCurrentState() {
        return currentState;
    }

    public void setState(ATMState state) {
        this.currentState = state;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card card) {
        this.currentCard = card;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }
}
