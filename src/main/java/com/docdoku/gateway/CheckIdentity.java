package com.docdoku.gateway;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
/**
 * Annotation to apply the IdentityFilter only on resources annotated by this annotation.
 * @see https://stackoverflow.com/questions/23641345/jersey-request-filter-only-on-certain-uri
 */
public @interface CheckIdentity {
	
}