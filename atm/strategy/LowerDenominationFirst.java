package atm.strategy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LowerDenominationFirst implements DenominationStrategy {

    @Override
    public List<Integer> getDenominationOrder(Set<Integer> availableDenominations) {
        return availableDenominations.stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
