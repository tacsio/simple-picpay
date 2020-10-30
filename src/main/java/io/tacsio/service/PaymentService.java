package io.tacsio.service;

import com.google.common.base.Preconditions;

import io.tacsio.api.dto.PaymentForm;
import io.tacsio.model.Payee;
import io.tacsio.model.Payer;
import io.tacsio.model.Transaction;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@ApplicationScoped
public class PaymentService {

    @Transactional
    public Transaction makePayment(PaymentForm paymentForm) {
        Payer payer = paymentForm.toPayer();
        Payee payee = paymentForm.toPayee();
        BigDecimal value = paymentForm.getValue();
        
        //only users can make payments
        Preconditions.checkArgument(payer.canPay(), "This user can not pay, only receive payments.");

        payer.getWallet().withdraw(value);
        payee.getWallet().deposit(value);

        Transaction transaction = new Transaction(payer, payee, value);
        transaction.confirm();

        return transaction;
    }
}
