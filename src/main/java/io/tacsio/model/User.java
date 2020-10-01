package io.tacsio.model;

import static io.tacsio.model.UserType.DEFAULT;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "picpay_user")
public class User extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private UUID id;

	@Email
	@Column(unique = true)
	private String email;

	@CPF
	@Column(unique = true)
	private String cpf;

	@CNPJ
	@Column(unique = true)
	private String cnpj;

	@NotEmpty
	private String encriptedPassword;

	@Enumerated(EnumType.STRING)
	private UserType type;

	@OneToOne(mappedBy = "user")
	private Wallet wallet;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	public User(UserType type, @Email String email, String document, @NotEmpty String encriptedPassword) {
		this.type = type;
		this.email = email;
		this.encriptedPassword = encriptedPassword;

		if (DEFAULT.equals(type)) {
			this.cpf = document;
		} else {
			this.cnpj = document;
		}
	}

	public boolean canPay() {
		return DEFAULT.equals(this.type);
	}

}
