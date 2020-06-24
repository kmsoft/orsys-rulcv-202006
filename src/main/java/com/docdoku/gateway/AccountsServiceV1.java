package com.docdoku.gateway;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.docdoku.accounts.AccountModel;
import com.docdoku.accounts.TransactionModel;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/accounts/v1")
@RegisterRestClient(configKey="accounts-api")
@Produces("application/json")
public interface AccountsServiceV1 {

	@Path("{id}")
	@GET
	AccountModel get(@PathParam("id") int id);

	@Path("{id}/transactions")
	@POST
	void createTransaction(@PathParam("id") int id, TransactionModel transaction);
}