package io.tacsio.model;

import com.google.common.base.Preconditions;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "picpay_wallet")
public class Wallet extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;

    @OneToOne
    private User owner;

    Wallet() {
    }

    public Wallet(BigDecimal balance, User owner) {
        this.balance = balance;
        this.owner = owner;
    }

    public void decrement(BigDecimal value) {
        //don't accepts a value greater than balance
        Preconditions.checkArgument(balance.compareTo(value) > 0, "The wallet doesn't have sufficient money to complete this transaction.");

        this.balance = balance.subtract(value);
        this.persist();
    }

    public void increment(BigDecimal value) {
        //don't accepts negative values
        Preconditions.checkArgument(value.compareTo(BigDecimal.ZERO) > 0, "You can only increment positive values.");

        this.balance = balance.add(value);
        this.persist();
    }

    public Transaction transfer(BigDecimal value, Wallet payee) {
        Preconditions.checkArgument(this.owner.canPay(), "This user can not pay, only receive payments.");
        Preconditions.checkArgument(value.compareTo(BigDecimal.ZERO) > 0, "Payment value must be greater than 0.");

        this.decrement(value);
        payee.increment(value);

        Transaction transaction = new Transaction(this.owner, payee.owner, value);
        return transaction;
    }
}
