package com.docdoku.common;

import java.io.IOException;
import java.util.Random;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Provider
@SimulateNetworkFailure
public class SimulateNetworkFailureFilter implements ContainerRequestFilter {
	private final Random random = new Random();

	@ConfigProperty(name = "simulate-failure")
	boolean simulateFailure;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (!simulateFailure || random.nextBoolean()) return;
		requestContext.abortWith(Response.status(Response.Status.GATEWAY_TIMEOUT).build());
	}
}