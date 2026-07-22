package strategy;

import java.util.List;
import model.Split;
import model.User;

public interface SplitStrategy {
    List<Split> calculateSplits(double totalAmount, User paidBy, List<User> participants, List<Double> splitValues);
}
