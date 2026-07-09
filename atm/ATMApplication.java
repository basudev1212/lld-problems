package atm;

import atm.enums.OperationType;
import atm.model.ATM;
import atm.model.Card;

public class ATMApplication {

    public static void main(String[] args) {
        ATM atm = ATM.getInstance();
        ATMSystem atmSystem = new ATMSystem(atm);

        // Matches the demo card/account seeded inside BankingService's constructor.
        Card card = new Card("1234-5678-9012-3456", "12/29", "123");

        System.out.println("--- Insert card ---");
        atmSystem.insertCard(card);

        System.out.println("--- Enter wrong PIN ---");
        try {
            atmSystem.enterPIN("0000");
        } catch (IllegalStateException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        System.out.println("--- Enter correct PIN ---");
        atmSystem.enterPIN("4321");

        System.out.println("--- Check balance ---");
        atmSystem.selectOperation(OperationType.CHECK_BALANCE);

        System.out.println("--- Withdraw 1600 (payable as 2x500 + 2x200 + 2x100) ---");
        atmSystem.selectOperation(OperationType.WITHDRAW_CASH, 1600);

        System.out.println("--- Check balance after withdrawal ---");
        atmSystem.selectOperation(OperationType.CHECK_BALANCE);

        System.out.println("--- Withdraw 350 (only 100-notes left -> not exactly payable) ---");
        atmSystem.selectOperation(OperationType.WITHDRAW_CASH, 350);

        System.out.println("--- Deposit 200 ---");
        atmSystem.selectOperation(OperationType.DEPOSIT_CASH, 200);

        System.out.println("--- Check balance after deposit ---");
        atmSystem.selectOperation(OperationType.CHECK_BALANCE);

        System.out.println("--- Eject card ---");
        atmSystem.ejectCard();
    }
}
