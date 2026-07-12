package strategy;

import model.Payment;

public interface PaymentStrategy {

    Payment payAmount(Double amount);
    
}
