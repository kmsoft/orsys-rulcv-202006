package com.docdoku.gateway;

import java.io.IOException;
import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
@CheckIdentity
/**
 * A request filter applied to resources annotated with CheckIdentity.
 * Authorizes the request if it contains a header Authorization that contains a user id.
 * @see https://itnext.io/how-to-implement-a-jax-rs-authentication-filter-3eee64b34b99
 * @see https://stackoverflow.com/questions/45381946/jax-rs-filter-pass-object-to-resource
 * @see https://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey
 */
public class CheckIdentityFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println("IdentityFilter " + requestContext.getUriInfo().getRequestUri());

		final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
		final User user = validateAuthorizationHeader(requestContext.getHeaderString("Authorization"));
		if (user == null) 
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());

		requestContext.setSecurityContext(new SecurityContext() {
			@Override
			public Principal getUserPrincipal() {
				return user;
			}
			@Override
			public boolean isUserInRole(String role) {
					return true;
			}
			@Override
			public boolean isSecure() {
					return currentSecurityContext.isSecure();
			}
			@Override
			public String getAuthenticationScheme() {
					return SecurityContext.BASIC_AUTH;
			}
		});
	}

	/**
	 * Simulates the valiation of a JWT.
	 * The Authorization header should contain the id of the user, without any encoding nor signing.
	 */
	private User validateAuthorizationHeader(String authorization) {
		User user;
		try {
			int userId = Integer.parseInt(authorization);
			user = new User(userId);
		} catch (NumberFormatException e) {
			user = null;
		}
		return user;
	}
	
}