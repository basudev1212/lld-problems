package atm.strategy;

import java.util.List;
import java.util.Set;

public interface DenominationStrategy {
    List<Integer> getDenominationOrder(Set<Integer> availableDenominations);
}
