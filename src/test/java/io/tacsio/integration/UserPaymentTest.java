package io.tacsio.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.tacsio.api.dto.PaymentForm;
import io.tacsio.fixtures.PaymentFixtures;
import io.tacsio.service.NotificationService;
import io.tacsio.service.TransactionValidatorService;
import io.tacsio.service.dto.NotificationResponse;

@QuarkusTest
public class UserPaymentTest {

  @InjectMock
  @RestClient
  NotificationService notificationService;

  @InjectMock
  @RestClient
  TransactionValidatorService transactionValidatorService;

  @BeforeEach
  public void setup(){
    Mockito.when(notificationService.sendNotification()).thenReturn(new NotificationResponse());
    Mockito.when(transactionValidatorService.validate()).thenReturn(PaymentFixtures.authorizedResponse());
  }


  @Test
  @DisplayName("An user should pay other users if he has sufficient money.")
  public void testUserToUserPayment() {
    PaymentForm userToUser = PaymentFixtures.userToUser(100.0);

    given().when().contentType(ContentType.JSON)
      .body(userToUser)
      .post("/transaction")
    .then()
      .log().all()
      .statusCode(200)
      .body(
        "id", notNullValue(),
        "timestamp", notNullValue()
      );
  }

  @Test
  @DisplayName("If the user hasn't sufficient amount of money the transaction will not be accepted and the user will receive a 400 http status code.")
  public void insufficientMoney() {
    PaymentForm userToUser = PaymentFixtures.userToUser(10000.0);
    
    given().when().contentType(ContentType.JSON)
    .body(userToUser)
    .post("/transaction")
  .then()
    .log().all()
    .statusCode(400)
    .body("error", Matchers.is("Insufficient money to complete this transaction."));
  }
  
  @Test
  @DisplayName("If the transaction validation service not authorize a payment, the transaction should not be completed. And user should recive a 401 http status.")
  public void testUnauthorizedTransaction() {
    Mockito.reset(transactionValidatorService);
    Mockito.when(transactionValidatorService.validate()).thenReturn(PaymentFixtures.unauthorizedResponse());

    PaymentForm userToUser = PaymentFixtures.userToUser(100.0);

    given().when().contentType(ContentType.JSON)
    .body(userToUser)
    .post("/transaction")
  .then()
    .log().all()
    .statusCode(401)
    .body("error", Matchers.is("Transaction not authorized."));
  }

}
