# BeautyProducts

#### Overview

This is a Java Spring Boot application that is designed to demonstrate the capabilities of the Spring framework and its modules. The application uses a RESTful API to handle HTTP requests and provide data to the client. It uses an instance of MariaDb Server(MySql) to persist the data.

#### Requirements
- Java 8 or later
- Gradle 6.5 or later
- Spring Boot 2.3.0 or later
- Database Server version: 10.4.27-MariaDB - mariadb.org binary distribution or later
- Protocol version: 10 - later

#### 1. Setup
- ##### 1.0 Prerequisits
    - make sure your MySql Server is running before starting the application.
    - if you are not using the default credentials for login, make sure you set them in the `application.properties` file

- ##### 1.1 Clone the repository:
```
git clone https://github.com/KaltrinaI/BeautyProducts.git
```
- ##### 1.2.Navigate to the project directory:
```
cd your-project-directory
```
- ##### 1.3.Build the project:
```
gradle build
```
- ##### 1.4.Run the application:
```
gradle bootRun
```

#### 2. Usage
After starting the application, you can access the RESTful API endpoint at http://localhost:8080/.

#### **3. EndPoints:**

##### 3.1 Products endpoints
- Get all Products: `GET /api/products`
- Get Product by ID: `GET /api/productById/{id}`
- Get Products by Category: `GET /api/productsByCategory/{category}`
- Get Products by Color: `GET /api/productsByColor/{color}`
- Get Products by Price: `GET /api/productsByPrice/{price}`
- Get Products by Type: `GET /api/productsByType/{type}`
- Get Products by Category and color: `GET /api/products/{category}/{color}`
- Get Products that are out of Stock: `GET /api/outOfStock`
- Create a Product: `POST /api/admin/createProduct`
- Update a Product: `PUT /api/admin/editProduct/{id}`
- Delete a Product: `DELETE /api/admin/deleteProduct/{id}`

#### 3.2 Customer endpoints
- Register a Customer: `POST /api/register`
- Get all Customers: `GET /api/customers`
- Get Customer: `GET /api/findCustomerById/{id}`
- Get Customer by Cart ID: `GET /api/findCustomerByCartID/{id}`

#### 3.3 Cart endpoints
- Add Product to Cart: `POST /api/addProductToCart`
- Delete Product from Cart: `DELETE /api/deleteProductFromCart/{cartId}/{productId}`
- View all Products in the Cart: `GET /api/productsInCart/{cartId}`
- Update the amount of a Product from the Cart: `PUT /api/editAmount/{cartId}/{productId}`
- See the total price of the products that are added to the Cart: `GET /api/totalPrice/{cartId}`

More information regarding the controller endpoints can be found [here.](https://github.com/KaltrinaI/BeautyProducts/tree/master/src/documentation)







