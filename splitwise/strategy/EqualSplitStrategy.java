package strategy;

import java.util.ArrayList;
import java.util.List;
import model.Split;
import model.User;

public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> calculateSplits(double totalAmount, User paidBy, List<User> participants,
                                       List<Double> splitValues) {
        List<Split> splits = new ArrayList<>();
        double amountPerPerson = totalAmount / participants.size();
        for (User participant : participants) {
            splits.add(new Split(participant, amountPerPerson));
        }
        return splits;
    }
}
