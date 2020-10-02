package io.tacsio.model;

import com.google.common.base.Preconditions;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "picpay_transaction")
public class Transaction extends PanacheEntityBase {

    @Id
    private UUID id;

    @PrePersist
    void generateId() {
        this.id = UUID.randomUUID();
    }

    @ManyToOne
    private User payer;

    @ManyToOne
    private User payee;

    @Positive
    private BigDecimal value;

    @CreationTimestamp
    private LocalDateTime timestamp;

    Transaction() {
    }

    public Transaction(User payer, User payee, @Positive BigDecimal value) {
        Preconditions.checkArgument(payer.canPay(), "This user can not pay, only receive payments.");
        Preconditions.checkArgument(value.compareTo(BigDecimal.ZERO) > 0, "Payment value must be greater than 0.");

        this.payer = payer;
        this.payee = payee;
        this.value = value;
        this.timestamp = LocalDateTime.now();
    }
}
