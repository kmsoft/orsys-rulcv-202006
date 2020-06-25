package com.docdoku.common;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Random;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Provider
@MakeSlow
public class MakeSlowFilter implements ContainerRequestFilter {
	@Context
	private ResourceInfo resourceInfo;

	private final Random random = new Random();

	@ConfigProperty(name = "simulate-failure")
	boolean simulateFailure;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (! simulateFailure || random.nextBoolean()) return;
		Method method = resourceInfo.getResourceMethod();
		MakeSlow makeSlow = method.getAnnotation(MakeSlow.class);
		try {
			Thread.sleep(makeSlow.delayInMs());
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
	
}