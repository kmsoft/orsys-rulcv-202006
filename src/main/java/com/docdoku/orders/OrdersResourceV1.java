package com.docdoku.orders;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/orders/v1")
public class OrdersResourceV1 {

	@Inject
	OrdersRepository orders;
	
	@GET
	public Collection<OrderModel> list() {
		return this.orders.getOrders();
	}
	
	@GET
	@Path("{id}")
	public OrderModel get(@PathParam("id") int id) {
		OrderModel order = orders.getOrder(id);
		if (order == null) throw new NotFoundException();
		return order;
	}
	
	@DELETE
	public void clear() {
		orders.clearOrders();
	}
	
	@POST
	public OrderModel create(OrderModel order) {
		return this.orders.createOrder(order);
	}
}
