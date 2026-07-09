package atm;

import atm.enums.OperationType;
import atm.model.ATM;
import atm.model.Account;
import atm.model.Card;
import atm.service.BankingService;
import atm.service.CashDispenser;
import atm.strategy.HigherDenominationFirst;

public class ATMApplication {

    public static void main(String[] args) {
        BankingService bankingService = new BankingService();
        Card card = new Card("1234-5678-9012-3456", "12/29", "123");
        Account account = new Account("ACC-001", "Jane Doe", "4321", 5000);
        bankingService.linkCardToAccount(card, account);

        CashDispenser cashDispenser = new CashDispenser(new HigherDenominationFirst());
        cashDispenser.loadCash(500, 2);
        cashDispenser.loadCash(200, 2);
        cashDispenser.loadCash(100, 5);

        ATM atm = ATM.getInstance("ATM-001", "MG Road Branch", cashDispenser, bankingService);
        ATMSystem atmSystem = new ATMSystem(atm);

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

        System.out.println("--- Eject card ---");
        atmSystem.ejectCard();
    }
}
