package io.tacsio.api;


import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.tacsio.api.dto.PaymentForm;
import io.tacsio.api.dto.PaymentResponse;
import io.tacsio.model.Transaction;
import io.tacsio.service.PaymentService;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    PaymentService paymentService;

    @POST
    public Response payment(@Valid PaymentForm form) {
        Transaction transaction = paymentService.makePayment(form);

        return Response.ok(new PaymentResponse(transaction)).build();
    }
}
