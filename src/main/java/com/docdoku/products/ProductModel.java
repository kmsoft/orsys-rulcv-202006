package com.docdoku.products;

public class ProductModel {
	
	public int id;
	public String name;
	public double price;

	public ProductModel() {
	}

	public ProductModel(int id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}
}
