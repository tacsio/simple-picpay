package io.tacsio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.CreationTimestamp;

import io.smallrye.common.constraint.Assert;

@Entity
@Table(name = "picpay_transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private UUID id;

	@ManyToOne
	private final User payer;

	@ManyToOne
	private final User payee;

	@Positive
	private final BigDecimal value;

	@CreationTimestamp
	private final LocalDateTime timestamp;

	public Transaction(User payer, User payee, @Positive BigDecimal value) {
		Assert.assertTrue(payer.canPay());
		Assert.assertTrue(value.compareTo(BigDecimal.ZERO) > 0);

		this.payer = payer;
		this.payee = payee;
		this.value = value;
		this.timestamp = LocalDateTime.now();
	}

}
