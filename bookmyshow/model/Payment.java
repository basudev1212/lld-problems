package model;

import enums.PaymentStatus;

public class Payment {
    private String id;
    private String transactionId;
    private Double amount;
    private PaymentStatus paymentStatus;

    public Payment() {
    }

    public Payment(String id, String transactionId, Double amount, PaymentStatus paymentStatus) {
        this.id = id;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
