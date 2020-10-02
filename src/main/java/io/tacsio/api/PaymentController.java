package io.tacsio.api;

import io.quarkus.security.UnauthorizedException;
import io.tacsio.api.dto.PaymentForm;
import io.tacsio.model.Transaction;
import io.tacsio.model.User;
import io.tacsio.service.TransactionValidatorService;
import io.tacsio.service.dto.TransactionValitadorResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    @RestClient
    TransactionValidatorService validatorService;

    @POST
    @Transactional
    public Response payment(@Valid PaymentForm form) {

        User payer = form.getPayer();
        User payee = form.getPayee();

        Transaction transaction = payer.pay(form.getValue(), payee);
        TransactionValitadorResponse validation = validatorService.validate();
        if (validation.authorized()) {
            transaction.persist();
        } else {
            throw new UnauthorizedException();
        }

        return Response.ok().build();
    }
}
