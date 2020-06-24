package com.docdoku.orders;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrdersRepository {
	
	private AtomicInteger lastId = new AtomicInteger(0);
	private Map<Integer, OrderModel> orders = new HashMap<>();

	public Collection<OrderModel> getOrders() {
		return orders.values();
	}

	public OrderModel getOrder(int id) {
		return orders.get(id);
	}

	public OrderModel createOrder(OrderModel order) {
		order = new OrderModel(lastId.incrementAndGet(), order);
		this.orders.put(order.id, order);
		return order;
	}

	public void clearOrders() {
		orders.clear();
	}
}