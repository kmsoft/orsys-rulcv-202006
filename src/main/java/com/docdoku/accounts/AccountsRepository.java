package com.docdoku.accounts;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountsRepository {
	private Map<Integer, AccountModel> accounts = new HashMap<>();

	public AccountsRepository() {
		addAccount(1, 100);
		addAccount(2, 200);
		addAccount(3, 300);
	}
	
	public AccountModel getAccount(int accountId) {
		return this.accounts.get(accountId);
	}
	
	private void addAccount(int id, int credit) {
		this.accounts.put(id, new AccountModel(id, credit));
	}
}