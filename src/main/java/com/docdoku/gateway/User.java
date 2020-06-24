package com.docdoku.gateway;

import java.security.Principal;

public class User implements Principal {
	public final int id;

	public User(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return "User " + id;
	}
}