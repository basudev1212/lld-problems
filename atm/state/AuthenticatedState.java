package atm.state;

import atm.ATMSystem;
import atm.enums.OperationType;
import atm.model.Card;

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
                try {
                    atmSystem.withdraw(amount);
                    System.out.println("Please collect your cash: " + amount);
                } catch (IllegalStateException e) {
                    System.out.println("Withdrawal failed: " + e.getMessage());
                }
                break;
            case DEPOSIT_CASH:
                atmSystem.deposit(amount);
                System.out.println("Deposited: " + amount);
                break;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operationType);
        }
    }

    @Override
    public void ejectCard(ATMSystem atmSystem) {
        atmSystem.setCurrentCard(null);
        atmSystem.setCurrentAccount(null);
        atmSystem.setState(new IdleState());
    }
}
