package io.tacsio.api;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.tacsio.api.dto.PaymentForm;
import io.tacsio.api.dto.PaymentResponse;
import io.tacsio.model.Payee;
import io.tacsio.model.Payer;
import io.tacsio.model.Transaction;
import io.tacsio.model.User;
import io.tacsio.service.PaymentService;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    PaymentService paymentService;

    @POST
    @Transactional
    public Response payment(@Valid PaymentForm form) {
        Payer payer = form.getPayer();
        Payee payee = form.getPayee();
        BigDecimal value = form.getValue();

        Transaction transaction = paymentService.makePayment(payer, payee, value);

        return Response.ok(new PaymentResponse(transaction)).build();
    }
}
