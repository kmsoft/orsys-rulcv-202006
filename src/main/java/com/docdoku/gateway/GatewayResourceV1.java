package com.docdoku.gateway;

import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.docdoku.accounts.AccountModel;
import com.docdoku.accounts.TransactionModel;
import com.docdoku.orders.OrderModel;
import com.docdoku.products.ProductModel;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
public class GatewayResourceV1 {

	@Context
	private SecurityContext securityContext;

	@Inject @RestClient
	// Package visibility for better performance when running Natively
	// https://quarkus.io/guides/cdi-reference#native-executables-and-private-members
	ProductsServiceV1 productsService;

	@Inject @RestClient
	AccountsServiceV1 accountsService;

	@Inject @RestClient
	OrdersServiceV1 ordersService;

	@GET
	@Path("products")
	public Collection<ProductModel> listProducts() {
		return productsService.list();
	}
	
	public Collection<ProductModel> listProductsFallback() {
		return Collections.emptyList();
	}
	
	@GET
	@Path("my-account")
	public AccountModel getAccount() {
		return accountsService.get(getUserId());
	}

	@POST
	@Path("orders")
	public OrderModel createOrder(OrderModel order) {
		// Get the product or fail
		ProductModel product = productsService.get(order.productId);
		
		// Debit the user or fail
		final int userId = getUserId(); 
		TransactionModel transaction = new TransactionModel(product.price * order.quantity);
		this.accountsService.createTransaction(userId, transaction);

		try {
			// Create the order
			order = this.ordersService.create(order);
		} catch (WebApplicationException e) {
			// Undo debit the user
			this.accountsService.createTransaction(userId, new TransactionModel(-transaction.amount));
			throw e;
		}

		return order;
	}

	private int getUserId() {
		// TODO: get user id from Authorization header
		return 1;
	}
}