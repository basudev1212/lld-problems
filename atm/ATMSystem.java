package atm;

import atm.enums.OperationType;
import atm.model.Account;
import atm.model.ATM;
import atm.model.Card;
import atm.service.BankingService;
import atm.service.CashDispenser;
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

    // Debit before dispensing: reversing a ledger entry (credit back) is far more
    // reliable than trying to undo a physical cash dispense after the fact. The
    // canDispense() pre-check catches the common "not enough cash" case before the
    // account is ever touched; the catch below only guards the rarer case where
    // dispense() still fails afterwards (e.g. a race with another concurrent session).
    public void withdraw(int amount) {
        CashDispenser cashDispenser = atm.getCashDispenser();
        BankingService bankingService = atm.getBankingService();

        if (!cashDispenser.canDispense(amount)) {
            throw new IllegalStateException("Insufficient cash available in the ATM.");
        }

        bankingService.debit(currentAccount, amount);
        try {
            cashDispenser.dispense(amount);
        } catch (RuntimeException e) {
            bankingService.credit(currentAccount, amount);
            throw new IllegalStateException("Could not dispense cash, withdrawal cancelled.", e);
        }
    }

    public void deposit(int amount) {
        atm.getBankingService().credit(currentAccount, amount);
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
