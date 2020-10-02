package io.tacsio.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static io.tacsio.model.UserType.DEFAULT;

@Entity
@Table(name = "picpay_user")
public class User extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private String encryptedPassword;

    @Enumerated(EnumType.STRING)
    private UserType type;

    @OneToOne(mappedBy = "owner")
    private Wallet wallet;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    User() {
    }

    public User(UserType type, @Email String email, String document, @NotEmpty String encryptedPassword) {
        this.type = type;
        this.email = email;
        this.encryptedPassword = encryptedPassword;

        if (DEFAULT.equals(type)) {
            this.cpf = document;
        } else {
            this.cnpj = document;
        }
    }

    public boolean canPay() {
        return DEFAULT.equals(this.type);
    }

    public Transaction pay(BigDecimal value, User payee) {
        return this.wallet.transfer(value, payee.wallet);
    }
}
