package io.tacsio.service;

import io.tacsio.service.dto.TransactionValitadorResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;

@ApplicationScoped
@RegisterRestClient(configKey = "transaction-validator")
public interface TransactionValidatorService {

    @GET
    TransactionValitadorResponse validate();
}
