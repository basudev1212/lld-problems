import java.util.Arrays;
import java.util.List;
import model.Expense;
import model.Group;
import model.Transaction;
import model.User;
import service.SplitwiseService;
import strategy.EqualSplitStrategy;
import strategy.ExactAmountSplitStrategy;
import strategy.PercentageSplitStrategy;

public class SplitwiseApplication {

    public static void main(String[] args) {
        SplitwiseService service = SplitwiseService.getInstance();

        System.out.println("=== Splitwise ===\n");

        User alice = service.addUser("Alice");
        User bob = service.addUser("Bob");
        User charlie = service.addUser("Charlie");
        User david = service.addUser("David");

        Group trip = service.addGroup("Goa Trip", List.of(alice, bob, charlie, david));

        System.out.println("--- Group Expenses (Goa Trip) ---");
        service.createExpense(new Expense.ExpenseBuilder()
                .setDescription("Hotel")
                .setAmount(1000)
                .setPaidBy(alice)
                .setGroup(trip)
                .setParticipants(Arrays.asList(alice, bob, charlie, david))
                .setSplitStrategy(new EqualSplitStrategy())
                .build());

        service.createExpense(new Expense.ExpenseBuilder()
                .setDescription("Dinner")
                .setAmount(500)
                .setPaidBy(david)
                .setGroup(trip)
                .setParticipants(Arrays.asList(alice, bob, charlie))
                .setSplitStrategy(new PercentageSplitStrategy())
                .setSplitValues(Arrays.asList(40.0, 30.0, 30.0))
                .build());

        service.showGroupBalanceSheet(trip.getId());
        System.out.println();

        System.out.println("--- Personal Expense (Alice & Bob only, no group) ---");
        service.createExpense(new Expense.ExpenseBuilder()
                .setDescription("Movie Tickets")
                .setAmount(370)
                .setPaidBy(alice)
                .setParticipants(Arrays.asList(bob, charlie))
                .setSplitStrategy(new ExactAmountSplitStrategy())
                .setSplitValues(Arrays.asList(120.0, 250.0))
                .build());

        System.out.println("Alice personal balances (no group expenses):");
        service.showPersonalBalanceSheet(alice.getId());
        System.out.println();

        System.out.println("Bob personal balances:");
        service.showPersonalBalanceSheet(bob.getId());
        System.out.println();

        System.out.println("Group balances (unchanged by personal expense):");
        service.showGroupBalanceSheet(trip.getId());
        System.out.println();

        service.showAllExpenses();
        System.out.println();

        service.showGroupExpenses(trip.getId());
        System.out.println();

        service.showPersonalExpenses();
        System.out.println();

        System.out.println("--- Simplify Group Debts for '" + trip.getName() + "' ---");
        List<Transaction> simplifiedDebts = service.simplifyGroupDebts(trip.getId());
        if (simplifiedDebts.isEmpty()) {
            System.out.println("All debts are settled within the group!");
        } else {
            simplifiedDebts.forEach(transaction -> System.out.println("  " + transaction));
        }
        System.out.println();

        System.out.println("--- Partial Group Settlement ---");
        service.settleGroupUp(trip.getId(), bob.getId(), alice.getId(), 100);
        service.showGroupBalanceSheet(trip.getId());
    }
}
