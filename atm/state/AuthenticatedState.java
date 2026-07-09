package atm.state;

import atm.ATMSystem;
import atm.enums.OperationType;
import atm.exception.InsufficientCashException;
import atm.model.Account;
import atm.model.Card;
import atm.service.BankingService;

public class AuthenticatedState implements ATMState {

    @Override
    public void insertCard(ATMSystem atmSystem, Card card) {
        throw new IllegalStateException("A card is already inserted.");
    }

    @Override
    public void enterPIN(ATMSystem atmSystem, String PIN) {
        throw new IllegalStateException("PIN already verified.");
    }

    @Override
    public void selectOperation(ATMSystem atmSystem, OperationType operationType, int amount) {
        switch (operationType) {
            case CHECK_BALANCE:
                double balance = atmSystem.getAtm().getBankingService().getBalance(atmSystem.getCurrentAccount());
                System.out.println("Available balance: " + balance);
                break;
            case WITHDRAW_CASH:
                withdrawCash(atmSystem, amount);
                break;
            case DEPOSIT_CASH:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                throw new IllegalArgumentException("Unknown operation: " + operationType);
        }
    }

    private void withdrawCash(ATMSystem atmSystem, int amount) {
        Account account = atmSystem.getCurrentAccount();
        BankingService bankingService = atmSystem.getAtm().getBankingService();

        if (bankingService.getBalance(account) < amount) {
            System.out.println("Insufficient balance in account.");
            return;
        }

        try {
            atmSystem.getAtm().getCashDispenser().dispense(amount);
        } catch (InsufficientCashException e) {
            System.out.println("Insufficient funds: " + e.getMessage());
            return;
        }

        bankingService.debit(account, amount);
        System.out.println("Please collect your cash: " + amount);
    }

    @Override
    public void ejectCard(ATMSystem atmSystem) {
        atmSystem.setCurrentCard(null);
        atmSystem.setCurrentAccount(null);
        atmSystem.setState(new IdleState());
    }
}
