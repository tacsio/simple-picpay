package io.tacsio.api.dto;

import io.tacsio.model.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class PaymentForm {
    @Positive
    public BigDecimal value;
    @NotNull
    public Long payer;
    @NotNull
    public Long payee;

    public PaymentForm() {
    }

    public BigDecimal getValue() {
        return value;
    }

    public User getPayer() {
        return User.findById(this.payer);
    }

    public User getPayee() {
        return User.findById(this.payee);
    }
}
