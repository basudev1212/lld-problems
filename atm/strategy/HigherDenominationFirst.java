package atm.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HigherDenominationFirst implements DenominationStrategy {

    @Override
    public List<Integer> getDenominationOrder(Set<Integer> availableDenominations) {
        return availableDenominations.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}
