package io.tacsio.fixtures;

import java.math.BigDecimal;
import java.util.List;

import io.tacsio.api.dto.PaymentForm;
import io.tacsio.model.User;
import io.tacsio.model.UserType;
import io.tacsio.service.dto.TransactionValitadorResponse;

public class PaymentFixtures {

  public static PaymentForm userToUser(double ammount) {

    List<User> users = User.list("type", UserType.DEFAULT);
    if (users.size() < 2) {
      throw new IllegalStateException("There is no 2 users registered in database to make this payment.");
    }

    long idPayer = users.get(0).getId();
    long idPayee = users.get(1).getId();
    BigDecimal value = BigDecimal.valueOf(ammount);

    return of(idPayer, idPayee, value);
  }

  public static PaymentForm of(Long idPayer, Long idPayee, BigDecimal value) {
    PaymentForm form = new PaymentForm();
    form.payer = idPayer;
    form.payee = idPayee;
    form.value = value;

    return form;
  }

  public static TransactionValitadorResponse authorizedResponse() {
    TransactionValitadorResponse response = new TransactionValitadorResponse();
    response.message = "Autorizado";

    return response;
  }

  public static TransactionValitadorResponse unauthorizedResponse() {
    TransactionValitadorResponse response = new TransactionValitadorResponse();
    response.message = "Denied";
    
    return response;
  }
}
