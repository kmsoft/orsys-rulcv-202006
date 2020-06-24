package com.docdoku.products;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductsRepository {
	private Map<Integer, ProductModel> products = new HashMap<>();

	public ProductsRepository() {
		addProduct(new ProductModel(1, "Brie", 9.9));
		addProduct(new ProductModel(2, "Cabécou", 14.9));
		addProduct(new ProductModel(3, "Maroilles", 19.9));
	}

	public Collection<ProductModel> getProducts() {
		return products.values();
	}

	public ProductModel getProduct(int id) {
		return products.get(id);
	}

	private void addProduct(ProductModel product) {
		products.put(product.id, product);
	}
}