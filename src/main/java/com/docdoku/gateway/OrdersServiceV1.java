package com.docdoku.gateway;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.docdoku.orders.OrderModel;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/orders/v1")
@RegisterRestClient(configKey="orders-api")
@Produces("application/json")
public interface OrdersServiceV1 {

	@POST
	OrderModel create(OrderModel order);
}