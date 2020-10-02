package io.tacsio.service;

import io.tacsio.service.dto.TransactionValitadorResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;

@RegisterRestClient(configKey = "transaction-validator")
public interface TransactionValidatorService {

    @GET
    TransactionValitadorResponse validate();
}
