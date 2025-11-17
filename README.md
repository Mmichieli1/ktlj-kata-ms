# API Standalone in Microservice
[![forthebadge](https://forthebadge.com/badges/made-with-java.svg)](forthebadge)

Manage Products and compute Cart

## First Step

Clone the repository Github.

## Build it in local

Use the package manager [mvn](https://maven.apache.org/) to build all the services.

```bash
mvn clean install 
```

## How to Start

Launch each service (API Gateway, Product-Service, Cart-Service and Discovery Server).
Using Spring Boot, you can do it by a simple right-click in the IDE of your choice.

### Using the command line
Open a terminal in the parent folder
There for each service (sub-folder)
```dos
cd _SERVICE_NAME_ && mvn spring-boot:run
```

## Once all is running
A full documentation is accessible at [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

- [localhost:8080/api/products](http://localhost:8080/api/products) - List all products
- [localhost:8080/api/carts/create](http://localhost:8080/api/carts/create) - simulate a cart with a random quantity of each product


## Computation Rules

- No tax is applied to basic necessities, namely FOOD and MEDICINE.
- A value-added tax of 10% is applied to BOOKS.
- A value-added tax of 20% is applied to all OTHER products.
- An additional 5% tax is applied to imported products, without exception.

The amount of each tax is rounded up to the nearest 5 cents, according to the following rule:

| Computed Taxe | Taxe imputée |
|:-------------:|:------------:|
|     0.99      |     1.00     |
|     1.00      |     1.00     |
|     1.01      |     1.05     |
|     1.02      |     1.05     |
