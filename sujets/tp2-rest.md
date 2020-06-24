# TP2: API REST

Le but du TP est d'apprendre à utiliser JAX-RS pour écrire un API REST.

Notre application se composera de 3 micro-services. Pour simplifier l'exercice, ces 3 services seront implémentés dans le même projet Quarkus, mais le projet sera structuré de sorte à être facilement divisé en micro-services par la suite.

L'application va gérer 3 types d'entités:
- `product`: un produit que peut acheter l'utilisateur
- `order`: une commande d'un produit passée par l'utilisateur
- `account`: le compte d'un utilisateur

Durant ce TP, vous aurez besoin du guide [Writing REST JSON Services](https://quarkus.io/guides/rest-json) de Quarkus.

## Structure du projet

Le projet est divisé en 3 packages:
- products
- orders
- accounts

Cela permettra de facilement découper notre monolithe par la suite.

## Test du projet

Les fichiers Postman suivants sont fournirs pour tester vos APIs REST:
- [quarkus-store.postman_collection.json](./quarkus-store.postman_collection.json): collection de test à importer ([guide](https://learning.postman.com/docs/postman/collections/importing-and-exporting-data/#importing-data-into-postman))
- [quarkus-store.postman_environment.json](./quarkus-store.postman_environment.json): variables d'environnement à importer ([guide](https://learning.postman.com/docs/postman/variables-and-environments/managing-environments/#accessing-environments))

## Produits

Initialiser et stocker quelques `ProductModel` dans cette classe.

- Ajouter la classe `products.ProductModel`:
```java
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
```

- Ajouter la classe `products.ProductsRepository`. Cette classe servira de DAO mais sans se connecter à une base de données:
```java
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
```
- Créer la classe `products.ProductResourceV1` qui répondra aux requêtes sur `/products/v1`.
- _Injecter_ un `ProductsRepository` dans cette classe.

### `GET /products/v1`

Renvoie tous les produits.

### `GET /products/v1/{id}`

Renvoie le produit identifié ou échoue si le produit n'existe pas.

## Commandes

Répondre aux requêtes suivantes:
- `GET /orders/v1`: renvoie toutes les commandes
- `GET /orders/v1/{id}`: renvoie une commande ou échoue
- `POST /orders/v1`: crée une commande.
- `DEL /orders/v1`: supprime toutes les commandes (pour débugger)

Classe `orders.OrderModel`:
```java
import java.time.Instant;

public class OrderModel {
	public final int id;
	public final Instant date;
	public int accountId;
	public int productId;
	public int quantity;
	
	public OrderModel() {
		this.id = -1;
		this.date = Instant.now();
	}
	public OrderModel(int id, OrderModel order) {
		this.id = id;
		this.date = order.date;
		this.accountId = order.accountId;
		this.productId = order.productId;
		this.quantity = order.quantity;
	}
}
```

Classe `orders.OrdersRepository`:
```java
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
```

## Comptes

Répondre aux requêtes suivantes:
- `GET /accounts/v1/{id}`: renvoie une compte ou échoue
- `POST /accounts/v1/{id}/transactions`: crée une transaction, c'est-à-dire décrédite le compte du montant de la transaction. Echoue si l'opération rend le crédit négatif.
- `PUT /accounts/v1/{id}`: modifie un compte (pour pouvoir le recréditer)

Classe `accounts.AccountModel`:
```java
public class AccountModel {
	public int id;
	public double credit;
	
	public AccountModel() {
	}
	
	public AccountModel(int id, double credit) {
		this.id = id;
		this.credit = credit;
	}
}
```

Classe `accounts.TransactionModel`:
```java
public class TransactionModel {
	public double amount;
	
	public TransactionModel() {
	}
}
```

Classe `accounts.AccountsRepository`:
```java
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
```

## Bonus

- Exposer l'API REST via un document OpenAPI et offrir une interface Swagger:
  https://quarkus.io/guides/openapi-swaggerui
- Rendre l'API plus "RESTful" en répondant avec le code 201 pour les requêtes de création:
  - https://medium.com/@mwaysolutions/10-best-practices-for-better-restful-api-cbe81b06f291#1d1a
  Pour cela, il faudra manipuler un objet `Response`: https://quarkus.io/guides/rest-json#using-response