package com.docdoku.products;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/products/v1")
@Produces(MediaType.APPLICATION_JSON)
public class ProductsResourceV1 {

	@Inject
	ProductsRepository products;

	@GET
	public Collection<ProductModel> list() {
		return products.getProducts();
	}

	@GET
	@Path("{id}")
	public ProductModel get(@PathParam("id") int id) {
		return getProductOrFail(id);
	}

	private ProductModel getProductOrFail(int id) {
		ProductModel product = products.getProduct(id);
		if (product == null)
			throw new NotFoundException();
		return product;
	}
}