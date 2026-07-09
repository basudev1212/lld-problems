package atm.service;

import atm.model.Account;
import atm.model.Card;

import java.util.HashMap;
import java.util.Map;

public class BankingService {

    private final Map<String, Account> accountByCardNumber = new HashMap<>();

    public BankingService() {
        Card demoCard = new Card("1234-5678-9012-3456", "12/29", "123");
        Account demoAccount = new Account("ACC-001", "Jane Doe", "4321", 5000);
        linkCardToAccount(demoCard, demoAccount);
    }

    public void linkCardToAccount(Card card, Account account) {
        accountByCardNumber.put(card.getCardNumber(), account);
    }

    public Account getAccount(Card card) {
        Account account = accountByCardNumber.get(card.getCardNumber());
        if (account == null) {
            throw new IllegalArgumentException("No account linked to card: " + card.getCardNumber());
        }
        return account;
    }

    public boolean validatePin(Account account, String pin) {
        return account.getPin().equals(pin);
    }

    public double getBalance(Account account) {
        return account.getBalance();
    }

    public void debit(Account account, double amount) {
        if (account.getBalance() < amount) {
            throw new IllegalStateException("Insufficient balance.");
        }
        account.setBalance(account.getBalance() - amount);
    }

    public void credit(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
    }
}
