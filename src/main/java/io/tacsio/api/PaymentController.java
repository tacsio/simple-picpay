package io.tacsio.api;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.tacsio.api.dto.PaymentForm;
import io.tacsio.api.dto.PaymentResponse;
import io.tacsio.model.Transaction;
import io.tacsio.model.User;
import io.tacsio.service.NotificationService;
import io.tacsio.service.TransactionValidatorService;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    @RestClient
    TransactionValidatorService validatorService;

    @Inject
    @RestClient
    NotificationService notificationService;

    @POST
    @Transactional
    public Response payment(@Valid PaymentForm form) {
        User payer = form.getPayer();
        User payee = form.getPayee();
        BigDecimal value = form.getValue();

        Transaction transaction = payer.pay(value, payee);

        return Response.ok(new PaymentResponse(transaction)).build();
    }
}
