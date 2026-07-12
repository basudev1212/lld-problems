package strategy;

import java.util.UUID;

import enums.PaymentStatus;
import model.Payment;

public class OfflinePayment implements PaymentStrategy {

    @Override
    public Payment payAmount(Double amount) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setTransactionId("TXN-OFFLINE-" + System.currentTimeMillis());
        payment.setAmount(amount);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        System.out.println("Offline payment of Rs." + amount + " completed successfully.");
        return payment;
    }
}
